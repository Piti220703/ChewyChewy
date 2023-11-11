package com.example.chewychewy.Models;

import java.io.Serializable;

public class Banh implements Serializable {
    private String id;
    private String image_Banh;
    private String ten;
    private float gia;
    private String mota;
    private Float HangDanhGia;
    private int SoLuongDanhGia;
    private String danhmucId;

    public Banh(String id, String ten,String danhmucId,String image_Banh ,float gia, String mota) {
        this.id = id;
        this.ten = ten;
        this.gia = gia;
        this.mota = mota;
        this.danhmucId = danhmucId;
        this.image_Banh = image_Banh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_Banh() {
        return image_Banh;
    }

    public void setImage_Banh(String image_Banh) {
        this.image_Banh = image_Banh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }


    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public Float getHangDanhGia() {
        return HangDanhGia;
    }

    public void setHangDanhGia(Float hangDanhGia) {
        HangDanhGia = hangDanhGia;
    }

    public int getSoLuongDanhGia() {
        return SoLuongDanhGia;
    }

    public void setSoLuongDanhGia(int soLuongDanhGia) {
        SoLuongDanhGia = soLuongDanhGia;
    }

    public String getDanhmucId() {
        return danhmucId;
    }

    public void setDanhmucId(String danhmucId) {
        this.danhmucId = danhmucId;
    }
}
