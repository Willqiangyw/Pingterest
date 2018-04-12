package com.example.yunweiqiang.pingterest;

/**
 * Created by yunweiqiang on 3/3/18.
 */

public class UserInfomation {
    private String mName;
    private String mKey;
    private String mGender;
    private String mLevel;
    private String mAge;
    private String mCity;
    private String mState;
    private String mZip;
    private String mDescription;
    private String mLatitude;
    private String mLongitude;
    private String mRating;
    private String mIsCoach;



    public UserInfomation(){

    }

    public UserInfomation(String name, String key, String gender, String level, String age, String city, String state, String zip,
                            String description, String latitude, String longitude, String rating, String isCoach){

        mName = name; mKey = key; mGender = gender;

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

    public String getState(){
        return mState;
    }
    public void setState(String c){
        mState = c;
    }

    public String getKey(){
        return mKey;
    }
    public void setKey(String c){
        mKey = c;
    }

    public String getZip(){
        return mZip;
    }
    public void setZip(String c){
        mZip = c;
    }

    public String getDescription(){
        return mDescription;
    }
    public void setDescription(String c){
        mDescription = c;
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
    public void setLongitude(String c){
        mLongitude = c;
    }

    public String getRating(){
        return mRating;
    }
    public void setRating(String c){
        mRating = c;
    }

    public String getIsCoach(){
        return mIsCoach;
    }
    public void setIsCoach(String c){
        mIsCoach = c;
    }
}
