package com.copycheck.similarity;

import com.copycheck.Settings;
import com.copycheck.model.Document;
import com.copycheck.model.PairResult;
import com.copycheck.text.TextCleaner;
import java.util.ArrayList;
import java.util.List;

// Compares documents in pairs.
public class Comparer {

    // Compares two documents and collects all the scores.
    public static PairResult compare(Document first, Document second) {
        List<String> words1 = TextCleaner.getWords(first.getText());
        List<String> words2 = TextCleaner.getWords(second.getText());
        double wordScore = WordFrequency.similarity(words1, words2);
        double sequenceScore = 0.0;
        if (Settings.USE_THREE_WORD_SEQUENCES) {
            sequenceScore = ThreeWordSequence.similarity(words1, words2);
        }
        List<String> common = CommonSentences.find(first.getText(), second.getText());
        return new PairResult(first, second, wordScore, sequenceScore, common);
    }

    // Compares every pair once: (1,2), (1,3), (2,3)...
    public static List<PairResult> compareAll(List<Document> documents) {
        if (documents == null || documents.size() < 2) {
            throw new IllegalArgumentException("Load at least two documents to compare.");
        }
        List<PairResult> results = new ArrayList<PairResult>();
        for (int i = 0; i < documents.size(); i++) {
            for (int j = i + 1; j < documents.size(); j++) {
                results.add(compare(documents.get(i), documents.get(j)));
            }
        }
        return results;
    }
}
