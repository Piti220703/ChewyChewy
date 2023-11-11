package com.example.chewychewy.Models;

public class DiaChi {
    private String id;
    private String userId;
    private String HoTen;
    private String SDT;
    private String DiaChi;

    public DiaChi(String userId,String hoTen, String SDT, String diaChi) {
        this.userId = userId;
        HoTen = hoTen;
        this.SDT = SDT;
        DiaChi = diaChi;
    }

    public DiaChi(String id,String userId,String hoTen, String SDT, String diaChi) {
        this.id = id;
        this.userId = userId;
        HoTen = hoTen;
        this.SDT = SDT;
        DiaChi = diaChi;
    }
    public DiaChi() {
             this.userId = "";
        HoTen = "";
        this.SDT = "";
        DiaChi = "";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }
}
