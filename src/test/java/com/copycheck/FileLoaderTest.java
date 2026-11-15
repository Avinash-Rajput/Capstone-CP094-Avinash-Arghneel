package com.copycheck;

import com.copycheck.io.FileLoader;
import com.copycheck.model.Document;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// Tests for FileLoader: loading one file, loading a folder and every error message.
public class FileLoaderTest {

    public static void run() {
        System.out.println("\nFileLoaderTest");
        try {
            checkLoadFile();
            checkLoadFolder();
        } catch (IOException problem) {
            Check.isTrue("test files could be created: " + problem.getMessage(), false);
        }
    }

    // One good file, the hidden marker, and every loadFile error.
    private static void checkLoadFile() throws IOException {
        Path folder = Files.createTempDirectory("copycheck");
        write(folder, "a.txt", "Hello world.");
        write(folder, "marker.txt", "﻿Text after the marker.");
        write(folder, "notes.md", "Not a text file.");
        write(folder, "blank.txt", "   ");

        Document doc = FileLoader.loadFile(path(folder, "a.txt"));
        Check.equal("loadFile uses the file name", "a.txt", doc.getName());
        Check.equal("loadFile reads the text", "Hello world.", doc.getText());

        Document marked = FileLoader.loadFile(path(folder, "marker.txt"));
        Check.equal("loadFile removes the hidden marker", "Text after the marker.", marked.getText());

        checkFileFails("a blank path is rejected", "   ");
        checkFileFails("a missing file is rejected", path(folder, "nope.txt"));
        checkFileFails("a folder is rejected", folder.toString());
        checkFileFails("a .md file is rejected", path(folder, "notes.md"));
        checkFileFails("a blank file is rejected", path(folder, "blank.txt"));
    }

    // A folder with good, bad and hidden files, plus the loadFolder errors.
    private static void checkLoadFolder() throws IOException {
        Path folder = Files.createTempDirectory("copycheck");
        write(folder, "b.txt", "Second file.");
        write(folder, "A.txt", "First file.");
        write(folder, "notes.md", "Not a text file.");
        write(folder, "blank.txt", "   ");
        write(folder, ".hidden.txt", "Hidden file.");
        Files.createDirectory(folder.resolve("sub"));

        List<Document> docs = FileLoader.loadFolder(folder.toString());
        Check.equal("loadFolder keeps only the two real .txt files", 2, docs.size());
        Check.equal("loadFolder sorts A.txt first", "A.txt", docs.get(0).getName());
        Check.equal("loadFolder sorts b.txt second", "b.txt", docs.get(1).getName());

        Path empty = Files.createTempDirectory("copycheck");
        Check.equal("an empty folder gives an empty list", 0, FileLoader.loadFolder(empty.toString()).size());

        try {
            FileLoader.loadFolder(path(folder, "missing-folder"));
            Check.isTrue("a missing folder is rejected", false);
        } catch (IllegalArgumentException expected) {
            Check.isTrue("a missing folder is rejected", true);
        }
    }

    private static void checkFileFails(String testName, String path) throws IOException {
        try {
            FileLoader.loadFile(path);
            Check.isTrue(testName, false);
        } catch (IllegalArgumentException expected) {
            Check.isTrue(testName, true);
        }
    }

    private static void write(Path folder, String name, String text) throws IOException {
        Files.write(folder.resolve(name), text.getBytes(StandardCharsets.UTF_8));
    }

    private static String path(Path folder, String name) {
        return folder.toString() + File.separator + name;
    }
}
