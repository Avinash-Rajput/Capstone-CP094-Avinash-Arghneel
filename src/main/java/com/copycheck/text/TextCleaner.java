package com.copycheck.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

// Cleans text so that two documents can be compared fairly.
public class TextCleaner {

    // Common English words we ignore, because every text has plenty of them.
    private static final Set<String> STOP_WORDS = Set.of(
            "a", "about", "above", "after", "again", "against", "all", "also", "am", "an",
            "and", "any", "are", "arent", "as", "at", "be", "because", "been", "before",
            "being", "below", "between", "both", "but", "by", "can", "cant", "could", "couldnt",
            "did", "didnt", "do", "does", "doesnt", "doing", "dont", "down", "during", "each",
            "few", "for", "from", "further", "had", "hadnt", "has", "hasnt", "have", "havent",
            "having", "he", "her", "here", "hers", "herself", "hes", "him", "himself", "his",
            "how", "i", "if", "im", "in", "into", "is", "isnt", "it", "its",
            "itself", "ive", "just", "lets", "may", "me", "might", "more", "most", "must",
            "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only",
            "or", "other", "our", "ours", "ourselves", "out", "over", "own", "same", "shall",
            "she", "shes", "should", "so", "some", "such", "than", "that", "thats", "the",
            "their", "theirs", "them", "themselves", "then", "there", "these", "they", "theyre", "this",
            "those", "through", "to", "too", "under", "until", "up", "us", "very", "was",
            "wasnt", "we", "were", "werent", "weve", "what", "when", "where", "which", "while",
            "who", "whom", "why", "will", "with", "wont", "would", "wouldnt", "you", "youre",
            "your", "yours", "yourself", "yourselves");

    // Lowercase, remove punctuation, squeeze spaces. "Hello, World!" -> "hello world"
    public static String clean(String text) {
        if (text == null) {
            return "";
        }
        String lower = text.toLowerCase(Locale.ROOT);
        String noApostrophes = lower.replace("'", "").replace("’", "").replace("‘", "");
        String spaced = noApostrophes.replaceAll("[^a-z0-9]+", " ");
        return spaced.trim();
    }

    // Cleaned words with common words (the, is, and...) removed.
    public static List<String> getWords(String text) {
        List<String> words = new ArrayList<String>();
        String cleaned = clean(text);
        if (cleaned.isEmpty()) {
            return words;
        }
        String[] parts = cleaned.split(" ");
        for (int i = 0; i < parts.length; i++) {
            if (!isStopWord(parts[i])) {
                words.add(parts[i]);
            }
        }
        return words;
    }

    // True if the word is a common word we ignore.
    public static boolean isStopWord(String word) {
        if (word == null) {
            return false;
        }
        return STOP_WORDS.contains(word);
    }
}
