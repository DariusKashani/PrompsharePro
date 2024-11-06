package com.example.promptsharepro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDatabase {

    private static PostDatabase instance;
    private Map<String, Post> posts; // Map to store posts by postId

    private PostDatabase() {
        posts = new HashMap<>();
        initializeSamplePosts();
    }

    public static synchronized PostDatabase getInstance() {
        if (instance == null) {
            instance = new PostDatabase();
        }
        return instance;
    }

    // Sample posts initialization
    private void initializeSamplePosts() {
        Post post1 = new Post("1", "CSCI 270 Homework", "Aaron Cote", "Claude Anthropic", "270 is a very cool class.", 4);
        post1.addComment(new Comment("1", "Great post!", "User A"));
        post1.addComment(new Comment("2", "Very informative.", "User B"));

        Post post2 = new Post("2", "Apple IOS Dev", "Marco Papa", "ChatGPT", "Apple is very cool.", 2);
        post2.addComment(new Comment("3", "Thanks for sharing!", "User C"));

        posts.put(post1.getPostId(), post1);
        posts.put(post2.getPostId(), post2);
    }

    // Get all posts, optionally filtered by title, author, LLM, or notes
    public List<Post> getAllPosts(String filter) {
        List<Post> filteredPosts = new ArrayList<>();
        for (Post post : posts.values()) {
            if (filter.isEmpty() ||
                    post.getPostTitle().toLowerCase().contains(filter.toLowerCase()) ||
                    post.getPostAuthor().toLowerCase().contains(filter.toLowerCase()) ||
                    post.getPostLLM().toLowerCase().contains(filter.toLowerCase()) ||
                    post.getPostNotes().toLowerCase().contains(filter.toLowerCase())) {
                filteredPosts.add(post);
            }
        }
        return filteredPosts;
    }

    // Get a specific post by ID
    public Post getPost(String postId) {
        return posts.get(postId);
    }

    // Create a new post
    public boolean createPost(String postId, String title, String author, String llm, String notes, int rating) {
        if (posts.containsKey(postId)) {
            return false; // Post with this ID already exists
        }
        posts.put(postId, new Post(postId, title, author, llm, notes, rating));
        return true;
    }

    // Delete a post by ID
    public boolean deletePost(String postId) {
        return posts.remove(postId) != null;
    }

    // Add a comment to a specific post
    public boolean createComment(String postId, String commentId, String commentNotes, String commentAuthor) {
        Post post = posts.get(postId);
        if (post != null) {
            post.addComment(new Comment(commentId, commentNotes, commentAuthor));
            return true;
        }
        return false;
    }

    // Delete a comment from a post by comment ID
    public boolean deleteComment(String postId, String commentId) {
        Post post = posts.get(postId);
        if (post != null) {
            return post.removeComment(commentId);
        }
        return false;
    }

    // Inner Post class
    public static class Post {
        private String postId;
        private String postTitle;
        private String postAuthor;
        private String postLLM;
        private String postNotes;
        private int postRating;
        private List<Comment> comments;

        public Post(String postId, String postTitle, String postAuthor, String postLLM, String postNotes, int postRating) {
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

        public boolean removeComment(String commentId) {
            return comments.removeIf(comment -> comment.getCommentId().equals(commentId));
        }

        public String getPostId() {
            return postId;
        }

        public String getPostTitle() {
            return postTitle;
        }

        public String getPostAuthor() {
            return postAuthor;
        }

        public String getPostLLM() {
            return postLLM;
        }

        public String getPostNotes() {
            return postNotes;
        }

        public int getPostRating() {
            return postRating;
        }

        public List<Comment> getComments() {
            return comments;
        }
    }

    // Inner Comment class
    public static class Comment {
        private String commentId;
        private String commentNotes;
        private String commentAuthor;

        public Comment(String commentId, String commentNotes, String commentAuthor) {
            this.commentId = commentId;
            this.commentNotes = commentNotes;
            this.commentAuthor = commentAuthor;
        }

        public String getCommentId() {
            return commentId;
        }

        public String getCommentNotes() {
            return commentNotes;
        }

        public String getCommentAuthor() {
            return commentAuthor;
        }
    }
}
