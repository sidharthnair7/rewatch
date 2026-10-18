---
title: I kept regretting the walk home. So I built an AI that rewatches my days, and it never leaves my laptop
published: false
tags: devchallenge, hf26challenge, ai, opensource
---

*This is a submission for the [Hacktoberfest Open-Source AI Challenge, Week 2: Side Hustle](https://dev.to/challenges/hacktoberfest-week2-2026-10-12).*

## What I Built

Rewatch reads your journal and shows you the patterns that keep pulling you away from your goals, with the dates and your own words as proof.

I'm a final-year CS student and money is tight right now. I kept doing the same thing: finish something at the library, walk home, tell myself "I earned it", and buy takeout I couldn't afford. One night my card got declined in front of a friend. I always noticed it after, never in the moment.

What finally helped was having an AI read two months of my notes. It found the pattern in a few minutes: most of the time it happened on the walk home right after I finished something, and more than once I'd written "I earned it" right before. But that meant sending my diary to a cloud AI. Your journal is the most private thing you have. So I built a version that runs on your own computer.

**Who would pay:** anyone who journals and wants to understand themselves, but doesn't want their diary on someone else's server. People already pay for journaling apps. Rewatch's pitch is the part they can't offer: [your price idea, e.g. a one-time $10, no subscription, no account].

## Demo

[your screen recording: load the sample month, press Rewatch, show the 3 patterns]

## Code

[embed your GitHub repo here]

## How I Built It

- **Gemma 4** (`gemma4:e4b-it-qat`), an open-weight model, running locally through **Ollama** on my RTX 3070. About 20 seconds for a month of entries.
- **Spring Boot** (Java) backend with one endpoint, and one plain HTML page.
- Gemma proposes patterns as structured JSON: what keeps happening, on which days, with quotes.
- **Then plain code checks every claim before you see it.** Dates have to be real entries. Quotes have to appear word for word. A pattern needs at least two days and one real quote. Anything else gets dropped. The model suggests, the code decides.

**How I tested it:** I wrote a made-up 28-day journal (not a real person) with three planted patterns on 13 days, plus two planned treat nights that should *not* be flagged. Five runs:

- Takeout after finishing something: **5 of 5 days, every run**
- Phone in bed, then missing the morning: **4 of 4 days, every run**
- "I'll start fresh tomorrow": only caught in **2 of 5 runs.** That's the honest miss.
- **Zero false alarms** in all five runs.

One bug taught me something: Gemma sometimes wrote quotes as "2026-09-08: ..." and my strict check threw them all away, which emptied a whole run. Now the check strips the date first. Another one: when the sample goals just said "cook at home", it flagged the planned treat nights too. That was fair. Your goals decide what counts, so now they can say "the Thursday shawarma night is fine."

**The trade-off I chose:** [your line 3, in your own words]

## Why Does Open Innovation Matter?

Because of what Rewatch reads. A journal has your worst days in it. With an open-weight model running locally, nothing leaves your computer: no account, no API key, no company reading your diary, and it costs nothing per entry. A closed API would have meant asking people to upload exactly the thing they least want anyone to see.

## Prize Categories

Best Use of Gemma
