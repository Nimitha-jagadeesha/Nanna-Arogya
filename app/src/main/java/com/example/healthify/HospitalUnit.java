package com.example.healthify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.List;

public class HospitalUnit extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText dateEditText;
    EditText timeEditText;
    EditText hospitalEditText;
    Calendar calendar;
    AlarmManager alarmManager=null;
    PendingIntent broadcast;
    DatabaseReference databaseHospital;
    FirebaseUser user;
    String id;
    ListView listViewHospital;
    List<HospitalData> hospitalList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_unit);
        listViewHospital=findViewById(R.id.list_view_hospital);
        user= FirebaseAuth.getInstance().getCurrentUser();
        id=user.getUid();
        databaseHospital= FirebaseDatabase.getInstance().getReference("hospitals").child(id);
        bindViews();
        listViewHospital.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HospitalData hospital=hospitalList.get(position);
                showDeleteDialog(hospital.getHospitalId(),hospital.getHospitalName());
                return true;
            }
        });
    }

    private void showDeleteDialog(final String hospitalId, String name)
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
                deleteHospital(hospitalId);
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

    private void deleteHospital(String hospitalId)
    {
        DatabaseReference databaseReferenceMedicine =FirebaseDatabase.getInstance().getReference("hospitals").child(user.getUid()).child(hospitalId);
        databaseReferenceMedicine.removeValue();
        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
    }

    private void bindViews()
    {
        hospitalList=new ArrayList<>();
        dateEditText=findViewById(R.id.editText_date);
        calendar=Calendar.getInstance();
        timeEditText=findViewById(R.id.time_EditText);
        hospitalEditText =findViewById(R.id.hospital_name_editText);
        dateEditText.setText(getCurrentDateAndTime());



    }

    public void onClickDatePicker(View v)
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        String date = dayOfMonth + "/" + month + "/" + year;
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR,year);
        dateEditText.setText(date);
    }
    public String getCurrentDateAndTime()
    {
        Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int dayOfMonth=c.get(Calendar.DAY_OF_MONTH);
        String date = dayOfMonth + "/" + month + "/" + year;
        calendar.set(year,month,dayOfMonth);
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String time=(hour+":"+minute);
        timeEditText.setText(time);
        return date;
    }

    public void onClickTimePicker(View view)
    {
        TimePickerDialog timePickerDialog =new TimePickerDialog(this,this,Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String s="";
        s+=hourOfDay+":";
        s+=minute+"";
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
        timeEditText.setText(s);
    }

    public void onClickSetHospital(View view)
    {
        if(hospitalEditText.getText().toString().isEmpty())
        {
            hospitalEditText.setError("Enter this Field");
            hospitalEditText.requestFocus();
            return;
        }
        AlarmReceiver.Notificationmsg=hospitalEditText.getText().toString();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        calendar.set(Calendar.SECOND,0);
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), broadcast);
        saveNotification(hospitalEditText.getText().toString(),calendar);
        Toast.makeText(this,"Set",Toast.LENGTH_SHORT).show();
    }

    private void saveNotification(String hospitalName,Calendar calendar)
    {

        String id=databaseHospital.push().getKey();
        String date=calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR);
        String time =calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
       HospitalData hospitalData=new HospitalData(date,id,hospitalName,time);
        databaseHospital.child(id).setValue(hospitalData);
        Toast.makeText(this,"Hospital name added successfully!",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseHospital.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hospitalList.clear();
                for(DataSnapshot hospitalSnapShot: dataSnapshot.getChildren())
                {
                    HospitalData hospitalData1= hospitalSnapShot.getValue(HospitalData.class);
                    hospitalList.add(hospitalData1);
                }
                HospitalList hospitalList1= new HospitalList(HospitalUnit.this,hospitalList);
                listViewHospital.setAdapter(hospitalList1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
