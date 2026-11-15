# Interview notes

Short answers to the questions we expect in the review. Every answer refers to our real classes, and every number comes from running the app on the files in `samples/`.

## String processing

**Why do we remove common words? What happens without it?**
Words like "the", "is" and "and" appear in every English text, so they make any two texts look alike. `TextCleaner.getWords()` drops a fixed list of 154 such words and keeps only the words that show what a text is about. We measured the difference: `original.txt` (online learning) against `unrelated.txt` (community gardens) scores 8.59% with common words removed, but 55.47% if we keep them. That would turn a clearly unrelated pair into "MEDIUM - needs review".

**Walk through `clean("Don't STOP, it's well-known!")`.**
First the text is lowercased with `Locale.ROOT`: `don't stop, it's well-known!`. Then apostrophes are deleted: `dont stop, its well-known!`. Then every run of characters that are not a-z or 0-9 becomes one space, so the comma, hyphen and `!` turn into spaces, and the result is trimmed: `dont stop its well known`. `getWords()` then splits this into words and removes the common words "dont" and "its", leaving `[stop, well, known]`.

**How does `SentenceSplitter` decide where a sentence ends?**
It reads the text one character at a time. A sentence ends at `.`, `!` or `?` when the next character is a space or a new line, or when it is the last character. New lines are first turned into spaces, so a sentence can continue on the next line. "2.5" does not split because no space follows the dot, but "Dr. Smith" does split, which is a known limitation.

**Why does the program not handle other languages well?**
The common-word list is English only. Also, `clean()` keeps only the letters a-z and the digits 0-9, so accented letters count as punctuation: "café" becomes "caf". Supporting other languages would need a different word list and keeping all Unicode letters.

## Similarity measures

**Explain cosine similarity with the apple/banana example. Why can it not be negative here?**
`WordFrequency` counts how often each word appears, so A = [apple, apple, banana] gives apple=2, banana=1, and B = [apple, banana, banana] gives apple=1, banana=2. The top of the formula multiplies the counts of shared words and adds them: 2x1 + 1x2 = 4. The bottom multiplies the "lengths" of the two count lists: the square root of (2² + 1²) times the square root of (1² + 2²), which is √5 x √5 = 5. So the score is 4 / 5 = 0.8. It cannot be negative because word counts are never negative, so every product on top is zero or more.

**Why does `[cat]` vs `[cat, cat]` score 100%?**
Cosine similarity compares proportions, not length. Both lists are 100% "cat", so they point in exactly the same direction and score 1.0. This is useful: a short copied text inside a longer one still shows up as similar.

**Why Jaccard for 3-word sequences but cosine for single words?**
Single words repeat a lot, and how often a key word is used says something about a text, so we compare counts with cosine. A 3-word sequence almost never appears twice in the same text, so counting adds nothing; the only question is whether a phrase appears in both texts. Jaccard answers that directly: shared sequences divided by all different sequences. It also lowers the score when one text has many phrases the other does not.

**Two essays say the same thing in different words. Would we catch it?**
Honestly, no. We compare the exact words, so "Students cheat on exams." and "Pupils copy in tests." score 0%. If the essays use the same words in a different order, the word score still catches it, as with pair 02-09. To catch real paraphrases we would need a synonym list, stemming (treating "student" and "students" as one word), or a language model that compares meaning, which is beyond the standard library.

**Why does pair 02-09 score high on Words and low on 3-Word?**
`submission_09.txt` is a paraphrase of `submission_02.txt` that we wrote on purpose: it uses the same key words (notifications, attention, focus, grades, sleep, procrastination, platforms) in about the same amounts, but every sentence is rewritten. So the word score is high (80.31%), but no 3 real words appear in the same order in both texts, so the 3-Word score is 0.00% and there are no common sentences. This is the case where single words give a false alarm and 3-word sequences correct it. The verdict still says HIGH because it uses the word score only, so the reader should look at both columns.

## Pairwise comparison

**How many comparisons for 10 documents? For 100?**
`Comparer.compareAll()` compares each pair once, so n documents give n x (n - 1) / 2 pairs. 10 documents give 45 pairs and 100 documents give 4,950. Each comparison only reads the two texts a few times, so even 4,950 pairs of essays take very little time. Sorting the 45 results in `Ranker` takes no noticeable time at all.

**How does the `HashSet` make finding common sentences fast?**
`CommonSentences.find()` turns every sentence into a key (its cleaned words joined by spaces) and puts all the keys of the second text into a `HashSet`. For each sentence of the first text it then asks the set "do you contain this key?", which takes about the same time however big the set is. So the work grows with the number of sentences in both texts added together, instead of comparing every sentence with every other sentence. A second set, `alreadyFound`, makes sure each sentence is reported only once.

**How does the ranking work?**
`Ranker.sortByWordScore()` copies the list and sorts the copy with `(a, b) -> Double.compare(b.getWordScore(), a.getWordScore())`. `Double.compare` returns a negative number, zero or a positive number, and putting `b` before `a` gives the highest score first. We sort a copy so the original list, which the full report uses, keeps its order. `top(results, n)` then takes the first n.

## Our code and process

**How did two people work at the same time without breaking each other's code?**
The first issue (CC-01) created every class with its public methods already named, so we both knew what the other's code would look like before it existed. Each later issue listed exactly which files it may change, was done on its own branch, and was merged through a pull request that the other person reviewed. Before merging we pulled the latest `main` into the branch and ran `./test.sh`. Each issue also un-commented only its own line in `AllTests.java`, so tests could be added in parallel.

**How do we test without JUnit?**
The brief allows only the standard library, so we wrote `Check.java` with three helpers: `isTrue`, `equal`, and `close` (for decimals, because 4/5 comes out as 0.7999999999999998). Each part of the program has its own test class, such as `WordFrequencyTest`, with a `run()` method, and `AllTests` runs them all and prints the summary. `./test.sh` runs 142 tests in 11 test classes. The menu is tested by hand and with piped input, and `samples/EXPECTED.md` checks the results on real texts.

**Why does only `Menu` print to the console?**
All other classes return values or throw an `IllegalArgumentException` with a friendly message. That makes them easy to test, because a test can simply check the returned value. It also lets us reuse them: `ReportPrinter.buildReport()` returns a `String`, so the same text is printed by `Menu` (option 7) and written to a .txt file by `ReportSaver` (option 9). And all the wording the user sees is in one place.

**How does the program avoid crashing on bad input?**
`Menu.start()` wraps every option in a `try/catch`. An `IllegalArgumentException` is shown as "Error: ..." with its message, a file problem as "Could not read or write the file: ...", and any other problem as "Something went wrong: ...". After the message, the menu simply shows again. When the input ends (Ctrl+D), `readLine()` returns an empty answer and the menu says "Goodbye!" instead of crashing.

**How does saving to a .csv file work?**
`ReportSaver.saveAsCsv()` writes one header line and then one row per pair: the two document names, the word and 3-word percentages, the verdict and the number of common sentences. If a name contains a comma or a quote, it is put in quotes (and quotes inside it are doubled), otherwise a spreadsheet would split it into two columns. The `reports` folder is created if it does not exist, and the file name contains the date and time, so an older report is not replaced. The `reports` folder is listed in `.gitignore`, so saved reports are not committed.

**Where would you change the HIGH / MEDIUM limits?**
In `Settings.java`: `HIGH_LIMIT` is 0.70 and `MEDIUM_LIMIT` is 0.40. `PairResult.getVerdict()` reads them, so changing one number changes every report. The minimum sentence length (3 real words) and the default number of ranked pairs (5) are there too.
