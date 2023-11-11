package com.example.chewychewy.Models;

public class DanhMuc {
    private String Id;
    private String TenDanhMuc;
    private String ImageDanhMuc;

    public DanhMuc(String id, String tenDanhMuc, String imageDanhMuc) {
        Id = id;
        TenDanhMuc = tenDanhMuc;
        ImageDanhMuc = imageDanhMuc;
    }
    public DanhMuc() {
        Id = "";
        TenDanhMuc = "";
        ImageDanhMuc = "";
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTenDanhMuc() {
        return TenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        TenDanhMuc = tenDanhMuc;
    }

    public String getImageDanhMuc() {
        return ImageDanhMuc;
    }

    public void setImageDanhMuc(String imageDanhMuc) {
        ImageDanhMuc = imageDanhMuc;
    }
}
