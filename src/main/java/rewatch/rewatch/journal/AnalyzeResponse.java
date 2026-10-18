package rewatch.rewatch.journal;

import java.util.List;

/** The report: the patterns found, which model found them, and how long it took. */
public record AnalyzeResponse(List<Pattern> patterns, String model, long millis) {
}
