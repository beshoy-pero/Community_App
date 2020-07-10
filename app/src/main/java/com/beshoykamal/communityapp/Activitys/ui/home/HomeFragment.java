package com.beshoykamal.communityapp.Activitys.ui.home;
import android.os.Bundle;

import com.beshoykamal.communityapp.Adapters.CommentAdapter;
import com.beshoykamal.communityapp.Models.Comment;
import com.beshoykamal.communityapp.Models.Post;
import com.beshoykamal.communityapp.Adapters.PostAdapter;
import com.beshoykamal.communityapp.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;


        RecyclerView postRecyclerView;
        PostAdapter postAdapter;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        List<Post> postlist;


    String postKey;

    CommentAdapter commentAdapter;

    static String COMMENT_KEY="comment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//            }
//        });



        postRecyclerView=root.findViewById(R.id.postRV);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
        postRecyclerView.setHasFixedSize(true);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("posts");
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        // get list post from dataBase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postlist= new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()){
                Post post= postSnap.getValue(Post.class);
                postlist.add(post);
            }
                postAdapter=new PostAdapter(getActivity(),postlist);
                postRecyclerView.setAdapter(postAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        postKey =getIntent().getExtras().getString("postKey");
//        btncomment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rcComment.setVisibility(View.VISIBLE);
//                iniRCComment();
//            }
//        });
    }
//    private void iniRCComment() {
//        rcComment.setLayoutManager(new LinearLayoutManager(getActivity()));
////        rcComment.
//
//        DatabaseReference commentRef=firebaseDatabase.getReference(COMMENT_KEY).child(postKey);
//        commentRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                listComment=new ArrayList<>();
//
//                for (DataSnapshot snap : dataSnapshot.getChildren()){
//                    Comment comment=snap.getValue(Comment.class);
//                    listComment.add(comment);
//                }
//                Collections.reverse(listComment);
//                commentAdapter=new CommentAdapter(getContext(), listComment);
//
//                rcComment.setAdapter(commentAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}