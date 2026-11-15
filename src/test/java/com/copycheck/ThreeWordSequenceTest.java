package com.copycheck;

import com.copycheck.model.Document;
import com.copycheck.model.PairResult;
import com.copycheck.report.ReportPrinter;
import com.copycheck.similarity.Comparer;
import com.copycheck.similarity.ThreeWordSequence;
import com.copycheck.similarity.WordFrequency;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Tests for ThreeWordSequence: building 3-word sequences and scoring the shared ones.
public class ThreeWordSequenceTest {

    public static void run() {
        System.out.println("\nThreeWordSequenceTest");
        checkMakeSequences();
        checkSimilarity();
        checkWordOrderMatters();
        checkReportShowsSequences();
    }

    // A sliding window of three words, with short lists giving nothing.
    private static void checkMakeSequences() {
        Check.equal("[a, b, c, d] gives two sequences", Set.of("a b c", "b c d"),
                ThreeWordSequence.makeSequences(List.of("a", "b", "c", "d")));
        Check.isTrue("two words give no sequence",
                ThreeWordSequence.makeSequences(List.of("a", "b")).isEmpty());
        Check.isTrue("null gives no sequence", ThreeWordSequence.makeSequences(null).isEmpty());
        Check.equal("a repeated sequence is only counted once",
                Set.of("a b c", "b c a", "c a b"),
                ThreeWordSequence.makeSequences(List.of("a", "b", "c", "a", "b", "c")));
    }

    // Jaccard similarity: shared sequences divided by all different sequences.
    private static void checkSimilarity() {
        List<String> words = List.of("a", "b", "c", "d", "e");
        Check.close("identical lists give 1.0", 1.0, ThreeWordSequence.similarity(words, words));
        List<String> textA = List.of("a", "b", "c", "d");
        List<String> textB = List.of("a", "b", "c", "e");
        Check.close("one shared sequence out of three gives 1/3", 1.0 / 3.0,
                ThreeWordSequence.similarity(textA, textB));
        Check.close("swapping the two lists gives the same score",
                ThreeWordSequence.similarity(textA, textB),
                ThreeWordSequence.similarity(textB, textA));
        Check.close("a short list gives 0.0", 0.0,
                ThreeWordSequence.similarity(List.of("a", "b"), words));
        Check.close("null gives 0.0", 0.0, ThreeWordSequence.similarity(null, words));
    }

    // Why the feature exists: the same words in a new order score 1.0 on words but 0.0 on sequences.
    private static void checkWordOrderMatters() {
        List<String> textA = List.of("dog", "bites", "man", "today");
        List<String> textB = List.of("man", "bites", "dog", "today");
        Check.close("reordered words share no 3-word sequence", 0.0,
                ThreeWordSequence.similarity(textA, textB));
        Check.close("but word frequency still gives 1.0", 1.0, WordFrequency.similarity(textA, textB));

        String text = "Social media harms student focus and sleep every single night.";
        PairResult result = Comparer.compare(new Document("one.txt", text),
                new Document("two.txt", text));
        Check.close("Comparer gives 1.0 for two identical documents", 1.0, result.getSequenceScore());
    }

    // With the setting on, the report shows the 3-word column and the 3-word line.
    private static void checkReportShowsSequences() {
        List<PairResult> results = new ArrayList<PairResult>();
        results.add(new PairResult(new Document("one.txt", "First text."),
                new Document("two.txt", "Second text."), 0.80, 0.25, new ArrayList<String>()));
        String report = ReportPrinter.buildReport(results);
        Check.isTrue("the table has a 3-Word column", report.contains("3-Word"));
        Check.isTrue("the details show the 3-word score", report.contains("3-word similarity:"));
        Check.isTrue("0.25 is shown as 25.00%", report.contains("25.00%"));
    }
}
