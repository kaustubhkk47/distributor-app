package com.bazara2z.distributorhelper.OffersPackage;

/**
 * Created by Maddy on 04-03-2016.
 */
public class OffersModel {
    public int id;
    public String offerDetails;
    public String productName;

    public OffersModel(){

    }

    public OffersModel(int id, String offerDetails, String productName){
        this.id = id;
        this.offerDetails = offerDetails;
        this.productName = productName;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getOfferDetails(){
        return offerDetails;
    }

    public void setOfferDetails(String offerDetails){
        this.offerDetails = offerDetails;
    }

    public String getProductName(){
        return productName;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }
}
