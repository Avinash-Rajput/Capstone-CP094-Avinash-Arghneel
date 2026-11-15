package com.copycheck;

import com.copycheck.model.Document;
import com.copycheck.model.PairResult;
import com.copycheck.similarity.Comparer;
import java.util.ArrayList;
import java.util.List;

// Tests for Comparer and the verdict: every pair once, and the right scores.
public class ComparerTest {

    public static void run() {
        System.out.println("\nComparerTest");
        checkVerdicts();
        checkPairOrder();
        checkScores();
        checkTooFewDocuments();
    }

    // The verdict text for one word score, built without the Comparer.
    private static String verdictFor(double wordScore) {
        Document first = new Document("a.txt", "Some text.");
        Document second = new Document("b.txt", "Some text.");
        PairResult result = new PairResult(first, second, wordScore, 0.0, new ArrayList<String>());
        return result.getVerdict();
    }

    // 70% and up is HIGH, 40% and up is MEDIUM, the rest is LOW.
    private static void checkVerdicts() {
        Check.isTrue("0.0 is LOW", verdictFor(0.0).startsWith("LOW"));
        Check.isTrue("0.39 is LOW", verdictFor(0.39).startsWith("LOW"));
        Check.isTrue("0.40 is MEDIUM", verdictFor(0.40).startsWith("MEDIUM"));
        Check.isTrue("0.69 is MEDIUM", verdictFor(0.69).startsWith("MEDIUM"));
        Check.isTrue("0.70 is HIGH", verdictFor(0.70).startsWith("HIGH"));
        Check.isTrue("1.0 is HIGH", verdictFor(1.0).startsWith("HIGH"));
    }

    // Three documents give the pairs (1,2), (1,3) and (2,3); four documents give six pairs.
    private static void checkPairOrder() {
        List<Document> three = new ArrayList<Document>();
        three.add(new Document("one.txt", "Cats sleep on warm roofs."));
        three.add(new Document("two.txt", "Dogs chase red balls."));
        three.add(new Document("three.txt", "Birds build small nests."));
        List<PairResult> results = Comparer.compareAll(three);
        Check.equal("three documents give three pairs", 3, results.size());
        Check.equal("pair 1 is one vs two", "one.txt", results.get(0).getFirst().getName());
        Check.equal("pair 1 second name", "two.txt", results.get(0).getSecond().getName());
        Check.equal("pair 2 is one vs three", "one.txt", results.get(1).getFirst().getName());
        Check.equal("pair 2 second name", "three.txt", results.get(1).getSecond().getName());
        Check.equal("pair 3 is two vs three", "two.txt", results.get(2).getFirst().getName());
        Check.equal("pair 3 second name", "three.txt", results.get(2).getSecond().getName());

        three.add(new Document("four.txt", "Fish swim near green weeds."));
        Check.equal("four documents give six pairs", 6, Comparer.compareAll(three).size());
    }

    // The same text scores 1.0, texts with no shared words score 0.0.
    private static void checkScores() {
        String text = "Online learning changed college education. This flexibility helps working students.";
        PairResult same = Comparer.compare(new Document("x.txt", text), new Document("y.txt", text));
        Check.close("identical texts score 1.0", 1.0, same.getWordScore());
        Check.equal("identical texts share both sentences", 2, same.getCommonSentences().size());

        PairResult different = Comparer.compare(
                new Document("x.txt", "Trains arrive every morning."),
                new Document("y.txt", "Buses leave late."));
        Check.close("nothing in common scores 0.0", 0.0, different.getWordScore());
        Check.equal("nothing in common shares no sentences", 0, different.getCommonSentences().size());
    }

    // One document or none is not enough to compare, so the user gets a clear message.
    private static void checkTooFewDocuments() {
        List<Document> one = new ArrayList<Document>();
        one.add(new Document("only.txt", "One lonely document here."));
        boolean thrownForOne = false;
        try {
            Comparer.compareAll(one);
        } catch (IllegalArgumentException error) {
            thrownForOne = true;
        }
        Check.isTrue("one document is refused", thrownForOne);

        boolean thrownForNone = false;
        try {
            Comparer.compareAll(new ArrayList<Document>());
        } catch (IllegalArgumentException error) {
            thrownForNone = true;
        }
        Check.isTrue("an empty list is refused", thrownForNone);
    }
}
