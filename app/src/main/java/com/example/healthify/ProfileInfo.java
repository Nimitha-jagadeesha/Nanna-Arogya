package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import static java.lang.Integer.parseInt;

public class ProfileInfo extends AppCompatActivity {

    EditText editTextHeight;
    EditText editTextWeight;
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
    }
    public void onClickSubmit()
    {
        float bodyMassIndex;
        int height = parseInt(editTextHeight.getText().toString());
        int weight=parseInt(editTextWeight.getText().toString());
        bodyMassIndex=((float)height)/weight;
        String result="";
        if(bodyMassIndex<0.80)
        {
            result+="";
        }
        else if(bodyMassIndex>=0.80&&bodyMassIndex<=1.8)
        {

        }
        else
        {

        }
    }
}
