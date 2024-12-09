package com.example.promptsharepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<PostDatabase.Comment> comments;
    private String username;
    private String postId;
    private OnCommentDeletedListener onCommentDeletedListener;

    public CommentsAdapter(List<PostDatabase.Comment> comments, String username, String postId, OnCommentDeletedListener listener) {
        this.comments = comments;
        this.username = username;
        this.postId = postId;
        this.onCommentDeletedListener = listener;
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

        if (comment.getCommentAuthor().equals(username)) {
            holder.deleteCommentButton.setVisibility(View.VISIBLE);
            holder.deleteCommentButton.setOnClickListener(view -> {
                boolean success = PostDatabase.getInstance().deleteComment(postId, comment.getCommentId());
                if (success) {
                    Toast.makeText(holder.itemView.getContext(), "Comment deleted successfully", Toast.LENGTH_SHORT).show();
                    comments.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, comments.size());
                    if (onCommentDeletedListener != null) {
                        onCommentDeletedListener.onCommentDeleted();
                    }
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Failed to delete comment", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.deleteCommentButton.setVisibility(View.GONE);
        }
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
        ImageButton deleteCommentButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.comment_text);
            commentAuthor = itemView.findViewById(R.id.comment_author);
            deleteCommentButton = itemView.findViewById(R.id.delete_comment_button);
        }
    }

    public interface OnCommentDeletedListener {
        void onCommentDeleted();
    }
}
