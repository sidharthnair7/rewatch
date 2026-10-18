package rewatch.rewatch.journal;

import java.time.LocalDate;

/** One day in the journal: the date and what happened that day. */
public record JournalEntry(LocalDate date, String text) {
}
