package com.example.chewychewy.Models;

import java.io.Serializable;
import java.util.List;


public class Bill implements Serializable{
    private String Billid, timestamp, day, time, Address;
    private List<GioHang> items;
    private int totalPrice =0;
    private String id;

    public Bill() {
        this.Billid = "";
        this.day = "";
        this.time = "";
        Address = "223";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bill(String customerId, String timestamp, String day, String time, String address, List<GioHang> items) {
        this.Billid = customerId;
        this.timestamp = timestamp;
        this.day = day;
        this.time = time;
        Address = address;
        this.items = items;
        total(items);// unsure
    }



    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.Billid = timestamp;
    }

    public String getCustomerId() {
        return Billid;
    }

    public void setCustomerId(String customerId) {
        this.Billid = customerId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public List<GioHang> getItems() {
        return items;
    }

    public void setItems(List<GioHang> items) {
        this.items = items;
    }

    private void total(List<GioHang> items1){
        for(GioHang i : items1){
            this.totalPrice += i.getTongGia();
        }
    }

}
