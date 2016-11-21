package com.example.android.quakereport;

public class Earthquake {

    private String mMagnitude, mDate, mTime, mOffset, mPriLoc;//, mUrl;

    public Earthquake(String magnitude, String Offset, String PriLoc, String date, String time){//}, String url){
        mMagnitude = magnitude;
        mDate      = date;
        mTime      = time;
        mOffset    = Offset;
        mPriLoc    = PriLoc;
       // mUrl       = url;
    }

    public String getMagnitude(){
        return mMagnitude;
    }

    public String getDate(){
        return mDate;
    }

    public String getTime(){
        return mTime;
    }

    public String getOffset() { return mOffset; }

    public String getPriLoc() { return  mPriLoc;}

   // public String getUrl() { return mUrl; }
}
