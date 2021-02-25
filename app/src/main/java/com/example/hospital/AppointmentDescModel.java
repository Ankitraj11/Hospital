package com.example.hospital;

public class AppointmentDescModel {


    String appoinmentTime;
    String appoinmentDate;
    String appoinmentDesc;
    String appoinmentid;
    String senderid;
    String receiverid;
    String appointmentStatus;

    public AppointmentDescModel() {
    }

    public AppointmentDescModel(String appointmentStatus,String appoinmentTime, String appoinmentDate, String appoinmentDesc, String appoinmentid, String senderid, String receiverid) {
        this.appoinmentTime = appoinmentTime;
        this.appointmentStatus=appointmentStatus;
        this.appoinmentDate = appoinmentDate;
        this.appoinmentDesc = appoinmentDesc;
        this.appoinmentid = appoinmentid;
        this.senderid = senderid;
        this.receiverid = receiverid;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getAppoinmentTime() {
        return appoinmentTime;
    }

    public String getAppoinmentDate() {
        return appoinmentDate;
    }

    public String getAppoinmentDesc() {
        return appoinmentDesc;
    }

    public String getAppoinmentid() {
        return appoinmentid;
    }

    public String getSenderid() {
        return senderid;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setAppoinmentTime(String appoinmentTime) {
        this.appoinmentTime = appoinmentTime;
    }

    public void setAppoinmentDate(String appoinmentDate) {
        this.appoinmentDate = appoinmentDate;
    }

    public void setAppoinmentDesc(String appoinmentDesc) {
        this.appoinmentDesc = appoinmentDesc;
    }

    public void setAppoinmentid(String appoinmentid) {
        this.appoinmentid = appoinmentid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }
}
