package com.example.healthify;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity
{

    EditText editTextFirsteName;
    ProgressBar progressBar;
    EditText editTextLastName;
    EditText editTextPhoneNumber;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    String FirstName;
    String LastName;
    String phoneNumber;
    String imageUrl=null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindViews();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void bindViews()
    {
        editTextFirsteName=findViewById(R.id.profile_EditText_profileName);
        editTextLastName =findViewById(R.id.profile_EditText_LastName);
        editTextPhoneNumber=findViewById(R.id.profile_EditText_PhoneNumber);
        progressBar=findViewById(R.id.profile_ProgressBar);
        user =FirebaseAuth.getInstance().getCurrentUser();
//        if(user!=null)
//        {
//
//        }

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
       // DocumentReference documentReference =firebaseFirestore.collection("Users").document(mAuth.getCurrentUser().getUid());
//        Map<String,Object> user =new HashMap<>();
//        user.put("LastName",LastName);
//        user.put("FirstName",FirstName);
//        user.put("Phone",phoneNumber);
//        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(ProfileActivity.this,"Saved!",Toast.LENGTH_SHORT).show();
            }
//        });
//    }


}
