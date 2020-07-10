package com.beshoykamal.communityapp.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beshoykamal.communityapp.Models.Comment;
import com.beshoykamal.communityapp.R;
import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.MyCommentViewHolder> {

    private Context mContext;
    private List<Comment> mData;


    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment,parent,false);

        return new MyCommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCommentViewHolder holder, int position) {

        holder.commentUserName.setText(mData.get(position).getUname());
        holder.commentContent.setText(mData.get(position).getContent());
            Glide.with(mContext).load(mData.get(position).getUimg()).into(holder.imageUserComment);



        holder.commentTime.setText(timeStampSrtrin((long)mData.get(position).getTimestamp()));


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyCommentViewHolder extends RecyclerView.ViewHolder{

        TextView commentUserName,commentContent,commentTime;
        ImageView imageUserComment;

        public MyCommentViewHolder(@NonNull View itemView) {
            super(itemView);

            commentUserName = itemView.findViewById(R.id.comment_username);
            commentContent = itemView.findViewById(R.id.comment_content);
            imageUserComment = itemView.findViewById(R.id.comment_user_img);
            commentTime = itemView.findViewById(R.id.time_comment);
        }
    }

    private String timeStampSrtrin(long time){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String dat= DateFormat.format("hh-mm",calendar).toString();
        return dat;
    }
}
