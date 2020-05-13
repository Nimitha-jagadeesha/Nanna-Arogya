package com.example.healthify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportsUpload extends AppCompatActivity {

    private ImageView imageView;
    private Button buttonUpload;
    ListView pdfListView;
    private static final int PICK_IMAGE_REQUEST=234;
    private Uri filePath;
    private StorageReference storageReference;
    EditText editTextPdfName;
    String id;
    FirebaseUser user;
    List<uploadPDF> uploadPDFList;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_upload);
        bindViews();
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextPdfName.getText().toString().isEmpty())
                {
                    editTextPdfName.setError("enter the pdf name");
                    editTextPdfName.requestFocus();
                    return;
                }
                selectPDF();
            }
        });
        viewAllFiles();
        pdfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                uploadPDF uploadPdf = uploadPDFList.get(position);
                Intent intent  =new Intent();
                intent.setDataAndType(Uri.parse(uploadPdf.getUrl()), "application/pdf");
                startActivity(Intent.createChooser(intent, "Choose an Application:"));
            }
        });
       pdfListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               deletePdf(position);
               return true;
           }
       });
    }

    private void deletePdf(int position)
    {
        final  uploadPDF uploadPdf = uploadPDFList.get(position);
        AlertDialog.Builder dialogBuilder =new AlertDialog.Builder(this);
        LayoutInflater inflater =getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog,null);
        dialogBuilder.setView(dialogView);
        final Button delete=dialogView.findViewById(R.id.delete_medicine_notification);
        dialogBuilder.setTitle("Deleting "+uploadPdf.getPdfName());
        final Button cancel=dialogView.findViewById(R.id.cancel_medicine_notification_dialog);
        final AlertDialog dialog=dialogBuilder.create();
        dialog.show();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReferenceMedicine =FirebaseDatabase.getInstance().getReference("reports").child(user.getUid()).child(uploadPdf.getId());
                databaseReferenceMedicine.removeValue();
                StorageReference deleteFile = storageReference.child((uploadPdf.getUrl()));
                deleteFile.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(ReportsUpload.this, "Pdf deleted", Toast.LENGTH_SHORT).show();

                    }
                });
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void viewAllFiles()
    {
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               uploadPDFList.clear();
               for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
               {
                   uploadPDF uploadPdf =postSnapshot.getValue(uploadPDF.class);
                   uploadPDFList.add(uploadPdf);
               }

               String [] uploads =new String[uploadPDFList.size()];
               for(int i=0;i<uploads.length;i++)
               {
                   uploads[i]=uploadPDFList.get(i).getPdfName();
               }
               ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads);
               pdfListView.setAdapter(adapter);

           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    private void bindViews()
    {
        user= FirebaseAuth.getInstance().getCurrentUser();
        id=user.getUid();
        editTextPdfName =findViewById(R.id.reportUpload_editText);
        pdfListView=findViewById(R.id.reports_listView);
        storageReference =FirebaseStorage.getInstance().getReference();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("reports").child(id);
        buttonUpload=findViewById(R.id.buttonUpload);
        uploadPDFList=new ArrayList<>();
    }

    private void selectPDF()
    {
        Intent intent =new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf File"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            uploadPDFFile(data.getData());
        }
    }

    private void uploadPDFFile(Uri data)
    {
        final ProgressDialog progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        StorageReference reference =storageReference.child("reports/"+System.currentTimeMillis()+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        try {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            String localId=databaseReference.push().getKey();
                            uploadPDF uploadPdf = new uploadPDF(localId, editTextPdfName.getText().toString(), url.toString());
                            databaseReference.child(localId).setValue(uploadPdf);
                            Toast.makeText(ReportsUpload.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(ReportsUpload.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot)
            {
                double progress= (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded"+(int)progress+"%");
            }
        });
    }


}