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

    public void setAddressLine1(String addressLine1){
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2(){
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2){
        this.addressLine2 = addressLine2;
    }
}
