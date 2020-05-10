package com.example.healthify;

import android.app.AlarmManager;
import android.app.PendingIntent;

class MedicineNotification
{
    String notificationId;
    AlarmManager alarmManager;
    String medicineName;
    int hour;
    int minute;
    public MedicineNotification()
    {

    }

    public MedicineNotification(String notificationId, AlarmManager alarmManager, String medicineName, int hour, int minute) {
        this.notificationId = notificationId;
        this.alarmManager = alarmManager;
        this.medicineName = medicineName;
        this.hour = hour;
        this.minute = minute;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public AlarmManager getAlarmManager() {
        return alarmManager;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
