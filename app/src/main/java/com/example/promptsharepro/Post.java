package com.example.promptsharepro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Post {
    public String id;
    public String userId;
    public String title; // New field for post title
    public ArrayList<String> titleWords; // New field for post title keywords
    public String content;
    public ArrayList<String> contentWords; // New field for post content keywords;
    public String authorNotes;
    public ArrayList<String> authorNotesWords;
    public String LLMType; // New field for LLM type
    public int num_ratings;// New field for post rating
    public int ratings;
    public Date timestamp;

    public Post() {} // Empty constructor required for Firestore

    public Post(String userId, String title, String content, String notes, String LLMType) {
        this.userId = userId;

        this.title = title;
        this.titleWords = transformString(title);
        this.content = content;
        this.contentWords = transformString(content);
        this.authorNotes = notes;
        this.authorNotesWords = transformString(notes);

        this.LLMType = LLMType;
        this.num_ratings = 0;
        this.ratings = 0;
        this.timestamp = new Date();
    }
    public static ArrayList<String> transformString(String input) {
        String[] words = input.toLowerCase().replaceAll("[^a-z0-9\\s]", "").split("\\s+");
        return new ArrayList<>(Arrays.asList(words));
    }
}
