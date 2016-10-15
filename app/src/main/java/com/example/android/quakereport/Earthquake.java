package com.example.android.quakereport;

/**
 * Created by Shikhar on 15-10-2016.
 */
public class Earthquake {

    private String mMagnitude, mLocation, mDate;

    public Earthquake(String magnitude, String location, String date){
        mMagnitude = magnitude;
        mLocation  = location;
        mDate      = date;
    }

    public String getMagnitude(){
        return mMagnitude;
    }

    public String getLocation(){
        return mLocation;
    }

    public String getDate(){
        return mDate;
    }
}
