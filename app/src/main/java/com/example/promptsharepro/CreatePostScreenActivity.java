package com.example.promptsharepro;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreatePostScreenActivity extends AppCompatActivity {

    private PostDatabase postDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_screen);

        postDatabase = PostDatabase.getInstance();

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        EditText titleInput = findViewById(R.id.title_input);
        EditText llmInput = findViewById(R.id.llm_input);
        EditText notesInput = findViewById(R.id.notes_input);

        Button createPostButton = findViewById(R.id.create_post_button);
        createPostButton.setOnClickListener(view -> {
            // Get input values
            String title = titleInput.getText().toString().trim();
            String llm = llmInput.getText().toString().trim();
            String notes = notesInput.getText().toString().trim();

            // Validate inputs
            if (title.isEmpty() || llm.isEmpty() || notes.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Generate a unique post ID
            String postId = String.valueOf(System.currentTimeMillis());

            // Create the post with a default rating of 0
            boolean success = postDatabase.createPost(postId, title, "Current User", llm, notes, 0);

            if (success) {
                Toast.makeText(this, "Post created successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity and return to the previous screen
            } else {
                Toast.makeText(this, "Failed to create post", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
