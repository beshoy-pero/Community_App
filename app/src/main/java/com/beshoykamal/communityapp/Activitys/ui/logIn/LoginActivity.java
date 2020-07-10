package com.beshoykamal.communityapp.Activitys.ui.logIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beshoykamal.communityapp.Activitys.HomeActivity;
import com.beshoykamal.communityapp.Activitys.Homeview;
import com.beshoykamal.communityapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText namelogin,passwordlogin;
    private ImageView imagelogin;
    private Button btnlogin;
    private ProgressBar progresslogin;
    private FirebaseAuth mAuth;
    private Intent homeActivity,registerActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        namelogin=findViewById(R.id.namelogin);
        passwordlogin=findViewById(R.id.passwordlogin);
        imagelogin=findViewById(R.id.loginimg);
        btnlogin=findViewById(R.id.login_btn);
        progresslogin=findViewById(R.id.loginprogress);
        homeActivity=new Intent(this, HomeActivity.class);
        registerActivity=new Intent(this, RegisterActivity.class);

        mAuth=FirebaseAuth.getInstance();

        progresslogin.setVisibility(View.INVISIBLE);

    }

    public void login(View view) {
        progresslogin.setVisibility(View.VISIBLE);
        btnlogin.setVisibility(View.INVISIBLE);
        final String mail=namelogin.getText().toString();
        final String password=passwordlogin.getText().toString();

        if (mail.isEmpty()||password.isEmpty()) {
            Toast.makeText(this, "Please verify all fields", Toast.LENGTH_SHORT).show();
            progresslogin.setVisibility(View.INVISIBLE);
            btnlogin.setVisibility(View.VISIBLE);
        }
        else {
            signIn(mail,password);
        }
    }
    private void signIn(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progresslogin.setVisibility(View.INVISIBLE);
                    btnlogin.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "LogIn ", Toast.LENGTH_SHORT).show();
                    updateUI();
                }
                else {
                    Toast.makeText(LoginActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progresslogin.setVisibility(View.INVISIBLE);
                    btnlogin.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateUI() {

        startActivity(homeActivity);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser User= mAuth.getCurrentUser();
        if (User !=null)
            updateUI();
    }

    public void imgreg(View view) {
        startActivity(registerActivity);
        finish();
    }
}
