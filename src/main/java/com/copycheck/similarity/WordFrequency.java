package com.copycheck.similarity;

import java.util.List;
import java.util.Map;

// Compares two word lists by how often each word appears (cosine similarity).
public class WordFrequency {

    // Counts each word. [a, b, a] -> {a=2, b=1}
    public static Map<String, Integer> countWords(List<String> words) {
        throw new UnsupportedOperationException("Not implemented yet - see CC-05");
    }

    // Returns 0.0 (nothing in common) to 1.0 (same words in the same proportions).
    public static double similarity(List<String> words1, List<String> words2) {
        throw new UnsupportedOperationException("Not implemented yet - see CC-05");
    }
}
