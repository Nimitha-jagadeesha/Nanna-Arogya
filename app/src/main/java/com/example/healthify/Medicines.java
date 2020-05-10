package com.example.healthify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Medicines extends AppCompatActivity  {

    EditText editTextMedicine;
    TimePicker timePicker;
    AlarmManager alarmManager=null;
    PendingIntent broadcast;
    DatabaseReference databaseReminders;
    FirebaseUser user;
    ArrayList<MedicineNotification> medicineList;
    ListView  listViewMedicine;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicines);
        bindViews();

    }

    private void bindViews()
    {
        medicineList=new ArrayList<>();
        user=FirebaseAuth.getInstance().getCurrentUser();
        id=user.getUid();
        databaseReminders= FirebaseDatabase.getInstance().getReference("reminders").child(id);
        listViewMedicine=findViewById(R.id.medicine_listView);
        editTextMedicine= findViewById(R.id.editText);
        timePicker = findViewById(R.id.timePicker);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void onClickSet(View view)
    {
        editTextMedicine= findViewById(R.id.editText);
         timePicker = findViewById(R.id.timePicker);
         if(editTextMedicine.getText().toString().isEmpty())
         {
             editTextMedicine.setError("Name cannot be empty");
             editTextMedicine.requestFocus();
             return;
         }
        AlarmReceiver.Notificationmsg=editTextMedicine.getText().toString();
         alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY,hour);
        startTime.set(Calendar.MINUTE,minute);
        startTime.set(Calendar.SECOND,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), broadcast);
        saveNotification(editTextMedicine.getText().toString(),alarmManager,hour,minute);
        Toast.makeText(this,"Set",Toast.LENGTH_SHORT).show();
    }

    private void saveNotification(String medicine,AlarmManager alarmManager,int hour,int minute)
    {

            String id=databaseReminders.push().getKey();
            MedicineNotification medicineNotification=new MedicineNotification(id,alarmManager,medicine,hour,minute);
            databaseReminders.child(id).setValue(medicineNotification);
            Toast.makeText(this,"Track name added successfully!",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReminders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicineList.clear();
                for(DataSnapshot trackSnapShot: dataSnapshot.getChildren())
                {
                    MedicineNotification track=trackSnapShot.getValue(MedicineNotification.class);
                    medicineList.add(track);
                }
                MedicineList notiList=new MedicineList(Medicines.this,medicineList);
                listViewMedicine.setAdapter(notiList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClickCancel(View view)
    {
        if(alarmManager!=null)
        {
            alarmManager.cancel(broadcast);
            Toast.makeText(this,"Reminder Cancelled",Toast.LENGTH_SHORT).show();
            alarmManager=null;
        }
        else
        {
            Toast.makeText(this,"No Reminder To Cancel",Toast.LENGTH_SHORT).show();
        }
    }

}