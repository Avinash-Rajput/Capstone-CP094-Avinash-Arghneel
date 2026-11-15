# Demo script (5 minutes)

Start in the project folder with a large terminal font. Type exactly what is shown in `code`; "Enter" means just press Enter. The numbers below are the real results on our sample files.

## 0:00 - The problem and our approach

- Say: "Colleges need a quick first check on whether two assignments are suspiciously similar."
- Say: "CopyCheck cleans each text, then compares every pair by word frequency, by 3-word sequences and by shared sentences, and gives each pair a percentage and a verdict."
- Run `./run.sh` (Windows: `run.bat`). Point out `Build OK` and the menu.

## 0:30 - Load a folder

- Press `3`, then type `samples/basic`.
- Point out: four files are added with their word counts, and "Loaded 4 documents."
- Press `4` to list them: `exact_copy.txt`, `light_edit.txt`, `original.txt`, `unrelated.txt`.
- Say: "These are test texts we wrote with known relationships: an exact copy, a light edit and an unrelated essay."

## 1:00 - The similarity report

- Press `7`. Scroll up to the summary table first.
- Point out exact_copy vs original: Words 100.00%, 3-Word 100.00%, "HIGH - likely copied".
- Point out light_edit vs original: Words 89.83%, 3-Word 57.65%, still HIGH. About 10 words were swapped for synonyms and one sentence was replaced, so the 3-word score drops more than the word score.
- Point out the unrelated essay: 8.59% against the original, "LOW - looks original".
- Scroll down to the details of exact_copy vs light_edit: "Common sentences (8)". Say: "These sentences match even though case and punctuation were changed."

## 2:00 - Cleaning works

- Press `2`, then type `samples/edge/punctuation_heavy.txt`.
- Say: "This is the original essay with ALL CAPS, extra commas, quotes, brackets, !!! and ... added."
- Press `7` again. In the table, find original.txt vs punctuation_heavy... (long names are shortened): Words 100.00%, 3-Word 100.00%.
- Say: "Cleaning removes the noise, so the program sees the same words."

## 2:45 - Ranking 10 submissions

- Press `6`, then `y` to remove all 5 documents.
- Press `3`, then type `samples/submissions`. Ten essays are loaded: 45 pairs.
- Press `8`, then Enter (5 pairs by default).
- Point out the top 3: 01-07 (97.26%), 02-09 (80.31%) and 03-05 (77.93%), and then a big gap to 26.40%. Say: "A teacher sees the suspicious pairs first instead of reading 45 results."

## 3:30 - Why 3-word sequences matter (pair 02-09)

- Stay on the ranking table.
- Point out 01-07: high on both columns (97.26% and 74.87%). It is a near-copy.
- Point out 02-09: Words 80.31% but 3-Word 0.00%.
- Say: "Essay 09 uses the same key words as 02 (notifications, attention, focus, grades, sleep), but every sentence is rewritten. Single words raise a false alarm; 3-word sequences show that no phrase was copied. That is why we show both scores."
- Optionally mention 03-05: 44.15% on 3-Word, because half of 03's sentences were copied into 05.

## 4:15 - Save the report

- Press `9`, then Enter to save both files.
- Point out the two "Saved:" lines, for example `reports/copycheck-report-20261115-233026.txt` and `.csv`.
- Open the .csv file (in VS Code or a spreadsheet program). Show the header and the 45 rows, one per pair, with the word and 3-word percentages, the verdict and the number of common sentences. The rows are in pair order (01-02, 01-03, ...), not in ranking order.
- Say: "The file name has the date and time, so older reports are kept."

## 4:40 - Tests and process

- Press `0` to exit.
- Run `./test.sh` (Windows: `test.bat`) and point out "Tests passed: 142, failed: 0" and "ALL TESTS PASSED". Say: "No JUnit, only the standard library: our own small Check class."
- Show the GitHub repository: the closed issues CC-01 to CC-12, one pull request per issue, each reviewed by the other member.

## Before the review

- [ ] A fresh clone works: `git clone`, `cd` into the folder, `./test.sh` prints ALL TESTS PASSED.
- [ ] `java -version` shows 17 or newer on the demo computer.
- [ ] `./test.sh` passes on the final `main`.
- [ ] The terminal font is big enough to read from the back of the room.
- [ ] The terminal is opened in the project folder, so the `samples/...` paths work.
- [ ] The GitHub repository is open in a browser tab.
- [ ] Old files in the `reports` folder are deleted, so only the new report shows.
- [ ] Both of us have done the demo once from start to finish and can answer the questions in `docs/INTERVIEW_NOTES.md` without looking.
