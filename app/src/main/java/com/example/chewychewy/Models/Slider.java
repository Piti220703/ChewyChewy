package com.example.chewychewy.Models;

import com.denzcoskun.imageslider.models.SlideModel;

public class Slider {
    private  String Id;
    private SlideModel imageSlider;

    public Slider(String Id, SlideModel imageSlider) {

        this.imageSlider = imageSlider;
        this.Id = Id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Slider() {
        this.imageSlider = null;
    }

    public SlideModel getImageSlider() {
        return imageSlider;
    }

    public void setImageSlider(SlideModel imageSlider) {
        this.imageSlider = imageSlider;
    }
}
