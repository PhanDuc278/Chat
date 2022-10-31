package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    FirebaseAuth auth ;
    FirebaseUser user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(() -> {
            if(user==null) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }else {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }
}