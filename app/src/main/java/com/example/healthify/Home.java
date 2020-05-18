package com.example.healthify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        loadSettings();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","healthifySupport@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support request");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
    }
    private void loadSettings()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("Data_heathery",MODE_PRIVATE);
        ProfileActivity.checked=sharedPreferences.getBoolean("Setting",false);
        if(ProfileActivity.checked)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(this,ProfileActivity.class));
                break;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out the app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.about:
                startActivity(new Intent(this,AboutUs.class));
                break;
            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickProfileInfo(View v)
    {
        startActivity(new Intent(this,ProfileInfo.class));
    }
    public void onClickMedicines(View view)
    {
        startActivity(new Intent(this,Medicines.class));

    }

    public void onClickReportUpload(View v)
    {
        startActivity(new Intent(this,ReportsUpload.class));

    }

    public void onClickHospital(View view)
    {
        startActivity(new Intent(this,HospitalUnit.class));

    }
}
