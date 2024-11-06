package com.example.promptsharepro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        TextView greetingTextView = findViewById(R.id.greeting);
        greetingTextView.setText("Hi, Keshav!");

        Button createPostButton = findViewById(R.id.create_post_button);
        createPostButton.setOnClickListener(view -> startActivity(new Intent(this, CreatePostScreenActivity.class)));

        initialLoad(getSamplePosts());
    }

    private void initialLoad(List<Post> posts) {
        LinearLayout postListLayout = findViewById(R.id.post_list);
        for (Post post : posts) {
            View postItem = LayoutInflater.from(this).inflate(R.layout.post_item, postListLayout, false);

            TextView postTitle = postItem.findViewById(R.id.post_title);
            postTitle.setText(post.getTitle());

            TextView authorName = postItem.findViewById(R.id.post_author);
            authorName.setText(post.getAuthor());

            TextView llmName = postItem.findViewById(R.id.post_llm);
            llmName.setText(post.getLlmName());

            TextView rating = postItem.findViewById(R.id.post_rating);
            rating.setText(post.getRating());

            TextView notes = postItem.findViewById(R.id.post_notes);
            notes.setText(post.getNotes());

            Button readMoreButton = postItem.findViewById(R.id.read_more_button);
            readMoreButton.setOnClickListener(view -> {
                Intent intent = new Intent(this, SinglePostScreenActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
            });

            postListLayout.addView(postItem);
        }
    }

    private List<Post> getSamplePosts() {
        List<Post> posts = new ArrayList<>();

        Post post1 = new Post("CSCI 270 Homework", "Aaron Cote", "Claude Anthropic", "4.5 / 5", "270 is a very cool class.");
        post1.addComment(new Comment("Great post!", "User A"));
        post1.addComment(new Comment("Very informative.", "User B"));

        Post post2 = new Post("Apple IOS Dev", "Marco Papa", "ChatGPT", "2 / 5", "Apple is very cool.");
        post2.addComment(new Comment("Thanks for sharing!", "User C"));

        posts.add(post1);
        posts.add(post2);

        return posts;
    }
}
