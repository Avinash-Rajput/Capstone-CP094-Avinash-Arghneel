package com.copycheck.ui;

import com.copycheck.Settings;
import com.copycheck.io.FileLoader;
import com.copycheck.model.Document;
import com.copycheck.model.PairResult;
import com.copycheck.report.ReportPrinter;
import com.copycheck.report.ReportSaver;
import com.copycheck.similarity.Comparer;
import com.copycheck.similarity.Ranker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

// The console menu. This is the only class that talks to the user.
public class Menu {

    private final Scanner scanner;
    private final List<Document> documents = new ArrayList<>();
    // Results of the last report, kept for saving later. Reset whenever the documents change.
    private List<PairResult> lastResults = new ArrayList<>();

    public Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    // Shows the menu until the user chooses Exit.
    public void start() {
        printBanner();
        while (true) {
            showOptions();
            System.out.print("Choose an option: ");
            if (!scanner.hasNextLine()) {
                System.out.println("\nGoodbye!");
                return;
            }
            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) {
                System.out.println("Goodbye!");
                return;
            }
            try {
                handleChoice(choice);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Could not read or write the file: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }
        }
    }

    // Prints the title once, when the program starts.
    private void printBanner() {
        System.out.println("==================================================");
        System.out.println(" CopyCheck - Text Similarity & Plagiarism Checker");
        System.out.println("==================================================");
    }

    // Prints how many documents are loaded, then the options.
    private void showOptions() {
        System.out.println();
        System.out.println("Loaded documents: " + documents.size());
        System.out.println("1. Add document (type or paste text)");
        System.out.println("2. Add document from a .txt file");
        System.out.println("3. Load all .txt files from a folder");
        System.out.println("4. List loaded documents");
        System.out.println("5. Remove a document");
        System.out.println("6. Clear all documents");
        System.out.println("7. Run similarity report");
        System.out.println("8. Show most similar pairs (ranking)");
        System.out.println("9. Save last report to a file");
        System.out.println("0. Exit");
    }

    // Runs the option the user picked.
    private void handleChoice(String choice) throws IOException {
        switch (choice) {
            case "1" -> addTypedDocument();
            case "2" -> addFileDocument();
            case "3" -> addFolderDocuments();
            case "4" -> listDocuments();
            case "5" -> removeDocument();
            case "6" -> clearDocuments();
            case "7" -> runReport();
            case "8" -> showRanking();
            case "9" -> saveReport();
            default -> System.out.println("Invalid choice. Please enter a number from the menu.");
        }
    }

    // Option 1: the user types or pastes text and ends it with a line that says END.
    private void addTypedDocument() {
        String defaultName = "Document " + (documents.size() + 1);
        String name = readLine("Document name (press Enter for \"" + defaultName + "\"): ").trim();
        if (name.isEmpty()) {
            name = defaultName;
        }
        System.out.println("Type or paste the text. Type END on a new line when you are done.");
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().equalsIgnoreCase("END")) {
                break;
            }
            lines.add(line);
        }
        String text = String.join("\n", lines);
        if (text.isBlank()) {
            throw new IllegalArgumentException("The text is empty.");
        }
        addDocument(new Document(name, text.trim()));
    }

    // Option 2: loads one .txt file.
    private void addFileDocument() throws IOException {
        String path = removeQuotes(readLine("Path to .txt file: "));
        addDocument(FileLoader.loadFile(path));
    }

    // Option 3: loads every .txt file in a folder.
    private void addFolderDocuments() throws IOException {
        String path = removeQuotes(readLine("Folder path: "));
        List<Document> loaded = FileLoader.loadFolder(path);
        if (loaded.isEmpty()) {
            System.out.println("No .txt files with text found in that folder.");
            return;
        }
        for (int i = 0; i < loaded.size(); i++) {
            addDocument(loaded.get(i));
        }
        System.out.println("Loaded " + loaded.size() + " documents.");
    }

    // Option 4: shows each document with its word count.
    private void listDocuments() {
        if (documents.isEmpty()) {
            System.out.println("No documents loaded yet.");
            return;
        }
        System.out.println("Loaded documents:");
        for (int i = 0; i < documents.size(); i++) {
            Document document = documents.get(i);
            System.out.println("  " + (i + 1) + ". " + document.getName()
                    + " (" + countWords(document.getText()) + " words)");
        }
    }

    // Option 5: removes one document chosen by its number.
    private void removeDocument() {
        if (documents.isEmpty()) {
            System.out.println("No documents to remove.");
            return;
        }
        listDocuments();
        int number = readNumber("Number of the document to remove (0 to cancel): ");
        if (number == 0) {
            System.out.println("Cancelled.");
            return;
        }
        if (number < 1 || number > documents.size()) {
            throw new IllegalArgumentException("Please enter a number from the list.");
        }
        Document removed = documents.remove(number - 1);
        lastResults = new ArrayList<>();
        System.out.println("Removed \"" + removed.getName() + "\".");
    }

    // Option 6: removes every document after the user confirms.
    private void clearDocuments() {
        if (documents.isEmpty()) {
            System.out.println("No documents to clear.");
            return;
        }
        String answer = readLine("Remove all " + documents.size() + " documents? (y/n): ");
        answer = answer.trim().toLowerCase(Locale.ROOT);
        if (answer.equals("y") || answer.equals("yes")) {
            documents.clear();
            lastResults = new ArrayList<>();
            System.out.println("All documents removed.");
        } else {
            System.out.println("Cancelled.");
        }
    }

    // Option 7: compares every pair and prints the report.
    private void runReport() {
        if (documents.size() < 2) {
            System.out.println("Please load at least two documents first (you have "
                    + documents.size() + ").");
            return;
        }
        lastResults = Comparer.compareAll(documents);
        System.out.print(ReportPrinter.buildReport(lastResults));
    }

    // Option 8: compares every pair and shows the most similar ones first.
    private void showRanking() {
        if (documents.size() < 2) {
            System.out.println("Please load at least two documents first (you have "
                    + documents.size() + ").");
            return;
        }
        lastResults = Comparer.compareAll(documents);
        int size = lastResults.size();
        int defaultCount = Math.min(Settings.TOP_PAIRS, size);
        String input = readLine("How many pairs to show? (1-" + size + ", Enter for "
                + defaultCount + "): ").trim();
        int count = defaultCount;
        if (!input.isEmpty()) {
            count = parseNumber(input);
        }
        if (count < 1 || count > size) {
            throw new IllegalArgumentException("Please enter a number from 1 to " + size + ".");
        }
        System.out.print(ReportPrinter.buildRanking(Ranker.top(lastResults, count)));
        System.out.println("Tip: option 7 shows the common sentences for every pair.");
    }

    // Option 9: writes the last report to a .txt file, a .csv file or both.
    private void saveReport() throws IOException {
        if (lastResults.isEmpty()) {
            System.out.println("Please run a report first (option 7 or 8).");
            return;
        }
        String choice = readLine("Save as 1) .txt  2) .csv  3) both (Enter for 3): ").trim();
        if (choice.isEmpty()) {
            choice = "3";
        }
        if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
            throw new IllegalArgumentException("Please enter 1, 2 or 3.");
        }
        if (choice.equals("1") || choice.equals("3")) {
            System.out.println("Saved: " + ReportSaver.saveAsText(lastResults, Settings.REPORT_FOLDER));
        }
        if (choice.equals("2") || choice.equals("3")) {
            System.out.println("Saved: " + ReportSaver.saveAsCsv(lastResults, Settings.REPORT_FOLDER));
        }
    }

    // Adds a document. If the name is already used, adds " (2)", " (3)"... to keep names unique.
    private void addDocument(Document document) {
        String name = document.getName();
        int copyNumber = 2;
        while (isNameTaken(name)) {
            name = document.getName() + " (" + copyNumber + ")";
            copyNumber++;
        }
        documents.add(new Document(name, document.getText()));
        lastResults = new ArrayList<>();
        System.out.println("Added \"" + name + "\" (" + countWords(document.getText()) + " words).");
    }

    // True if a loaded document already has this name (ignoring upper and lower case).
    private boolean isNameTaken(String name) {
        for (int i = 0; i < documents.size(); i++) {
            if (documents.get(i).getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    // Prints a question and returns the answer, or "" if the input has ended.
    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return "";
        }
        return scanner.nextLine();
    }

    // Asks for a whole number.
    private int readNumber(String prompt) {
        return parseNumber(readLine(prompt));
    }

    // Turns text into a whole number, with a friendly message if it is not one.
    private int parseNumber(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a whole number.");
        }
    }

    // Removes the quotes that Windows "Copy as path" puts around a path.
    private String removeQuotes(String path) {
        String trimmed = path.trim();
        if (trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }

    // Counts words separated by spaces, tabs or new lines.
    private int countWords(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }
}
