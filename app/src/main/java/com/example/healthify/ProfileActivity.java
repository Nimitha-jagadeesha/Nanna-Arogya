package com.example.healthify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity
{

    private static final int CHOOSE_IMAGE =101 ;
    EditText editTextProfileName;
    ImageView imageViewProfilePicture;
    Uri uriProfileImage;
    ProgressBar progressBar;
    String profileImageUrl;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindViews();
       // loadUserInformation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), com.example.healthify.MainActivity.class));
        }

    }

    private void loadUserInformation()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user.getPhotoUrl().toString()!=null) {
            //Glide.with(this).load(user.getPhotoUrl().toString()).into(imageViewProfilePicture);
            Toast.makeText(this,user.getPhotoUrl().toString(),Toast.LENGTH_SHORT).show();
        }
        if(user.getDisplayName()!=null) {
            editTextProfileName.setText(user.getPhotoUrl().toString());
        }

    }

    private void bindViews()
    {
        editTextProfileName=findViewById(R.id.profile_EditText_profileName);
        imageViewProfilePicture=findViewById(R.id.profile_imageView_picture);
        progressBar=findViewById(R.id.profile_ProgressBar);
        mAuth=FirebaseAuth.getInstance();
    }

    public void onClickCamera(View view)
    {
        showImageChooser();
    }

    public void onClickSave(View view)
    {
        saveUserInformation();
    }

    private void saveUserInformation()
    {
        String name = editTextProfileName.getText().toString();
        if(name.isEmpty())
        {
            editTextProfileName.setError("Name required");
            editTextProfileName.requestFocus();
            return;
        }
        FirebaseUser user =mAuth.getCurrentUser();

        if(user!=null&&profileImageUrl!=null)
        {
            UserProfileChangeRequest profile =new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ProfileActivity.this,"ProfileUpdated",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void showImageChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"),CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_IMAGE&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            uriProfileImage=data.getData();
            imageViewProfilePicture.setImageURI(uriProfileImage);
            uploadImageToFirebaseStorage();
        }
    }

    private void uploadImageToFirebaseStorage()
    {
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(uriProfileImage!=null)
        {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    profileImageUrl=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
