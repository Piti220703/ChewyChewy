package com.example.chewychewy.Models;

public class User {
    private String Id;
    private String password;
    private String Imagea;
    private String HoTen;
    private String Email;
    private String SDT;
    private String NgaySinh;

    public User(String Id, String imagea, String hoTen, String email, String SDT, String ngaySinh) {
        this.Id = Id;
        this.Imagea = imagea;
        this.HoTen = hoTen;
        this.Email = email;
        this.SDT = SDT;
        this.NgaySinh = ngaySinh;
    }
    public User(String Id, String imagea, String hoTen) {
        this.Id = Id;
        this.Imagea = imagea;
        this.HoTen = hoTen;

    }
    public User() {
        this.Id = "";
        this.Imagea = "";
        this.HoTen = "";
        this.Email = "";
        this.SDT = "";
        this.NgaySinh = "";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImagea() {
        return Imagea;
    }

    public void setImagea(String imagea) {
        Imagea = imagea;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        NgaySinh = ngaySinh;
    }
}
