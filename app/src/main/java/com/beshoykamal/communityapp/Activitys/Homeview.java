package com.beshoykamal.communityapp.Activitys;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.beshoykamal.communityapp.Adapters.CommentAdapter;
import com.beshoykamal.communityapp.Models.Comment;
import com.beshoykamal.communityapp.Models.Post;
import com.beshoykamal.communityapp.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.beshoykamal.communityapp.Activitys.Post_details.COMMENT_KEY;

public class Homeview extends AppCompatActivity implements IPickResult {

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth mAuth;
    private FirebaseUser curentuser;
    private FirebaseDatabase firebaseDatabase;

    Dialog popadd;
    ImageView popUserImg,popuImgpin1,popuImgpin2,popuImgpin3,popuImgpost;

    EditText popuTitele,popudisc;
    ProgressBar popuprogress;
    Uri bickimg=null;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeside);
        Toolbar toolbar = findViewById(R.id.toolbar);
        popuImgpost=findViewById(R.id.popuimgpost);



        mAuth=FirebaseAuth.getInstance();
        curentuser=mAuth.getCurrentUser();

        inipopup();
//        pageImgpost()

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popadd.show();
            }
        });


        // nav Bar
        DrawerLayout drawer = findViewById(R.id.activity_homeside);
        NavigationView navigationView = findViewById(R.id.nav_view);

        updateNavHedar();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



    }




    @Override
    public void onPickResult(PickResult r) {
        bickimg=r.getUri();
        popuImgpost.setImageURI(r.getUri());
//         bitmap = r.getBitmap();

//        popuImgpost.setImageBitmap(r.getBitmap());

    }



    private void inipopup() {
        popadd=new Dialog(this);
        popadd.setContentView(R.layout.popup_addpost);
        popadd.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        popadd.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popadd.getWindow().getAttributes().gravity= Gravity.TOP;
        // widget

        popUserImg   = popadd.findViewById(R.id.popuImg_profil);
        popuImgpost  = popadd.findViewById(R.id.popuimgpost);

        popuImgpin1  = popadd.findViewById(R.id.popuPin);
        popuImgpin2  = popadd.findViewById(R.id.popuPin1);
        popuImgpin3  = popadd.findViewById(R.id.popuPin2);
        popuTitele   = popadd.findViewById(R.id.popuTitle);
        popudisc     = popadd.findViewById(R.id.popuDisc);
        popuprogress = popadd.findViewById(R.id.popuprogres);

        // load img
        Glide.with(Homeview.this).load(curentuser.getPhotoUrl()).into(popUserImg);

        // add post all data & check

        popuImgpin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            popuprogress.setVisibility(View.VISIBLE);
            popuImgpin1.setVisibility(View.INVISIBLE);

            // check inputs
            if (!popuTitele.getText().equals("")
                    &&!popudisc.getText().equals("")
                    && bickimg !=null)
            {
                Toast.makeText(Homeview.this, "Load Data", Toast.LENGTH_SHORT).show();
                // access storage FireBase and goto create class post

                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Community_Image");
                final StorageReference imgFilePath=storageReference.child(bickimg.getLastPathSegment());
                imgFilePath.putFile(bickimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                        String imagDownloadLink =uri.toString();

                        if (curentuser.getPhotoUrl()!=null) {
                            // create post object
                            Post post = new Post(popuTitele.getText().toString(),
                                popudisc.getText().toString(),
                                imagDownloadLink, curentuser.getUid(), curentuser.getPhotoUrl().toString()
                            );
                            addPost(post);
                        }else {
                            Post post = new Post(popuTitele.getText().toString(),
                                popudisc.getText().toString(),
                                imagDownloadLink, curentuser.getUid(),null
                            );
                      /////// add post if right
                            addPost(post);
                        }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //somthing wron Upload

                        isFinishing();
                        popuprogress.setVisibility(View.INVISIBLE);
                        popuImgpin1.setVisibility(View.VISIBLE);
                        }
                    });
                    }
                });

            }
                else
                    Toast.makeText(Homeview.this, "Please fill All Fild", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void addPost(Post post) {

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("posts").push();
        // get post unique ID and update post key
        String key=myRef.getKey();
        post.setPostkey(key);

        // add post data to fireBase database
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Homeview.this, "post add successfully ", Toast.LENGTH_SHORT).show();
                popuprogress.setVisibility(View.INVISIBLE);
                popuImgpin1.setVisibility(View.VISIBLE);
                popadd.dismiss();
                bickimg=(null);
                popuTitele.setText("");
                popudisc.setText("");
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void updateNavHedar (){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View       hedarview=navigationView.getHeaderView(0);
        TextView navusername=hedarview.findViewById(R.id.nav_username);
        TextView navusermail=hedarview.findViewById(R.id.nav_useremail);
        ImageView navuserimg=hedarview.findViewById(R.id.nav_userimage);

        navusername.setText(curentuser.getDisplayName());
        navusermail.setText(curentuser.getEmail());


            Glide.with(this).load(curentuser.getPhotoUrl()).into(navuserimg);

    }

    public void postimg(View view) {
        PickImageDialog.build(new PickSetup()).show(Homeview.this);
    }
}
