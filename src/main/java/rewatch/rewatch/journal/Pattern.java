package rewatch.rewatch.journal;

import java.util.List;

/**
 * One obvious pattern that moves the person away from their goals.
 *
 * @param pattern   what keeps happening, in one sentence
 * @param timesSeen how many days it showed up on
 * @param dates     the days it showed up on (yyyy-MM-dd)
 * @param evidence  exact quotes from those entries
 */
public record Pattern(String pattern, int timesSeen, List<String> dates, List<String> evidence) {
}
