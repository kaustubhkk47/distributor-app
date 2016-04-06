package com.bazara2z.distributorhelper.OrderSummaryPackage;

import android.util.Log;

public class OrderSummaryModel {
    public int orderId;
    public int productCount;
    public double totalPrice;
    public double modifiedPrice;
    public int confirmedStatus;
    public int retailerId;
    public String retailerName;
    public int isNewOrder;
    public int isOrderSynced;

    public int getOrderId(){
        return orderId;
    }

    public void setOrderId(int orderId){
        this.orderId = orderId;
    }

    public int getProductCount(){
        return productCount;
    }

    public void setProductCount(int productCount){
        this.productCount = productCount;
    }

    public double getTotalPrice(){
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice){
        this.totalPrice = totalPrice;
    }

    public double getModifiedPrice(){
        return modifiedPrice;
    }

    public void setModifiedPrice(double modifiedPrice){
        this.modifiedPrice = modifiedPrice;
    }

    public int getConfirmedStatus(){
        return confirmedStatus;
    }

    public void setConfirmedStatus(int confirmedStatus){
        this.confirmedStatus = confirmedStatus;
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

    public int getIsNewOrder(){
        return this.isNewOrder;
    }

    public void setIsNewOrder(int isNewOrder){
        this.isNewOrder = isNewOrder;
    }

    public int getIsOrderSynced(){
        return this.isOrderSynced;
    }

    public void setIsOrderSynced(int isOrderSynced){
        this.isOrderSynced = isOrderSynced;
    }
}
