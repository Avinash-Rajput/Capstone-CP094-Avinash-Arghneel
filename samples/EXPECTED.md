# Sample dataset - expected results

This folder holds the test inputs for CopyCheck. Every file was written with a known relationship to the others (exact copy, light edit, paraphrase, partial copy or unrelated), so the program's output can be checked against these expectations instead of simply being trusted. The dataset was designed before the similarity algorithms were written.

Bands follow `AppConfig`: HIGH is a score of 70% or more, MODERATE is 40% to 69%, LOW is below 40%.

## Files

| File | Role | How it was made |
|---|---|---|
| `basic/original.txt` | Source essay for the core-feature demo | Original essay (264 words, 16 sentences, 4 paragraphs) on how online learning has changed education for college students |
| `basic/exact_copy.txt` | Exact copy | Byte-for-byte copy of `original.txt` |
| `basic/light_edit.txt` | Light edit | Copy of `original.txt` with capitalisation and punctuation changes, 10 words replaced by synonyms across 7 sentences, one sentence deleted (slow rural internet) and one new sentence added (office hours over video calls); 8 sentences are unchanged apart from case and punctuation |
| `basic/unrelated.txt` | Unrelated text | Original essay on the benefits of community gardening in cities |
| `submissions/submission_01.txt` | Original (Student A) | Balanced essay: social media as a second classroom for collaboration, but open to copying and misinformation |
| `submissions/submission_02.txt` | Original (Student B) | Essay that repeatedly uses the key terms notifications, attention, grades, sleep, procrastination, platforms and focus |
| `submissions/submission_03.txt` | Original (Student C) | Essay (14 sentences) on the emotional side: comparison, anxiety, cyberbullying and online support |
| `submissions/submission_04.txt` | Original (Student D) | First-person account of a 30-day experiment without social apps |
| `submissions/submission_05.txt` | Partial copy of 03 | 7 of the 14 sentences of `submission_03.txt` copied verbatim and spread across 4 paragraphs, with 5 new sentences written around them |
| `submissions/submission_06.txt` | Original (Student E) | Positive view: educational creators, language learning, careers and creative work |
| `submissions/submission_07.txt` | Near-copy of 01 | Same sentences as `submission_01.txt` with capitalisation and punctuation changes, 6 words swapped for synonyms, and paragraphs 2 and 3 swapped |
| `submissions/submission_08.txt` | Original (Student F) | Argument about the effect of short posts on reading stamina and formal writing |
| `submissions/submission_09.txt` | Vocabulary paraphrase of 02 | Every sentence of `submission_02.txt` rewritten with different word order and structure, keeping the same key terms in roughly the same proportions; no run of 3 consecutive content words and no sentence is shared with 02 |
| `submissions/submission_10.txt` | Original (Student G) | Shared-responsibility view: schools, families, regulators and app design |
| `edge/empty.txt` | Edge case | Completely empty file (0 bytes) |
| `edge/whitespace_only.txt` | Edge case | Only spaces, tabs and blank lines |
| `edge/stopwords_only.txt` | Edge case | Three sentences made only of stop words; nothing remains after normalisation |
| `edge/punctuation_heavy.txt` | Normalisation test | The 16 sentences of `original.txt` with ALL CAPS words, extra commas, `!!!`, `...`, quotes, brackets and hyphens added; every sentence still ends with `.`, `!` or `?` |
| `edge/single_sentence.txt` | Edge case | Exactly one sentence (16 words) on social media and students |

## Expected relationships

| Pair | Relationship | Expected word-frequency band | Expected 3-word band | Common sentences expected | Actual (filled in CC-12) |
|---|---|---|---|---|---|
| original vs exact_copy | Identical | HIGH (100%) | HIGH (100%) | All | - |
| original vs light_edit | Light edit | HIGH | MODERATE-HIGH | 6+ | - |
| original vs unrelated | Unrelated | LOW | LOW | 0 | - |
| original vs punctuation_heavy | Normalisation test | HIGH (~100%) | HIGH | Most | - |
| submission_01 vs submission_07 | Near-copy | HIGH | HIGH | Many | - |
| submission_03 vs submission_05 | Partial copy | MODERATE-HIGH | MODERATE | About half of 03 | - |
| submission_02 vs submission_09 | Vocabulary paraphrase | HIGH | LOW | 0 | - |
| Any other submission pair | Independent, same topic | LOW-MODERATE | LOW | 0 | - |

## Expected top 3 by word frequency

When all 10 submissions are compared and ranked by word-frequency similarity, the top 3 pairs should be, in any order:

- submission_01 vs submission_07
- submission_02 vs submission_09
- submission_03 vs submission_05

## Expected top 2 by 3-word sequences

When the same 10 submissions are ranked by 3-word sequence similarity, the top 2 pairs should be, in any order:

- submission_01 vs submission_07
- submission_03 vs submission_05

The pair submission_02 vs submission_09 should drop out of the top. The two essays share vocabulary but no phrases, so single-word similarity raises a false alarm and 3-word sequences correct it.

## Edge cases

| File | Expected behaviour |
|---|---|
| `edge/empty.txt` | Rejected or skipped with a clear message; no crash |
| `edge/whitespace_only.txt` | Rejected or skipped with a clear message; no crash |
| `edge/stopwords_only.txt` | Loads, but no content words remain; similarity with any other text is 0% and no common sentences are listed; no crash |
| `edge/punctuation_heavy.txt` | Works normally; about 100% word-frequency similarity to `basic/original.txt` because normalisation removes the noise |
| `edge/single_sentence.txt` | Works normally like any other document |
