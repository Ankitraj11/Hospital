package com.example.hospital;

public class MedicineModel {

    String medicine1;
    String medicine2;
    String medicine3;
    String medicine4;
    String dieases1;
    String medicineStatus;
    String dieases2;
    String dieases3;
    String dieases4;
    String medicineid;
    String medicineSenderid;
    String medicineReceiverid;

    public MedicineModel() {
    }

    public MedicineModel( String medicineid,String medicineStatus, String medicineSenderid, String medicineReceiverid,String medicine1, String medicine2, String medicine3, String medicine4, String dieases1, String dieases2, String dieases3, String dieases4) {
        this.medicine1 = medicine1;
        this.medicine2 = medicine2;
        this.medicine3 = medicine3;
        this.medicine4 = medicine4;
        this.medicineStatus=medicineStatus;
        this.dieases1 = dieases1;
        this.medicineid=medicineid;
        this.medicineSenderid=medicineSenderid;
        this.medicineReceiverid=medicineReceiverid;
        this.dieases2 = dieases2;
        this.dieases3 = dieases3;
        this.dieases4 = dieases4;
    }

    public MedicineModel(String medicineStatus,String medicine1, String dieases1, String medicineid, String medicineSenderid, String medicineReceiverid) {
        this.medicine1 = medicine1;
        this.medicineStatus=medicineStatus;
        this.dieases1 = dieases1;
        this.medicineid = medicineid;
        this.medicineSenderid = medicineSenderid;
        this.medicineReceiverid = medicineReceiverid;
    }

    public String getMedicineStatus() {
        return medicineStatus;
    }

    public void setMedicineStatus(String medicineStatus) {
        this.medicineStatus = medicineStatus;
    }

    public String getMedicineid() {
        return medicineid;
    }

    public void setMedicineid(String medicineid) {
        this.medicineid = medicineid;
    }

    public String getMedicineSenderid() {
        return medicineSenderid;
    }

    public void setMedicineSenderid(String medicineSenderid) {
        this.medicineSenderid = medicineSenderid;
    }

    public String getMedicineReceiverid() {
        return medicineReceiverid;
    }

    public void setMedicineReceiverid(String medicineReceiverid) {
        this.medicineReceiverid = medicineReceiverid;
    }

    public String getMedicine1() {
        return medicine1;
    }

    public void setMedicine1(String medicine1) {
        this.medicine1 = medicine1;
    }

    public String getMedicine2() {
        return medicine2;
    }

    public void setMedicine2(String medicine2) {
        this.medicine2 = medicine2;
    }

    public String getMedicine3() {
        return medicine3;
    }

    public void setMedicine3(String medicine3) {
        this.medicine3 = medicine3;
    }

    public String getMedicine4() {
        return medicine4;
    }

    public void setMedicine4(String medicine4) {
        this.medicine4 = medicine4;
    }

    public String getDieases1() {
        return dieases1;
    }

    public void setDieases1(String dieases1) {
        this.dieases1 = dieases1;
    }

    public String getDieases2() {
        return dieases2;
    }

    public void setDieases2(String dieases2) {
        this.dieases2 = dieases2;
    }

    public String getDieases3() {
        return dieases3;
    }

    public void setDieases3(String dieases3) {
        this.dieases3 = dieases3;
    }

    public String getDieases4() {
        return dieases4;
    }

    public void setDieases4(String dieases4) {
        this.dieases4 = dieases4;
    }
}

