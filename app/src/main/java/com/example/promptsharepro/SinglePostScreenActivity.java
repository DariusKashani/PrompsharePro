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
import java.util.List;

public class SinglePostScreenActivity extends AppCompatActivity {

    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private EditText commentInput;
    private PostDatabase.Post post;
    private PostDatabase postDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_post_screen);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        postDatabase = PostDatabase.getInstance();

        // Retrieve post by ID from PostDatabase
        String postId = getIntent().getStringExtra("postId");
        post = postDatabase.getPost(postId);

        if (post != null) {
            initialLoad(post);
            setupCommentsSection();
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

        TextView ratingTextView = findViewById(R.id.post_rating);
        ratingTextView.setText(String.valueOf(post.getPostRating()));

        TextView notesTextView = findViewById(R.id.notes);
        notesTextView.setText(post.getPostNotes());
    }

    private void setupCommentsSection() {
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get comments from the post and set up the adapter
        List<PostDatabase.Comment> comments = post.getComments();
        commentsAdapter = new CommentsAdapter(comments);
        commentsRecyclerView.setAdapter(commentsAdapter);

        commentInput = findViewById(R.id.comment_input);
        Button addCommentButton = findViewById(R.id.add_comment_button);
        addCommentButton.setOnClickListener(view -> addComment());
    }

    private void addComment() {
        String commentText = commentInput.getText().toString().trim();
        if (commentText.isEmpty()) {
            Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use createComment to add the comment to the post in PostDatabase
        boolean success = postDatabase.createComment(post.getPostId(), String.valueOf(post.getComments().size() + 1), commentText, "Current User"); // Replace "Current User" with actual username

        if (success) {
            commentsAdapter.notifyDataSetChanged(); // Refresh the comments view
            commentInput.setText(""); // Clear the input field
        } else {
            Toast.makeText(this, "Failed to add comment", Toast.LENGTH_SHORT).show();
        }
    }
}
