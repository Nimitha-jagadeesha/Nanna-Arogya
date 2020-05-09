package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class ReportsUpload extends AppCompatActivity {

    Button buttonUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_upload);
        bindViews();
    }

    private void bindViews()
    {
        buttonUpload = findViewById(R.id.Button_upload);
    }
}
