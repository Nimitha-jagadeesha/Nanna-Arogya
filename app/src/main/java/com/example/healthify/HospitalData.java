package com.example.healthify;
public class HospitalData
{
    String date;
    String hospitalId;
    String hospitalName;
    String time;


    public HospitalData() {
    }

    public HospitalData(String date, String hospitalId, String hospitalName, String time) {
        this.date = date;
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.time = time;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
