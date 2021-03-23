package com.sungkyul.imagesearch.es;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    public String title;
    private String food_key;
    private String food_address;
    private String food_open;
    private String food_img;
    private String food_tel;
    private String food_menu;
    private String food_review;

    protected Food(Parcel in) {
        title = in.readString();
        food_key = in.readString();
        food_address = in.readString();
        food_open = in.readString();
        food_img = in.readString();
        food_tel = in.readString();
        food_menu = in.readString();
        food_review = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFood_key() {
        return food_key;
    }

    public void setFood_key(String food_key) {
        this.food_key = food_key;
    }

    public String getFood_address() {
        return food_address;
    }

    public void setFood_address(String food_address) {
        this.food_address = food_address;
    }

    public String getFood_open() {
        return food_open;
    }

    public void setFood_open(String food_open) {
        this.food_open = food_open;
    }

    public String getFood_img() {
        return food_img;
    }

    public void setFood_img(String food_img) {
        this.food_img = food_img;
    }

    public String getFood_tel() {
        return food_tel;
    }

    public void setFood_tel(String food_tel) {
        this.food_tel = food_tel;
    }

    public String getFood_menu() {
        return food_menu;
    }

    public void setFood_menu(String food_menu) {
        this.food_menu = food_menu;
    }

    public String getFood_review() {
        return food_review;
    }

    public void setFood_review(String food_review) {
        this.food_review = food_review;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(food_key);
        dest.writeString(food_address);
        dest.writeString(food_open);
        dest.writeString(food_img);
        dest.writeString(food_tel);
        dest.writeString(food_menu);
        dest.writeString(food_review);
    }

    @Override
    public String toString() {
        return "Food{" +
                "title='" + title + '\'' +
                ", food_key='" + food_key + '\'' +
                ", food_address='" + food_address + '\'' +
                ", food_open='" + food_open + '\'' +
                ", food_img='" + food_img + '\'' +
                ", food_tel='" + food_tel + '\'' +
                ", food_menu='" + food_menu + '\'' +
                ", food_review='" + food_review + '\'' +
                '}';
    }
}
