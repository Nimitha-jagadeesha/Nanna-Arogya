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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUp extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    EditText conformPasswordEditText;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        bindViews();
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), Home.class));
        }
    }

    private void bindViews()
    {
        progressBar=findViewById(R.id.progressbarSignUp);
        emailEditText=findViewById(R.id.editTextEmailId);
        passwordEditText=findViewById(R.id.SignUpEditTextPassword);
        conformPasswordEditText=findViewById(R.id.SignUpEditTextConformPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickLogin(View v)
    {
        Intent intent = new Intent(this, com.example.healthify.MainActivity.class);
        startActivity(intent);
    }
    public void onClickSignUpButton(View v)
    {
        registerUser();
    }

    private void registerUser()
    {
        String mail =emailEditText.getText().toString().trim();
        String password=passwordEditText.getText().toString();
        String conformPassword =conformPasswordEditText.getText().toString();
        if(mail.isEmpty())
        {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            passwordEditText.setError("Password cannot be empty");
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
        if(!password.equals(conformPassword))
        {
            conformPasswordEditText.setError("Password and conform password must be same");
            conformPasswordEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "User Register Sucessfull", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SignUp.this,Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    finish();
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                        Toast.makeText(SignUp.this,"You have already registered",Toast.LENGTH_SHORT).show();

                    else
                        Toast.makeText(SignUp.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
