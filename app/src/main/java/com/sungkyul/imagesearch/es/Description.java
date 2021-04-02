package com.sungkyul.imagesearch.es;

import android.os.Parcel;
import android.os.Parcelable;

public class Description implements Parcelable {
    public String title;
    private String back_des;
    private String back_address;
    private String back_open;
    private String back_img;
    private String back_tel;

    protected Description(Parcel in) {
        title = in.readString();
        back_des = in.readString();
        back_address = in.readString();
        back_open = in.readString();
        back_img = in.readString();
        back_tel = in.readString();
    }

    public static final Creator<Description> CREATOR = new Creator<Description>() {
        @Override
        public Description createFromParcel(Parcel in) {
            return new Description(in);
        }

        @Override
        public Description[] newArray(int size) {
            return new Description[size];
        }
    };

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


    @Override
    public String toString() {
        return "Description{" +
                "title='" + title + '\'' +
                ", back_des='" + back_des + '\'' +
                ", back_address='" + back_address + '\'' +
                ", back_open='" + back_open + '\'' +
                ", back_img='" + back_img + '\'' +
                ", back_tel='" + back_tel + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(back_des);
        dest.writeString(back_address);
        dest.writeString(back_open);
        dest.writeString(back_img);
        dest.writeString(back_tel);
    }
}
