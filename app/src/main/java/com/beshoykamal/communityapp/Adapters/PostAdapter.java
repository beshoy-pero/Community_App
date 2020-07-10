package com.beshoykamal.communityapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beshoykamal.communityapp.Models.Comment;
import com.beshoykamal.communityapp.Models.Post;
import com.beshoykamal.communityapp.Activitys.Post_details;
import com.beshoykamal.communityapp.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData;


    List<Comment> listComment;
    RecyclerView rcm;
    String COMMENT_KEY="comment";
    String postKey;
    TextView countCommen;
    int count=0;
    FirebaseDatabase firebaseDatabase;
    CommentAdapter commentAdapter;



    /// still here in idea below

//    public PostAdapter(Context mContext, List<Post> mData,List<Comment> mComment) {
//        this.mContext = mContext;
//        this.mData = mData;
//        this.mComment = mComment;
//    }
public PostAdapter(Context mContext, List<Post> mData) {
    this.mContext = mContext;
    this.mData = mData;

}


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);

        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.tvTitle.setText(mData.get(position).getTitele());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imagePostt);
        String userImg=mData.get(position).getUserPicture();

            Glide.with(mContext).load(userImg).into(holder.imagePostProfile);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle,comment;
        ImageView imagePostt, imagePostProfile;



        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imagePostt = itemView.findViewById(R.id.imagePostt);
            imagePostProfile = itemView.findViewById(R.id.imagePostProfile);

            comment = itemView.findViewById(R.id.tvComment);




            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    int positionC = 0;
                    firebaseDatabase=FirebaseDatabase.getInstance();
                   int  positionC = getAdapterPosition();
                    postKey= mData.get(positionC).getPostkey();



//
                        rcm = itemView.findViewById(R.id.rcComment);
                    countCommen = itemView.findViewById(R.id.countComment);

                    if (rcm.getVisibility() ==(View.VISIBLE))
                    rcm.setVisibility(View.GONE);
                    else
                        rcm.setVisibility(View.VISIBLE);


                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (rcm.getVisibility() == View.VISIBLE){
                                iniRVcomments();
                            }
//                            try {
//                                Thread.sleep(4000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }

                        }
                    });
                    thread.start();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(mContext, Post_details.class);
                    int position = getAdapterPosition();

                    in.putExtra("title",mData.get(position).getTitele());
                    in.putExtra("description",mData.get(position).getDiscreption());
                    in.putExtra("postKey",mData.get(position).getPostkey());
                    in.putExtra("postImage",mData.get(position).getPicture());
                    in.putExtra("userImages",mData.get(position).getUserPicture());
                    long timestamp  = (long)mData.get(position).getTimeStamp();
                    in.putExtra("postTime",timestamp);

//                    in.putExtra("userName",mData.get(position).getUserName());
                    mContext.startActivity(in);

                }
            });


        }

    }

    private void iniRVcomments() {


            rcm.setLayoutManager(new LinearLayoutManager(mContext));
//        RvComment.



            DatabaseReference commentRef=firebaseDatabase.getReference(COMMENT_KEY).child(postKey);
            commentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listComment=new ArrayList<>();

                    for (DataSnapshot snap : dataSnapshot.getChildren()){
                        Comment comment=snap.getValue(Comment.class);
                        listComment.add(comment);
                    }
                    Collections.reverse(listComment);
                     commentAdapter =new CommentAdapter(mContext , listComment);

                    rcm.setAdapter(commentAdapter);

                     count=listComment.size();
                    Toast.makeText(mContext, "Comments "+listComment.size(), Toast.LENGTH_SHORT).show();
                    countCommen.setText(count+"");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }
}
