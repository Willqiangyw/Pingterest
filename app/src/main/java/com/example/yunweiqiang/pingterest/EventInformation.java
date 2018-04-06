package com.example.yunweiqiang.pingterest;

/**
 * Created by yunweiqiang on 3/25/18.
 */

public class EventInformation {
    private String mHolder;
    private String mTime;
    private String mTime2;
    private String mLocation;
    private String mParticipant;
    private String mKey;
    private String mLatitude;
    private String mLongitude;
    private String mDescription;

    public String getHolder(){
        return mHolder;
    }
    public void setHolder(String c){
        mHolder = c;
    }

    public String getTime(){
        return mTime;
    }
    public void setTime(String c){
        mTime = c;
    }

    public String getTime2(){
        return mTime2;
    }
    public void setTime2(String c){
        mTime2 = c;
    }

    public String getLocation(){
        return mLocation;
    }
    public void setLocation(String c){
        mLocation = c;
    }

    public String getParticipant(){
        return mParticipant;
    }
    public void setParticipant(String c){
        mParticipant = c;
    }

    public String getKey(){
        return mKey;
    }
    public void setKey(String c){
        mKey = c;
    }

    public String getLatitude(){
        return mLatitude;
    }
    public void setLatitude(String c){
        mLatitude = c;
    }

    public String getLongitude(){
        return mLongitude;
    }
    public void setLongitude(String c){mLongitude = c;}

    public String getDescription(){
        return mDescription;
    }
    public void setDescription(String c){
        mDescription = c;
    }
}
