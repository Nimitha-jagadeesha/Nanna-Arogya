package com.example.healthify;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity
{

    EditText editTextFirsteName;
    ProgressBar progressBar;
    EditText editTextLastName;
    EditText editTextPhoneNumber;
    FirebaseUser user;
    String FirstName;
    String LastName;
    String phoneNumber;
    DatabaseReference databaseReference;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindViews();

    }

    private void bindViews()
    {
        editTextFirsteName=findViewById(R.id.profile_EditText_profileName);
        editTextLastName =findViewById(R.id.profile_EditText_LastName);
        editTextPhoneNumber=findViewById(R.id.profile_EditText_PhoneNumber);
        progressBar=findViewById(R.id.profile_ProgressBar);
        user=FirebaseAuth.getInstance().getCurrentUser();
        id=user.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("profiles").child(id);
    }


    public void onClickSave(View view)
    {
       FirstName = editTextFirsteName.getText().toString().trim();
       LastName =editTextLastName.getText().toString().trim();
       phoneNumber=editTextPhoneNumber.getText().toString().trim();

       if(FirstName.isEmpty())
       {
           editTextFirsteName.setError("This field is required");
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
                    editTextFirsteName.setText(profile.getFirstname());
                    editTextLastName.setText(profile.getLastname());
                    editTextPhoneNumber.setText(profile.getPhoneNumber());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
