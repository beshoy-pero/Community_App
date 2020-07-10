package com.beshoykamal.communityapp.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beshoykamal.communityapp.Adapters.CommentAdapter;
import com.beshoykamal.communityapp.Models.Comment;
import com.beshoykamal.communityapp.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Post_details extends AppCompatActivity {

    ImageView imgPost,imgUserpPost,imgCurentUser;
    TextView textPostTitle,textPostDisc,textPostDate;
    EditText editCommentPost;
    Button btnAddComment;
    String postKey;

    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    static String COMMENT_KEY="comment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hid flag bar
        Window w=getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_post_details);
        imgPost = findViewById(R.id.post_detail_img);
        imgUserpPost = findViewById(R.id.image_detail_user_post);
        imgCurentUser=findViewById(R.id.post_detail_current_img);

        textPostTitle = findViewById(R.id.post_detail_title);
        textPostDisc = findViewById(R.id.post_detail_discrip);
        textPostDate = findViewById(R.id.post_date_detilpost);

        editCommentPost= findViewById(R.id.post_detail_comment);
        btnAddComment= findViewById(R.id.post_ptn_add_comment);

        RvComment=findViewById(R.id.rv_coment);

        firebaseAuth =FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();

        //btn addComment
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editCommentPost.getText().toString().isEmpty()) {

                    btnAddComment.setVisibility(View.INVISIBLE);

                    DatabaseReference commentrefrence = firebaseDatabase.getReference("comment").child(postKey).push();
                    String comment_content = editCommentPost.getText().toString();
                    String uid = firebaseUser.getUid();
                        String uname = firebaseUser.getDisplayName();

                    String uimage = firebaseUser.getPhotoUrl().toString();
                    Comment comment = new Comment(comment_content, uid, uimage, uname);

                    commentrefrence.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Post_details.this, "comment add", Toast.LENGTH_SHORT).show();
                            editCommentPost.setText("");
                            btnAddComment.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Post_details.this, "fail to add comment", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                    Toast.makeText(Post_details.this, "Empty", Toast.LENGTH_SHORT).show();
            }
        });

        // we need bind all data into this view
        // first we need get post data
        // we need to get post details data to this activity first ..


        String postImag= getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImag).into(imgPost);

        String postUserImg= getIntent().getExtras().getString("userImages");
        Glide.with(this).load(postUserImg).into(imgUserpPost);

        String title =getIntent().getExtras().getString("title");
        textPostTitle.setText(title);

        String descrip =getIntent().getExtras().getString("description");
        textPostDisc.setText(descrip);

        //set image post curent user
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurentUser);
        //get post id
         postKey =getIntent().getExtras().getString("postKey");

        String date =timeStampSrtrin(getIntent().getExtras().getLong("postTime"));
        textPostDate.setText(date);

        iniRvComment();

    }

    private void iniRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));
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
                commentAdapter=new CommentAdapter(getApplicationContext(),listComment);

                RvComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String timeStampSrtrin(long time){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String dat= DateFormat.format("dd-MM-yyyy",calendar).toString();
        return dat;
    }
}
