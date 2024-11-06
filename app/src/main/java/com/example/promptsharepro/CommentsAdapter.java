package com.example.promptsharepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<PostDatabase.Comment> comments;

    public CommentsAdapter(List<PostDatabase.Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        PostDatabase.Comment comment = comments.get(position);
        holder.commentText.setText(comment.getCommentNotes());
        holder.commentAuthor.setText(comment.getCommentAuthor());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<PostDatabase.Comment> newComments) {
        this.comments = newComments;
        notifyDataSetChanged();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentText;
        TextView commentAuthor;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.comment_text);
            commentAuthor = itemView.findViewById(R.id.comment_author);
        }
    }
}
