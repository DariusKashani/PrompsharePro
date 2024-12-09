package com.example.promptsharepro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ProfileScreenActivity extends AppCompatActivity {

    private EditText usernameInput, emailInput, passwordInput, retypePasswordInput;
    private TextView numPostsText, numCommentsText, avgRatingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        usernameInput = findViewById(R.id.edit_username);
        emailInput = findViewById(R.id.edit_email);
        passwordInput = findViewById(R.id.edit_password);
        retypePasswordInput = findViewById(R.id.edit_retype_password);
        numPostsText = findViewById(R.id.num_posts);
        numCommentsText = findViewById(R.id.num_comments);
        avgRatingText = findViewById(R.id.avg_rating);

        // Pre-fill username and email if passed
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("useremail");

        if (username != null) {
            usernameInput.setText(username);
        }
        if (email != null) {
            emailInput.setText(email);
        }

        // Load statistics
        if (email != null) {
            loadStatistics(username);
        }

        TextView backToHomeButton = findViewById(R.id.back_to_home_button);
        backToHomeButton.setOnClickListener(view -> finish()); // Simply finish to go back to the previous screen

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(view -> {
            String newUsername = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String retypePassword = retypePasswordInput.getText().toString().trim();

            if (newUsername.isEmpty() || email.isEmpty() || password.isEmpty() || retypePassword.isEmpty()) {
                Toast.makeText(ProfileScreenActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(retypePassword)) {
                Toast.makeText(ProfileScreenActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Interact with UserDatabase to update the user
            UserDatabase userDatabase = UserDatabase.getInstance();
            boolean isUpdated = userDatabase.updateUser(email, newUsername, password);

            if (isUpdated) {
                Toast.makeText(ProfileScreenActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                // Return updated username to HomeScreenActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("username", newUsername);
                setResult(RESULT_OK, resultIntent);
                finish(); // Return to the home screen after updating
            } else {
                Toast.makeText(ProfileScreenActivity.this, "Failed to update profile. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStatistics(String userEmail) {
        new Thread(() -> {
            PostDatabase postDatabase = PostDatabase.getInstance();
            List<PostDatabase.Post> allPosts = postDatabase.getAllPosts("");

            int postCount = 0;
            int totalComments = 0;
            double totalRating = 0.0;

            for (PostDatabase.Post post : allPosts) {
                if (post.getPostAuthor().equalsIgnoreCase(userEmail)) {
                    postCount++;
                    totalComments += post.getComments().size();

                    // Extract and parse the rating
                    try {
                        String postRating = post.getPostRating();
                        if (postRating.startsWith("rating:")) {
                            String ratingPart = postRating.split(";")[0].replace("rating:", "").trim();
                            totalRating += Double.parseDouble(ratingPart);
                        }
                    } catch (Exception e) {
                        // Ignore any parsing errors
                    }
                }
            }

            double avgRating = postCount > 0 ? totalRating / postCount : 0.0;

            // Update UI on the main thread
            int finalPostCount = postCount;
            int finalTotalComments = totalComments;
            runOnUiThread(() -> {
                numPostsText.setText("Number of Posts: " + finalPostCount);
                numCommentsText.setText("Number of Comments Under Your Posts: " + finalTotalComments);
                avgRatingText.setText(String.format("Average Rating Per Post: %.2f", avgRating));
            });
        }).start();
    }
}
