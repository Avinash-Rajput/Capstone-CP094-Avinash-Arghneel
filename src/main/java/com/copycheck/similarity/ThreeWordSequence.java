package com.copycheck.similarity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Compares word lists by shared 3-word sequences, which catch copied phrases that single words miss.
public class ThreeWordSequence {

    // [a, b, c, d] -> {"a b c", "b c d"}
    public static Set<String> makeSequences(List<String> words) {
        Set<String> sequences = new HashSet<String>();
        if (words == null) {
            return sequences;
        }
        for (int i = 0; i + 2 < words.size(); i++) {
            sequences.add(words.get(i) + " " + words.get(i + 1) + " " + words.get(i + 2));
        }
        return sequences;
    }

    // Shared sequences divided by all distinct sequences (0.0 to 1.0).
    public static double similarity(List<String> words1, List<String> words2) {
        Set<String> sequences1 = makeSequences(words1);
        Set<String> sequences2 = makeSequences(words2);
        if (sequences1.isEmpty() || sequences2.isEmpty()) {
            return 0.0;
        }
        int shared = 0;
        for (String sequence : sequences1) {
            if (sequences2.contains(sequence)) {
                shared++;
            }
        }
        int total = sequences1.size() + sequences2.size() - shared;
        return (double) shared / total;
    }
}
