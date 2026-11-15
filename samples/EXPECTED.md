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
| original vs exact_copy | Identical | HIGH (100%) | HIGH (100%) | All | Words 100.00% / 3-Word 100.00% / HIGH / 16 sentences |
| original vs light_edit | Light edit | HIGH | MODERATE-HIGH | 6+ | Words 89.83% / 3-Word 57.65% / HIGH / 8 sentences |
| original vs unrelated | Unrelated | LOW | LOW | 0 | Words 8.59% / 3-Word 0.00% / LOW / 0 sentences |
| original vs punctuation_heavy | Normalisation test | HIGH (~100%) | HIGH | Most | Words 100.00% / 3-Word 100.00% / HIGH / 16 sentences |
| submission_01 vs submission_07 | Near-copy | HIGH | HIGH | Many | Words 97.26% / 3-Word 74.87% / HIGH / 11 sentences |
| submission_03 vs submission_05 | Partial copy | MODERATE-HIGH | MODERATE | About half of 03 | Words 77.93% / 3-Word 44.15% / HIGH / 7 sentences (of 14 in 03) |
| submission_02 vs submission_09 | Vocabulary paraphrase | HIGH | LOW | 0 | Words 80.31% / 3-Word 0.00% / HIGH / 0 sentences |
| Any other submission pair | Independent, same topic | LOW-MODERATE | LOW | 0 | 42 pairs: Words 5.87% to 26.40% / 3-Word 0.00% to 0.30% / all LOW / 0 sentences |

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

## Results

Measured in CC-12 with `./run.sh` (options 2, 3, 7 and 8) and `./test.sh` (142 tests passed). The 3-word check was switched on (`Settings.USE_THREE_WORD_SEQUENCES = true`). In this file MODERATE means 40% to 69%; the app calls the same band MEDIUM.

| Expectation | Result | Explanation |
|---|---|---|
| original vs exact_copy | MET | 100% on both measures and all 16 sentences found. |
| original vs light_edit | MET | Words 89.83% (HIGH), 3-Word 57.65% (MODERATE) and 8 common sentences, more than the 6 required. |
| original vs unrelated | MET | Words 8.59% and 3-Word 0.00%, with no common sentences. |
| original vs punctuation_heavy | MET | 100% on both measures and all 16 sentences found, so cleaning removes the caps, quotes, brackets and extra punctuation. |
| submission_01 vs submission_07 | MET | HIGH on both measures (97.26% and 74.87%) and 11 common sentences. |
| submission_03 vs submission_05 | MET | Words 77.93% (HIGH, inside MODERATE-HIGH), 3-Word 44.15% (MODERATE) and 7 of the 14 sentences of 03 found. |
| submission_02 vs submission_09 | MET | Words 80.31% (HIGH) but 3-Word 0.00% and no common sentences, so the 3-word check corrects the single-word false alarm. |
| Any other submission pair | MET | All 42 pairs are LOW (highest Words 26.40%, for 03 vs 06), 3-Word is at most 0.30% and no pair shares a sentence. |
| Top 3 by word frequency | MET | Option 8 lists 01-07 (97.26%), 02-09 (80.31%) and 03-05 (77.93%) first. The 4th pair is far behind at 26.40%. |
| Top 2 by 3-word sequences | MET | Sorting the 3-Word column of the report gives 01-07 (74.87%) and 03-05 (44.15%). 02-09 drops to 0.00%. Option 8 itself ranks by word score only. |
| edge/empty.txt | MET | Option 2 prints "Error: File is empty: samples/edge/empty.txt" and the menu continues. Option 3 skips the file. |
| edge/whitespace_only.txt | MET | Option 2 prints "Error: File is empty: samples/edge/whitespace_only.txt" and the menu continues. Option 3 skips the file. |
| edge/stopwords_only.txt | MET | Loads (19 words), but no real words remain: 0.00% against original.txt, no common sentences, no crash. |
| edge/punctuation_heavy.txt | MET | Loads normally and scores 100.00% against original.txt (see above). |
| edge/single_sentence.txt | MET | Loads normally (16 words) and scores 13.35% (LOW) against original.txt. |
