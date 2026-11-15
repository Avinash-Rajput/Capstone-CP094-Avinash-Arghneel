package com.copycheck.io;

import com.copycheck.model.Document;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

// Reads .txt files from disk and turns them into Documents.
public class FileLoader {

    // Loads one .txt file. The document name is the file name.
    public static Document loadFile(String path) throws IOException {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Please enter a file path.");
        }
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + path);
        }
        if (file.isDirectory()) {
            throw new IllegalArgumentException("That is a folder, not a file: " + path);
        }
        if (!file.getName().toLowerCase(Locale.ROOT).endsWith(".txt")) {
            throw new IllegalArgumentException("Only .txt files are supported: " + path);
        }
        String text = readText(file);
        if (text.isBlank()) {
            throw new IllegalArgumentException("File is empty: " + path);
        }
        return new Document(file.getName(), text);
    }

    // Loads every .txt file in a folder (not sub-folders), sorted by name. Empty files are skipped.
    public static List<Document> loadFolder(String folderPath) throws IOException {
        if (folderPath == null || folderPath.isBlank()) {
            throw new IllegalArgumentException("Please enter a folder path.");
        }
        File folder = new File(folderPath);
        if (!folder.exists()) {
            throw new IllegalArgumentException("Folder not found: " + folderPath);
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("That is not a folder: " + folderPath);
        }
        List<Document> documents = new ArrayList<Document>();
        File[] files = folder.listFiles();
        if (files == null) {
            return documents;
        }
        Arrays.sort(files, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile() && isTextFile(file)) {
                String text = readText(file);
                if (!text.isBlank()) {
                    documents.add(new Document(file.getName(), text));
                }
            }
        }
        return documents;
    }

    // True for a normal .txt file, false for hidden files like ".notes.txt".
    private static boolean isTextFile(File file) {
        String name = file.getName();
        return name.toLowerCase(Locale.ROOT).endsWith(".txt") && !name.startsWith(".");
    }

    // Reads the whole file as UTF-8 and drops the hidden marker some editors put first.
    private static String readText(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        String text = new String(bytes, StandardCharsets.UTF_8);
        if (text.startsWith("﻿")) {
            text = text.substring(1);
        }
        return text;
    }
}
