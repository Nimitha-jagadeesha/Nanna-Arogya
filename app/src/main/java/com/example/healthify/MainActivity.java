package com.example.healthify;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    int AUTH_UI_REQUEST_CODE =10001;
    BroadCast connectivity=new BroadCast();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null&&mAuth.getCurrentUser().isEmailVerified()) {
            finish();
            startActivity(new Intent(getApplicationContext(),AllActivities.class));
        }

        setContentView(R.layout.activity_main);

    }


    public void onClickSignUp(View v)
    {
       List<AuthUI.IdpConfig> provider= Arrays.asList(
               new AuthUI.IdpConfig.EmailBuilder().build(),
               new AuthUI.IdpConfig.GoogleBuilder().build(),
               new AuthUI.IdpConfig.PhoneBuilder().build()
       );
       Intent intent = AuthUI.getInstance()
               .createSignInIntentBuilder()
               .setAvailableProviders(provider)
               .setTosAndPrivacyPolicyUrls("https://example.com","https://example.com")
               .setAlwaysShowSignInMethodScreen(true)
               .setLogo(R.mipmap.login)
               .build();
       startActivityForResult(intent,AUTH_UI_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AUTH_UI_REQUEST_CODE)
        {
            if(resultCode==RESULT_OK)
            {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                if(user.getMetadata().getCreationTimestamp()==user.getMetadata().getLastSignInTimestamp())
                {
                    Toast.makeText(this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Welcome Back", Toast.LENGTH_SHORT).show();

                }
                    finish();
                    startActivity(new Intent(this,AllActivities.class));
            }
            else
            {

            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivity,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectivity);
    }

}
