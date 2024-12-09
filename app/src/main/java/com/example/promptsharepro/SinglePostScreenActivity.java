package com.example.promptsharepro;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SinglePostScreenActivity extends AppCompatActivity {

    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private EditText commentInput;
    private TextView ratingTextView;
    private PostDatabase.Post post;
    private PostDatabase postDatabase;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_post_screen);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        postDatabase = PostDatabase.getInstance();

        String postId = getIntent().getStringExtra("postId");
        username = getIntent().getStringExtra("username");

        post = postDatabase.getPost(postId);

        if (post != null) {
            initialLoad(post);
            setupCommentsSection();
            setupRatingControls();
        } else {
            Toast.makeText(this, "Post not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initialLoad(PostDatabase.Post post) {
        TextView titleTextView = findViewById(R.id.post_title);
        titleTextView.setText(post.getPostTitle());

        TextView authorTextView = findViewById(R.id.post_author);
        authorTextView.setText(post.getPostAuthor());

        TextView llmTextView = findViewById(R.id.post_llm);
        llmTextView.setText(post.getPostLLM());

        ratingTextView = findViewById(R.id.post_rating);
        String postRating = post.getPostRating();
        String ratingValue = "0";

        if (postRating.contains(";users:")) {
            String[] parts = postRating.split(";users:");
            ratingValue = parts[0].replace("rating:", "").trim(); // Extract the rating number
        }

        ratingTextView.setText(ratingValue);

        TextView notesTextView = findViewById(R.id.notes);
        notesTextView.setText(post.getPostNotes());
    }

    private void setupCommentsSection() {
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<PostDatabase.Comment> comments = post.getComments();
        commentsAdapter = new CommentsAdapter(comments, username, post.getPostId(), this::updateComments);
        commentsRecyclerView.setAdapter(commentsAdapter);

        commentInput = findViewById(R.id.comment_input);
        Button addCommentButton = findViewById(R.id.add_comment_button);
        addCommentButton.setOnClickListener(view -> addComment());
    }

    // Callback to refresh comments or other UI after a comment is deleted
    private void updateComments() {
        // Logic to refresh comments or perform other actions
        commentsAdapter.notifyDataSetChanged();
    }

    private void setupRatingControls() {
        Button minusButton = findViewById(R.id.minus_button);
        Button plusButton = findViewById(R.id.plus_button);

        minusButton.setOnClickListener(view -> updateRating(-1));
        plusButton.setOnClickListener(view -> updateRating(1));
    }

    private void updateRating(int change) {
        String postRating = post.getPostRating();
        int currentRating = 0;
        List<String> users = new ArrayList<>();

        if (postRating.contains(";users:")) {
            String[] parts = postRating.split(";users:");
            currentRating = Integer.parseInt(parts[0].replace("rating:", "").trim());

            if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                users = new ArrayList<>(Arrays.asList(parts[1].trim().split(", ")));
            }
        } else {
            currentRating = Integer.parseInt(postRating.replace("rating:", "").trim());
        }

        if (change > 0) { // Upvote
            if (users.contains(username)) {
                Toast.makeText(this, "You have already upvoted", Toast.LENGTH_SHORT).show();
                return;
            } else {
                currentRating += 1;
                users.add(username);
            }
        } else {
            if (users.contains(username)) {
                currentRating = Math.max(0, currentRating - 1);
                users.remove(username);
            } else {
                Toast.makeText(this, "You haven't upvoted yet", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String updatedPostRating = "rating: " + currentRating + ";users: " + String.join(", ", users);
        ratingTextView.setText(String.valueOf(currentRating));

        post.setPostRating(updatedPostRating);
        postDatabase.updatePost(post.getPostId(), post.getPostTitle(), post.getPostAuthor(), post.getPostLLM(), post.getPostNotes(), updatedPostRating); //
    }

    private void addComment() {
        String commentText = commentInput.getText().toString().trim();
        if (commentText.isEmpty()) {
            Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String commentId = postDatabase.createComment(post.getPostId(), commentText, username);

        if (commentId != null) {
            PostDatabase.Comment newComment = new PostDatabase.Comment(commentId, commentText, username);
            post.getComments().add(newComment);

            commentsAdapter.notifyItemInserted(post.getComments().size() - 1);

            commentsRecyclerView.scrollToPosition(post.getComments().size() - 1);

            commentInput.setText("");
            Toast.makeText(this, "Comment added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add comment", Toast.LENGTH_SHORT).show();
        }
    }
}
