package com.example.promptsharepro;

import java.io.Serializable;

public class Comment implements Serializable {
    private String text;
    private String author;

    public Comment(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }
}
