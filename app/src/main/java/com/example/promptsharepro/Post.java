package com.example.promptsharepro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    private String title;
    private String author;
    private String llmName;
    private String rating;
    private String notes;
    private List<Comment> comments;

    public Post(String title, String author, String llmName, String rating, String notes) {
        this.title = title;
        this.author = author;
        this.llmName = llmName;
        this.rating = rating;
        this.notes = notes;
        this.comments = new ArrayList<>();
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getLlmName() { return llmName; }
    public String getRating() { return rating; }
    public String getNotes() { return notes; }
    public List<Comment> getComments() { return comments; }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
