package com.copycheck.text;

import java.util.ArrayList;
import java.util.List;

// Splits text into sentences.
public class SentenceSplitter {

    // A sentence ends with . ! or ? followed by a space, a new line, or the end of the text.
    public static List<String> split(String text) {
        List<String> sentences = new ArrayList<String>();
        if (text == null) {
            return sentences;
        }
        String oneLine = text.replace('\r', ' ').replace('\n', ' ');
        StringBuilder sentence = new StringBuilder();
        for (int i = 0; i < oneLine.length(); i++) {
            char letter = oneLine.charAt(i);
            sentence.append(letter);
            boolean isEndMark = letter == '.' || letter == '!' || letter == '?';
            boolean isLast = i == oneLine.length() - 1;
            boolean spaceNext = !isLast && Character.isWhitespace(oneLine.charAt(i + 1));
            if (isEndMark && (isLast || spaceNext)) {
                addSentence(sentences, sentence.toString());
                sentence.setLength(0);
            }
        }
        addSentence(sentences, sentence.toString());
        return sentences;
    }

    // Tidies the spaces and only keeps a sentence that has something in it.
    private static void addSentence(List<String> sentences, String sentence) {
        String tidy = sentence.trim().replaceAll("\\s+", " ");
        if (!tidy.isEmpty()) {
            sentences.add(tidy);
        }
    }
}
