package com.example.healthify;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MedicineList extends ArrayAdapter<MedicineNotification> {
    private Activity contest;
    private List<MedicineNotification> medicineList;
    public  MedicineList( Activity contest, List<MedicineNotification> trackList) {
        super(contest,R.layout.notification_list_view,trackList);
        this.contest = contest;
        this.medicineList = trackList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =contest.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.notification_list_view,null,true);
        TextView textViewName=listViewItem.findViewById(R.id.medicine_name);
        TextView textViewTime=listViewItem.findViewById(R.id.time_of_dosage);
        MedicineNotification medicine=medicineList.get(position);
        textViewName.setText(String.valueOf(medicine.getMedicineName()));
        textViewTime.setText(medicine.getHour()+":"+medicine.getMinute());

        return listViewItem;
    }
}
