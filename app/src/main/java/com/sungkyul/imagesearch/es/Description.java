package com.sungkyul.imagesearch.es;

public class Description {
    public String title;
    private String back_des;
    private String back_address;
    private String back_open;
    private String back_img;
    private String back_tel;
    private String back_review;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBack_des() {
        return back_des;
    }

    public void setBack_des(String back_des) {
        this.back_des = back_des;
    }

    public String getBack_address() {
        return back_address;
    }

    public void setBack_address(String back_address) {
        this.back_address = back_address;
    }

    public String getBack_open() {
        return back_open;
    }

    public void setBack_open(String back_open) {
        this.back_open = back_open;
    }

    public String getBack_img() {
        return back_img;
    }

    public void setBack_img(String back_img) {
        this.back_img = back_img;
    }

    public String getBack_tel() {
        return back_tel;
    }

    public void setBack_tel(String back_tel) {
        this.back_tel = back_tel;
    }

    public String getBack_review() {
        return back_review;
    }

    public void setBack_review(String back_review) {
        this.back_review = back_review;
    }

    @Override
    public String toString() {
        return "Description{" +
                "title='" + title + '\'' +
                ", back_des='" + back_des + '\'' +
                ", back_address='" + back_address + '\'' +
                ", back_open='" + back_open + '\'' +
                ", back_img='" + back_img + '\'' +
                ", back_tel='" + back_tel + '\'' +
                ", back_review='" + back_review + '\'' +
                '}';
    }
}
