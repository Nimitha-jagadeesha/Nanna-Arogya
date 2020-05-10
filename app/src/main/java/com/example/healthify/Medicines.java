package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Medicines extends AppCompatActivity  {

    EditText editTextMedicine;
    TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicines);

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
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY,hour);
        startTime.set(Calendar.MINUTE,minute);
        startTime.set(Calendar.SECOND,0);
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.SECOND, 5);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), broadcast);
        Toast.makeText(this,"Set",Toast.LENGTH_SHORT).show();
    }

}