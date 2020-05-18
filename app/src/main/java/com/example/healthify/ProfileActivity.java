package com.example.healthify;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity
{

    private static final int PIC_IMAGE = 1;
    EditText editTextFirstName;
    ImageView imageViewProfilePic;
    ProgressBar progressBar;
    EditText editTextLastName;
    EditText editTextPhoneNumber;
    FirebaseUser user;
    String FirstName;
    String LastName;
    String phoneNumber;
    DatabaseReference databaseReference;
    String id;
    FirebaseAuth mAuth;
    Uri uriprofileImage;
    String imageUrl;
    static boolean checked=false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindViews();
        final Switch sw =  findViewById(R.id.switch_home);
        sw.setChecked(checked);
        if(sw!=null)
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        checked=true;
                        saveSettings(true);
                        finish();
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        checked=false;
                        saveSettings(false);
                        finish();
                    }
                }
            });
    }

    private void saveSettings(boolean b)
    {
        SharedPreferences sharedPreferences=getSharedPreferences("Data_heathery",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("Setting",b);
        editor.apply();
    }

    private void loadUserInformation()
    {
        if(mAuth.getCurrentUser()!=null)
        {
            FirebaseUser user=mAuth.getCurrentUser();
            if(user.getPhotoUrl()!=null)
            {
                String url =user.getPhotoUrl().toString();
                Glide.with(this).load(url).into(imageViewProfilePic);
            }

        }
    }

    private void bindViews()
    {
        editTextFirstName=findViewById(R.id.profile_EditText_profileName);
        editTextLastName =findViewById(R.id.profile_EditText_LastName);
        editTextPhoneNumber=findViewById(R.id.profile_EditText_PhoneNumber);
        progressBar=findViewById(R.id.profile_ProgressBar);
        user=FirebaseAuth.getInstance().getCurrentUser();
        id=user.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("profiles").child(id);
        imageViewProfilePic=findViewById(R.id.profile_imageView_picture);
        mAuth=FirebaseAuth.getInstance();
    }


    public void onClickSave(View view)
    {
        FirstName = editTextFirstName.getText().toString().trim();
        LastName =editTextLastName.getText().toString().trim();
        phoneNumber=editTextPhoneNumber.getText().toString().trim();

        if(FirstName.isEmpty())
        {
            editTextFirstName.setError("This field is required");
            return;
        }
        if(LastName.isEmpty())
        {
            editTextLastName.setError("This field is required");
            return;
        }
        if(phoneNumber.isEmpty())
        {
            editTextPhoneNumber.setError("This field is required");
            return;
        }
        if(user!=null&&imageUrl!=null)
        {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(FirstName + " " + LastName)
                    .setPhotoUri(Uri.parse(imageUrl))
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ProfileActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        id=user.getUid();
        Profile newProfile=new Profile(id,FirstName,LastName,phoneNumber);
        databaseReference.child(id).setValue(newProfile);
        Toast.makeText(this,"Profile Updated!",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot trackSnapShot: dataSnapshot.getChildren())
                {
                    Profile profile=trackSnapShot.getValue(Profile.class);
                    editTextFirstName.setText(profile.getFirstname());
                    editTextLastName.setText(profile.getLastname());
                    editTextPhoneNumber.setText(profile.getPhoneNumber());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void onClickProfilePicImageView(View view)
    {
        showImageChooser();
    }

    private void showImageChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile image"),PIC_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PIC_IMAGE&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            uriprofileImage=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uriprofileImage);
                imageViewProfilePic.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {
        StorageReference profileImageReference= FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(uriprofileImage!=null)
        {
            progressBar.setVisibility(View.VISIBLE);
            profileImageReference.putFile(uriprofileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    imageUrl=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}