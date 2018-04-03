package com.example.yunweiqiang.pingterest;


public class EquipmentInformation {
    private String mBrand;
    private String mItemDescription;
    private String mItemName;
    private String mItemPrice;
    private String mItemType;
    private String mKey;
    private String mSellerInfo;
    private String mLocation;



    public String getBrand(){
        return mBrand;
    }
    public String getDescription(){return mItemDescription;}
    public String getItemName(){return mItemName;}
    public String getItemPrice(){return mItemPrice;}
    public String getItemType(){return mItemType;}
    public String getKey(){return mKey;}
    public String getSellerInfo(){return mSellerInfo;}
    public String getLocation(){return mLocation;}


    public void setBrand(String c){ mBrand = c; }
    public void setItemDescription(String c){mItemDescription = c;}
    public void setItemName(String c){mItemName = c;}
    public void setItemPrice(String c){mItemPrice = c;}
    public void setItemType(String c){mItemType = c;}
    public void setKey(String c){mKey = c;}
    public void setSellerInfo(String c){mSellerInfo = c;}
    public void setLocation(String c){mLocation = c;}

}
