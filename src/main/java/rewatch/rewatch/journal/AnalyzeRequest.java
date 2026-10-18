package rewatch.rewatch.journal;

import java.util.List;

/** What the user sends: their goals, and the journal entries to rewatch. */
public record AnalyzeRequest(String goals, List<JournalEntry> entries) {
}
