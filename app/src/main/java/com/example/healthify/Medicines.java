package com.example.healthify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class Medicines extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    EditText editTextMedicine;
    EditText editTextTime;
    AlarmManager alarmManager=null;
    PendingIntent broadcast;
    DatabaseReference databaseReminders;
    FirebaseUser user;
    ArrayList<MedicineNotification> medicineList;
    ListView  listViewMedicine;
    String id;
    Calendar startTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicines);
        bindViews();
        listViewMedicine.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MedicineNotification medicine=medicineList.get(position);
                showDeleteDialog(medicine.getNotificationId(),medicine.getMedicineName());
                return true;
            }
        });
    }

    private void showDeleteDialog(final String notificationId,  String name)
    {
        AlertDialog.Builder dialogBuilder =new AlertDialog.Builder(this);
        LayoutInflater inflater =getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog,null);
        dialogBuilder.setView(dialogView);
        final Button delete=dialogView.findViewById(R.id.delete_medicine_notification);
        dialogBuilder.setTitle("Deleting "+name);
        final Button cancel=dialogView.findViewById(R.id.cancel_medicine_notification_dialog);
        final AlertDialog dialog=dialogBuilder.create();
        dialog.show();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMedicine(notificationId);
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

    private void deleteMedicine(String notificationId)
    {
        DatabaseReference databaseReferenceMedicine =FirebaseDatabase.getInstance().getReference("reminders").child(user.getUid()).child(notificationId);
        databaseReferenceMedicine.removeValue();
        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
    }

    private void bindViews()
    {
        medicineList=new ArrayList<>();
        user=FirebaseAuth.getInstance().getCurrentUser();
        databaseReminders= FirebaseDatabase.getInstance().getReference("reminders").child(user.getUid());
        listViewMedicine=findViewById(R.id.medicine_listView);
        editTextMedicine= findViewById(R.id.editText);
        startTime = Calendar.getInstance();
        editTextTime=findViewById(R.id.editText_time_medicine);
        editTextTime.setText(startTime.get(Calendar.HOUR)+":"+startTime.get(Calendar.MINUTE));
    }

    public  void onClickSet(View view)
    {
        editTextMedicine= findViewById(R.id.editText);
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

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), broadcast);
       saveNotification(editTextMedicine.getText().toString(),startTime.get(Calendar.HOUR),startTime.get(Calendar.MINUTE));
        Toast.makeText(this,"Set",Toast.LENGTH_SHORT).show();
    }

    private void saveNotification(String medicine,int hour,int minute)
    {

            String id=databaseReminders.push().getKey();
            MedicineNotification medicineNotification=new MedicineNotification(id,medicine,hour,minute);
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
    public void onClickTimePickerMedicine(View view)
    {
        TimePickerDialog timePickerDialog =new TimePickerDialog(this,this,Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        startTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
        startTime.set(Calendar.MINUTE,minute);
        startTime.set(Calendar.SECOND,0);
        editTextTime.setText(hourOfDay+":"+minute);
    }
}