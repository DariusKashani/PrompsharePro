package com.example.promptsharepro;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {

    private List<PostDatabase.Post> displayedPosts;
    private LinearLayout postListLayout;
    private EditText searchInput;
    private Spinner filterSpinner;
    private PostDatabase postDatabase;
    private String username;
    private String useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        filterSpinner = findViewById(R.id.search_filter_spinner);
        searchInput = findViewById(R.id.search_input);

        Button goToProfileButton = findViewById(R.id.go_to_profile_button);
        goToProfileButton.setOnClickListener(view -> {
            Intent profileIntent = new Intent(HomeScreenActivity.this, ProfileScreenActivity.class);
            profileIntent.putExtra("username", username);
            profileIntent.putExtra("useremail", useremail);
            startActivityForResult(profileIntent, 1); // Use a request code
        });

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        useremail = intent.getStringExtra("useremail");

        TextView greetingTextView = findViewById(R.id.greeting);
        greetingTextView.setText("Hi, " + username + "!");

        TextView logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(view -> {
            Intent logoutIntent = new Intent(HomeScreenActivity.this, LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);
            finish();
            Toast.makeText(HomeScreenActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
        });

        postDatabase = PostDatabase.getInstance();
        Button createPostButton = findViewById(R.id.create_post_button);
        createPostButton.setOnClickListener(view -> {
            Intent createPostScreenIntent = new Intent(this, CreatePostScreenActivity.class);
            createPostScreenIntent.putExtra("username", username);
            startActivity(createPostScreenIntent);
        });

        postListLayout = findViewById(R.id.post_list);

        // Listen to changes in the search input
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPosts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Update post list when the dropdown selection changes
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterPosts(searchInput.getText().toString()); // Apply current search input with the new filter
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        updatePostList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePostList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) { // Check if the request code and result code match
            if (data != null && data.hasExtra("username")) {
                username = data.getStringExtra("username");
                TextView greetingTextView = findViewById(R.id.greeting);
                greetingTextView.setText("Hi, " + username + "!");
            }
        }
    }

    private void updatePostList() {
        displayedPosts = postDatabase.getAllPosts("");
        displayPosts(displayedPosts);
    }

    private void filterPosts(String query) {
        // Fetch all posts first
        List<PostDatabase.Post> allPosts = postDatabase.getAllPosts("");

        // Check if the search input is empty
        if (query.trim().isEmpty()) {
            // If no text is entered, display all posts
            displayedPosts = allPosts;
        } else {
            // Filter posts based on the selected dropdown filter and search query
            String selectedFilter = filterSpinner.getSelectedItem().toString().toLowerCase();
            displayedPosts = new ArrayList<>();

            for (PostDatabase.Post post : allPosts) {
                switch (selectedFilter) {
                    case "author":
                        if (post.getPostAuthor().toLowerCase().contains(query.toLowerCase())) {
                            displayedPosts.add(post);
                        }
                        break;
                    case "model":
                        if (post.getPostLLM().toLowerCase().contains(query.toLowerCase())) {
                            displayedPosts.add(post);
                        }
                        break;
                    case "notes":
                        if (post.getPostNotes().toLowerCase().contains(query.toLowerCase())) {
                            displayedPosts.add(post);
                        }
                        break;
                    case "all":
                    default:
                        // If "All" is selected, match any field
                        if (post.getPostAuthor().toLowerCase().contains(query.toLowerCase()) ||
                                post.getPostLLM().toLowerCase().contains(query.toLowerCase()) ||
                                post.getPostNotes().toLowerCase().contains(query.toLowerCase())) {
                            displayedPosts.add(post);
                        }
                        break;
                }
            }
        }

        // Display the filtered posts
        displayPosts(displayedPosts);
    }

    private void displayPosts(List<PostDatabase.Post> posts) {
        postListLayout.removeAllViews();
        for (PostDatabase.Post post : posts) {
            View postItem = LayoutInflater.from(this).inflate(R.layout.post_item, postListLayout, false);

            TextView postTitle = postItem.findViewById(R.id.post_title);
            postTitle.setText(post.getPostTitle());

            TextView authorName = postItem.findViewById(R.id.post_author);
            authorName.setText(post.getPostAuthor());

            TextView llmName = postItem.findViewById(R.id.post_llm);
            llmName.setText(post.getPostLLM());

            TextView ratingTextView = postItem.findViewById(R.id.post_rating);

            String postRating = post.getPostRating();
            String ratingValue = "0";

            if (postRating.contains(";users:")) {
                String[] parts = postRating.split(";users:");
                ratingValue = parts[0].replace("rating:", "").trim();
            }

            ratingTextView.setText(ratingValue);

            TextView notes = postItem.findViewById(R.id.post_notes);
            notes.setText(post.getPostNotes());

            // Only display the delete button if the post author matches the logged-in user
            ImageButton trashButton = postItem.findViewById(R.id.trash_button);
            if (post.getPostAuthor().equals(username)) {
                trashButton.setVisibility(View.VISIBLE);
                trashButton.setOnClickListener(view -> {
                    postDatabase.deletePost(post.getPostId());
                    Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show();
                    updatePostList();
                });
            } else {
                trashButton.setVisibility(View.GONE);
            }

            Button readMoreButton = postItem.findViewById(R.id.read_more_button);
            readMoreButton.setOnClickListener(view -> {
                Intent intent = new Intent(this, SinglePostScreenActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("username", username);
                startActivity(intent);
            });

            postListLayout.addView(postItem);
        }
    }
}
