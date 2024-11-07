package com.example.promptsharepro;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreatePostScreenActivity extends AppCompatActivity {

    private PostDatabase postDatabase;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_screen);

        postDatabase = PostDatabase.getInstance();

        // Retrieve the username passed from HomeScreenActivity
        username = getIntent().getStringExtra("username");

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        EditText titleInput = findViewById(R.id.title_input);
        EditText llmInput = findViewById(R.id.llm_input);
        EditText notesInput = findViewById(R.id.notes_input);

        Button createPostButton = findViewById(R.id.create_post_button);
        createPostButton.setOnClickListener(view -> {
            String title = titleInput.getText().toString().trim();
            String llm = llmInput.getText().toString().trim();
            String notes = notesInput.getText().toString().trim();

            if (title.isEmpty() || llm.isEmpty() || notes.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            String initialRating = "rating: 0;users:";

            boolean success = postDatabase.createPost(title, username, llm, notes, initialRating);

            if (success) {
                Toast.makeText(this, "Post created successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to create post", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
