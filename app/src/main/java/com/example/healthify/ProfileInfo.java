package com.example.healthify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class ProfileInfo extends AppCompatActivity {

    EditText editTextHeight;
    EditText editTextWeight;
    EditText editTextAge;
    RadioButton maleRadioButton;
    RadioButton femaleRadioButton;
    DatabaseReference databasePersonalDetails;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        bindViews();
    }

    private void bindViews()
    {
        editTextHeight=findViewById(R.id.personalDetails_height);
        editTextWeight=findViewById(R.id.personalDetails_Weight);
        editTextAge = findViewById(R.id.PersonalDetails_Age);
        maleRadioButton=findViewById(R.id.radio_male);
        femaleRadioButton =findViewById(R.id.radio_female);
        user= FirebaseAuth.getInstance().getCurrentUser();
        String id =user.getUid();
        databasePersonalDetails= FirebaseDatabase.getInstance().getReference("personalInfo").child(id);
    }
    public void onClickSubmit(View view)
    {
        float bodyMassIndex;
        try {
            float height = parseFloat(editTextHeight.getText().toString().trim());
            float weight = parseFloat(editTextWeight.getText().toString().trim());

            bodyMassIndex = ((float) weight) / (height*height);
            int resInt;
            if(bodyMassIndex<18.5)
            {
                resInt =-1;
            }
            else if(bodyMassIndex>=18.5&&bodyMassIndex<=24.9)
            {
                resInt =0;
            }
            else
            {
                resInt =1;
            }
            showDialogMessage(resInt);
        }
        catch (Exception e)
        {
            if(editTextHeight.getText().toString().trim().isEmpty())
            {
                editTextHeight.setError("Enter this Field");
            }
            if(editTextWeight.getText().toString().trim().isEmpty())
            {
                editTextWeight.setError("Enter this Field");
            }
        }

    }
    private void showDialogMessage(int resInt)
    {
        AlertDialog.Builder dialogBuilder =new AlertDialog.Builder(this);
        LayoutInflater inflater =getLayoutInflater();
        final View dialogView;
        if(resInt==0)
        dialogView = inflater.inflate(R.layout.dialog,null);
        else if(resInt==-1)
        {
            dialogView = inflater.inflate(R.layout.under_weight_dialog,null);

        }
        else
            dialogView = inflater.inflate(R.layout.over_weight,null);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("");
        Button button = dialogView.findViewById(R.id.dialog_buttonOkk);
        final AlertDialog dialog=dialogBuilder.create();
        dialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void onClickDailyCalories(View view)
    {
        AlertDialog.Builder dialogBuilder =new AlertDialog.Builder(this);
        LayoutInflater inflater =getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialy_chalories,null);
        TextView bmr =dialogView.findViewById(R.id.bmr_textView);
        TextView dialyCalories= dialogView.findViewById(R.id.daily_calories_textView);
            if(maleRadioButton.isChecked())
            {
                try {
                    float height = parseFloat(editTextHeight.getText().toString().trim());
                   float weight = parseFloat(editTextWeight.getText().toString().trim());
                    int age = parseInt(editTextAge.getText().toString().trim());
                    double BMR = 66.47+(13.75*weight)+(12.7*height*39.37)-(6.755*age);
                    bmr.setText(Math.round(BMR)+"");
                    dialyCalories.setText(Math.round(BMR*1.2)+"");
                }
                catch(Exception e)
                {
                    if(editTextHeight.getText().toString().trim().isEmpty())
                    {
                        editTextHeight.setError("Enter this Field");
                        editTextHeight.requestFocus();
                    }
                    if(editTextWeight.getText().toString().trim().isEmpty())
                    {
                        editTextWeight.setError("Enter this Field");
                        editTextWeight.requestFocus();
                    }
                    if(editTextAge.getText().toString().trim().isEmpty())
                    {
                        editTextAge.setError("Enter this Field");
                        editTextAge.requestFocus();
                    }
                    return;

                }
            }
            else
            {
                try {
                    float height = parseFloat(editTextHeight.getText().toString().trim());
                    float weight = parseFloat(editTextWeight.getText().toString().trim());
                    int age = parseInt(editTextAge.getText().toString().trim());
                    double BMR = 655.1+(weight*9.563)+(4.7*height*39.37)-(4.667*age);
                    bmr.setText(Math.round(BMR)+"");
                    dialyCalories.setText(Math.round(BMR*1.2)+"");
                }
                catch(Exception e)
                {
                    if(editTextHeight.getText().toString().trim().isEmpty())
                    {
                        editTextHeight.setError("Enter this Field");
                        editTextHeight.requestFocus();
                    }
                    if(editTextWeight.getText().toString().trim().isEmpty())
                    {
                        editTextWeight.setError("Enter this Field");
                        editTextWeight.requestFocus();
                    }
                    if(editTextAge.getText().toString().trim().isEmpty())
                    {
                        editTextAge.setError("Enter this Field");
                        editTextAge.requestFocus();
                    }
                    return;
                }
            }

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("");
        Button button = dialogView.findViewById(R.id.dialog_buttonOkk);
        final AlertDialog dialog=dialogBuilder.create();
        dialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void onClickBloodVolume(View view)
    {
        try {
            int age = parseInt(editTextAge.getText().toString().trim());
            float weight = parseFloat(editTextWeight.getText().toString().trim());
            float bloodVolume;
            if(age<65)
            {
                bloodVolume=weight*70;
            }
            else
            {
                bloodVolume=weight*60;
            }
            AlertDialog.Builder dialogBuilder =new AlertDialog.Builder(this);
            dialogBuilder.setTitle("");
            dialogBuilder.setMessage("Your Blood Volume is "+bloodVolume+" liters");
            final AlertDialog dialog=dialogBuilder.create();
            dialog.show();
        }
        catch (Exception e)
        {
            if(editTextAge.getText().toString().trim().isEmpty())
            {
                editTextAge.setError("Enter this Field");
                editTextAge.requestFocus();
            }
            if(editTextWeight.getText().toString().trim().isEmpty())
            {
                editTextWeight.setError("Enter this Field");
                editTextWeight.requestFocus();

            }
        }

    }

    public void onClickBodyWater(View view)
    {
        try {
            float weight = parseFloat(editTextWeight.getText().toString().trim());
            double bodyWater;
            bodyWater=weight*0.0434;
            AlertDialog.Builder dialogBuilder =new AlertDialog.Builder(this);
            dialogBuilder.setTitle("");
            dialogBuilder.setMessage("You have to drink  "+bodyWater+" liters/day");
            final AlertDialog dialog=dialogBuilder.create();
            dialog.show();
        }
        catch (Exception e)
        {
            if(editTextWeight.getText().toString().trim().isEmpty())
            {
                editTextWeight.setError("Enter this Field");
                editTextWeight.requestFocus();

            }
        }

    }

    public void onClickUpdatePersonalInfo(View view)
    {
        try {
            float height = parseFloat(editTextHeight.getText().toString().trim());
            float weight = parseFloat(editTextWeight.getText().toString().trim());
            int age = parseInt(editTextAge.getText().toString().trim());
            String id=user.getUid();
            String gender="";
            if(maleRadioButton.isChecked())
                gender="male";
            else
                gender="female";
            PersonalInfoData personalInfoData =new PersonalInfoData(height,weight,gender,age,id);
           databasePersonalDetails.child(id).setValue(personalInfoData);
            Toast.makeText(this,"Personal Details Updated!",Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            if(editTextHeight.getText().toString().trim().isEmpty())
            {
                editTextHeight.setError("Enter this Field");
                editTextHeight.requestFocus();
            }
            if(editTextWeight.getText().toString().trim().isEmpty())
            {
                editTextWeight.setError("Enter this Field");
                editTextWeight.requestFocus();
            }
            if(editTextAge.getText().toString().trim().isEmpty())
            {
                editTextAge.setError("Enter this Field");
                editTextAge.requestFocus();
            }
            return;

        }
    }
    protected void onStart() {
        super.onStart();
        databasePersonalDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot trackSnapShot: dataSnapshot.getChildren())
                {
                    PersonalInfoData profile=trackSnapShot.getValue(PersonalInfoData.class);
                    editTextHeight.setText(profile.getHeight()+"");
                    editTextWeight.setText(profile.getWeight()+"");
                    editTextAge.setText(profile.getAge()+"");
                    if(profile.getGender().equals("male"))
                        maleRadioButton.setChecked(true);
                    else
                        femaleRadioButton.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
