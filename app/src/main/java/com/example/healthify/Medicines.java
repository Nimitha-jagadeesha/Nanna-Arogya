package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

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

public class Medicines extends AppCompatActivity implements View.OnClickListener {

    private int notificationId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicines);
        findViewById(R.id.setBtn).setOnClickListener(this);
        findViewById(R.id.cancelBtn).setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view)
    {
        EditText editText = findViewById(R.id.editText);
        TimePicker timePicker = findViewById(R.id.timePicker);

        Intent intent = new Intent(Medicines.this,AlarmReceiver.class);
        intent.putExtra("notificationId",notificationId);
        intent.putExtra("todo",editText.getText().toString());

        PendingIntent alarmIntent = PendingIntent.getBroadcast(Medicines.this,
                0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        switch (view.getId()){
            case R.id.setBtn:
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                Toast.makeText(this,hour+" "+minute,Toast.LENGTH_SHORT).show();
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY,hour);
                startTime.set(Calendar.MINUTE,minute);
                startTime.set(Calendar.SECOND,0);
                long alarmStartTime  = startTime.getTimeInMillis();
                alarm.set(AlarmManager.RTC_WAKEUP,alarmStartTime,alarmIntent);

                Toast.makeText(this,"Done!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.cancelBtn:
                alarm.cancel(alarmIntent);




        }
    }

}