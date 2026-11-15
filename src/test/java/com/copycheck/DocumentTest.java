package com.copycheck;

import com.copycheck.model.Document;
import com.copycheck.model.PairResult;
import java.util.List;

// Tests for Document and PairResult.
public class DocumentTest {

    public static void run() {
        System.out.println("\nDocumentTest");

        Document doc = new Document("essay.txt", "Some text.");
        Check.equal("document keeps its name", "essay.txt", doc.getName());
        Check.equal("document keeps its text", "Some text.", doc.getText());

        Document other = new Document("other.txt", "More text.");
        PairResult result = new PairResult(doc, other, 0.8, 0.3, List.of("Some text."));
        Check.equal("result keeps the first document", "essay.txt", result.getFirst().getName());
        Check.equal("result keeps the second document", "other.txt", result.getSecond().getName());
        Check.close("result keeps the word score", 0.8, result.getWordScore());
        Check.close("result keeps the sequence score", 0.3, result.getSequenceScore());
        Check.equal("result keeps the common sentences", 1, result.getCommonSentences().size());
    }
}
