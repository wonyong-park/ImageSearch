package com.sungkyul.imagesearch.es;

import android.os.Parcel;
import android.os.Parcelable;

//관광지 정보 객체
public class Tourist implements Parcelable {
    public String title;//
    private String tourist_key;//관광지 이름
    private String tourist_address;//관광지 주소
    private String tourist_open;//관광지 오픈시간
    private String tourist_img;//관광지 이미지
    private String tourist_latitude;//위도
    private String tourist_longitude;//경도

    protected Tourist(Parcel in) {
        title = in.readString();
        tourist_key = in.readString();
        tourist_address = in.readString();
        tourist_open = in.readString();
        tourist_img = in.readString();
        tourist_latitude = in.readString();
        tourist_longitude = in.readString();
    }

    public static final Creator<Tourist> CREATOR = new Creator<Tourist>() {
        @Override
        public Tourist createFromParcel(Parcel in) {
            return new Tourist(in);
        }

        @Override
        public Tourist[] newArray(int size) {
            return new Tourist[size];
        }
    };

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

    public String getTourist_latitude() {
        return tourist_latitude;
    }

    public void setTourist_latitude(String tourist_latitude) {
        this.tourist_latitude = tourist_latitude;
    }

    public String getTourist_longitude() {
        return tourist_longitude;
    }

    public void setTourist_longitude(String tourist_longitude) {
        this.tourist_longitude = tourist_longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(tourist_key);
        dest.writeString(tourist_address);
        dest.writeString(tourist_open);
        dest.writeString(tourist_img);
        dest.writeString(tourist_latitude);
        dest.writeString(tourist_longitude);
    }

    @Override
    public String toString() {
        return "Tourist{" +
                "title='" + title + '\'' +
                ", tourist_key='" + tourist_key + '\'' +
                ", tourist_address='" + tourist_address + '\'' +
                ", tourist_open='" + tourist_open + '\'' +
                ", tourist_img='" + tourist_img + '\'' +
                ", tourist_latitude='" + tourist_latitude + '\'' +
                ", tourist_longitude='" + tourist_longitude + '\'' +
                '}';
    }
}
