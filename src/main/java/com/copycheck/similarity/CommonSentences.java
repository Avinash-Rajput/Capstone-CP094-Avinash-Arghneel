package com.copycheck.similarity;

import com.copycheck.Settings;
import com.copycheck.text.SentenceSplitter;
import com.copycheck.text.TextCleaner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Finds sentences that appear in both texts.
public class CommonSentences {

    // Returns the sentences of text1 that also appear in text2 (ignoring case, punctuation and common words).
    public static List<String> find(String text1, String text2) {
        Set<String> keysInText2 = new HashSet<String>();
        List<String> sentences2 = SentenceSplitter.split(text2);
        for (int i = 0; i < sentences2.size(); i++) {
            String key = makeKey(sentences2.get(i));
            if (key != null) {
                keysInText2.add(key);
            }
        }

        List<String> common = new ArrayList<String>();
        Set<String> alreadyFound = new HashSet<String>();
        List<String> sentences1 = SentenceSplitter.split(text1);
        for (int i = 0; i < sentences1.size(); i++) {
            String sentence = sentences1.get(i);
            String key = makeKey(sentence);
            if (key != null && keysInText2.contains(key) && !alreadyFound.contains(key)) {
                common.add(sentence);
                alreadyFound.add(key);
            }
        }
        return common;
    }

    // The cleaned words of a sentence joined by spaces, or null if the sentence is too short to count.
    private static String makeKey(String sentence) {
        List<String> words = TextCleaner.getWords(sentence);
        if (words.size() < Settings.MIN_SENTENCE_WORDS) {
            return null;
        }
        return String.join(" ", words);
    }
}
