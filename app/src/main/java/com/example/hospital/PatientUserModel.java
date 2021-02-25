package com.example.hospital;

public class PatientUserModel {
    String name;
    String profession;
    String image;
    String userid;

    public PatientUserModel() {
    }

    public PatientUserModel(String name, String profession, String image,String userid) {
        this.name = name;
        this.profession = profession;
        this.image = image;
        this.userid=userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

