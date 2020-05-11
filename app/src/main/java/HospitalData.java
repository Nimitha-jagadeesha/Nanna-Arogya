public class HospitalData
{
    String HospitalName;
    String Date;
    String Time;

    public HospitalData()
    {

    }

    public HospitalData(String hospitalName, String date, String time)
    {
        HospitalName = hospitalName;
        Date = date;
        Time = time;
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
