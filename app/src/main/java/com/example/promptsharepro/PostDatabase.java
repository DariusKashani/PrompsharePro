package com.example.promptsharepro;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PostDatabase {

    private static final String BASE_URL = "https://promptshareproserver-production.up.railway.app"; // Replace with your FastAPI server URL
    private static PostDatabase instance;

    private PostDatabase() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static synchronized PostDatabase getInstance() {
        if (instance == null) {
            instance = new PostDatabase();
        }
        return instance;
    }

    public List<Post> getAllPosts(String filter) {
        List<Post> posts = new ArrayList<>();
        try {
            String urlString = BASE_URL + "/getAllPosts" + (filter.isEmpty() ? "" : "?filter=" + filter);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONArray postsArray = new JSONObject(response.toString()).getJSONArray("posts");

                for (int i = 0; i < postsArray.length(); i++) {
                    JSONObject postObject = postsArray.getJSONObject(i);
                    Post post = parsePostFromJson(postObject);
                    posts.add(post);
                }
            }
            conn.disconnect();
        } catch (Exception e) {
            Log.e("PostDatabase", "Error fetching posts: " + e.getMessage());
        }
        return posts;
    }

    public Post getPost(String postId) {
        try {
            String urlString = BASE_URL + "/getPost?postId=" + postId;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONObject postObject = new JSONObject(response.toString()).getJSONObject("post");
                return parsePostFromJson(postObject);
            }
            conn.disconnect();
        } catch (Exception e) {
            Log.e("PostDatabase", "Error fetching post: " + e.getMessage());
        }
        return null;
    }

    public boolean createPost(String title, String author, String llm, String notes, String rating) {
        try {
            String urlString = BASE_URL + "/createPost?postTitle=" + title + "&postAuthor=" + author +
                    "&postLLM=" + llm + "&postNotes=" + notes + "&postRating=" + rating;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            Log.e("PostDatabase", "Error creating post: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePost(String postId, String title, String author, String llm, String notes, String rating) {
        try {
            String urlString = BASE_URL + "/updatePost?postId=" + postId + "&postTitle=" + title +
                    "&postAuthor=" + author + "&postLLM=" + llm + "&postNotes=" + notes + "&postRating=" + rating;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            Log.e("PostDatabase", "Error updating post: " + e.getMessage());
            return false;
        }
    }

    public String createComment(String postId, String commentNotes, String commentAuthor) {
        String commentId = null;
        try {
            String urlString = BASE_URL + "/createComment?postId=" + postId + "&commentNotes=" + commentNotes +
                    "&commentAuthor=" + commentAuthor;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("commentId")) {
                    commentId = jsonResponse.getString("commentId");
                }
            }

            conn.disconnect();
        } catch (Exception e) {
            Log.e("PostDatabase", "Error creating comment: " + e.getMessage());
        }

        return commentId;
    }


    public boolean deletePost(String postId) {
        try {
            String urlString = BASE_URL + "/deletePost?postId=" + postId;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            Log.e("PostDatabase", "Error deleting post: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteComment(String postId, String commentId) {
        try {
            String urlString = BASE_URL + "/deleteComment?postId=" + postId + "&commentId=" + commentId;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            Log.e("PostDatabase", "Error deleting comment: " + e.getMessage());
            return false;
        }
    }

    private Post parsePostFromJson(JSONObject postObject) throws Exception {
        String postId = postObject.getString("_id");
        String postTitle = postObject.getString("postTitle");
        String postAuthor = postObject.getString("postAuthor");
        String postLLM = postObject.getString("postLLM");
        String postNotes = postObject.getString("postNotes");
        String postRating = postObject.getString("postRating");

        Post post = new Post(postId, postTitle, postAuthor, postLLM, postNotes, postRating);

        JSONArray commentsArray = postObject.getJSONArray("comments");
        for (int j = 0; j < commentsArray.length(); j++) {
            JSONObject commentObject = commentsArray.getJSONObject(j);
            Comment comment = new Comment(
                    commentObject.getString("commentId"),
                    commentObject.getString("commentNotes"),
                    commentObject.getString("commentAuthor")
            );
            post.addComment(comment);
        }
        return post;
    }

    public static class Post {
        private String postId;
        private String postTitle;
        private String postAuthor;
        private String postLLM;
        private String postNotes;
        private String postRating;
        private List<Comment> comments;

        public Post(String postId, String postTitle, String postAuthor, String postLLM, String postNotes, String postRating) {
            this.postId = postId;
            this.postTitle = postTitle;
            this.postAuthor = postAuthor;
            this.postLLM = postLLM;
            this.postNotes = postNotes;
            this.postRating = postRating;
            this.comments = new ArrayList<>();
        }

        public void addComment(Comment comment) {
            comments.add(comment);
        }

        public String getPostId() { return postId; }
        public String getPostTitle() { return postTitle; }
        public String getPostAuthor() { return postAuthor; }
        public String getPostLLM() { return postLLM; }
        public String getPostNotes() { return postNotes; }
        public String getPostRating() { return postRating; }
        public void setPostRating(String postRating) { this.postRating = postRating; }
        public List<Comment> getComments() { return comments; }
    }

    public static class Comment {
        private String commentId;
        private String commentNotes;
        private String commentAuthor;

        public Comment(String commentId, String commentNotes, String commentAuthor) {
            this.commentId = commentId;
            this.commentNotes = commentNotes;
            this.commentAuthor = commentAuthor;
        }

        public String getCommentId() { return commentId; }
        public String getCommentNotes() { return commentNotes; }
        public String getCommentAuthor() { return commentAuthor; }
    }
}
