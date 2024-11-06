package com.example.promptsharepro;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {

    private List<PostDatabase.Post> displayedPosts; // List to keep only filtered posts
    private LinearLayout postListLayout;
    private EditText searchInput;
    private PostDatabase postDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        postDatabase = PostDatabase.getInstance();

        TextView greetingTextView = findViewById(R.id.greeting);
        greetingTextView.setText("Hi, Keshav!");

        Button createPostButton = findViewById(R.id.create_post_button);
        createPostButton.setOnClickListener(view -> startActivity(new Intent(this, CreatePostScreenActivity.class)));

        postListLayout = findViewById(R.id.post_list);

        // Initialize the search input and set up TextWatcher for search functionality
        searchInput = findViewById(R.id.search_input);
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

        // Display all posts initially
        updatePostList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePostList(); // Refresh posts whenever this activity resumes
    }

    private void updatePostList() {
        // Retrieve and display all posts without a filter
        displayedPosts = postDatabase.getAllPosts("");
        displayPosts(displayedPosts);
    }

    private void filterPosts(String query) {
        // Retrieve filtered posts using PostDatabase's method
        displayedPosts = postDatabase.getAllPosts(query);
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

            TextView rating = postItem.findViewById(R.id.post_rating);
            rating.setText(String.valueOf(post.getPostRating()));

            TextView notes = postItem.findViewById(R.id.post_notes);
            notes.setText(post.getPostNotes());

            Button readMoreButton = postItem.findViewById(R.id.read_more_button);
            readMoreButton.setOnClickListener(view -> {
                Intent intent = new Intent(this, SinglePostScreenActivity.class);
                intent.putExtra("postId", post.getPostId()); // Pass only the post ID
                startActivity(intent);
            });

            postListLayout.addView(postItem);
        }
    }
}