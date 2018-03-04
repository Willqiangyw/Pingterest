package com.example.yunweiqiang.pingterest;

/**
 * Created by yunweiqiang on 3/3/18.
 */

public class UserInfomation {
    private String mName;


    public UserInfomation(){

    }

    public UserInfomation(String c){
        mName = c;
    }

    public String getName(){
        return mName;
    }

    public void setName(String c){
        mName = c;
    }
}
