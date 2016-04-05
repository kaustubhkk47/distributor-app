package com.bazara2z.distributorhelper.BuildOrderPackage;

/**
 * Created by Maddy on 06-03-2016.
 */
public class BuildOrderModel {

    public int id;
    public String offerDetails;
    public String productName;
    public double pricePerUnit;
    public double totalPrice;
    public int quantity;
    public int productId;
    public int orderItemId;
    public String unit;

    public BuildOrderModel(){

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

    public double getTotalPrice(){
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice){
        this.totalPrice = totalPrice;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getProductId(){
        return productId;
    }

    public void setProductId(int productId){
        this.productId = productId;
    }

    public int getOrderItemId(){
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId){
        this.orderItemId = orderItemId;
    }

    public String getUnit(){
        return unit;
    }

    public void setUnit(String unit){
        this.unit = unit;
    }

}
