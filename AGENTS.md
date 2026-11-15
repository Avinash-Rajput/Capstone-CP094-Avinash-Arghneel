# Rules for AI coding assistants

We use AI assistants (Claude Code / Antigravity) to help write this project. These rules keep the code simple enough for us to explain every line. Each GitHub issue repeats them in its prompt.

PROJECT RULES (follow all of them):
1. Project: CopyCheck, a menu-driven Java console app that checks how similar text submissions are. It is a training capstone: two students must be able to read, explain and change every line in a review. Write code the way a good student would: simple and clear, not clever.
2. Java 17, standard library only. No Maven, Gradle, JUnit or any other external library.
3. Console only. No GUI, web or database. The only file output allowed is .txt and .csv.
4. Keep it simple:
   - Plain classes with private fields, a constructor and getters. No records, no inheritance, no new interfaces, no generic classes of your own.
   - Plain for-loops and if/else. No streams, no Optional, no var, no reflection, no method references (Foo::bar). A short lambda is allowed only for sorting.
   - Short methods (about 25 lines or less). Clear variable names (wordCounts, not wc).
   - Do not add classes, public methods or files that this task does not list.
5. Comments: one short line above each class and each public method, in plain English, saying what it does or why. No Javadoc blocks, no long comments, no comments that just repeat the code.
6. Do not change the name, parameters or return type of any existing public method. The rest of the team's code depends on them.
7. Only the Menu class prints to the console. Other classes return values, or throw IllegalArgumentException with a clear message for the user.
8. Wrong user input must never crash the program with a stack trace.
9. Use Locale.ROOT in toLowerCase() and String.format(). Read and write files as UTF-8.
10. Formatting: 4 spaces, no tabs, no wildcard imports (no import java.util.*).
11. Tests: put test classes in src/test/java/com/copycheck/. Each has public static void run() that prints its name and uses Check.isTrue, Check.equal and Check.close. Then remove the // in front of its line in AllTests.java. Do not change Check.java.
12. Build with ./build.sh and test with ./test.sh (Windows: build.bat, test.bat). Before you finish, the build must print "Build OK" and the tests "ALL TESTS PASSED".
13. Only touch the files listed under FILES. If something else seems to need a change, stop and tell me instead.
14. Do not run git commands. I review the changes and commit them myself.

## Project structure

```
src/main/java/com/copycheck/
  Main.java              starts the program
  Settings.java          numbers we may want to change (limits, folder names)
  model/                 Document, PairResult
  io/                    FileLoader            reads .txt files and folders
  text/                  TextCleaner           lowercase, punctuation, common words
                         SentenceSplitter      text -> sentences
  similarity/            WordFrequency         cosine similarity of word counts
                         CommonSentences       sentences found in both texts
                         ThreeWordSequence     3-word sequence similarity
                         Comparer              compares every pair
                         Ranker                sorts pairs, most similar first
  report/                ReportPrinter         builds the report text
                         ReportSaver           saves .txt / .csv
  ui/                    Menu                  the only class that talks to the user
src/test/java/com/copycheck/
  Check.java, AllTests.java, *Test.java
samples/                 test texts
```

## Commands

| | Linux / macOS | Windows |
|---|---|---|
| Build | `./build.sh` | `build.bat` |
| Test | `./test.sh` | `test.bat` |
| Run | `./run.sh` | `run.bat` |
