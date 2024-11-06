package com.example.promptsharepro;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {

    private FirebaseFirestore firestore;

    public DatabaseManager() {
        firestore = FirebaseFirestore.getInstance();
    }
    // --- User Operations ---

    public void registerUser(User user, OnCompleteListener<Void> onComplete) {
        firestore.collection("users").document(user.uid)
                .set(user)
                .addOnCompleteListener(onComplete);
    }

    public void getUser(String userId, OnCompleteListener<DocumentSnapshot> onComplete) {
        firestore.collection("users").document(userId)
                .get()
                .addOnCompleteListener(onComplete);
    }

    // --- Post Operations ---

    public void createPost(Post post, OnCompleteListener<Void> onComplete) {
        DocumentReference postRef = firestore.collection("posts").document();
        post.id = postRef.getId();
        postRef.set(post)
                .addOnCompleteListener(onComplete);
    }

    public void getPost(String postId, OnCompleteListener<DocumentSnapshot> onComplete) {
        firestore.collection("posts").document(postId)
                .get()
                .addOnCompleteListener(onComplete);
    }

    public void updatePost(String postId, Map<String, Object> updates, OnCompleteListener<Void> onComplete) {
        firestore.collection("posts").document(postId)
                .update(updates)
                .addOnCompleteListener(onComplete);
    }

    public void deletePost(String postId, OnCompleteListener<Void> onComplete) {
        firestore.collection("posts").document(postId)
                .delete()
                .addOnCompleteListener(onComplete);
    }

    public void getAllPosts(OnCompleteListener<QuerySnapshot> onComplete) {
        firestore.collection("posts")
                .get()
                .addOnCompleteListener(onComplete);
    }

    // --- Comment Operations ---

    public void createComment(String postId, Comment comment, OnCompleteListener<Void> onComplete) {
        DocumentReference commentRef = firestore.collection("posts")
                .document(postId)
                .collection("comments").document();
        comment.id = commentRef.getId();
        commentRef.set(comment)
                .addOnCompleteListener(onComplete);
    }

    public void getCommentsForPost(String postId, OnCompleteListener<QuerySnapshot> onComplete) {
        firestore.collection("posts").document(postId).collection("comments")
                .get()
                .addOnCompleteListener(onComplete);
    }

    public void updateComment(String postId, String commentId, Map<String, Object> updates, OnCompleteListener<Void> onComplete) {
        firestore.collection("posts").document(postId).collection("comments").document(commentId)
                .update(updates)
                .addOnCompleteListener(onComplete);
    }

    public void deleteComment(String postId, String commentId, OnCompleteListener<Void> onComplete) {
        firestore.collection("posts").document(postId).collection("comments").document(commentId)
                .delete()
                .addOnCompleteListener(onComplete);
    }

    public void searchPostsByLLMKind(String llmKind, OnCompleteListener<QuerySnapshot> onComplete) {
        firestore.collection("posts")
                .whereEqualTo("LLMType", llmKind)
                .get()
                .addOnCompleteListener(onComplete);
    }

    public void searchPostsByTitleKeyword(String keyword, OnCompleteListener<QuerySnapshot> onComplete) {// Preprocess keyword
        String lowercaseKeyword = keyword.toLowerCase();

        firestore.collection("posts")
                .whereArrayContains("titleWords", lowercaseKeyword) // Search for keyword in titleWords array
                .get()
                .addOnCompleteListener(onComplete);
    }


    public void fullTextSearchPosts(String keyword, OnCompleteListener<List<Post>> onComplete) {
        // Preprocess keyword
        String lowercaseKeyword = keyword.toLowerCase();

        firestore.collection("posts")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Post> matchingPosts = new ArrayList<>();

                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Post post = document.toObject(Post.class);
                        if (post != null) {
                            // Set the ID from the document
                            post.id = document.getId();

                            // Check if keyword exists in any searchable fields
                            if (containsKeyword(post.titleWords, lowercaseKeyword) ||
                                    containsKeyword(post.contentWords, lowercaseKeyword) ||
                                    containsKeyword(post.authorNotesWords, lowercaseKeyword)) {
                                matchingPosts.add(post);
                            }
                        }
                    }

                    // Complete with the list of matching posts
                    onComplete.onComplete(Tasks.forResult(matchingPosts));
                })
                .addOnFailureListener(e -> onComplete.onComplete(Tasks.forException(e)));
    }

    // Helper function to check if a list of words contains a keyword
    private boolean containsKeyword(List<String> words, String keyword) {
        for (String word : words) {
            if (word.equals(keyword)) {
                return true;
            }
        }
        return false;
    }



}