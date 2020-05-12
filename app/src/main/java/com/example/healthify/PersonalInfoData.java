package com.example.healthify;

public class PersonalInfoData
{
    float height;
    float weight;
    String gender;
    int age;
    String userId;

    public PersonalInfoData() {
    }

    public PersonalInfoData(float height, float weight, String gender, int age, String userId) {
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.age = age;
        this.userId = userId;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getUserId() {
        return userId;
    }
}
