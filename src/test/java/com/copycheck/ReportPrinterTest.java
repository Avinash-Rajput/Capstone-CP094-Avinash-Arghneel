package com.copycheck;

import com.copycheck.model.Document;
import com.copycheck.model.PairResult;
import com.copycheck.report.ReportPrinter;
import java.util.ArrayList;
import java.util.List;

// Tests for ReportPrinter: the report text holds the scores, verdicts and names.
public class ReportPrinterTest {

    public static void run() {
        System.out.println("\nReportPrinterTest");
        checkEmptyList();
        checkOnePair();
        checkNoCommonSentences();
        checkLongName();
    }

    // A list holding one result, built by hand so the report is the only thing tested.
    private static List<PairResult> oneResult(String nameA, String nameB,
                                             double wordScore, List<String> commonSentences) {
        List<PairResult> results = new ArrayList<PairResult>();
        results.add(new PairResult(new Document(nameA, "First text."),
                new Document(nameB, "Second text."), wordScore, 0.0, commonSentences));
        return results;
    }

    // Nothing to report gives one short line.
    private static void checkEmptyList() {
        Check.equal("an empty list says there is nothing to show",
                "No results to show.\n", ReportPrinter.buildReport(new ArrayList<PairResult>()));
    }

    // The usual case: header, score, verdict, names and the common sentences.
    private static void checkOnePair() {
        List<String> sentences = new ArrayList<String>();
        sentences.add("Online learning changed college education.");
        String report = ReportPrinter.buildReport(oneResult("one.txt", "two.txt", 0.8745, sentences));
        Check.isTrue("the report has a title", report.contains("CopyCheck Similarity Report"));
        Check.isTrue("the report counts the pairs", report.contains("Pairs compared: 1"));
        Check.isTrue("0.8745 is shown as 87.45%", report.contains("87.45%"));
        Check.isTrue("0.8745 is a HIGH verdict", report.contains("HIGH - likely copied"));
        Check.isTrue("the first name is in the report", report.contains("one.txt"));
        Check.isTrue("the second name is in the report", report.contains("two.txt"));
        Check.isTrue("the common sentences are counted", report.contains("Common sentences (1):"));
        Check.isTrue("the shared sentence is listed",
                report.contains("  - Online learning changed college education.\n"));
        Check.isTrue("the report ends properly", report.contains("End of report"));

        String full = ReportPrinter.buildReport(oneResult("a.txt", "b.txt", 1.0, sentences));
        Check.isTrue("1.0 is shown as 100.00%", full.contains("100.00%"));
    }

    // A pair with no shared sentences says so instead of printing an empty list.
    private static void checkNoCommonSentences() {
        String report = ReportPrinter.buildReport(
                oneResult("a.txt", "b.txt", 0.1, new ArrayList<String>()));
        Check.isTrue("no shared sentences says none", report.contains("Common sentences: none"));
        Check.isTrue("0.1 is a LOW verdict", report.contains("LOW - looks original"));
    }

    // A long name is cut short in the table but stays complete in the details.
    private static void checkLongName() {
        String longName = "long_student_submission_01.txt";
        Check.equal("the test name is 30 characters", 30, longName.length());
        String report = ReportPrinter.buildReport(
                oneResult(longName, "b.txt", 0.5, new ArrayList<String>()));
        Check.isTrue("the table shows the first 17 characters and dots",
                report.contains(longName.substring(0, 17) + "..."));
        Check.isTrue("the details show the full name", report.contains(longName));
    }
}
