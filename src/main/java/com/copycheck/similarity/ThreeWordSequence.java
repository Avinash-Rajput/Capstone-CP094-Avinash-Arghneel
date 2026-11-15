package com.copycheck.similarity;

import java.util.List;
import java.util.Set;

// Compares two word lists by the 3-word sequences they share.
public class ThreeWordSequence {

    // [a, b, c, d] -> {"a b c", "b c d"}
    public static Set<String> makeSequences(List<String> words) {
        throw new UnsupportedOperationException("Not implemented yet - see CC-10");
    }

    // Shared sequences divided by all distinct sequences (0.0 to 1.0).
    public static double similarity(List<String> words1, List<String> words2) {
        throw new UnsupportedOperationException("Not implemented yet - see CC-10");
    }
}
