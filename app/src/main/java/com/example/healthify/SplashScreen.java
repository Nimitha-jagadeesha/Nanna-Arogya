package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth=FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if(mAuth.getCurrentUser()!=null)
                {
                    i=new Intent(SplashScreen.this,AllActivities.class);
                }
                else {
                   i = new Intent(SplashScreen.this, MainActivity.class);

                }
                startActivity(i);
                finish();
            }
        }, 500);
    }
}
