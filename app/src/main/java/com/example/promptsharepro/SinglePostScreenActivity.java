package com.example.promptsharepro;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SinglePostScreenActivity extends AppCompatActivity {

    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_post_screen);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        Post post = (Post) getIntent().getSerializableExtra("post");
        initialLoad(post);

        commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Comment> comments = post.getComments();
        commentsAdapter = new CommentsAdapter(comments);
        commentsRecyclerView.setAdapter(commentsAdapter);
    }

    private void initialLoad(Post post) {
        TextView titleTextView = findViewById(R.id.post_title);
        titleTextView.setText(post.getTitle());

        TextView authorTextView = findViewById(R.id.post_author);
        authorTextView.setText(post.getAuthor());

        TextView llmTextView = findViewById(R.id.post_llm);
        llmTextView.setText(post.getLlmName());

        TextView ratingTextView = findViewById(R.id.post_rating);
        ratingTextView.setText(post.getRating());

        TextView notesTextView = findViewById(R.id.notes);
        notesTextView.setText(post.getNotes());
    }
}
