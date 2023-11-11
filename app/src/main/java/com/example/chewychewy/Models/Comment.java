package com.example.chewychewy.Models;

import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
    private String id_user;
    private String id_banh;
    private String content;
    private String time;
    private Float rating;


    public Comment(String id, String id_banh,String comment, Float rating_user) {
        this.id_user = id;
        this.id_banh = id_banh;
        this.content = comment;
        this.rating = rating_user;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.time = formatter.format(new Date());
    }

    public Comment(String id, String id_banh,String comment, Float rating_user,String time) {
        this.id_user = id;
        this.id_banh = id_banh;
        this.content = comment;
        this.rating = rating_user;
        this.time = time;
    }


    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getId_banh() {
        return id_banh;
    }

    public void setId_banh(String id_banh) {
        this.id_banh = id_banh;
    }
}
