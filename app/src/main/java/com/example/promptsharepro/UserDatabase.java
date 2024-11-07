package com.example.promptsharepro;

import java.util.HashMap;
import java.util.Map;

public class UserDatabase {

    private static UserDatabase instance;
    private Map<String, User> users; // Map to store users by userId

    private UserDatabase() {
        users = new HashMap<>();
        // Initialize with sample user data if needed
    }

    public static synchronized UserDatabase getInstance() {
        if (instance == null) {
            instance = new UserDatabase();
        }
        return instance;
    }

    public User getUserByEmail(String email) {
        for (User user : users.values()) {
            if (user.getUserEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    // Register a new user
    public boolean registerUser(String userId, String userName, String userEmail, String userPassword) {
        if (users.containsKey(userId)) {
            return false; // User with this ID already exists
        }
        users.put(userId, new User(userId, userName, userEmail, userPassword));
        return true;
    }

    // Delete an existing user by userId
    public boolean deleteUser(String userId) {
        return users.remove(userId) != null;
    }

    // Login a user by checking email and password
    public boolean loginUser(String userEmail, String userPassword) {
        for (User user : users.values()) {
            if (user.getUserEmail().equals(userEmail) && user.getUserPassword().equals(userPassword)) {
                return true; // Login successful
            }
        }
        return false; // Login failed
    }

    // Inner User class
    public static class User {
        private String userId;
        private String userName;
        private String userEmail;
        private String userPassword;

        public User(String userId, String userName, String userEmail, String userPassword) {
            this.userId = userId;
            this.userName = userName;
            this.userEmail = userEmail;
            this.userPassword = userPassword;
        }

        public String getUserId() {
            return userId;
        }


        public String getUserName() {
            return userName;
        }


        public String getUserEmail() {
            return userEmail;
        }

        public String getUserPassword() {
            return userPassword;
        }
    }

}
