package com.bazara2z.distributorhelper.SyncAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bazara2z.distributorhelper.Data.DistributorContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Maddy on 02-04-2016.
 */
public class SyncFunctions {

    private final Context mContext;

    public static final String DEFAULT_URL = "http://api.probzip.com/";

    public static final String CORRECT_RESPONSE_CODE = "2XX";

    public static final String LOGIN_URL = DEFAULT_URL + "salesman/login";
    public static final String RETAILER_DOWNLOAD_URL = DEFAULT_URL + "users/retailers";
    public static final String RETAILER_UPLOAD_URL = DEFAULT_URL + "users/retailers/";
    public static final String OFFER_DOWNLOAD_URL = DEFAULT_URL + "offers";
    public static final String PRODUCT_DOWNLOAD_URL = DEFAULT_URL + "products/";
    public static final String ORDERS_UPLOAD_URL = DEFAULT_URL + "orders/";
    public static final String TRACKING_UPLOAD_URL = DEFAULT_URL + "tracking/";

    ///LOGIN
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_PASSWORD = "password";

    ///RETAILER
    public static final String KEY_RETAILER_ID = "retailerID";
    public static final String KEY_PROFILE_PICTURE = "profile_picture";
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_ADDRESS_LINE_1 = "address_line_1";
    public static final String KEY_ADDRESS_LINE_2 = "address_line_2";
    public static final String KEY_UPDATED_AT = "updated_at";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_NAME = "name";
    public static final String KEY_PINCODE = "pincode";
    public static final String KEY_LANDMARK = "landmark";

    ///PRODUCT
    public static final String KEY_PRODUCT_NAME = "name";
    public static final String KEY_PRICE_PER_UNIT = "price_per_unit";
    public static final String KEY_PRODUCT_ID = "productID";


    ///OFFER
    public static final String KEY_OFFER_STATUS = "offer_status";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PRODUCT_OFFER_ID = "productOfferID";
    public static final String KEY_DISTRIBUTOR_ID = "distributorID";
    public static final String KEY_OFFER_TYPE = "offerType";
    public static final String KEY_OFFER_TYPE_ID = "offerTypeID";
    public static final String KEY_OFFER_TYPE_NAME = "offerTypeName";
    public static final String KEY_OFFER_DESCRIPTION = "description";
    public static final String KEY_OFFER_MINIMUM_ORDER_QUANTITY = "MOQ";
    public static final String KEY_OFFER_DISCOUNT_PERCENT = "discount";
    public static final String KEY_OFFER_X_COUNT = "x";
    public static final String KEY_OFFER_Y_COUNT = "y";
    public static final String KEY_Y_NAME = "B";

    public static final String KEY_ORDER_OFFER_ID = "orderOfferID";
    public static final String KEY_ORDER_OFFER_DISCOUNT = "discount";
    public static final String KEY_ORDER_OFFER_NAME = "name";

    ///ORDERS
    public static final String KEY_ORDERS = "orders";
    public static final String KEY_PRODUCT_COUNT = "productCount";
    public static final String KEY_TOTAL_PRICE = "totalPrice";
    public static final String KEY_EDITED_PRICE = "editedPrice";
    public static final String KEY_ORDER_PRODUCTS = "products";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_ORDER_OFFER_IDS = "orderOffer";
    public static final String KEY_PRODUCT_OFFERS = "productOffer";
    public static final String KEY_FREE_UNITS = "freeQuantity";

    ///TRACKING
    public static final String KEY_TRACKING = "latlngs";
    public static final String KEY_TRACKED_LATITUDE = "lat";
    public static final String KEY_TRACKED_LONGITUDE = "lng";
    public static final String KEY_TRACKED_TIME = "time";

    public final String LOG_TAG = "Sync Adapter";

    public static final int SYNC_INTERVAL = 60 * 30;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    public SyncFunctions(Context context){
        mContext = context;
    }


    public void getRetailerData() {

        if (mContext == null){
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);

        String url = RETAILER_DOWNLOAD_URL + accessTokenUrl();

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String statusCode = response.getString("statusCode");
                            String body = response.getString("body");
                            JSONObject bodyJSON = new JSONObject(body);

                            if (statusCode.equals(CORRECT_RESPONSE_CODE)) {

                                fillRetailerData(body);

                            } else {
                                Log.w(LOG_TAG, "Status code was : " + statusCode);
                                String error = bodyJSON.getString("error");
                                Log.w(LOG_TAG, "Status code was : " + error);
                            }
                        } catch (JSONException e) {
                            Log.w(LOG_TAG, e.toString());
                        }
                    }
                },
                    new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(LOG_TAG, error.toString());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void fillRetailerData(String body){

        if (mContext == null){
            return;
        }

        ArrayList<Integer> retailerIds = getAllRetailerIds();

        JSONObject bodyJSON;
        JSONArray retailers;

        try {
            bodyJSON = new JSONObject(body);
            retailers = bodyJSON.getJSONArray("retailers");
        }
        catch (JSONException e){
            return;
        }

        ArrayList<ContentValues> cVVector = new ArrayList<ContentValues>();

        for (int i = 0; i < retailers.length(); i = i + 1) {

            JSONObject retailer;

            try {
                retailer = retailers.getJSONObject(i);
            }
            catch (JSONException e){
                return;
            }

            int mRetailerId;
            String mShopName;
            String mFirstName;
            String mPhoneNumber;

            String mAddressLine1;
            String mAddressLine2;
            String mLandmark;
            String mPincode;

            int mLocationPresent;
            String mLatitude;
            String mLongitude;
            int mUploadSyncStatus;
            int mRetailerEdited;


            try {
                mRetailerId = retailer.getInt(KEY_RETAILER_ID);
                mShopName = retailer.getString(KEY_COMPANY_NAME);
                mFirstName = retailer.getString(KEY_NAME);
                mPhoneNumber = retailer.getString(KEY_MOBILE_NUMBER);
                mAddressLine1 = retailer.getString(KEY_ADDRESS_LINE_1);
                mAddressLine2 = retailer.getString(KEY_ADDRESS_LINE_2);
                mLandmark = retailer.getString(KEY_LANDMARK);
                mPincode = retailer.getString(KEY_PINCODE);
                mLatitude = retailer.getString(KEY_LATITUDE);
                mLongitude = retailer.getString(KEY_LONGITUDE);
            }
            catch (JSONException e){
                return;
            }

            if (mLatitude.equals("null") || mLongitude.equals("null")){
                mLocationPresent = 0;
                mLatitude = "0.0";
                mLongitude = "0.0";
            }
            else if(mLatitude == null || mLongitude == null){
                mLocationPresent = 0;
                mLatitude = "0.0";
                mLongitude = "0.0";
            }
            else {
                mLocationPresent = 1;
            }

            mUploadSyncStatus = 1;
            mRetailerEdited = 0;

            if(!retailerIds.contains(mRetailerId)) {

                ContentValues retailerValues = new ContentValues();
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_RETAILER_ID, mRetailerId);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_SHOP_NAME, mShopName);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_FIRST_NAME, mFirstName);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_PHONE_NUMBER, mPhoneNumber);

                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_ADDRESS_LINE_1, mAddressLine1);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_ADDRESS_LINE_2, mAddressLine2);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_LANDMARK, mLandmark);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_PINCODE, mPincode);

                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_LOCATION_PRESENT, mLocationPresent);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_RETAILER_LATITUDE, Double.valueOf(mLatitude));
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_RETAILER_LONGITUDE, Double.valueOf(mLongitude));
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_UPLOAD_SYNC_STATUS, mUploadSyncStatus);
                retailerValues.put(DistributorContract.RetailersEntry.COLUMN_RETAILER_EDITED, mRetailerEdited);


                //Uri insertedUri = mContext.getContentResolver().insert(RetailersEntry.INSERT_URI, retailerValues);

                cVVector.add(retailerValues);
            }
        }

        ContentValues[] cvArray = new ContentValues[cVVector.size()];
        cVVector.toArray(cvArray);

        int count = mContext.getContentResolver().bulkInsert(DistributorContract.RetailersEntry.BULK_INSERT_URI, cvArray);

        Log.w(LOG_TAG, "Inserted " + count + " retailers");

        afterRetailerDownload();
    }

    public ArrayList<Integer> getAllRetailerIds(){

        if (mContext == null){
            return null;
        }

        ArrayList<Integer> retailerIds = new ArrayList<Integer>();

        int mretailerId;

        String[] columns = {DistributorContract.RetailersEntry.COLUMN_RETAILER_ID};

        Cursor cursor = mContext.getContentResolver().query(DistributorContract.RetailersEntry.CHECK_URI, columns, null, null, null);

        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                mretailerId = cursor.getInt(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_RETAILER_ID));

                retailerIds.add(mretailerId);
            }
        }

        cursor.close();

        return retailerIds;
    }

    public void deleteRetailerData(){

        if (mContext == null){
            return;
        }

        String selection = null;
        String[] selectionArgs = null;

        int deletedrows = mContext.getContentResolver().delete(DistributorContract.RetailersEntry.DELETE_URI, selection ,selectionArgs);

    }

    public void getOfferData() {

        if (mContext == null){
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);

        String url = OFFER_DOWNLOAD_URL + accessTokenUrl();

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String statusCode = response.getString("statusCode");
                            String body = response.getString("body");
                            JSONObject bodyJSON = new JSONObject(body);

                            if (statusCode.equals(CORRECT_RESPONSE_CODE)) {

                                fillOfferData(body);

                            } else {
                                Log.w(LOG_TAG, "Status code was : " + statusCode);
                                String error = bodyJSON.getString("error");
                                Log.w(LOG_TAG, "Status code was : " + error);
                            }
                        } catch (JSONException e) {
                            Log.w(LOG_TAG, e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(LOG_TAG, error.toString());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void fillOfferData(String body){

        if (mContext == null){
            return;
        }

        ArrayList<Integer> productOfferIds = getAllProductOfferIds();
        ArrayList<Integer> orderOfferIds = getAllOrderOfferIds();

        JSONObject bodyJSON;
        JSONObject offerJSON;
        JSONArray productOffers = new JSONArray();
        JSONArray orderOffers = new JSONArray();

        try {
            bodyJSON = new JSONObject(body);
            offerJSON = bodyJSON.getJSONObject("offers");
            productOffers = offerJSON.getJSONArray("productOffers");
            orderOffers = offerJSON.getJSONArray("orderOffers");
        }
        catch (JSONException e){
            Log.w(LOG_TAG, e.toString());
        }

        //----------------------------------------------------------------------------------------------------------

        ArrayList<ContentValues> cVVector1 = new ArrayList<ContentValues>(orderOffers.length());

        for (int i = 0; i < orderOffers.length(); i = i + 1){
            JSONObject orderOffer = new JSONObject();

            try {
                orderOffer = orderOffers.getJSONObject(i);
            }
            catch (JSONException e){
                Log.w(LOG_TAG, e.toString());
            }

            int orderOfferId = 0;
            String orderOfferName = "";
            double discountPercent = 0.0;

            try {
                orderOfferId = orderOffer.getInt(KEY_ORDER_OFFER_ID);
                orderOfferName = orderOffer.getString(KEY_ORDER_OFFER_NAME);
                discountPercent = orderOffer.getDouble(KEY_ORDER_OFFER_DISCOUNT);
            }
            catch (JSONException e){
                Log.w(LOG_TAG, e.toString());
            }

            if(!orderOfferIds.contains(orderOfferId)){
                ContentValues offerValues = new ContentValues();

                offerValues.put(DistributorContract.OrderOffersEntry.COLUMN_OFFER_ID, orderOfferId);
                offerValues.put(DistributorContract.OrderOffersEntry.COLUMN_OFFER_NAME, orderOfferName);
                offerValues.put(DistributorContract.OrderOffersEntry.COLUMN_DISCOUNT_PERCENT, discountPercent);

                cVVector1.add(offerValues);
            }


        }

        ContentValues[] cvArray1 = new ContentValues[cVVector1.size()];
        cVVector1.toArray(cvArray1);

        int count = mContext.getContentResolver().bulkInsert(DistributorContract.OrderOffersEntry.BULK_INSERT_URI, cvArray1);

        Log.w(LOG_TAG, "Inserted " + count + " order offers");

        //----------------------------------------------------------------------------------------------------------

        ArrayList<ContentValues> cVVector = new ArrayList<ContentValues>(productOffers.length());

        for (int i = 0; i < productOffers.length(); i = i + 1) {

            JSONObject productOffer = new JSONObject();

            try {
                productOffer = productOffers.getJSONObject(i);
            }
            catch (JSONException e){
                Log.w(LOG_TAG, e.toString());
            }

            int mProductOfferId = 0;
            int mProductId = 0;
            JSONObject mOfferType = new JSONObject();
            int mOfferTypeId = 0;
            String mOfferTypeName = "";
            JSONObject mOfferDescription = new JSONObject();
            int minimumOrderQuantity = 0;
            double discountPercent = 0.0;
            int xCount = 0;
            int yCount = 0;
            String yName = "";

            try {
                mProductOfferId = productOffer.getInt(KEY_PRODUCT_OFFER_ID);
                mProductId = productOffer.getInt(KEY_PRODUCT_ID);
                mOfferType = productOffer.getJSONObject(KEY_OFFER_TYPE);
                mOfferTypeId = mOfferType.getInt(KEY_OFFER_TYPE_ID);
                mOfferTypeName = mOfferType.getString(KEY_OFFER_TYPE_NAME);
                mOfferDescription = productOffer.getJSONObject(KEY_OFFER_DESCRIPTION);

                switch (mOfferTypeId){
                    case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_VOLUME_DISCOUNT:
                        minimumOrderQuantity = mOfferDescription.getInt(KEY_OFFER_MINIMUM_ORDER_QUANTITY);
                        discountPercent = mOfferDescription.getDouble(KEY_OFFER_DISCOUNT_PERCENT);
                        break;
                    case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_BUY_X_GET_Y_FREE:
                        xCount = mOfferDescription.getInt(KEY_OFFER_X_COUNT);
                        yCount = mOfferDescription.getInt(KEY_OFFER_Y_COUNT);
                        break;
                    case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_BUY_A_GET_B_FREE:
                        xCount = mOfferDescription.getInt(KEY_OFFER_X_COUNT);
                        yCount = mOfferDescription.getInt(KEY_OFFER_Y_COUNT);
                        yName = mOfferDescription.getString(KEY_Y_NAME);
                        break;
                }
            }
            catch (JSONException e){
                Log.w(LOG_TAG, e.toString());
            }

            if(!productOfferIds.contains(mProductOfferId)) {

                ContentValues offerValues = new ContentValues();

                offerValues.put(DistributorContract.ProductOffersEntry.COLUMN_OFFER_ID, mProductOfferId);
                offerValues.put(DistributorContract.ProductOffersEntry.COLUMN_OFFER_TYPE_NAME, mOfferTypeName);
                offerValues.put(DistributorContract.ProductOffersEntry.COLUMN_PRODUCT_ID, mProductId);
                offerValues.put(DistributorContract.ProductOffersEntry.COLUMN_OFFER_TYPE, mOfferTypeId);

                switch (mOfferTypeId){
                    case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_VOLUME_DISCOUNT:
                        offerValues.put(DistributorContract.ProductOffersEntry.COLUMN_MINIMUM_ORDER_QUANTITY, minimumOrderQuantity);
                        offerValues.put(DistributorContract.ProductOffersEntry.COLUMN_DISCOUNT_PERCENT, discountPercent);
                        break;
                    case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_BUY_X_GET_Y_FREE:
                        offerValues.put(DistributorContract.ProductOffersEntry.COLUMN_X_COUNT, xCount);
                        offerValues.put(DistributorContract.ProductOffersEntry.COLUMN_Y_COUNT, yCount);
                        break;
                    case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_BUY_A_GET_B_FREE:
                        offerValues.put(DistributorContract.ProductOffersEntry.COLUMN_X_COUNT, xCount);
                        offerValues.put(DistributorContract.ProductOffersEntry.COLUMN_Y_COUNT, yCount);
                        offerValues.put(DistributorContract.ProductOffersEntry.COLUMN_Y_NAME, yName);
                        break;
                }


                //Uri insertedUri = mContext.getContentResolver().insert(RetailersEntry.INSERT_URI, retailerValues);

                cVVector.add(offerValues);
            }
        }

        ContentValues[] cvArray = new ContentValues[cVVector.size()];
        cVVector.toArray(cvArray);

        count = mContext.getContentResolver().bulkInsert(DistributorContract.ProductOffersEntry.BULK_INSERT_URI, cvArray);

        Log.w(LOG_TAG, "Inserted " + count + " product offers");

        afterOfferDownload();
    }

    public ArrayList<Integer> getAllProductOfferIds(){

        if (mContext == null){
            return null;
        }

        ArrayList<Integer> offerIds = new ArrayList<Integer>();

        int offerId;

        String[] columns = {DistributorContract.ProductOffersEntry.COLUMN_OFFER_ID};

        Cursor cursor = mContext.getContentResolver().query(DistributorContract.ProductOffersEntry.CHECK_URI, columns, null, null, null);


        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                offerId = cursor.getInt(cursor.getColumnIndex(DistributorContract.ProductOffersEntry.COLUMN_OFFER_ID));

                offerIds.add(offerId);
            }
        }

        cursor.close();

        return offerIds;
    }

    public ArrayList<Integer> getAllOrderOfferIds(){

        if (mContext == null){
            return null;
        }

        ArrayList<Integer> offerIds = new ArrayList<Integer>();

        int offerId;

        String[] columns = {DistributorContract.OrderOffersEntry.COLUMN_OFFER_ID};

        Cursor cursor = mContext.getContentResolver().query(DistributorContract.OrderOffersEntry.CHECK_URI, columns, null, null, null);


        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                offerId = cursor.getInt(cursor.getColumnIndex(DistributorContract.OrderOffersEntry.COLUMN_OFFER_ID));

                offerIds.add(offerId);
            }
        }

        cursor.close();

        return offerIds;
    }

    public void deleteOfferData(){

        if (mContext == null){
            return;
        }

        String selection = null;
        String[] selectionArgs = null;

        int deletedrows = mContext.getContentResolver().delete(DistributorContract.ProductOffersEntry.DELETE_URI, selection ,selectionArgs);

    }

    public void getProductData() {

        if (mContext == null){
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);

        String url = PRODUCT_DOWNLOAD_URL + accessTokenUrl();

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String statusCode = response.getString("statusCode");
                            String body = response.getString("body");
                            JSONObject bodyJSON = new JSONObject(body);

                            if (statusCode.equals(CORRECT_RESPONSE_CODE)) {

                                fillProductData(body);

                            } else {
                                Log.w(LOG_TAG, "Status code was : " + statusCode);
                                String error = bodyJSON.getString("error");
                                Log.w(LOG_TAG, "Status code was : " + error);
                            }
                        } catch (JSONException e) {
                            Log.w(LOG_TAG, e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(LOG_TAG, error.toString());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void fillProductData(String body){

        if (mContext == null){
            return;
        }

        ArrayList<Integer> productIds = getAllProductIds();

        JSONObject bodyJSON;
        JSONArray products;

        try {
            bodyJSON = new JSONObject(body);
            products = bodyJSON.getJSONArray("products");
        }
        catch (JSONException e){
            Log.w(LOG_TAG, e.toString());
            return;
        }


        ArrayList<ContentValues> cVVector = new ArrayList<ContentValues>(products.length());

        for (int i = 0; i < products.length(); i = i + 1) {

            JSONObject product;

            try {
                product = products.getJSONObject(i);
            }
            catch (JSONException e){
                Log.w(LOG_TAG, e.toString());
                return;
            }

            int mProductId;
            String mProductName;
            double mPricePerUnit;

            try {
                mProductId = product.getInt(KEY_PRODUCT_ID);
                mProductName = product.getString(KEY_PRODUCT_NAME);
                mPricePerUnit = product.getDouble(KEY_PRICE_PER_UNIT);
            }
            catch (JSONException e){
                Log.w(LOG_TAG, e.toString());
                return;
            }

            if(!productIds.contains(mProductId)) {

                ContentValues productValues = new ContentValues();

                productValues.put(DistributorContract.ProductsEntry.COLUMN_PRODUCT_ID, mProductId);
                productValues.put(DistributorContract.ProductsEntry.COLUMN_PRODUCT_NAME, mProductName);
                productValues.put(DistributorContract.ProductsEntry.COLUMN_PRICE_PER_UNIT, mPricePerUnit);


                cVVector.add(productValues);
            }
        }

        ContentValues[] cvArray = new ContentValues[cVVector.size()];
        cVVector.toArray(cvArray);

        int count = mContext.getContentResolver().bulkInsert(DistributorContract.ProductsEntry.BULK_INSERT_URI, cvArray);

        Log.w(LOG_TAG, "Inserted " + count + " products");

        afterProductDownload();
    }

    public ArrayList<Integer> getAllProductIds(){

        if (mContext == null){
            return null;
        }

        ArrayList<Integer> productIds = new ArrayList<Integer>();

        int productId;

        String[] columns = {DistributorContract.ProductsEntry.COLUMN_PRODUCT_ID};

        Cursor cursor = mContext.getContentResolver().query(DistributorContract.ProductsEntry.CHECK_URI, columns, null, null, null);

        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                productId = cursor.getInt(cursor.getColumnIndex(DistributorContract.ProductsEntry.COLUMN_PRODUCT_ID));

                productIds.add(productId);
            }
        }

        cursor.close();

        return productIds;
    }

    public void deleteProductData(){

        if (mContext == null){
            return;
        }

        String selection = null;
        String[] selectionArgs = null;

        int deletedrows = mContext.getContentResolver().delete(DistributorContract.ProductsEntry.DELETE_URI, selection ,selectionArgs);

    }

    public String accessTokenUrl(){

        if (mContext == null){
            return null;
        }

        String[] columns = {DistributorContract.UserEntry.COLUMN_TOKEN};

        Cursor cursor = mContext.getContentResolver().query(DistributorContract.UserEntry.CHECK_URI, columns, null, null, null);

        if (cursor.getCount() > 0) {

            cursor.moveToNext();
            String token;
            token = cursor.getString(cursor.getColumnIndex(DistributorContract.UserEntry.COLUMN_TOKEN));

            token = "?access_token=" + token;

            return token;
        }

        return "";
    }

    public void getUnsyncedRetailerData(){

        if (mContext == null){
            return;
        }

        String[] columns = {DistributorContract.RetailersEntry._ID, DistributorContract.RetailersEntry.COLUMN_SHOP_NAME, DistributorContract.RetailersEntry.COLUMN_FIRST_NAME,
                DistributorContract.RetailersEntry.COLUMN_PHONE_NUMBER, DistributorContract.RetailersEntry.COLUMN_ADDRESS_LINE_1, DistributorContract.RetailersEntry.COLUMN_ADDRESS_LINE_2,
                DistributorContract.RetailersEntry.COLUMN_LANDMARK, DistributorContract.RetailersEntry.COLUMN_PINCODE, DistributorContract.RetailersEntry.COLUMN_LOCATION_PRESENT,
                DistributorContract.RetailersEntry.COLUMN_RETAILER_LATITUDE, DistributorContract.RetailersEntry.COLUMN_RETAILER_LONGITUDE};

        String selection = DistributorContract.RetailersEntry.COLUMN_UPLOAD_SYNC_STATUS + " = 0";

        Cursor cursor = mContext.getContentResolver().query(DistributorContract.RetailersEntry.CHECK_URI, columns, selection, null, null);

        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {

                cursor.moveToNext();

                int retailerId = cursor.getInt(cursor.getColumnIndex(DistributorContract.RetailersEntry._ID));
                String shopName = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_SHOP_NAME));
                String firstName = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_FIRST_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_PHONE_NUMBER));
                String addressLine1 = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_ADDRESS_LINE_1));
                String addressLine2 = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_ADDRESS_LINE_2));
                String landmark = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_LANDMARK));
                String pincode = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_PINCODE));
                int locationPresent = cursor.getInt(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_LOCATION_PRESENT));
                Double latitude = cursor.getDouble(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_RETAILER_LATITUDE));
                Double longitude = cursor.getDouble(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_RETAILER_LONGITUDE));

                JSONObject params = new JSONObject();

                try {
                    params.put(KEY_COMPANY_NAME, shopName.toString());
                    params.put(KEY_NAME, firstName.toString());
                    params.put(KEY_MOBILE_NUMBER, phoneNumber.toString());
                    params.put(KEY_ADDRESS_LINE_1, addressLine1.toString());
                    params.put(KEY_ADDRESS_LINE_2, addressLine2.toString());
                    params.put(KEY_LANDMARK, landmark.toString());
                    params.put(KEY_PINCODE, pincode.toString());
                    if (locationPresent == 1) {
                        params.put(KEY_LATITUDE, latitude.toString());
                        params.put(KEY_LONGITUDE, longitude.toString());
                    }
                } catch (JSONException e){
                    Log.w(LOG_TAG, e.toString());
                }
                final String mRequestBody = params.toString();

                int lastRetailer = 0;
                if (i == (cursor.getCount() - 1)){
                    lastRetailer = 1;
                }

                sendRetailerData(mRequestBody, retailerId, lastRetailer);
            }

        }
        else {
            Log.w(LOG_TAG, "No unsynced retailers");
            afterRetailerUpdate();
        }

        cursor.close();

    }

    public void sendRetailerData(final String mRequestBody, final int retailerId, final int lastRetailer) {

        if (mContext == null){
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);

        String url = RETAILER_UPLOAD_URL + accessTokenUrl();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w(LOG_TAG, response);
                            JSONObject jsonResponse = new JSONObject(response);
                            String statusCode = jsonResponse.getString("statusCode");
                            String body = jsonResponse.getString("body");
                            JSONObject bodyJSON = new JSONObject(body);



                            if (statusCode.equals(CORRECT_RESPONSE_CODE)) {
                                updateRetailerData(body, retailerId, lastRetailer);
                            } else {
                                Log.w(LOG_TAG, "Status code was : " + statusCode);
                                String error = bodyJSON.getString("error");
                                Log.w(LOG_TAG, "Status code was : " + error);
                            }
                        } catch (JSONException e) {
                            Log.w(LOG_TAG, e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(LOG_TAG, error.toString());
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                return mRequestBody == null ? null : mRequestBody.getBytes();
            }

        };

        queue.add(postRequest);

    }

    public void updateRetailerData(String body, int retailerId, int lastRetailer){

        if (mContext == null){
            return;
        }

        JSONObject bodyJSON;
        JSONArray retailers;

        try {
            bodyJSON = new JSONObject(body);
            retailers = bodyJSON.getJSONArray("retailer");
        }
        catch (JSONException e){
            Log.w(LOG_TAG, e.toString());
            return;
        }

        //Log.w(LOG_TAG, "retailers.length() was " + retailers.length());

        for (int i = 0; i < retailers.length(); i = i + 1) {

            JSONObject retailer;

            try {
                retailer = retailers.getJSONObject(i);
            }
            catch (JSONException e){
                Log.w(LOG_TAG, e.toString());
                return;
            }

            int mRetailerId;

            try {
                mRetailerId = retailer.getInt(KEY_RETAILER_ID);
            }
            catch (JSONException e){
                Log.w(LOG_TAG, e.toString());
                return;
            }

            ContentValues retailerValues = new ContentValues();

            retailerValues.put(DistributorContract.RetailersEntry.COLUMN_RETAILER_ID, mRetailerId);
            retailerValues.put(DistributorContract.RetailersEntry.COLUMN_UPLOAD_SYNC_STATUS, 1);

            Log.w(LOG_TAG, "The retailer id received back was " + mRetailerId);

            String selection = DistributorContract.RetailersEntry._ID +" = " + retailerId;
            String[] selectionArgs = null;

            int editedrows = mContext.getContentResolver().update(DistributorContract.RetailersEntry.UPDATE_URI, retailerValues,selection,selectionArgs);
            Log.w(LOG_TAG, "The updated retailer rows was " + editedrows);

        }

        if (lastRetailer == 1){
            afterRetailerUpdate();
        }

    }

    public void getUnsyncedOrders(){

        if (mContext == null){
            return;
        }

        ArrayList<Integer> orderIds = new ArrayList<Integer>();

        int orderId;

        String[] columns = {DistributorContract.OrdersEntry._ID};

        String selection = DistributorContract.OrdersEntry.COLUMN_UPLOAD_SYNC_STATUS + " = 0" ;

        Cursor cursor = mContext.getContentResolver().query(DistributorContract.OrdersEntry.CHECK_URI, columns, selection, null, null);


        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                orderId = cursor.getInt(cursor.getColumnIndex(DistributorContract.OrdersEntry._ID));

                orderIds.add(orderId);
            }
            constructOrderJSON(orderIds);
        }
        else{
            afterOrderUpdate();
        }

        cursor.close();


    }

    public void constructOrderJSON(ArrayList<Integer> orderIds){

        if (mContext == null){
            return;
        }

        int orderCount = orderIds.size();

        JSONArray allOrders = new JSONArray();

        JSONObject ordersData = new JSONObject();

        int productCount;
        Double totalPrice;
        Double modifiedPrice;
        int retailerId;
        int productId;
        int quantity;
        int orderOfferApplied;
        int orderOfferId;
        String orderOffersString;
        String productOffersString;
        int freeUnits;

        for (int i = 0; i< orderCount; i++){

            JSONObject orderData = new JSONObject();

            String[] columns = {DistributorContract.RetailersEntry.TABLE_NAME + "." + DistributorContract.RetailersEntry.COLUMN_RETAILER_ID, DistributorContract.OrdersEntry.COLUMN_PRODUCT_COUNT,
                    DistributorContract.OrdersEntry.COLUMN_TOTAL_PRICE, DistributorContract.OrdersEntry.COLUMN_MODIFIED_PRICE,
                    DistributorContract.OrdersEntry.COLUMN_ORDER_OFFER_APPLIED, DistributorContract.OrdersEntry.COLUMN_ORDER_OFFER_ID};

            String selection = DistributorContract.OrdersEntry.TABLE_NAME + "." + DistributorContract.OrdersEntry._ID + " = " + orderIds.get(i).toString() ;

            Cursor cursor = mContext.getContentResolver().query(DistributorContract.OrdersEntry.DISPLAY_ORDERS_URI, columns, selection, null, null);

            if (cursor.getCount() > 0) {

                cursor.moveToNext();

                retailerId = cursor.getInt(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_RETAILER_ID));
                productCount = cursor.getInt(cursor.getColumnIndex(DistributorContract.OrdersEntry.COLUMN_PRODUCT_COUNT));
                totalPrice = cursor.getDouble(cursor.getColumnIndex(DistributorContract.OrdersEntry.COLUMN_TOTAL_PRICE));
                modifiedPrice = cursor.getDouble(cursor.getColumnIndex(DistributorContract.OrdersEntry.COLUMN_MODIFIED_PRICE));
                orderOfferApplied = cursor.getInt(cursor.getColumnIndex(DistributorContract.OrdersEntry.COLUMN_ORDER_OFFER_APPLIED));
                if (orderOfferApplied == 0){
                    orderOffersString = "[]";
                }
                else {
                    orderOfferId = cursor.getInt(cursor.getColumnIndex(DistributorContract.OrdersEntry.COLUMN_ORDER_OFFER_ID));
                    orderOffersString = "[" + orderOfferId + "]";
                }

                String[] columns1 = {DistributorContract.OrderItemsEntry.COLUMN_PRODUCT_ID, DistributorContract.OrderItemsEntry.COLUMN_QUANTITY,
                        DistributorContract.OrderItemsEntry.COLUMN_OFFERS_APPLIED, DistributorContract.OrderItemsEntry.COLUMN_FREE_UNITS};

                String selection1 = DistributorContract.OrderItemsEntry.COLUMN_ORDER_ID + " = " + orderIds.get(i).toString();

                Cursor cursor1 = mContext.getContentResolver().query(DistributorContract.OrderItemsEntry.CHECK_URI, columns1, selection1, null, null);

                JSONArray orderItems = new JSONArray();

                if (cursor1.getCount() > 0) {
                    for (int j = 0; j < cursor1.getCount(); j++) {
                        cursor1.moveToNext();

                        productId = cursor1.getInt(cursor1.getColumnIndex(DistributorContract.OrderItemsEntry.COLUMN_PRODUCT_ID));
                        quantity = cursor1.getInt(cursor1.getColumnIndex(DistributorContract.OrderItemsEntry.COLUMN_QUANTITY));
                        productOffersString = cursor1.getString(cursor1.getColumnIndex(DistributorContract.OrderItemsEntry.COLUMN_OFFERS_APPLIED));
                        freeUnits = cursor1.getInt(cursor1.getColumnIndex(DistributorContract.OrderItemsEntry.COLUMN_FREE_UNITS));

                        JSONObject orderItemsData = new JSONObject();
                        try {
                            orderItemsData.put(KEY_PRODUCT_ID, productId);
                            orderItemsData.put(KEY_QUANTITY, quantity);
                            orderItemsData.put(KEY_PRODUCT_OFFERS, productOffersString);
                            orderItemsData.put(KEY_FREE_UNITS, freeUnits);

                        } catch (JSONException e) {
                            Log.w(LOG_TAG, e.toString());
                        }

                        orderItems.put(orderItemsData);
                    }
                }

                cursor1.close();

                String orderItemsString = orderItems.toString();
                Log.w(LOG_TAG, orderItemsString);

                try {
                    orderData.put(KEY_PRODUCT_COUNT, productCount);
                    orderData.put(KEY_TOTAL_PRICE, totalPrice);
                    orderData.put(KEY_EDITED_PRICE, modifiedPrice);
                    orderData.put(KEY_RETAILER_ID, retailerId);
                    orderData.put(KEY_ORDER_PRODUCTS, orderItems);
                    orderData.put(KEY_ORDER_OFFER_IDS, orderOffersString);
                } catch (JSONException e) {
                    Log.w(LOG_TAG, e.toString());
                }

                allOrders.put(orderData);
            }

            cursor.close();

        }

        try {
            ordersData.put(KEY_ORDERS, allOrders);
        }
        catch (JSONException e){

        }


        String allOrdersString = ordersData.toString();

        Log.w(LOG_TAG, allOrdersString);

        sendOrdersData(allOrdersString, orderIds);
    }

    public void sendOrdersData(final String mRequestBody, final ArrayList<Integer> orderIds) {

        if (mContext == null){
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);

        String url = ORDERS_UPLOAD_URL + accessTokenUrl();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w(LOG_TAG, response);
                            JSONObject jsonResponse = new JSONObject(response);
                            String statusCode = jsonResponse.getString("statusCode");
                            String body = jsonResponse.getString("body");
                            JSONObject bodyJSON = new JSONObject(body);

                            if (statusCode.equals(CORRECT_RESPONSE_CODE)) {
                                updateOrdersData(body, orderIds);
                            } else {
                                Log.w(LOG_TAG, "Status code was : " + statusCode);
                                String error = bodyJSON.getString("error");
                                Log.w(LOG_TAG, "Status code was : " + error);
                            }
                        } catch (JSONException e) {
                            Log.w(LOG_TAG, e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(LOG_TAG, error.toString());
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                return mRequestBody == null ? null : mRequestBody.getBytes();
            }

        };

        queue.add(postRequest);

    }

    public void updateOrdersData(String body, ArrayList<Integer> orderIds){

        if (mContext == null){
            return;
        }

        JSONObject bodyJSON;
        JSONArray orders;
        ArrayList<Integer> newOrderIds = new ArrayList<Integer>();

        try {
            bodyJSON = new JSONObject(body);
            orders = bodyJSON.getJSONArray("orderIDs");

            if (orders != null) {
                int len = orders.length();
                for (int i=0;i<len;i++){
                    newOrderIds.add(orders.getInt(i));
                }
            }
        }
        catch (JSONException e){
            Log.w(LOG_TAG, e.toString());
            return;
        }

        for (int i = 0; i < newOrderIds.size(); i = i + 1) {

            ContentValues orderValues = new ContentValues();

            orderValues.put(DistributorContract.OrdersEntry.COLUMN_ORDER_ID, newOrderIds.get(i));
            orderValues.put(DistributorContract.OrdersEntry.COLUMN_UPLOAD_SYNC_STATUS, 1);

            String selection = DistributorContract.OrdersEntry._ID +" = " + orderIds.get(i);
            String[] selectionArgs = null;

            int editedrows = mContext.getContentResolver().update(DistributorContract.OrdersEntry.UPDATE_URI, orderValues,selection,selectionArgs);

            Log.w(LOG_TAG, "Order updated");

            if (i == newOrderIds.size()){
                afterOrderUpdate();
            }

        }


    }

    public void getUnsyncedTracking(){

        if (mContext == null){
            return;
        }

        JSONArray allTracking = new JSONArray();
        JSONObject allTrackingData = new JSONObject();


        int retailerId;
        Long trackedTime;
        Double trackedLatitude;
        Double trackedLongitude;

        String[] columns = {DistributorContract.RetailersEntry.TABLE_NAME + "." +DistributorContract.RetailersEntry.COLUMN_RETAILER_ID, DistributorContract.TrackingEntry.COLUMN_TRACKED_LATITUDE,
                DistributorContract.TrackingEntry.COLUMN_TRACKED_LONGITUDE, DistributorContract.TrackingEntry.COLUMN_TRACKED_TIME, DistributorContract.RetailersEntry.COLUMN_SHOP_NAME};

        String selection = DistributorContract.TrackingEntry.TABLE_NAME + "." + DistributorContract.TrackingEntry.COLUMN_UPLOAD_SYNC_STATUS + " = 0" ;

        Cursor cursor = mContext.getContentResolver().query(DistributorContract.TrackingEntry.CHECK_WITH_RETAILERS_URI, columns, selection, null, null);


        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                JSONObject trackingData = new JSONObject();

                retailerId = cursor.getInt(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_RETAILER_ID));
                trackedTime = cursor.getLong(cursor.getColumnIndex(DistributorContract.TrackingEntry.COLUMN_TRACKED_TIME));
                trackedLatitude = cursor.getDouble(cursor.getColumnIndex(DistributorContract.TrackingEntry.COLUMN_TRACKED_LATITUDE));
                trackedLongitude = cursor.getDouble(cursor.getColumnIndex(DistributorContract.TrackingEntry.COLUMN_TRACKED_LONGITUDE));
                String shopName = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_SHOP_NAME));

                try {
                    trackingData.put(KEY_RETAILER_ID, retailerId);
                    trackingData.put(KEY_TRACKED_LATITUDE, trackedLatitude);
                    trackingData.put(KEY_TRACKED_LONGITUDE, trackedLongitude);
                    trackingData.put(KEY_TRACKED_TIME, trackedTime);
                    trackingData.put(KEY_COMPANY_NAME, shopName);

                } catch (JSONException e) {
                    Log.w(LOG_TAG, e.toString());
                }

                allTracking.put(trackingData);
            }

            try {
                allTrackingData.put(KEY_TRACKING, allTracking);
            }
            catch (JSONException e){

            }


            String allTrackingString = allTrackingData.toString();

            Log.w(LOG_TAG, allTrackingString);

            sendTrackingData(allTrackingString);

        }
        else {
            Log.w(LOG_TAG, "No unsynced tracking");
            afterTrackingUpdate();
        }

        cursor.close();

    }

    public void sendTrackingData(final String mRequestBody) {

        if (mContext == null){
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);

        String url = TRACKING_UPLOAD_URL + accessTokenUrl();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w(LOG_TAG, response);
                            JSONObject jsonResponse = new JSONObject(response);
                            String statusCode = jsonResponse.getString("statusCode");
                            String body = jsonResponse.getString("body");
                            JSONObject bodyJSON = new JSONObject(body);

                            if (statusCode.equals(CORRECT_RESPONSE_CODE)) {
                                Log.w(LOG_TAG, "Tracking data successfully received");
                                updateTrackingData();

                            } else {
                                Log.w(LOG_TAG, "Status code was : " + statusCode);
                                String error = bodyJSON.getString("error");
                                Log.w(LOG_TAG, "Status code was : " + error);
                            }
                        } catch (JSONException e) {
                            Log.w(LOG_TAG, e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(LOG_TAG, error.toString());
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                return mRequestBody == null ? null : mRequestBody.getBytes();
            }

        };

        queue.add(postRequest);

    }

    public void updateTrackingData(){

        if (mContext == null){
            return;
        }

        String selection = DistributorContract.TrackingEntry.COLUMN_UPLOAD_SYNC_STATUS + " = 0" ;
        String[] selectionArgs = null;

        ContentValues trackingValues = new ContentValues();

        trackingValues.put(DistributorContract.TrackingEntry.COLUMN_UPLOAD_SYNC_STATUS, 1);

        int editedrows = mContext.getContentResolver().update(DistributorContract.TrackingEntry.UPDATE_URI, trackingValues, selection, selectionArgs);

        Log.w(LOG_TAG, "Tracking updated");

        afterTrackingUpdate();

    }

    public void getEditedRetailerData(){

        if (mContext == null){
            return;
        }

        String[] columns = {DistributorContract.RetailersEntry._ID, DistributorContract.RetailersEntry.COLUMN_RETAILER_ID,  DistributorContract.RetailersEntry.COLUMN_SHOP_NAME, DistributorContract.RetailersEntry.COLUMN_FIRST_NAME,
                DistributorContract.RetailersEntry.COLUMN_PHONE_NUMBER, DistributorContract.RetailersEntry.COLUMN_ADDRESS_LINE_1, DistributorContract.RetailersEntry.COLUMN_ADDRESS_LINE_2,
                DistributorContract.RetailersEntry.COLUMN_LANDMARK, DistributorContract.RetailersEntry.COLUMN_PINCODE, DistributorContract.RetailersEntry.COLUMN_LOCATION_PRESENT,
                DistributorContract.RetailersEntry.COLUMN_RETAILER_LATITUDE, DistributorContract.RetailersEntry.COLUMN_RETAILER_LONGITUDE};

        String selection = DistributorContract.RetailersEntry.COLUMN_RETAILER_EDITED + " = 1";

        Cursor cursor = mContext.getContentResolver().query(DistributorContract.RetailersEntry.CHECK_URI, columns, selection, null, null);
        Log.w(LOG_TAG, "The number of edited retailers is " +  cursor.getCount());

        if (cursor.getCount() > 0){

            for (int i = 0; i < cursor.getCount(); i++) {

                cursor.moveToNext();

                int retailerId = cursor.getInt(cursor.getColumnIndex(DistributorContract.RetailersEntry._ID));
                int retailerActualId = cursor.getInt(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_RETAILER_ID));
                String shopName = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_SHOP_NAME));
                String firstName = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_FIRST_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_PHONE_NUMBER));
                String addressLine1 = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_ADDRESS_LINE_1));
                String addressLine2 = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_ADDRESS_LINE_2));
                String landmark = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_LANDMARK));
                String pincode = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_PINCODE));
                int locationPresent = cursor.getInt(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_LOCATION_PRESENT));
                Double latitude = cursor.getDouble(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_RETAILER_LATITUDE));
                Double longitude = cursor.getDouble(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_RETAILER_LONGITUDE));

                JSONObject params = new JSONObject();

                try {
                    params.put(KEY_RETAILER_ID, retailerActualId);
                    params.put(KEY_COMPANY_NAME, shopName);
                    params.put(KEY_NAME, firstName);
                    params.put(KEY_MOBILE_NUMBER, phoneNumber);
                    params.put(KEY_ADDRESS_LINE_1, addressLine1);
                    params.put(KEY_ADDRESS_LINE_2, addressLine2);
                    params.put(KEY_LANDMARK, landmark);
                    params.put(KEY_PINCODE, pincode);
                    if (locationPresent == 1) {
                        params.put(KEY_LATITUDE, latitude);
                        params.put(KEY_LONGITUDE, longitude);
                    }
                } catch (JSONException e){
                    Log.w(LOG_TAG, e.toString());
                }
                final String mRequestBody = params.toString();

                int lastRetailer = 0;
                if (i == (cursor.getCount() - 1)){
                    lastRetailer = 1;
                }

                sendEditedRetailerData(mRequestBody, retailerId, lastRetailer);
            }

        }
        else {
            afterEditedRetailerUpdate();
        }

        cursor.close();

    }

    public void sendEditedRetailerData(final String mRequestBody, final int retailerId, final int lastRetailer) {

        if (mContext == null){
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);

        String url = RETAILER_UPLOAD_URL + accessTokenUrl();

        StringRequest postRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w(LOG_TAG, response);
                            JSONObject jsonResponse = new JSONObject(response);
                            String statusCode = jsonResponse.getString("statusCode");
                            String body = jsonResponse.getString("body");
                            JSONObject bodyJSON = new JSONObject(body);

                            if (statusCode.equals(CORRECT_RESPONSE_CODE)) {
                                updateEditedRetailerData(body, retailerId, lastRetailer);
                            } else {
                                Log.w(LOG_TAG, "Status code was : " + statusCode);
                                String error = bodyJSON.getString("error");
                                Log.w(LOG_TAG, "Status code was : " + error);
                            }
                        } catch (JSONException e) {
                            Log.w(LOG_TAG, e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(LOG_TAG, error.toString());
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                return mRequestBody == null ? null : mRequestBody.getBytes();
            }

        };

        queue.add(postRequest);

    }

    public void updateEditedRetailerData(String body, int retailerId, int lastRetailer){

        if (mContext == null){
            return;
        }

        JSONObject bodyJSON;
        JSONArray retailers;

        try {
            bodyJSON = new JSONObject(body);
            retailers = bodyJSON.getJSONArray("retailer");
        }
        catch (JSONException e){
            Log.w(LOG_TAG, e.toString());
            return;
        }


        for (int i = 0; i < retailers.length(); i = i + 1) {

            ContentValues retailerValues = new ContentValues();

            retailerValues.put(DistributorContract.RetailersEntry.COLUMN_RETAILER_EDITED, 0);


            String selection = DistributorContract.RetailersEntry._ID +" = " + retailerId;
            String[] selectionArgs = null;

            int editedrows = mContext.getContentResolver().update(DistributorContract.RetailersEntry.UPDATE_URI, retailerValues,selection,selectionArgs);
            Log.w(LOG_TAG, "The updated edited retailer rows was " + editedrows);

        }

        if (lastRetailer == 1){
            afterEditedRetailerUpdate();
        }

    }

    public void afterRetailerUpdate(){
        getUnsyncedOrders();
    }

    public void afterOrderUpdate(){
        getRetailerData();
    }

    public void afterRetailerDownload(){
        getProductData();
    }

    public void afterProductDownload(){
        getOfferData();
    }

    public void afterOfferDownload(){
        getUnsyncedTracking();
    }

    public void afterTrackingUpdate(){
        getEditedRetailerData();
    }

    public void afterEditedRetailerUpdate(){

    }

}
