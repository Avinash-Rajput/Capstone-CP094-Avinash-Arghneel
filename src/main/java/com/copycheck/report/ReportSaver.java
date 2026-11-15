package com.copycheck.report;

import com.copycheck.model.PairResult;
import java.io.IOException;
import java.util.List;

// Saves the report to a file.
public class ReportSaver {

    // Saves the report as a .txt file in the folder and returns the file path.
    public static String saveAsText(List<PairResult> results, String folder) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet - see CC-11");
    }

    // Saves one row per pair as a .csv file in the folder and returns the file path.
    public static String saveAsCsv(List<PairResult> results, String folder) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet - see CC-11");
    }
}
