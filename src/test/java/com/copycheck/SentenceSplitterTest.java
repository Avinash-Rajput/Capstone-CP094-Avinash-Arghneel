package com.copycheck;

import com.copycheck.text.SentenceSplitter;
import java.util.List;

// Tests for SentenceSplitter: where sentences end and what is skipped.
public class SentenceSplitterTest {

    public static void run() {
        System.out.println("\nSentenceSplitterTest");
        Check.equal("split finds three sentences",
                List.of("Hello world.", "How are you?", "I am fine!"),
                SentenceSplitter.split("Hello world. How are you? I am fine!"));
        Check.equal("a sentence may continue on the next line",
                List.of("First line continues here.", "Second."),
                SentenceSplitter.split("First line\ncontinues here. Second."));
        Check.equal("a dot inside a number does not end the sentence",
                List.of("Version 2.5 is out."),
                SentenceSplitter.split("Version 2.5 is out."));
        Check.equal("the last sentence needs no full stop",
                List.of("No full stop here"),
                SentenceSplitter.split("No full stop here"));
        Check.equal("split of windows line ends",
                List.of("One.", "Two."),
                SentenceSplitter.split("One.\r\nTwo."));
        Check.equal("split of null is empty", List.of(), SentenceSplitter.split(null));
        Check.equal("split of spaces is empty", List.of(), SentenceSplitter.split("   "));
    }
}
