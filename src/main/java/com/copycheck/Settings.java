package com.copycheck;

// All the numbers we might want to tweak, kept in one place.
public class Settings {

    // A pair scoring 70% or more is HIGH, 40% or more is MEDIUM, below that is LOW
    public static final double HIGH_LIMIT = 0.70;
    public static final double MEDIUM_LIMIT = 0.40;

    // A sentence needs at least this many real words to count as a common sentence
    public static final int MIN_SENTENCE_WORDS = 3;

    // How many pairs the ranking shows by default
    public static final int TOP_PAIRS = 5;

    // Folder where saved reports go
    public static final String REPORT_FOLDER = "reports";

    // Set to true once the 3-word sequence check is ready
    public static final boolean USE_THREE_WORD_SEQUENCES = true;
}
