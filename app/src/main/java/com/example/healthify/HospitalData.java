package com.example.healthify;
public class HospitalData
{
    String Date;
    String HospitalId;
    String HospitalName;
    String Time;


    public HospitalData() {
    }

    public HospitalData(String date, String hospitalId, String hospitalName, String time) {
        Date = date;
        HospitalId = hospitalId;
        HospitalName = hospitalName;
        Time = time;
    }

    public String getHospitalId() {
        return HospitalId;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }
}
