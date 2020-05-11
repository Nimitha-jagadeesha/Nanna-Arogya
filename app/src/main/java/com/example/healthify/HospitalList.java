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

public class HospitalList extends ArrayAdapter <HospitalData> {
    private Activity contest;
    private List<HospitalData> hospitalList;
    public  HospitalList( Activity contest, List<HospitalData> hospitalList) {
        super(contest,R.layout.hospital_list,hospitalList);
        this.contest = contest;
        this.hospitalList = hospitalList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =contest.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.hospital_list,null,true);
        TextView textViewName=listViewItem.findViewById(R.id.hospital_textView_hospitalName);
        TextView textViewDate=listViewItem.findViewById(R.id.hospital_textView_Date);
        TextView textViewTime = listViewItem.findViewById(R.id.hospital_textView_Time);
        HospitalData hospitalData=hospitalList.get(position);
        textViewName.setText(hospitalData.getHospitalName());
        textViewTime.setText(hospitalData.getTime());
        textViewDate.setText(hospitalData.getDate());
        return listViewItem;
    }


}
