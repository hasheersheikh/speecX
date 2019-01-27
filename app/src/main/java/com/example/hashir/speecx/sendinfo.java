package com.example.hashir.speecx;

import android.os.Parcel;
import android.os.Parcelable;

public class sendinfo implements Parcelable {
    String name;
    int number;
    public sendinfo(String name,int number){
        this.name=name;
        this.number=number;
    }

    protected sendinfo(Parcel in) {
        name = in.readString();
        number = in.readInt();
    }

    public static final Creator<sendinfo> CREATOR = new Creator<sendinfo>() {
        @Override
        public sendinfo createFromParcel(Parcel in) {
            return new sendinfo(in);
        }

        @Override
        public sendinfo[] newArray(int size) {
            return new sendinfo[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(number);
    }
}
