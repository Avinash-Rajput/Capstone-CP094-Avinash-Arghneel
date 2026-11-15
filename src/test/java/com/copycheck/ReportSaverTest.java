package com.copycheck;

import com.copycheck.model.Document;
import com.copycheck.model.PairResult;
import com.copycheck.report.ReportPrinter;
import com.copycheck.report.ReportSaver;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Tests for ReportSaver: the .txt matches the screen report and the .csv has proper columns.
public class ReportSaverTest {

    public static void run() {
        System.out.println("\nReportSaverTest");
        try {
            String folder = Files.createTempDirectory("copycheck").toString();
            checkTextFile(folder);
            checkCsvFile(folder);
            checkNameWithComma(folder);
            checkNewFolderIsCreated(folder);
        } catch (IOException e) {
            Check.isTrue("saving should not fail: " + e.getMessage(), false);
        }
        checkEmptyListIsRefused();
    }

    // A list holding one result, built by hand so the saving is the only thing tested.
    private static List<PairResult> oneResult(String nameA, String nameB, double wordScore) {
        List<String> sentences = new ArrayList<String>();
        sentences.add("Online learning changed college education.");
        List<PairResult> results = new ArrayList<PairResult>();
        results.add(new PairResult(new Document(nameA, "First text."),
                new Document(nameB, "Second text."), wordScore, 0.7412, sentences));
        return results;
    }

    // The .txt file holds exactly the text that ReportPrinter builds.
    private static void checkTextFile(String folder) throws IOException {
        List<PairResult> results = oneResult("one.txt", "two.txt", 0.913);
        String saved = ReportSaver.saveAsText(results, folder);
        Check.isTrue("the saved .txt file exists", Files.exists(Path.of(saved)));
        Check.isTrue("the file name ends with .txt", saved.endsWith(".txt"));
        Check.equal("the file holds the report text", ReportPrinter.buildReport(results),
                Files.readString(Path.of(saved), StandardCharsets.UTF_8));
    }

    // The .csv file starts with the header row and shows the score as 91.30
    private static void checkCsvFile(String folder) throws IOException {
        String saved = ReportSaver.saveAsCsv(oneResult("one.txt", "two.txt", 0.913), folder);
        Check.isTrue("the saved .csv file exists", Files.exists(Path.of(saved)));
        Check.isTrue("the file name ends with .csv", saved.endsWith(".csv"));
        String text = Files.readString(Path.of(saved), StandardCharsets.UTF_8);
        String firstLine = text.substring(0, text.indexOf("\n"));
        Check.isTrue("the header starts with the two document columns",
                firstLine.startsWith("Document A,Document B,Word similarity %"));
        Check.isTrue("the header ends with the verdict and sentence columns",
                firstLine.endsWith("Verdict,Common sentences"));
        Check.isTrue("0.913 is written as 91.30", text.contains("91.30"));
        Check.isTrue("the verdict is in the row", text.contains("HIGH - likely copied"));
    }

    // A name with a comma or a quote in it is wrapped in quotes, so the columns do not shift.
    private static void checkNameWithComma(String folder) throws IOException {
        String saved = ReportSaver.saveAsCsv(
                oneResult("my \"final\", essay.txt", "two.txt", 0.124), folder);
        String text = Files.readString(Path.of(saved), StandardCharsets.UTF_8);
        Check.isTrue("the name is quoted and its quotes are doubled",
                text.contains("\"my \"\"final\"\", essay.txt\""));
        Check.isTrue("a plain name is not quoted", text.contains(",two.txt,"));
    }

    // A folder that is not there yet is created instead of failing.
    private static void checkNewFolderIsCreated(String folder) throws IOException {
        String newFolder = Path.of(folder).resolve("reports").toString();
        Check.isTrue("the folder does not exist yet", !Files.exists(Path.of(newFolder)));
        String saved = ReportSaver.saveAsCsv(oneResult("a.txt", "b.txt", 0.5), newFolder);
        Check.isTrue("the folder was created", Files.isDirectory(Path.of(newFolder)));
        Check.isTrue("the file is inside the new folder", saved.startsWith(newFolder));
    }

    // With no results there is nothing to save, so both methods complain.
    private static void checkEmptyListIsRefused() {
        Check.isTrue("saving an empty list as .txt is refused",
                textIsRefused(new ArrayList<PairResult>()));
        Check.isTrue("saving an empty list as .csv is refused",
                csvIsRefused(new ArrayList<PairResult>()));
        Check.isTrue("saving null as .txt is refused", textIsRefused(null));
        Check.isTrue("saving null as .csv is refused", csvIsRefused(null));
    }

    // True if saveAsText throws IllegalArgumentException for these results.
    private static boolean textIsRefused(List<PairResult> results) {
        try {
            ReportSaver.saveAsText(results, "reports");
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // True if saveAsCsv throws IllegalArgumentException for these results.
    private static boolean csvIsRefused(List<PairResult> results) {
        try {
            ReportSaver.saveAsCsv(results, "reports");
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
