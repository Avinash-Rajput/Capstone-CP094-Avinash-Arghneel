package com.copycheck;

import com.copycheck.text.TextCleaner;
import java.util.List;

// Tests for TextCleaner: cleaning text, picking out words and spotting common words.
public class TextCleanerTest {

    public static void run() {
        System.out.println("\nTextCleanerTest");
        checkClean();
        checkGetWords();
        checkStopWords();
    }

    // Lowercase, apostrophes removed and punctuation turned into single spaces.
    private static void checkClean() {
        Check.equal("clean removes punctuation", "hello world", TextCleaner.clean("Hello, World!"));
        Check.equal("clean handles apostrophes, tabs and new lines",
                "dont stop believing", TextCleaner.clean("  Don't   STOP\n\tbelieving "));
        Check.equal("clean splits hyphens and brackets",
                "well known e mail 2024", TextCleaner.clean("well-known e-mail (2024)"));
        Check.equal("clean of only punctuation is empty", "", TextCleaner.clean("!!!...???"));
        Check.equal("clean of null is empty", "", TextCleaner.clean(null));
    }

    // Words in order, duplicates kept, common words dropped.
    private static void checkGetWords() {
        Check.equal("getWords keeps order and duplicates",
                List.of("cat", "hat", "sat", "mat", "cat"),
                TextCleaner.getWords("The cat and the hat sat on the mat. The cat!"));
        Check.equal("getWords of only common words is empty",
                List.of(), TextCleaner.getWords("It is what it is."));
        Check.equal("getWords keeps numbers",
                List.of("top", "10", "tips"), TextCleaner.getWords("Top 10 tips"));
    }

    // Only the common words are stop words, and null is safe.
    private static void checkStopWords() {
        Check.isTrue("the is a stop word", TextCleaner.isStopWord("the"));
        Check.isTrue("student is not a stop word", !TextCleaner.isStopWord("student"));
        Check.isTrue("null is not a stop word", !TextCleaner.isStopWord(null));
    }
}
