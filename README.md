# CopyCheck - Text Similarity and Plagiarism Detection

CopyCheck is a menu-driven Java console application that compares two or more text submissions and reports how similar each pair is.
For every pair it gives a percentage, a verdict (HIGH, MEDIUM or LOW) and the sentences that appear in both texts.
It is meant as a quick first check for a teacher, not as final proof of copying.

## The brief

> **CopyCheck: Text Similarity and Plagiarism Detection Engine**
> Level: Advanced. Domain: Productivity, Security and Developer Tools.
> Colleges need a quick first check on whether two assignments are suspiciously similar.

Core features:

1. Read two or more text inputs
2. Normalise the text: lowercase, remove punctuation and common words
3. Calculate word-frequency similarity between each pair
4. List sentences that appear in both texts
5. Print a similarity report with a percentage per pair

Stretch features:

1. Compare a set of 10 submissions and rank the most similar pairs
2. Detect similarity using 3-word sequences instead of single words

Extra: save the report to a .txt or .csv file.

## Features

| Feature | Done | Class | Menu option |
|---|---|---|---|
| Core 1: read two or more text inputs | Yes | `FileLoader`, `Menu` | 1 (type or paste), 2 (one .txt file), 3 (a folder of .txt files) |
| Core 2: lowercase, remove punctuation and common words | Yes | `TextCleaner` | Used by 7 and 8 |
| Core 3: word-frequency similarity for each pair | Yes | `WordFrequency`, `Comparer` | 7 |
| Core 4: sentences that appear in both texts | Yes | `SentenceSplitter`, `CommonSentences` | 7 |
| Core 5: report with a percentage for each pair | Yes | `ReportPrinter` | 7 |
| Stretch 1: rank the most similar pairs of 10 submissions | Yes | `Ranker`, `ReportPrinter` | 8 |
| Stretch 2: similarity using 3-word sequences | Yes | `ThreeWordSequence` | 7 and 8 (the 3-Word column) |
| Extra: save the report as .txt / .csv | Yes | `ReportSaver` | 9 |

Options 4, 5 and 6 list, remove and clear the loaded documents. Option 0 exits.

## How to run

You need JDK 17 or newer. Check with `java -version`. Nothing else is needed: no Maven, Gradle or external libraries.

| | Linux / macOS | Windows |
|---|---|---|
| Build | `./build.sh` | `build.bat` |
| Run the app | `./run.sh` | `run.bat` |
| Run the tests | `./test.sh` | `test.bat` |

`run` and `test` build the project first, so they work straight after cloning:

```bash
git clone https://github.com/Avinash-Rajput/Capstone-CP094-Avinash-Arghneel.git
cd Capstone-CP094-Avinash-Arghneel
./run.sh
```

The build compiles everything in `src/` into the `out/` folder and prints `Build OK`.

## Example

A real session: load the `samples/basic` folder with option 3, then show the 3 most similar pairs with option 8. What you type is shown after each prompt. The menu is printed again after every option; it is shown in full only once here.

```
==================================================
 CopyCheck - Text Similarity & Plagiarism Checker
==================================================

Loaded documents: 0
1. Add document (type or paste text)
2. Add document from a .txt file
3. Load all .txt files from a folder
4. List loaded documents
5. Remove a document
6. Clear all documents
7. Run similarity report
8. Show most similar pairs (ranking)
9. Save last report to a file
0. Exit
Choose an option: 3
Folder path: samples/basic
Added "exact_copy.txt" (264 words).
Added "light_edit.txt" (264 words).
Added "original.txt" (264 words).
Added "unrelated.txt" (250 words).
Loaded 4 documents.

Loaded documents: 4
Choose an option: 8
How many pairs to show? (1-6, Enter for 5): 3
========== Top 3 most similar pairs ==========
No.  Document A            Document B              Words   3-Word  Verdict
---  --------------------  --------------------  -------  -------  ---------------------
1    exact_copy.txt        original.txt          100.00%  100.00%  HIGH - likely copied
2    exact_copy.txt        light_edit.txt         89.83%   57.65%  HIGH - likely copied
3    light_edit.txt        original.txt           89.83%   57.65%  HIGH - likely copied
Tip: option 7 shows the common sentences for every pair.

Loaded documents: 4
Choose an option: 0
Goodbye!
```

Option 7 prints the same kind of table for every pair, followed by the details of each pair, including the list of common sentences.

## How it works

**Text cleaning (`TextCleaner`).** `clean()` lowercases the text, deletes apostrophes (so "don't" becomes "dont"), turns every other character that is not a letter a-z or a digit into a space, and squeezes the spaces. `getWords()` splits the cleaned text into words and drops 154 common English words such as "the", "is" and "and". Duplicates and word order are kept. Example: `Don't STOP, it's well-known!` is cleaned to `dont stop its well known`, and the words left are `[stop, well, known]`.

**Word-frequency similarity (`WordFrequency`).** We count how often each word appears in each text and compare the two sets of counts with cosine similarity:

```
              sum of (count in A x count in B) for every word
similarity = --------------------------------------------------
             square root of (sum of A counts squared) x square root of (sum of B counts squared)
```

Example: A = [apple, apple, banana] and B = [apple, banana, banana]. The top is 2x1 + 1x2 = 4. The bottom is the square root of 5 times the square root of 5, which is 5. So the similarity is 4 / 5 = 0.8 (80%). The result is 1.0 when both texts use the same words in the same proportions and 0.0 when they share no words.

**Common sentences (`SentenceSplitter`, `CommonSentences`).** A sentence ends at `.`, `!` or `?` when a space, a new line or the end of the text follows. Each sentence is turned into a key: its cleaned words joined by spaces. Sentences with fewer than 3 real words (`Settings.MIN_SENTENCE_WORDS`) are ignored. The keys of the second text go into a `HashSet`, and each sentence of the first text is looked up in that set. So differences in case, punctuation and common words do not matter, but every real word must be the same.

**3-word sequences (`ThreeWordSequence`).** Every run of 3 cleaned words in a row is one sequence: `[a, b, c, d]` gives `"a b c"` and `"b c d"`. We compare the two sets with Jaccard similarity: shared sequences divided by all different sequences. Single words ignore word order, so "dog bites man" and "man bites dog" score 100% on words but 0% on 3-word sequences. A high 3-Word score means whole phrases were copied.

**Comparing every pair and ranking (`Comparer`, `Ranker`).** `Comparer.compareAll()` compares each pair once: (1,2), (1,3), (2,3) and so on. For n documents that is n x (n - 1) / 2 pairs: 10 submissions give 45 pairs, and 100 would give 4,950. `Ranker` sorts a copy of the results by word score, highest first, and option 8 shows the top pairs (5 by default, `Settings.TOP_PAIRS`).

**Verdict (`PairResult`, `Settings`).** A word score of 70% or more is "HIGH - likely copied", 40% or more is "MEDIUM - needs review", and anything lower is "LOW - looks original". The limits are `HIGH_LIMIT` and `MEDIUM_LIMIT` in `Settings`. The verdict uses the word score only; the 3-Word score is shown next to it so the reader can judge whether phrases were copied too.

**Saving (`ReportSaver`).** Option 9 saves the results of the last report (option 7 or 8) into the `reports` folder, as a .txt file, a .csv file or both. The .txt file holds the same full report that option 7 prints. The .csv file has one row per pair, in the same order as the report: the two names, the word and 3-word percentages, the verdict and the number of common sentences. A name with a comma or a quote in it is put in quotes, so the columns do not shift. Each file name contains the date and time, for example `copycheck-report-20261115-233026.csv`. The saved results are cleared whenever documents are added or removed, so a saved file always matches the loaded documents.

## Project structure

```
src/main/java/com/copycheck/
  Main.java                    starting point of the program
  Settings.java                all the numbers we might want to tweak, in one place
  model/Document.java          one piece of text to compare, such as one student's submission
  model/PairResult.java        the result of comparing two documents, with the verdict
  io/FileLoader.java           reads .txt files and folders and turns them into Documents
  text/TextCleaner.java        lowercase, remove punctuation and common words
  text/SentenceSplitter.java   splits text into sentences
  similarity/WordFrequency.java       word-count (cosine) similarity
  similarity/CommonSentences.java     finds sentences that appear in both texts
  similarity/ThreeWordSequence.java   3-word sequence (Jaccard) similarity
  similarity/Comparer.java            compares every pair of documents
  similarity/Ranker.java              sorts pairs, most similar first
  report/ReportPrinter.java    builds the report and ranking text
  report/ReportSaver.java      saves the last report as .txt / .csv in the reports folder
  ui/Menu.java                 the console menu, the only class that talks to the user
src/test/java/com/copycheck/
  Check.java                   our small test helper
  AllTests.java                runs every test class
  *Test.java                   one test class per production class
samples/                       test texts with known relationships, and EXPECTED.md
docs/                          interview notes and demo script
build / run / test scripts     .sh for Linux and macOS, .bat for Windows
```

## Testing

The brief allows only the Java standard library, so we do not use JUnit. Instead `Check.java` has three small helpers: `isTrue`, `equal`, and `close` (for decimals, allowing a difference of 0.0001). Each part of the program has its own test class, such as `TextCleanerTest` or `WordFrequencyTest`, with a `run()` method, and `AllTests` runs them all, prints the summary and exits with an error code if anything failed.

`./test.sh` builds the project and runs `AllTests`. There are currently 142 tests in 11 test classes, and all of them pass.

The menu is tested by hand and with piped input, for example `printf '3\nsamples/basic\n7\n0\n' | ./run.sh`. The `samples/` folder holds texts with known relationships (an exact copy, a light edit, a partial copy, a paraphrase, unrelated texts and edge cases). `samples/EXPECTED.md` lists what each pair should score and the real results; every expectation is met.

## Limitations

- Abbreviations with a full stop end a sentence early: "Dr. Smith teaches maths." is split into "Dr." and "Smith teaches maths."
- A full stop inside quotes does not end a sentence: `He said "stop." Then he left.` stays one sentence.
- A line without a full stop, such as a title, is joined to the sentence after it, because a new line is treated as a space.
- Synonyms and paraphrases are not detected: "Students cheat on exams." and "Pupils copy in tests." score 0%.
- English only. The common-word list is English, and letters outside a-z are treated as punctuation, so "café" becomes "caf".
- Exact sentence matching only: if one real word in a sentence is changed, it no longer counts as a common sentence.
- The verdict is based on the word score only, so a paraphrase with no copied phrases can still be marked HIGH.

## Team

- &lt;Member A name&gt;
- &lt;Member B name&gt;

How we worked: we split the project into GitHub issues (CC-01 to CC-12), each with its own specification. Every issue was done on its own branch and merged through a pull request that the other member reviewed. Before merging, each branch was updated with the latest `main` and `./test.sh` had to pass, so `main` always builds and passes the tests.

AI assistance: We used AI coding assistants (Claude Code / Antigravity) to help write parts of the code from our own issue specifications; we reviewed, tested and can explain all of it.
