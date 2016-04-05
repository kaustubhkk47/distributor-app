package com.bazara2z.distributorhelper.ProductsDisplayPackage;

/**
 * Created by Maddy on 04-03-2016.
 */
public class ProductsDisplayModel {

    public int id;
    public String offerDetails;
    public String productName;
    public String unit;
    public double pricePerUnit;

    public ProductsDisplayModel(){

    }

    public ProductsDisplayModel(int id, String offerDetails, String productName, double pricePerUnit){
        this.id = id;
        this.offerDetails = offerDetails;
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
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

    public double getPricePerUnit(){
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit){
        this.pricePerUnit = pricePerUnit;
    }

    public String getUnit(){
        return unit;
    }

    public void setUnit(String unit){
        this.unit = unit;
    }
}
