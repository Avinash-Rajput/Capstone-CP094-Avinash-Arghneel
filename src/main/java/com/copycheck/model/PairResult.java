package com.copycheck.model;

import java.util.List;

// The result of comparing two documents.
public class PairResult {

    private final Document first;
    private final Document second;
    private final double wordScore;            // 0.0 to 1.0
    private final double sequenceScore;        // 0.0 to 1.0 (stays 0 while the 3-word check is off)
    private final List<String> commonSentences;

    public PairResult(Document first, Document second, double wordScore,
                      double sequenceScore, List<String> commonSentences) {
        this.first = first;
        this.second = second;
        this.wordScore = wordScore;
        this.sequenceScore = sequenceScore;
        this.commonSentences = commonSentences;
    }

    public Document getFirst() {
        return first;
    }

    public Document getSecond() {
        return second;
    }

    public double getWordScore() {
        return wordScore;
    }

    public double getSequenceScore() {
        return sequenceScore;
    }

    public List<String> getCommonSentences() {
        return commonSentences;
    }

    // Returns "HIGH - likely copied", "MEDIUM - needs review" or "LOW - looks original"
    public String getVerdict() {
        throw new UnsupportedOperationException("Not implemented yet - see CC-07");
    }
}
