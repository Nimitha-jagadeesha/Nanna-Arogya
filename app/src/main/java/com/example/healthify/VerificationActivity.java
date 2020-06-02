package com.example.healthify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificationActivity extends AppCompatActivity {

    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
    }
    public void onClickResend(View v)
    {
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(VerificationActivity.this, "Mail Sent", Toast.LENGTH_SHORT).show();
            }
    });
}
        public void OnClickVerified(View view)
        {
            FirebaseUser user =mAuth.getCurrentUser();
            user.reload();
            if(user.isEmailVerified())
            {
                finish();
                startActivity(new Intent(this,AllActivities.class));
            }
            else
                Toast.makeText(this, "Sorry! Your Email is not yet Verified", Toast.LENGTH_SHORT).show();
        }
}
