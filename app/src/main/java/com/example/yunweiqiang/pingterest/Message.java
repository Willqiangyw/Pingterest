package com.example.yunweiqiang.pingterest;

/**
 * Created by yunweiqiang on 3/1/18.
 */

public class Message {
    private String mContent;
    private String mUser;
    private String mTime;

    public Message(){

    }

    public Message(String c, String u, String t){
        mContent = c;
        mUser = u;
        mTime = t;
    }

    public String getContent(){
        return mContent;
    }

    public void setContent(String c){
        mContent = c;
    }

    public String getUser(){
        return mUser;
    }

    public void setUser(String u){
        mUser = u;
    }

    public String getTime(){
        return mTime;
    }

    public void setTime(String t){
        mTime = t;
    }
}
