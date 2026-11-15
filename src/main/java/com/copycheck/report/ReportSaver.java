package com.copycheck.report;

import com.copycheck.Settings;
import com.copycheck.model.PairResult;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

// Saves the report to a file.
public class ReportSaver {

    // Saves the report as a .txt file in the folder and returns the file path.
    public static String saveAsText(List<PairResult> results, String folder) throws IOException {
        checkNotEmpty(results);
        Path path = makeFilePath(folder, ".txt");
        Files.writeString(path, ReportPrinter.buildReport(results), StandardCharsets.UTF_8);
        return path.toString();
    }

    // Saves one row per pair as a .csv file in the folder and returns the file path.
    public static String saveAsCsv(List<PairResult> results, String folder) throws IOException {
        checkNotEmpty(results);
        StringBuilder csv = new StringBuilder();
        csv.append("Document A,Document B,Word similarity %,");
        if (Settings.USE_THREE_WORD_SEQUENCES) {
            csv.append("3-word similarity %,");
        }
        csv.append("Verdict,Common sentences\n");
        for (int i = 0; i < results.size(); i++) {
            csv.append(buildRow(results.get(i)));
        }
        Path path = makeFilePath(folder, ".csv");
        Files.writeString(path, csv.toString(), StandardCharsets.UTF_8);
        return path.toString();
    }

    // One CSV row: the two names, the scores, the verdict and how many sentences they share.
    private static String buildRow(PairResult result) {
        StringBuilder row = new StringBuilder();
        row.append(csvValue(result.getFirst().getName())).append(",");
        row.append(csvValue(result.getSecond().getName())).append(",");
        row.append(number(result.getWordScore())).append(",");
        if (Settings.USE_THREE_WORD_SEQUENCES) {
            row.append(number(result.getSequenceScore())).append(",");
        }
        row.append(csvValue(result.getVerdict())).append(",");
        row.append(countSentences(result)).append("\n");
        return row.toString();
    }

    // Stops us from writing a file with no rows in it.
    private static void checkNotEmpty(List<PairResult> results) {
        if (results == null || results.isEmpty()) {
            throw new IllegalArgumentException("Nothing to save. Run a report first.");
        }
    }

    // Makes the folder if needed and builds a name with the date and time, so nothing is overwritten.
    private static Path makeFilePath(String folder, String extension) throws IOException {
        Files.createDirectories(Path.of(folder));
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        return Path.of(folder).resolve("copycheck-report-" + time + extension);
    }

    // 0.913 -> "91.30"
    private static String number(double score) {
        return String.format(Locale.ROOT, "%.2f", score * 100);
    }

    // How many sentences the pair has in common.
    private static int countSentences(PairResult result) {
        List<String> sentences = result.getCommonSentences();
        if (sentences == null) {
            return 0;
        }
        return sentences.size();
    }

    // A value with a comma or a quote in it has to be quoted, or the columns would shift.
    private static String csvValue(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
