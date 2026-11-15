package com.copycheck.text;

import java.util.List;

// Cleans text so that two documents can be compared fairly.
public class TextCleaner {

    // Lowercase, remove punctuation, squeeze spaces. "Hello, World!" -> "hello world"
    public static String clean(String text) {
        throw new UnsupportedOperationException("Not implemented yet - see CC-04");
    }

    // Cleaned words with common words (the, is, and...) removed.
    public static List<String> getWords(String text) {
        throw new UnsupportedOperationException("Not implemented yet - see CC-04");
    }

    // True if the word is a common word we ignore.
    public static boolean isStopWord(String word) {
        throw new UnsupportedOperationException("Not implemented yet - see CC-04");
    }
}
