package com.example.healthify;

public class PhoneNumbers
{
    String phoneNumberId;
    String phoneNumber;
    String phoneNumberName;
    public PhoneNumbers() {
    }

    public PhoneNumbers(String phoneNumberId, String phoneNumber, String phoneNumberName) {
        this.phoneNumberId = phoneNumberId;
        this.phoneNumber = phoneNumber;
        this.phoneNumberName = phoneNumberName;
    }

    public String getPhoneNumberId() {
        return phoneNumberId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoneNumberName() {
        return phoneNumberName;
    }
}
