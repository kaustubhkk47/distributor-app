package com.bazara2z.distributorhelper.OffersPackage;

import android.util.Log;

import com.bazara2z.distributorhelper.Data.DistributorContract;

/**
 * Created by Maddy on 10-04-2016.
 */
public class ProductOffersModel {

    private String LOG_TAG = "ProductOffersModel";

    public int id;
    public String offerDetails;
    public String productName;

    public double discountPercent;
    public int minimumOrderQuantity;
    public int offerApplied = 0;
    public int offerId;
    public int offerType;
    public int productId;
    public int xCount;
    public int yCount;
    public String yName;

    public ProductOffersModel(){

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

    public double getDiscountPercent(){
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent){
        this.discountPercent = discountPercent;
    }

    public int getMinimumOrderQuantity(){
        return minimumOrderQuantity;
    }

    public void setMinimumOrderQuantity(int minimumOrderQuantity){
        this.minimumOrderQuantity = minimumOrderQuantity;
    }

    public int getOfferApplied(){
        return offerApplied;
    }

    public void setOfferApplied(int offerApplied){
        this.offerApplied = offerApplied;
    }

    public int getOfferId(){
        return offerId;
    }

    public void setOfferId(int offerId){
        this.offerId = offerId;
    }

    public int getOfferType(){
        return offerType;
    }

    public void setOfferType(int offerType){
        this.offerType = offerType;
    }

    public int getProductId(){
        return productId;
    }

    public void setProductId(int productId){
        this.productId = productId;
    }

    public int getxCount(){
        return xCount;
    }

    public void setxCount(int xCount){
        this.xCount = xCount;
    }

    public int getyCount(){
        return yCount;
    }

    public void setyCount(int yCount){
        this.yCount = yCount;
    }

    public String getyName(){
        return yName;
    }

    public void setyName(String yName){
        this.yName = yName;
    }

    public String offerAppliedText(int count)
    {
        switch (getOfferType())
        {
            default:
                return "";
            case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_VOLUME_DISCOUNT:

                if (count >= this.minimumOrderQuantity) {
                    return getDiscountPercent() + " % discount";
                }
                else {
                    return "No Discount";
                }

            case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_BUY_X_GET_Y_FREE:

                count /= this.xCount;
                int i = this.yCount;
                return count * i + " units free";

            case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_BUY_A_GET_B_FREE:

                count /= this.xCount;
                i = this.yCount;
                return count * i + " units free of " + this.yName;

        }

    }

    public String getOfferDetailsString()
    {
        switch (getOfferType())
        {
            default:
                return "";
            case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_VOLUME_DISCOUNT: {
                try {
                    String str1 = "Buy " + getMinimumOrderQuantity() + " units to get " + getDiscountPercent() + " % discount";
                    return str1;
                } catch (Exception localException1) {
                    Log.w(this.LOG_TAG, localException1.toString());
                    return "";
                }
            }
            case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_BUY_X_GET_Y_FREE: {
                try {
                    String str2 = "Buy " + getxCount() + " units to get " + getyCount() + " units free";
                    return str2;
                } catch (Exception localException2) {
                    Log.w(this.LOG_TAG, localException2.toString());
                    return "";
                }
            }
            case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_BUY_A_GET_B_FREE:{
                try
                {
                    String str3 = "Buy " + getxCount() + " units to get " + getyCount() + " units of " + getyName() + " free";
                    return str3;
                }
                catch (Exception localException3)
                {
                    Log.w(this.LOG_TAG, localException3.toString());
                    return "";
                }

            }

        }

    }


}
