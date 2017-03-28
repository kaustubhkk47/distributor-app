package com.bazara2z.distributorhelper.SelectRetailerPackage;

/**
 * Created by Maddy on 05-03-2016.
 */
public class SelectRetailerModel {

    public int id;
    public int retailerId;
    public String retailerName;
    public String phoneNumber;
    public String addressLine1;
    public String addressLine2;
    public String landmark;

    public SelectRetailerModel(){

    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getRetailerId(){
        return retailerId;
    }

    public void setRetailerId(int retailerId){
        this.retailerId = retailerId;
    }

    public String getRetailerName(){
        return retailerName;
    }

    public void setRetailerName(String retailerName){
        this.retailerName = retailerName;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getAddressLine1(){
        return addressLine1;
    }

    public void setAddressLine1(String address){
        this.addressLine1 = address;
    }

    public String getAddressLine2(){
        return addressLine2;
    }

    public void setAddressLine2(String address){
        this.addressLine2 = address;
    }

    public String getLandmark(){
        return landmark;
    }

    public void setLandmark(String landmark){
        this.landmark = landmark;
    }

}
