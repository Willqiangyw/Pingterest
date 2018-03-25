package com.example.yunweiqiang.pingterest;

/**
 * Created by yunweiqiang on 3/25/18.
 */

public class EventInformation {
    private String mHolder;
    private String mTime;
    private String mLocation;
    private String mParticipant;
    private String mKey;

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
}
