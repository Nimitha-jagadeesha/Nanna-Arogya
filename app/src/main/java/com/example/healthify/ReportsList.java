package com.example.healthify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.storage.UploadTask;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ReportsList extends ArrayAdapter<uploadPDF>
{
    private Activity context;
    private List<uploadPDF> uploadPDFList;
    public ReportsList(@NonNull Activity context, List<uploadPDF> uploadPDFList) {
        super(context, R.layout.upload_file_list,uploadPDFList);
        this.context=context;
        this.uploadPDFList=uploadPDFList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.upload_file_list,null,true);
        TextView reportName=listViewItem.findViewById(R.id.Uploads_textView);
        uploadPDF uploadPDFs= uploadPDFList.get(position);
       reportName.setText(uploadPDFs.getPdfName());
        return listViewItem;
    }

}
