package com.bazara2z.distributorhelper.ProductsDisplayPackage;

import com.bazara2z.distributorhelper.OffersPackage.ProductOffersModel;

import java.util.ArrayList;

/**
 * Created by Maddy on 04-03-2016.
 */
public class ProductsDisplayModel {

    public int id;
    public int productId;
    public String offerDetails;
    public String productName;
    public String unit;
    public double pricePerUnit;
    public ArrayList<ProductOffersModel> productOffersModels;

    public ProductsDisplayModel(){

    }


    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getProductId(){
        return productId;
    }

    public void setProductId(int productId){
        this.productId = productId;
    }

    public ArrayList<ProductOffersModel> getProductOffers(){
        return productOffersModels;
    }

    public void setProductOffers(ArrayList<ProductOffersModel> productOffersModels){
        this.productOffersModels = productOffersModels;
    }

    public int getProductOffersCount(){
        return this.productOffersModels.size();
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
