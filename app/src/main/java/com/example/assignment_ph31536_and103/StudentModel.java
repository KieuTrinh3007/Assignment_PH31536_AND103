package com.example.assignment_ph31536_and103;

public class StudentModel {
    
    private String _id;

    private String tenSV;

    private String maSV;

    private double diemTB;

    private String avatar;

    public StudentModel() {
    }

    public StudentModel(String _id, String tenSV, String maSV, double diemTB, String avatar) {
        this._id = _id;
        this.tenSV = tenSV;
        this.maSV = maSV;
        this.diemTB = diemTB;
        this.avatar = avatar;
    }

    public StudentModel(String tenSV, String maSV, double diemTB, String avatar) {
        this.tenSV = tenSV;
        this.maSV = maSV;
        this.diemTB = diemTB;
        this.avatar = avatar;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTenSV() {
        return tenSV;
    }

    public void setTenSV(String tenSV) {
        this.tenSV = tenSV;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public double getDiemTB() {
        return diemTB;
    }

    public void setDiemTB(double diemTB) {
        this.diemTB = diemTB;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Data [NAME=" + maSV + ", CLASS=" + tenSV + ", THIRTY=" + diemTB
                + ", NINETY=" + avatar + "]";
    }
}
