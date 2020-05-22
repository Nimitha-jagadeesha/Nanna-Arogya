package com.example.healthify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EmergencyCall extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText editTextPhoneNumber;
    EditText editTextName;
    DatabaseReference databasePhoneNumbers;
    FirebaseUser user;
    List<PhoneNumbers>phoneNumberList;
    ListView phoneNumberListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        bindViews();
        loadSettings();
        phoneNumberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhoneNumbers phoneNumber = phoneNumberList.get(position);
               editTextName.setText(phoneNumber.getPhoneNumberName());
               editTextPhoneNumber.setText(phoneNumber.getPhoneNumber());

            }
        });
       phoneNumberListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteNumber(position);
                return true;
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void deleteNumber(int position)
    {
        final  PhoneNumbers number = phoneNumberList.get(position);
        AlertDialog.Builder dialogBuilder =new AlertDialog.Builder(this);
        LayoutInflater inflater =getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog,null);
        dialogBuilder.setView(dialogView);
        final Button delete=dialogView.findViewById(R.id.delete_medicine_notification);
        dialogBuilder.setTitle("Deleting "+number.getPhoneNumberName());
        final Button cancel=dialogView.findViewById(R.id.cancel_medicine_notification_dialog);
        final AlertDialog dialog=dialogBuilder.create();
        dialog.show();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("phoneNumbers").child(user.getUid()).child(number.getPhoneNumberId());
                databaseReference.removeValue();
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

    private void bindViews()
    {
        editTextPhoneNumber = findViewById(R.id.emergencyCall_editTextPhoneNumber);
        editTextName= findViewById(R.id.emergencyCall_editTextName);
        user=FirebaseAuth.getInstance().getCurrentUser();
        databasePhoneNumbers= FirebaseDatabase.getInstance().getReference("phoneNumbers").child(user.getUid());
        phoneNumberList=new ArrayList<>();
        phoneNumberListView=findViewById(R.id.ListView_contact);
    }

    public void onClickAdd(View v)
    {
        if (editTextPhoneNumber.getText().toString().trim().isEmpty()) {
           editTextPhoneNumber.setError("This field cannot be empty");
            return;
        }
        if (editTextName.getText().toString().trim().isEmpty()) {
            editTextName.setError("This field cannot be empty");
            return;
        }
        String id = databasePhoneNumbers.push().getKey();
        PhoneNumbers phoneNumber =new PhoneNumbers(id,editTextPhoneNumber.getText().toString().trim(),editTextName.getText().toString().trim());
        databasePhoneNumbers.child(id).setValue(phoneNumber);
        Toast.makeText(this,"Phone Number Added!",Toast.LENGTH_SHORT).show();
    }

    public void onClickCall(View v) {
        if (editTextPhoneNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Number cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String s = "tel:" + editTextPhoneNumber.getText().toString().trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(s));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},1);
            return;
        }
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
//        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(connectivity,filter);
           databasePhoneNumbers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //progressBar.setVisibility(View.VISIBLE);
                phoneNumberList.clear();
                for(DataSnapshot trackSnapShot: dataSnapshot.getChildren())
                {
                    PhoneNumbers numbers=trackSnapShot.getValue(PhoneNumbers.class);
                    phoneNumberList.add(numbers);
                }
                String names[]=new String[phoneNumberList.size()];
                for(int i=0;i<names.length;i++)
                {
                    names[i]=phoneNumberList.get(i).getPhoneNumberName();
                }
//                ReportsList reportslist= new ReportsList(ReportsUpload.this,uploadPDFList);
//                pdfListView.setAdapter(reportslist);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,names);
               phoneNumberListView.setAdapter(adapter);
//                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
