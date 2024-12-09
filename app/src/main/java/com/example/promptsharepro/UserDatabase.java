package com.example.promptsharepro;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserDatabase {

    private static final String BASE_URL = "https://promptshareproserver-production.up.railway.app";
    private static UserDatabase instance;

    private UserDatabase() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static synchronized UserDatabase getInstance() {
        if (instance == null) {
            instance = new UserDatabase();
        }
        return instance;
    }

    public boolean registerUser(String userName, String userEmail, String userPassword) {
        try {
            String urlString = BASE_URL + "/registerUser?username=" + userName + "&email=" + userEmail + "&password=" + userPassword;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            Log.e("UserDatabase", "Registration error: " + e.getMessage());
            return false;
        }
    }

    public boolean loginUser(String userEmail, String userPassword) {
        try {

            String urlString = BASE_URL + "/loginUser?email=" + userEmail + "&password=" + userPassword;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            Log.e("UserDatabase", "Login error: " + e.getMessage());
            return false;
        }
    }

    public User getUserByEmail(String email) {
        try {
            String urlString = BASE_URL + "/getUserByEmail?email=" + email;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                conn.disconnect();

                String[] userData = response.toString().replace("{", "").replace("}", "").split(",");
                String userId = userData[0].split(":")[1].replace("\"", "");
                String userName = userData[1].split(":")[1].replace("\"", "");
                String userEmail = userData[2].split(":")[1].replace("\"", "");

                return new User(userId, userName, userEmail, "");
            } else {
                conn.disconnect();
                return null;
            }
        } catch (Exception e) {
            Log.e("UserDatabase", "Error fetching user by email: " + e.getMessage());
            return null;
        }
    }

    public boolean updateUser(String userEmail, String newUserName, String newPassword) {
        try {
            StringBuilder urlString = new StringBuilder(BASE_URL + "/updateUser?email=" + userEmail);
            if (newUserName != null && !newUserName.isEmpty()) {
                urlString.append("&username=").append(newUserName);
            }
            if (newPassword != null && !newPassword.isEmpty()) {
                urlString.append("&password=").append(newPassword);
            }

            URL url = new URL(urlString.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int responseCode = conn.getResponseCode();
            conn.disconnect();

            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            Log.e("UserDatabase", "Update error: " + e.getMessage());
            return false;
        }
    }

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
