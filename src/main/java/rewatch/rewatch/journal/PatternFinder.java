package rewatch.rewatch.journal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import rewatch.rewatch.ollama.OllamaClient;
import tools.jackson.databind.json.JsonMapper;

/**
 * Rewatches the journal: Gemma proposes patterns, then plain code checks them
 * against the actual entries before anything is shown to the user.
 */
@Service
public class PatternFinder {

    private static final String SYSTEM_PROMPT = """
            You read a person's journal. Their goals are given first.
            Find only very obvious patterns: something that happens on two or more
            different days and moves the person away from their goals.
            For each pattern give: what keeps happening (one sentence), how many days
            it happened on, the dates (yyyy-MM-dd), and exact quotes copied word for
            word from those entries as evidence.
            If there is no obvious pattern, return an empty list.
            Never invent events, dates or quotes.
            """;

    /** The shape Gemma has to answer in (Ollama structured outputs). */
    private static final Map<String, Object> SCHEMA = Map.of(
            "type", "object",
            "required", List.of("patterns"),
            "properties", Map.of(
                    "patterns", Map.of(
                            "type", "array",
                            "items", Map.of(
                                    "type", "object",
                                    "required", List.of("pattern", "timesSeen", "dates", "evidence"),
                                    "properties", Map.of(
                                            "pattern", Map.of("type", "string"),
                                            "timesSeen", Map.of("type", "integer"),
                                            "dates", Map.of("type", "array", "items", Map.of("type", "string")),
                                            "evidence", Map.of("type", "array", "items", Map.of("type", "string")))))));

    private record ModelReport(List<Pattern> patterns) {
    }

    private final OllamaClient ollama;
    private final JsonMapper json = JsonMapper.builder().build();

    public PatternFinder(OllamaClient ollama) {
        this.ollama = ollama;
    }

    public AnalyzeResponse analyze(AnalyzeRequest request) {
        long start = System.currentTimeMillis();

        String reply = ollama.chat(SYSTEM_PROMPT, buildUserMessage(request), SCHEMA);
        ModelReport proposed = json.readValue(reply, ModelReport.class);
        List<Pattern> checked = keepOnlyWhatTheJournalSupports(proposed.patterns(), request.entries());

        return new AnalyzeResponse(checked, ollama.model(), System.currentTimeMillis() - start);
    }

    private String buildUserMessage(AnalyzeRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("GOALS:\n").append(request.goals()).append("\n\nJOURNAL:\n");
        for (JournalEntry entry : request.entries()) {
            sb.append(entry.date()).append(": ").append(entry.text()).append("\n");
        }
        return sb.toString();
    }

    /**
     * The model proposes, the code decides: drop dates that aren't in the journal,
     * drop quotes that don't appear word for word, recount the days, and drop any
     * pattern that ends up on fewer than two days or with no real quote.
     */
    private List<Pattern> keepOnlyWhatTheJournalSupports(List<Pattern> proposed, List<JournalEntry> entries) {
        if (proposed == null) {
            return List.of();
        }
        Set<String> realDates = new HashSet<>();
        StringBuilder allText = new StringBuilder();
        for (JournalEntry entry : entries) {
            realDates.add(entry.date().toString());
            allText.append(normalize(entry.text())).append('\n');
        }

        List<Pattern> checked = new ArrayList<>();
        for (Pattern p : proposed) {
            List<String> dates = p.dates() == null ? List.of()
                    : p.dates().stream().filter(realDates::contains).distinct().toList();
            List<String> evidence = p.evidence() == null ? List.of()
                    : p.evidence().stream()
                            .map(PatternFinder::cleanQuote)
                            .filter(q -> !q.isEmpty() && allText.indexOf(normalize(q)) >= 0)
                            .toList();
            if (dates.size() >= 2 && !evidence.isEmpty()) {
                checked.add(new Pattern(p.pattern(), dates.size(), dates, evidence));
            }
        }
        return checked;
    }

    /**
     * Gemma sometimes writes a quote as "2026-09-08: the words" or wraps it in
     * quotation marks. Strip those so the word-for-word check sees just the words.
     */
    private static String cleanQuote(String q) {
        if (q == null) {
            return "";
        }
        String s = q.trim().replaceFirst("^\\d{4}-\\d{2}-\\d{2}\\s*[:\\-]\\s*", "");
        return s.replaceAll("^[\"'“”‘’]+|[\"'“”‘’]+$", "").trim();
    }

    private static String normalize(String s) {
        return s == null ? "" : s.toLowerCase(Locale.ROOT).replaceAll("\\s+", " ").trim();
    }
}
