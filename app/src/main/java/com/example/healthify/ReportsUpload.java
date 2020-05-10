package com.example.healthify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ReportsUpload extends AppCompatActivity {

    Button buttonUpload;
    EditText editTextName;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_upload);
        bindViews();
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPDFFile();
            }
        });
    }

    private void setPDFFile()
    {
        Intent intent =new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select pdf File"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&&requestCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            uploadPDFFile(data.getData());
        }
    }

    private void uploadPDFFile(Uri data)
    {
        progressBar.setVisibility(View.VISIBLE);
        StorageReference reference =storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");
        reference.putFile(data)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete());
                Uri url =uri.getResult();
                uploadPDF PDFUpload =new uploadPDF(editTextName.getText().toString(),url.toString());
                databaseReference.child(databaseReference.push().getKey()).setValue(PDFUpload);
                Toast.makeText(ReportsUpload.this,"Uploaded",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        })
        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

            }
        });

    }

    private void bindViews()
    {
        buttonUpload = findViewById(R.id.Button_upload);
        editTextName =findViewById(R.id.editTextFileName);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
        progressBar=findViewById(R.id.ReportsUpload_progressBar);
    }
}
