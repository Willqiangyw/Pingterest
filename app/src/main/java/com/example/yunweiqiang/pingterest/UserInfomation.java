package com.example.yunweiqiang.pingterest;

/**
 * Created by yunweiqiang on 3/3/18.
 */

public class UserInfomation {
    private String mName;

    private String mGender;
    private String mLevel;
    private String mAge;
    private String mCity;
    private String mKey;



    public UserInfomation(){

    }

    public UserInfomation(String n){
        mName = n;
//        mGender = g;
//        mLevel = l;
//        mAge = a;
//        mCity = c;
    }

    public UserInfomation(String n, String g, String l, String a, String c, String k){
        mName = n;
        mGender = g;
        mLevel = l;
        mAge = a;
        mCity = c;
        mKey = k;
    }

    public String getName(){
        return mName;
    }

    public void setName(String c){
        mName = c;
    }

    public String getGender(){
        return mGender;
    }

    public void setGender(String c){
        mGender = c;
    }

    public String getLevel(){
        return mLevel;
    }
    public void setLevel(String c){
        mLevel = c;
    }

    public String getAge(){
        return mAge;
    }

    public void setAge(String c){
        mAge = c;
    }

    public String getCity(){
        return mCity;
    }

    public void setCity(String c){
        mCity = c;
    }

    public String getKey(){
        return mKey;
    }

    public void setKey(String c){
        mKey = c;
    }
}
