# Rewatch

Rewatch your days. Write your goals and a few lines about each day; Rewatch finds the patterns that keep pulling you away from those goals, with the dates and your own words as proof. It runs entirely on your computer: your journal never leaves it.

## How it works
1. You give it your goals and journal entries (date + what happened).
2. **Gemma 4** (`gemma4:e4b-it-qat`, an open-weight model) runs locally through **Ollama** and proposes patterns: what keeps happening, on which days, with quotes.
3. **Plain code checks every claim** before you see it: dates must be real entries, quotes must appear word for word, and a pattern needs at least two days and one real quote. Anything that fails is dropped.

## Run it
Requirements: Java 17+, [Ollama](https://ollama.com) running, and the model pulled:

```bash
ollama pull gemma4:e4b-it-qat
./mvnw spring-boot:run
```

Open http://localhost:8080, then try **"Try a made-up sample month"**.

## Tested on a made-up month
`src/main/resources/static/sample-month.json` is a made-up 28-day journal (not a real person) with three planted patterns on 13 days, plus two planned treat nights that should not be flagged. Answers and full results: [`docs/sample-month-answers.md`](docs/sample-month-answers.md).

Five runs on an RTX 3070 (about 20 seconds each):
- Takeout after finishing something: **5 of 5 days, every run**
- Phone in bed, missed morning: **4 of 4 days, every run**
- "I'll start fresh tomorrow": found in **2 of 5 runs** (3 of 4 days when found)
- **0 false alarms** in all 5 runs, including the planned treat nights

## Stack
Spring Boot 4.1 (Java), Gemma 4 on Ollama (local HTTP, `localhost:11434`), one plain HTML page.
