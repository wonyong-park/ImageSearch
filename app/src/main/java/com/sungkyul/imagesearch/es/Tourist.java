package com.sungkyul.imagesearch.es;

public class Tourist {
    public String title;
    private String tourist_key;
    private String tourist_address;
    private String tourist_open;
    private String tourist_img;
    private String tourist_tel;
    private String tourist_des;
    private String tourist_review;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTourist_key() {
        return tourist_key;
    }

    public void setTourist_key(String tourist_key) {
        this.tourist_key = tourist_key;
    }

    public String getTourist_address() {
        return tourist_address;
    }

    public void setTourist_address(String tourist_address) {
        this.tourist_address = tourist_address;
    }

    public String getTourist_open() {
        return tourist_open;
    }

    public void setTourist_open(String tourist_open) {
        this.tourist_open = tourist_open;
    }

    public String getTourist_img() {
        return tourist_img;
    }

    public void setTourist_img(String tourist_img) {
        this.tourist_img = tourist_img;
    }

    public String getTourist_tel() {
        return tourist_tel;
    }

    public void setTourist_tel(String tourist_tel) {
        this.tourist_tel = tourist_tel;
    }

    public String getTourist_des() {
        return tourist_des;
    }

    public void setTourist_des(String tourist_des) {
        this.tourist_des = tourist_des;
    }

    public String getTourist_review() {
        return tourist_review;
    }

    public void setTourist_review(String tourist_review) {
        this.tourist_review = tourist_review;
    }

    @Override
    public String toString() {
        return "Tourist{" +
                "title='" + title + '\'' +
                ", tourist_key='" + tourist_key + '\'' +
                ", tourist_address='" + tourist_address + '\'' +
                ", tourist_open='" + tourist_open + '\'' +
                ", tourist_img='" + tourist_img + '\'' +
                ", tourist_tel='" + tourist_tel + '\'' +
                ", tourist_des='" + tourist_des + '\'' +
                ", tourist_review='" + tourist_review + '\'' +
                '}';
    }
}
