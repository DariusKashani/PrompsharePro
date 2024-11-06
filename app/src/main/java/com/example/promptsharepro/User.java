package com.example.promptsharepro;

public class User {
    public String uid;
    public String displayName;
    public String email;

    public User() {} // Empty constructor required for Firestore

    public User(String uid, String displayName, String email) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
    }
}