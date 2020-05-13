package com.example.healthify;

public class uploadPDF
{
    public  String id;
    public String pdfName;
    public String url;

    public uploadPDF() {
    }

    public uploadPDF(String id, String pdfName, String url) {
        this.id = id;
        this.pdfName = pdfName;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getPdfName() {
        return pdfName;
    }

    public String getUrl() {
        return url;
    }
}
