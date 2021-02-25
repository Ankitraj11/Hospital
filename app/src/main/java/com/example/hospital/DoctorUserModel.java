package com.example.hospital;

public class DoctorUserModel {
    String userid;
    String image;
    String name;
    String profession;
    String speciality;

    public DoctorUserModel() {
    }

    public DoctorUserModel(String userid,String image, String name, String profession, String speciality) {
        this.image = image;
        this.name = name;
        this.profession = profession;
        this.speciality = speciality;
        this.userid=userid;
    }

    public DoctorUserModel(String userid, String image, String name) {
        this.userid = userid;
        this.image = image;
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
