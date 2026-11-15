package com.copycheck;

import com.copycheck.similarity.CommonSentences;
import java.util.List;

// Tests for CommonSentences: which sentences count as the same in both texts.
public class CommonSentencesTest {

    public static void run() {
        System.out.println("\nCommonSentencesTest");
        Check.equal("finds the one shared sentence",
                List.of("Social media affects student focus."),
                CommonSentences.find("Social media affects student focus. Cats are great pets.",
                        "Dogs are loyal animals. Social media affects student focus."));
        Check.equal("case and punctuation do not matter",
                List.of("Online classes save travel time."),
                CommonSentences.find("Online classes save travel time.",
                        "ONLINE classes, save travel time!"));
        Check.equal("common words do not matter",
                1,
                CommonSentences.find("The students use the phone during lectures.",
                        "Students use a phone during lectures.").size());
        Check.equal("sentences with fewer than three real words are skipped",
                List.of(),
                CommonSentences.find("It is true. Yes indeed.", "It is true. Yes indeed."));
        Check.equal("exactly three real words still counts",
                1,
                CommonSentences.find("Students skip lectures.", "students skip lectures").size());
        Check.equal("a repeated sentence is reported only once",
                List.of("Group work builds teamwork."),
                CommonSentences.find("Group work builds teamwork. Group work builds teamwork.",
                        "Group work builds teamwork."));
        Check.equal("results follow the order of text1",
                List.of("Phones distract young learners.", "Exams measure real understanding."),
                CommonSentences.find("Phones distract young learners. Exams measure real understanding.",
                        "Exams measure real understanding. Phones distract young learners."));
        Check.equal("no shared sentence gives an empty list",
                List.of(),
                CommonSentences.find("Trains arrive every morning.", "Buses leave every evening."));
        Check.equal("a null text gives an empty list",
                List.of(),
                CommonSentences.find(null, "Some text here."));
    }
}
