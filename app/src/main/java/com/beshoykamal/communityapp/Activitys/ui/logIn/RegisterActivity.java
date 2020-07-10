package com.beshoykamal.communityapp.Activitys.ui.logIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beshoykamal.communityapp.Activitys.Homeview;
import com.beshoykamal.communityapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;


public class RegisterActivity extends AppCompatActivity implements IPickResult {

    ImageView photo;

    Uri bickimgUri ;


    private EditText username,useremail,userpassword,userpassword2;
    private ProgressBar lodingprogressbar;
    private Button btn;
//    Uri imgDefult= Uri.parse("https://firebasestorage.googleapis.com/v0/b/community-app-4b457.appspot.com/o/users_photo%2F397230?alt=media&token=f96bea22-798b-47e8-9014-20afebdf0873");
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photo=findViewById(R.id.regprofilpic);
        username=findViewById(R.id.regname);
        useremail=findViewById(R.id.regemail);
        userpassword=findViewById(R.id.regpassword);
        userpassword2=findViewById(R.id.regpassword2);
        lodingprogressbar=findViewById(R.id.progressBar);
        btn=findViewById(R.id.regptn);

        lodingprogressbar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();

        btn.setVisibility(View.INVISIBLE);

    }

    public void profilpic(View view) {
        PickImageDialog.build(new PickSetup()).show(this);
    }
    @Override
    public void onPickResult(PickResult r) {
        bickimgUri =r.getUri();
        photo.setImageURI(r.getUri());
        btn.setVisibility(View.VISIBLE);

    }
    public void regist(View view) {

        btn.setVisibility(View.INVISIBLE);
        lodingprogressbar.setVisibility(View.VISIBLE);

         String Email = useremail.getText().toString();
         String Password = userpassword.getText().toString();
        final String Password2 = userpassword2.getText().toString();
        final String Name = username.getText().toString();

        if (Email.isEmpty()||Name.isEmpty()||Password.isEmpty()||!Password.equals(Password2)){

            Toast.makeText(this, "Please verify all fields ", Toast.LENGTH_SHORT).show();
            btn.setVisibility(View.VISIBLE);
            lodingprogressbar.setVisibility(View.INVISIBLE);
        }
        else
            {
            Toast.makeText(this, "You Are Welcome", Toast.LENGTH_SHORT).show();

            CreateUserAcount(Email,Name,Password);
        }

    }

    private void CreateUserAcount(String email, final String name, String password) {

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

//                            if (bickimgUri != null) {

                                updateUserInfo(name, bickimgUri, mAuth.getCurrentUser());
//                            }
//                           else
//                                updateUserInfoWithoutPhoto(name, imgDefult, mAuth.getCurrentUser());

                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Account Creation filed "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            btn.setVisibility(View.INVISIBLE);
                            lodingprogressbar.setVisibility(View.INVISIBLE);

                        }
                    }

                });
    }

    private void updateUserInfo(final String name, Uri bickimg, final FirebaseUser currentUser) {

        StorageReference mStorag= FirebaseStorage.getInstance().getReference().child("users_photo");
        final StorageReference imgfilepath = mStorag.child(bickimg.getLastPathSegment());
        imgfilepath.putFile(bickimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imgfilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest prfileupdate= new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();


                        currentUser.updateProfile(prfileupdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Register Completed", Toast.LENGTH_SHORT).show();

                                            updateUI();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }
    // without image
//    private void updateUserInfoWithoutPhoto(final String name, Uri imgDefult, final FirebaseUser currentUser) {
//
//        StorageReference mStorag= FirebaseStorage.getInstance().getReference().child("users_photo");
//        final StorageReference imgfilepath = mStorag.child(imgDefult.getLastPathSegment());
//        imgfilepath.putFile(imgDefult).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                imgfilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        UserProfileChangeRequest prfileupdate= new UserProfileChangeRequest.Builder()
//                                .setDisplayName(name)
//                                .setPhotoUri(uri)
//                                .build();
//
//
//                        currentUser.updateProfile(prfileupdate)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(RegisterActivity.this, "Register Completed", Toast.LENGTH_SHORT).show();
//
//                                            updateUI();
//                                        }
//                                    }
//                                });
//                    }
//                });
//            }
//        });
//    }


    private void updateUI() {
        Intent in=new Intent(this, Homeview.class);
        startActivity(in);
        finish();
    }
}
