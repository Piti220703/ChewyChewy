package com.example.chewychewy.Models;


import com.google.type.DateTime;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class GioHang implements Serializable {

    private String id;
    private String banh_id;
    private String customer_id;
    private int soLuong;
    private float tongGia;
    private boolean thanhToan;
    private String timestamp;


    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public float getTongGia() {
        return tongGia;
    }

    public boolean isThanhToan() {
        return thanhToan;
    }

    public void setThanhToan(boolean thanhToan) {
        this.thanhToan = thanhToan;
    }

    public void setTongGia(float tongGia) {
        this.tongGia = tongGia;
    }

    public GioHang(){}

    public GioHang(String id,String banh_id,int soLuong , boolean thanhToan , String timestamp , float tongGia){
        this.id = id;
        this.banh_id = banh_id;
        this.soLuong = soLuong;
        this.thanhToan = thanhToan;
        this.timestamp = timestamp;
        this.tongGia = tongGia;
    }

    public GioHang(Banh banh, int soLuong , String customer_id) {
        this.banh_id = banh.getId();
        this.customer_id = customer_id;
        this.soLuong = soLuong;
        this.tongGia = banh.getGia() * soLuong;
        this.thanhToan = false;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.timestamp = formatter.format(new Date());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanh_id() {
        return banh_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getTimestamp() {
        return timestamp;
    }
}


