package com.example.promptsharepro;

import java.util.Date;

public class Comment {
    public String id;
    public String postId;
    public String content;
    public Date timestamp;

    public Comment() {} // Empty constructor required for Firestore

    public Comment(String postId, String content) {
        this.postId = postId;
        this.content = content;
        this.timestamp = new Date(); // Set timestamp on creation
    }
}
