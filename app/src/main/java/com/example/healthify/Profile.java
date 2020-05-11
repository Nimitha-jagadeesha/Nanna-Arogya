package com.example.healthify;

public class Profile
{
    String profileId;
    String firstname;
    String lastname;
    String phoneNumber;

    public Profile() {
    }

    public Profile(String profileId, String firstName, String lastName, String phoneNumber) {
        this.profileId = profileId;
        firstname = firstName;
        lastname = lastName;
        this.phoneNumber = phoneNumber;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
