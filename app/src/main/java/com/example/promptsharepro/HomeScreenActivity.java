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

    private List<PostDatabase.Post> displayedPosts;
    private LinearLayout postListLayout;
    private EditText searchInput;
    private PostDatabase postDatabase;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        TextView greetingTextView = findViewById(R.id.greeting);
        greetingTextView.setText("Hi, " + username + "!");

        postDatabase = PostDatabase.getInstance();
        Button createPostButton = findViewById(R.id.create_post_button);
        createPostButton.setOnClickListener(view -> startActivity(new Intent(this, CreatePostScreenActivity.class)));

        postListLayout = findViewById(R.id.post_list);
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

        updatePostList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePostList();
    }

    private void updatePostList() {
        displayedPosts = postDatabase.getAllPosts("");
        displayPosts(displayedPosts);
    }

    private void filterPosts(String query) {
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
                intent.putExtra("postId", post.getPostId());
                startActivity(intent);
            });

            postListLayout.addView(postItem);
        }
    }
}
