package com.copycheck.similarity;

import com.copycheck.model.Document;
import com.copycheck.model.PairResult;
import java.util.List;

// Compares documents in pairs.
public class Comparer {

    // Compares two documents and collects all the scores.
    public static PairResult compare(Document first, Document second) {
        throw new UnsupportedOperationException("Not implemented yet - see CC-07");
    }

    // Compares every pair once: (1,2), (1,3), (2,3)...
    public static List<PairResult> compareAll(List<Document> documents) {
        throw new UnsupportedOperationException("Not implemented yet - see CC-07");
    }
}
