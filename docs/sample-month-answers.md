# Sample month: the planted answers

`src/main/resources/static/sample-month.json` is a made-up 28-day journal (not a real person). Three patterns were planted on purpose, plus two planned treats that should **not** be flagged.

| Pattern | Days |
|---|---|
| A. Finishes something, then buys takeout on the way home ("I earned it") | Sep 3, 7, 12, 18, 24 (5 days) |
| B. Skips the planned work and says "I'll start fresh tomorrow" | Sep 5, 10, 16, 22 (4 days) |
| C. Phone in bed past 1am, then misses the morning plan | Sep 8, 14, 20, 26 (4 days) |

**Should not be flagged:** Sep 9 and Sep 23, the planned Thursday shawarma with a friend (planned in advance, split the bill).

Scoring: a planted day counts as found if it appears in the matching pattern's dates. A flagged day that isn't in any planted pattern counts as a false alarm.

## Results (Oct 18, 2026, gemma4:e4b-it-qat on an RTX 3070, 5 runs, temperature 0.2)
| | Run 1 | Run 2 | Run 3 | Run 4 | Run 5 |
|---|---|---|---|---|---|
| A. takeout after finishing something (5 days) | 5 | 5 | 5 | 5 | 5 |
| B. "start fresh tomorrow" (4 days) | 3 | 0 | 0 | 3 | 0 |
| C. phone in bed, missed morning (4 days) | 4 | 4 | 4 | 4 | 4 |
| Planted days found (of 13) | 12 | 9 | 9 | 12 | 9 |
| False alarms (incl. the planned treat nights) | 0 | 0 | 0 | 0 | 0 |

About 20 seconds a run. Every quote shown is checked word for word against the entries before it reaches the screen; quotes Gemma prefixed with a date are cleaned first (that bug emptied one earlier run). The goals line decides what counts: before the sample goals said the Thursday treat is planned and fine, all 5 runs flagged those two nights.
