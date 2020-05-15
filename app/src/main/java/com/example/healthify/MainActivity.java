package com.example.healthify;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), Home.class));
        }
        setContentView(R.layout.activity_main);
        bindViews();

    }

    private void bindViews()
    {
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText=findViewById(R.id.editTextPassword);
        progressBar=findViewById(R.id.progressbar);

    }

    public void onClickSignUp(View v)
    {
        Intent intent =new Intent(this,SignUp.class);
        startActivity(intent);
    }
    public void onClickLoginButton(View v)
    {
        userLogin();
    }

    private void userLogin()
    {
        String mail =emailEditText.getText().toString().trim();
        String password=passwordEditText.getText().toString();
        if(mail.isEmpty())
        {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches())
        {
            emailEditText.setError("Enter the valid Email Id");
            emailEditText.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            passwordEditText.setError("Minimum Length of password Should be 6");
            passwordEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent intent=new Intent(MainActivity.this,Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    finish();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Login Unsucessful",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
