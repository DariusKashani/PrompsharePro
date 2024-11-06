package com.example.promptsharepro;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CreatePostScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_screen);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish()); // Goes back to HomeScreen

        initialLoad("Title your post", "Claude Anthropic", "Enter notes here...");
    }

    private void initialLoad(String postTitle, String chosenLLM, String notes) {
        TextView titleTextView = findViewById(R.id.post_title);
        titleTextView.setText(postTitle);

        EditText llmEditText = findViewById(R.id.llm_input);
        llmEditText.setText(chosenLLM);

        EditText notesEditText = findViewById(R.id.notes_input);
        notesEditText.setText(notes);
    }
}
