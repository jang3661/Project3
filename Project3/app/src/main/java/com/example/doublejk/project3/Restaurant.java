package com.example.doublejk.project3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by doublejk on 2017-07-20.
 */

public class Restaurant implements Parcelable{
    private String title;
    private String address;
    private String number;
    private String content;

    public Restaurant(String title, String address, String number, String content) {
        this.title = title;
        this.address = address;
        this.number = number;
        this.content = content;
    }

    protected Restaurant(Parcel in) {
        title = in.readString();
        address = in.readString();
        number = in.readString();
        content = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(address);
        dest.writeString(number);
        dest.writeString(content);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
