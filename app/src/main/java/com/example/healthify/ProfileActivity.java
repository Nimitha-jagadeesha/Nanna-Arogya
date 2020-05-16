package com.example.healthify;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    Uri uriprofileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindViews();
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
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}