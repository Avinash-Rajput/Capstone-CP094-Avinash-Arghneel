package com.copycheck.io;

import com.copycheck.model.Document;
import java.io.IOException;
import java.util.List;

// Reads .txt files from disk and turns them into Documents.
public class FileLoader {

    // Loads one .txt file. The document name is the file name.
    public static Document loadFile(String path) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet - see CC-03");
    }

    // Loads every .txt file in a folder (not sub-folders), sorted by name. Empty files are skipped.
    public static List<Document> loadFolder(String folderPath) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet - see CC-03");
    }
}
