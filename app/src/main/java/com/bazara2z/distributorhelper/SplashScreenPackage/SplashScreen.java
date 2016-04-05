package com.bazara2z.distributorhelper.SplashScreenPackage;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.bazara2z.distributorhelper.Data.DistributorContract;
import com.bazara2z.distributorhelper.LoginActivityPackage.LoginActivity;
import com.bazara2z.distributorhelper.MainActivityPackage.MainActivity;
import com.bazara2z.distributorhelper.SyncAdapter.SyncFunctions;

import java.util.Vector;

/**
 * Created by Maddy on 08-03-2016.
 */
public class SplashScreen extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT_MAIN = 1000;
    private static int SPLASH_TIME_OUT_LOGIN = 1000;
    public static String LOG_TAG = "SplashScreenActivity";
    private SyncFunctions syncFunctions;
    private Context mContext;
    int checkLogin = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //createSyncTable();


        checkLogin();
        mContext = getApplicationContext();
        syncFunctions = new SyncFunctions(mContext);

        //insertSampleData();


        //setContentView(R.layout.activity_splash_screen);

        if (checkLogin == 1) {
            syncFunctions.getUnsyncedRetailerData();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT_MAIN);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT_LOGIN);
        }

    }

    public void checkLogin(){
        String[] columns = {DistributorContract.UserEntry.COLUMN_LOGIN_STATUS};

        Context mContext = getApplicationContext();

        Cursor cursor = mContext.getContentResolver().query(DistributorContract.UserEntry.CHECK_URI, columns,null,null,null);

        if (cursor.getCount() > 0) {

            cursor.moveToNext();
            if(cursor.getString(cursor.getColumnIndex(DistributorContract.UserEntry.COLUMN_LOGIN_STATUS)).equals("1")){

                checkLogin =1;

                //Toast.makeText(getApplicationContext(), "Hey you were already logged in", Toast.LENGTH_LONG).show();
            }
        }
        cursor.close();
    }


    public void insertSampleData(){

        Context mContext = getApplicationContext();

        //---------------------------------------------------------
        // INSERT RETAILERS


        int retailersCount = 1;

        String[] retailerColumns = {DistributorContract.RetailersEntry.COLUMN_RETAILER_ID};

        Cursor retailerCursor = mContext.getContentResolver().query(DistributorContract.RetailersEntry.CHECK_URI, retailerColumns,null,null,null);

        if (retailerCursor.getCount() > 0) {
            retailersCount = 0;
        }

        retailerCursor.close();

        if (retailersCount == 1) {

            Vector<ContentValues> cVVector = new Vector<ContentValues>(5);

            for (int i = 1; i <= 5; i = i + 1) {

                int mRetailerId = i;
                String mShopName = "Retailer Shop " + String.valueOf(i);
                String mAddress = "Address Line 1";
                String mPhoneNumber = "9999999999";
                String mPincode = "111111";
                int mLocationPresent = 0;
                double mLatitude = 0.0;
                double mLongitude = 0.0;
                int mUploadSyncStatus = 0;

                ContentValues retailerValues = new ContentValues();
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_RETAILER_ID, mRetailerId);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_SHOP_NAME, mShopName);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_ADDRESS_LINE_1, mAddress);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_PINCODE, mPincode);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_LOCATION_PRESENT, mLocationPresent);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_RETAILER_LATITUDE, mLatitude);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_RETAILER_LONGITUDE, mLongitude);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_UPLOAD_SYNC_STATUS, mUploadSyncStatus);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_PHONE_NUMBER, mPhoneNumber);

                //Uri insertedUri = mContext.getContentResolver().insert(RetailersEntry.INSERT_URI, retailerValues);

                cVVector.add(retailerValues);
            }

            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            mContext.getContentResolver().bulkInsert(DistributorContract.RetailersEntry.BULK_INSERT_URI, cvArray);

            Log.w(LOG_TAG,"Inserted retailers");
        }

        //---------------------------------------------------------
        // INSERT PRODUCTS

        int productsCount = 1;

        String[] productColumns = {DistributorContract.ProductsEntry.COLUMN_PRODUCT_ID};

        Cursor productsCursor = mContext.getContentResolver().query(DistributorContract.ProductsEntry.CHECK_URI, productColumns,null,null,null);

        if (productsCursor.getCount() > 0) {
            productsCount = 0;
        }

        productsCursor.close();

        if (productsCount == 1) {

            Vector<ContentValues> cVVector = new Vector<ContentValues>(20);

            for (int i = 1; i <= 20; i = i + 1) {

                int mProductId = i;
                String mProductName = "Product " + String.valueOf(i);
                double mPricePerUnit = 10.0;

                ContentValues productValues = new ContentValues();

                productValues.put(DistributorContract.ProductsEntry.COLUMN_PRODUCT_ID, mProductId);
                productValues.put(DistributorContract.ProductsEntry.COLUMN_PRODUCT_NAME, mProductName);
                productValues.put(DistributorContract.ProductsEntry.COLUMN_PRICE_PER_UNIT, mPricePerUnit);

                //Uri insertedUri = mContext.getContentResolver().insert(ProductsEntry.INSERT_URI, productValues);

                cVVector.add(productValues);
            }

            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            mContext.getContentResolver().bulkInsert(DistributorContract.ProductsEntry.BULK_INSERT_URI, cvArray);

            Log.w(LOG_TAG,"Inserted products");
        }

        //---------------------------------------------------------
        // INSERT OFFERS

        int offersCount = 1;

        String[] offerColumns = {DistributorContract.OffersEntry.COLUMN_OFFER_ID};

        Cursor offerCursor = mContext.getContentResolver().query(DistributorContract.OffersEntry.CHECK_URI, offerColumns,null,null,null);

        if (offerCursor.getCount() > 0) {
            offersCount = 0;
        }

        offerCursor.close();

        if (offersCount == 1) {

            Vector<ContentValues> cVVector = new Vector<ContentValues>(10);

            for (int i = 1; i <= 10; i = i + 1) {


                String mOfferDetails = "Discount of 10%";
                int mOfferId = i;

                ContentValues offerValues = new ContentValues();

                offerValues.put(DistributorContract.OffersEntry.COLUMN_OFFER_ID, mOfferId);
                offerValues.put(DistributorContract.OffersEntry.COLUMN_OFFER_DETAILS, mOfferDetails);

                //Uri insertedUri = mContext.getContentResolver().insert(OffersEntry.INSERT_URI, offerValues);

                cVVector.add(offerValues);
            }

            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            mContext.getContentResolver().bulkInsert(DistributorContract.OffersEntry.BULK_INSERT_URI, cvArray);

            Log.w(LOG_TAG,"Inserted offers");
        }
    }

    public void createSyncTable(){

        int syncExistCheck = 1;

        Context mContext = getApplicationContext();

        String[] syncColumns = {DistributorContract.SyncStatesEntry.COLUMN_SYNC_ENTRY};

        Cursor syncCursor = mContext.getContentResolver().query(DistributorContract.SyncStatesEntry.CHECK_URI, syncColumns,null,null,null);

        if (syncCursor.getCount() > 0) {
            syncExistCheck = 0;
        }

        syncCursor.close();

        if (syncExistCheck == 1) {

            Vector<ContentValues> cVVector = new Vector<ContentValues>(10);

            for (int i = 1; i <= DistributorContract.SyncStatesEntry.SYNC_STATES_COUNT; i = i + 1) {

                int id = i;
                int syncStatus = 0;
                int lastSyncedTime = 0;

                ContentValues syncValues = new ContentValues();

                syncValues.put(DistributorContract.SyncStatesEntry.COLUMN_SYNC_ENTRY, id);
                syncValues.put(DistributorContract.SyncStatesEntry.COLUMN_SYNC_STATUS, syncStatus);
                syncValues.put(DistributorContract.SyncStatesEntry.COLUMN_LAST_SYNCED_TIME, lastSyncedTime);

                //Uri insertedUri = mContext.getContentResolver().insert(OffersEntry.INSERT_URI, offerValues);

                cVVector.add(syncValues);
            }

            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            mContext.getContentResolver().bulkInsert(DistributorContract.SyncStatesEntry.BULK_INSERT_URI, cvArray);

            Log.w(LOG_TAG,"Created sync table");
        }
    }

}
