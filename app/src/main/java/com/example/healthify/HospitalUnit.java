package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class HospitalUnit extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText dateEditText;
    EditText timeEditText;
    EditText hospitalEditText;
    Calendar calendar;
    AlarmManager alarmManager=null;
    PendingIntent broadcast;
    DatabaseReference databaseReminders;
    FirebaseUser user;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_unit);
        bindViews();
    }

    private void bindViews()
    {
        dateEditText=findViewById(R.id.editText_date);
        calendar=Calendar.getInstance();
        timeEditText=findViewById(R.id.time_EditText);
        hospitalEditText =findViewById(R.id.hospital_name_editText);
        dateEditText.setText(getCurrentDateAndTime());
        user= FirebaseAuth.getInstance().getCurrentUser();
        id=user.getUid();
        databaseReminders= FirebaseDatabase.getInstance().getReference("hospitals").child(id);
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
        AlarmReceiver.Notificationmsg=hospitalEditText.getText().toString();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        calendar.set(Calendar.SECOND,0);
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), broadcast);
        saveNotification(hospitalEditText.getText().toString(),alarmManager,calendar);
        Toast.makeText(this,"Set",Toast.LENGTH_SHORT).show();
    }

    private void saveNotification(String toString, AlarmManager alarmManager, Calendar calendar)
    {

    }
}
