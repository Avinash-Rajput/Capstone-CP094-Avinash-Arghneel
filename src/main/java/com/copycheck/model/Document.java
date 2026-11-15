package com.copycheck.model;

// One piece of text to compare, for example one student's submission.
public class Document {

    private final String name;
    private final String text;

    public Document(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
