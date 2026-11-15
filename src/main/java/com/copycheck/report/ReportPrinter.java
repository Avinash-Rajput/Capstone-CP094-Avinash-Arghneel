package com.copycheck.report;

import com.copycheck.Settings;
import com.copycheck.model.PairResult;
import java.util.List;
import java.util.Locale;

// Builds the similarity report as text.
public class ReportPrinter {

    // Returns the full report: a summary table, then the details of each pair.
    public static String buildReport(List<PairResult> results) {
        if (results == null || results.isEmpty()) {
            return "No results to show.\n";
        }
        StringBuilder report = new StringBuilder();
        report.append("========== CopyCheck Similarity Report ==========\n");
        report.append("Pairs compared: ").append(results.size()).append("\n\n");
        report.append(buildTable(results));
        report.append("\n");
        for (int i = 0; i < results.size(); i++) {
            report.append(buildDetails(i + 1, results.get(i)));
        }
        report.append("========== End of report ==========\n");
        return report.toString();
    }

    // The summary table: a header, a dashed line, then one numbered row per pair.
    private static String buildTable(List<PairResult> results) {
        StringBuilder table = new StringBuilder();
        table.append(formatRow("No.", "Document A", "Document B", "Words", "3-Word", "Verdict"));
        table.append(formatRow("---", "--------------------", "--------------------",
                "-------", "-------", "---------------------"));
        for (int i = 0; i < results.size(); i++) {
            PairResult result = results.get(i);
            table.append(formatRow(String.valueOf(i + 1),
                    shorten(result.getFirst().getName()),
                    shorten(result.getSecond().getName()),
                    percent(result.getWordScore()),
                    percent(result.getSequenceScore()),
                    result.getVerdict()));
        }
        return table.toString();
    }

    // One table row. The 3-word column is only there while that check is switched on.
    private static String formatRow(String number, String nameA, String nameB,
                                    String words, String sequence, String verdict) {
        if (Settings.USE_THREE_WORD_SEQUENCES) {
            return String.format(Locale.ROOT, "%-3s  %-20s  %-20s  %7s  %7s  %s\n",
                    number, nameA, nameB, words, sequence, verdict);
        }
        return String.format(Locale.ROOT, "%-3s  %-20s  %-20s  %7s  %s\n",
                number, nameA, nameB, words, verdict);
    }

    // The details of one pair: the scores, the verdict and the sentences found in both texts.
    private static String buildDetails(int number, PairResult result) {
        StringBuilder details = new StringBuilder();
        details.append("--- Pair ").append(number).append(": ")
                .append(result.getFirst().getName()).append(" vs ")
                .append(result.getSecond().getName()).append(" ---\n");
        details.append("Word similarity: ").append(percent(result.getWordScore())).append("\n");
        if (Settings.USE_THREE_WORD_SEQUENCES) {
            details.append("3-word similarity: ").append(percent(result.getSequenceScore())).append("\n");
        }
        details.append("Verdict: ").append(result.getVerdict()).append("\n");
        List<String> sentences = result.getCommonSentences();
        if (sentences == null || sentences.isEmpty()) {
            details.append("Common sentences: none\n");
        } else {
            details.append("Common sentences (").append(sentences.size()).append("):\n");
            for (int i = 0; i < sentences.size(); i++) {
                details.append("  - ").append(sentences.get(i)).append("\n");
            }
        }
        details.append("\n");
        return details.toString();
    }

    // 0.8745 -> "87.45%"
    private static String percent(double score) {
        return String.format(Locale.ROOT, "%.2f%%", score * 100);
    }

    // Keeps a long document name from breaking the column: 17 characters plus "..."
    private static String shorten(String name) {
        if (name == null) {
            return "";
        }
        if (name.length() > 20) {
            return name.substring(0, 17) + "...";
        }
        return name;
    }
}
