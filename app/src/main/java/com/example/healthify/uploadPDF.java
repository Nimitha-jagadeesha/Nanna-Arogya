package com.example.healthify;

public class uploadPDF
{
    public String PdfName;
    public String url;

    public uploadPDF() {
    }

    public uploadPDF(String pdfName, String url)
    {
        PdfName = pdfName;
        this.url = url;
    }

    public String getPdfName() {
        return PdfName;
    }

    public String getUrl() {
        return url;
    }
}
