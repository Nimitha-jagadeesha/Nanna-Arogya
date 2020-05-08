package com.example.healthify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
    public void onClickSubmit(View view)
    {
        float bodyMassIndex;
        int height = parseInt(editTextHeight.getText().toString());
        int weight=parseInt(editTextWeight.getText().toString());
        bodyMassIndex=((float)height)/weight;
        int resInt;
        String result="";
        if(bodyMassIndex<0.80)
        {
            resInt =-1;
        }
        else if(bodyMassIndex>=0.80&&bodyMassIndex<=1.8)
        {
            resInt =0;
        }
        else
        {
            resInt =1;
        }
        showDialog(result,resInt);
    }
    private void showDialog(String message,int resInt)
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
}
