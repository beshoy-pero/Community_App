package com.beshoykamal.communityapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.beshoykamal.communityapp.R;

public class HomeActivity extends AppCompatActivity {

    Intent homeViewActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeViewActivity=new Intent(this, Homeview.class);
        /// make anim latter
        Thread thread=new Thread(){
            @Override
            public void run(){
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(homeViewActivity);
                    isFinishing();
                }
            }
        };
        thread.start();
    }

    public void clicklogoSplash(View view) {
        startActivity(homeViewActivity);
        isFinishing();
    }
}
