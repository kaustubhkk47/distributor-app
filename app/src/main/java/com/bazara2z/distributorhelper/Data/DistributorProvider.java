package com.bazara2z.distributorhelper.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.bazara2z.distributorhelper.Data.DistributorContract.*;

/**
 * Created by Maddy on 28-02-2016.
 */
public class DistributorProvider extends ContentProvider {

    public static final String LOG_TAG = "DistributorProvider";

    static final int CREATE_DB = 99;

    static final int USER_CHECK = 100;
    static final int USER_INSERT = 101;
    static final int USER_LOGIN_STATUS = 103;
    static final int USER_DELETE = 104;

    static final int RETAILER_INSERT = 200;
    static final int RETAILER_CHECK = 201;
    static final int RETAILER_UPDATE = 202;
    static final int RETAILER_BULK_INSERT = 203;
    static final int RETAILER_DELETE = 203;

    static final int PRODUCT_INSERT = 300;
    static final int PRODUCT_CHECK = 301;
    static final int PRODUCT_DISPLAY = 302;
    static final int PRODUCT_BULK_INSERT = 303;
    static final int PRODUCTS_WITH_QUANTITY = 304;
    static final int PRODUCTS_DELETE = 305;
    static final int PRODUCTS_ORDER_SUMMARY = 306;

    static final int PRODUCT_OFFER_INSERT = 400;
    static final int PRODUCT_OFFER_CHECK = 401;
    static final int PRODUCT_OFFER_VIEW_WITH_PRODUCTS = 402;
    static final int PRODUCT_OFFER_BULK_INSERT = 403;
    static final int PRODUCT_OFFER_DELETE = 404;

    static final int ORDER_OFFER_BULK_INSERT = 701;
    static final int ORDER_OFFER_CHECK = 700;

    static final int ORDER_CHECK = 500;
    static final int ORDER_INSERT = 501;
    static final int ORDER_DISPLAY = 502;
    static final int ORDER_UPDATE = 503;

    static final int ORDER_ITEM_BULK_INSERT = 600;
    static final int ORDER_ITEM_DELETE = 601;
    static final int ORDER_ITEM_CHECK = 602;

    static final int TRACKING_INSERT = 800;
    static final int TRACKING_CHECK = 801;
    static final int TRACKING_UPDATE = 802;
    static final int TRACKING_CHECK_WITH_RETAILERS = 803;

    static final int SYNC_INTIAL_INSERT = 900;
    static final int SYNC_EXIST_CHECK = 901;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DistributorContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DistributorContract.PATH_USER + "/" + UserEntry.INSERT, USER_INSERT);
        matcher.addURI(authority, DistributorContract.PATH_USER + "/" + UserEntry.CHECK, USER_CHECK);
        matcher.addURI(authority, DistributorContract.PATH_USER + "/" + UserEntry.LOGIN_STATUS, USER_LOGIN_STATUS);
        matcher.addURI(authority, DistributorContract.PATH_USER + "/" + UserEntry.DELETE, USER_DELETE);

        matcher.addURI(authority, DistributorContract.PATH_RETAILERS + "/" + RetailersEntry.INSERT, RETAILER_INSERT);
        matcher.addURI(authority, DistributorContract.PATH_RETAILERS + "/" + RetailersEntry.CHECK, RETAILER_CHECK);
        matcher.addURI(authority, DistributorContract.PATH_RETAILERS + "/" + RetailersEntry.UPDATE, RETAILER_UPDATE);
        matcher.addURI(authority, DistributorContract.PATH_RETAILERS + "/" + RetailersEntry.BULK_INSERT, RETAILER_BULK_INSERT);
        matcher.addURI(authority, DistributorContract.PATH_RETAILERS + "/" + RetailersEntry.DELETE, RETAILER_DELETE);

        matcher.addURI(authority, DistributorContract.PATH_PRODUCTS + "/" + ProductsEntry.INSERT, PRODUCT_INSERT);
        matcher.addURI(authority, DistributorContract.PATH_PRODUCTS + "/" + ProductsEntry.CHECK, PRODUCT_CHECK);
        matcher.addURI(authority, DistributorContract.PATH_PRODUCTS + "/" + ProductsEntry.DISPLAY, PRODUCT_DISPLAY);
        matcher.addURI(authority, DistributorContract.PATH_PRODUCTS + "/" + ProductsEntry.BULK_INSERT, PRODUCT_BULK_INSERT);
        matcher.addURI(authority, DistributorContract.PATH_PRODUCTS + "/" +
                ProductsEntry.PRODUCTS_WITH_QUANTITY, PRODUCTS_WITH_QUANTITY);
        matcher.addURI(authority, DistributorContract.PATH_PRODUCTS + "/" + ProductsEntry.DELETE, PRODUCTS_DELETE);
        matcher.addURI(authority, DistributorContract.PATH_PRODUCTS + "/" +
                ProductsEntry.PRODUCTS_ORDER_SUMMARY, PRODUCTS_ORDER_SUMMARY);

        matcher.addURI(authority, DistributorContract.PATH_PRODUCT_OFFERS + "/" + ProductOffersEntry.INSERT, PRODUCT_OFFER_INSERT);
        matcher.addURI(authority, DistributorContract.PATH_PRODUCT_OFFERS + "/" + ProductOffersEntry.CHECK, PRODUCT_OFFER_CHECK);
        matcher.addURI(authority, DistributorContract.PATH_PRODUCT_OFFERS + "/" +
                ProductOffersEntry.VIEW_WITH_PRODUCTS, PRODUCT_OFFER_VIEW_WITH_PRODUCTS);
        matcher.addURI(authority, DistributorContract.PATH_PRODUCT_OFFERS + "/" +
                ProductOffersEntry.BULK_INSERT, PRODUCT_OFFER_BULK_INSERT);
        matcher.addURI(authority, DistributorContract.PATH_PRODUCT_OFFERS + "/" + ProductOffersEntry.DELETE, PRODUCT_OFFER_DELETE);

        matcher.addURI(authority, DistributorContract.PATH_ORDER_OFFERS + "/" + OrderOffersEntry.CHECK, ORDER_OFFER_CHECK);
        matcher.addURI(authority, DistributorContract.PATH_ORDER_OFFERS + "/" + OrderOffersEntry.BULK_INSERT, ORDER_OFFER_BULK_INSERT);

        matcher.addURI(authority, DistributorContract.PATH_ORDERS + "/" + OrdersEntry.CHECK, ORDER_CHECK);
        matcher.addURI(authority, DistributorContract.PATH_ORDERS + "/" + OrdersEntry.INSERT, ORDER_INSERT);
        matcher.addURI(authority, DistributorContract.PATH_ORDERS + "/" + OrdersEntry.DISPLAY_ORDERS, ORDER_DISPLAY);
        matcher.addURI(authority, DistributorContract.PATH_ORDERS + "/" + OrdersEntry.UPDATE, ORDER_UPDATE);

        matcher.addURI(authority, DistributorContract.PATH_ORDER_ITEMS + "/" + OrderItemsEntry.BULK_INSERT, ORDER_ITEM_BULK_INSERT);
        matcher.addURI(authority, DistributorContract.PATH_ORDER_ITEMS + "/" + OrderItemsEntry.DELETE, ORDER_ITEM_DELETE);
        matcher.addURI(authority, DistributorContract.PATH_ORDER_ITEMS + "/" + OrderItemsEntry.CHECK, ORDER_ITEM_CHECK);

        matcher.addURI(authority, DistributorContract.PATH_TRACKING + "/" + TrackingEntry.INSERT, TRACKING_INSERT);
        matcher.addURI(authority, DistributorContract.PATH_TRACKING + "/" + TrackingEntry.CHECK, TRACKING_CHECK);
        matcher.addURI(authority, DistributorContract.PATH_TRACKING + "/" + TrackingEntry.UPDATE, TRACKING_UPDATE);
        matcher.addURI(authority, DistributorContract.PATH_TRACKING + "/" +
                TrackingEntry.CHECK_WITH_RETAILERS, TRACKING_CHECK_WITH_RETAILERS);

        matcher.addURI(authority, DistributorContract.PATH_SYNC_STATES + "/" + SyncStatesEntry.BULK_INSERT, SYNC_INTIAL_INSERT);
        matcher.addURI(authority, DistributorContract.PATH_SYNC_STATES + "/" + SyncStatesEntry.CHECK, SYNC_EXIST_CHECK);

        return matcher;
    }

    private DistributorDBHelper mOpenHelper;


    @Override
    public boolean onCreate() {

        mOpenHelper = new DistributorDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case USER_CHECK:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(UserEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case RETAILER_CHECK:{
                retCursor = mOpenHelper.getReadableDatabase().query(RetailersEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case PRODUCT_CHECK:{
                retCursor = mOpenHelper.getReadableDatabase().query(ProductsEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case PRODUCT_OFFER_CHECK:{
                retCursor = mOpenHelper.getReadableDatabase().query(ProductOffersEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case ORDER_OFFER_CHECK:{
                retCursor = mOpenHelper.getReadableDatabase().query(OrderOffersEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case PRODUCT_OFFER_VIEW_WITH_PRODUCTS:{
                final SQLiteQueryBuilder orderDisplayQueryBuilder = new SQLiteQueryBuilder();
                orderDisplayQueryBuilder.setTables(ProductOffersEntry.TABLE_NAME + " LEFT JOIN " +
                        ProductsEntry.TABLE_NAME + " ON " + ProductOffersEntry.TABLE_NAME  + "." +
                        ProductOffersEntry.COLUMN_PRODUCT_ID + " = " + ProductsEntry.TABLE_NAME + "." +
                        ProductsEntry.COLUMN_PRODUCT_ID);
                retCursor = orderDisplayQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case PRODUCT_DISPLAY:{
                retCursor = mOpenHelper.getReadableDatabase().query(ProductsEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case ORDER_CHECK:{
                retCursor = mOpenHelper.getReadableDatabase().query(OrdersEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case ORDER_ITEM_CHECK:{
                retCursor = mOpenHelper.getReadableDatabase().query(OrderItemsEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case ORDER_DISPLAY:{
                final SQLiteQueryBuilder orderDisplayQueryBuilder = new SQLiteQueryBuilder();
                orderDisplayQueryBuilder.setTables(OrdersEntry.TABLE_NAME + " LEFT JOIN " +
                        RetailersEntry.TABLE_NAME + " ON " + OrdersEntry.TABLE_NAME  + "." +
                        OrdersEntry.COLUMN_RETAILER_ID + " = " + RetailersEntry.TABLE_NAME + "." +
                        RetailersEntry._ID);
                retCursor = orderDisplayQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case PRODUCTS_WITH_QUANTITY:{
                String TEMP_TABLE = "temp_table";
                int orderId = Integer.valueOf(selection);

                String QUERY = "SELECT " + ProductsEntry.TABLE_NAME + "." + ProductsEntry.COLUMN_PRODUCT_ID
                        + ", " + ProductsEntry.TABLE_NAME + "." + ProductsEntry.COLUMN_PRODUCT_NAME
                        + ", " + ProductsEntry.TABLE_NAME + "." + ProductsEntry.COLUMN_PRICE_PER_UNIT
                        + ", " + TEMP_TABLE + "." + OrderItemsEntry.COLUMN_QUANTITY
                        + ", " + TEMP_TABLE + "." + OrderItemsEntry.COLUMN_OFFERS_APPLIED
                        + ", " + TEMP_TABLE + "." + OrderItemsEntry.COLUMN_TOTAL_PRICE
                        + ", " + TEMP_TABLE + "." + OrderItemsEntry.COLUMN_EDITED_PRICE
                        + " FROM " + ProductsEntry.TABLE_NAME
                        + " LEFT JOIN " + " ( "
                        + " SELECT " + OrderItemsEntry.TABLE_NAME + "." + OrderItemsEntry.COLUMN_QUANTITY
                        + ", " + OrderItemsEntry.TABLE_NAME + "." + OrderItemsEntry.COLUMN_OFFERS_APPLIED
                        + ", " + OrderItemsEntry.TABLE_NAME + "." + OrderItemsEntry.COLUMN_TOTAL_PRICE
                        + ", " + OrderItemsEntry.TABLE_NAME + "." + OrderItemsEntry.COLUMN_EDITED_PRICE
                        + ", " + OrderItemsEntry.TABLE_NAME + "." + OrderItemsEntry.COLUMN_PRODUCT_ID
                        + " FROM " + OrderItemsEntry.TABLE_NAME
                        + " WHERE " + OrderItemsEntry.TABLE_NAME + "." + OrderItemsEntry.COLUMN_ORDER_ID
                        + " = " + orderId + " ) "
                        + " AS " + TEMP_TABLE + " ON "
                        + ProductsEntry.TABLE_NAME + "." + ProductsEntry.COLUMN_PRODUCT_ID + " = "
                        + TEMP_TABLE + "." + ProductsEntry.COLUMN_PRODUCT_ID + ";";

                retCursor = db.rawQuery(QUERY,null);
                break;
            }
            case PRODUCTS_ORDER_SUMMARY:{
                String TEMP_TABLE = "temp_table";
                int orderId = Integer.valueOf(selection);

                String QUERY = "SELECT " + ProductsEntry.TABLE_NAME + "." + ProductsEntry.COLUMN_PRODUCT_ID
                        + ", " + ProductsEntry.TABLE_NAME + "." + ProductsEntry.COLUMN_PRODUCT_NAME
                        + ", " + ProductsEntry.TABLE_NAME + "." + ProductsEntry.COLUMN_PRICE_PER_UNIT
                        + ", " + TEMP_TABLE + "." + OrderItemsEntry.COLUMN_QUANTITY
                        + ", " + TEMP_TABLE + "." + OrderItemsEntry.COLUMN_OFFERS_APPLIED
                        + ", " + TEMP_TABLE + "." + OrderItemsEntry.COLUMN_TOTAL_PRICE
                        + ", " + TEMP_TABLE + "." + OrderItemsEntry.COLUMN_EDITED_PRICE
                        + " FROM " + ProductsEntry.TABLE_NAME
                        + " LEFT JOIN " + " ( "
                        + " SELECT " + OrderItemsEntry.TABLE_NAME + "." + OrderItemsEntry.COLUMN_QUANTITY
                        + ", " + OrderItemsEntry.TABLE_NAME + "." + OrderItemsEntry.COLUMN_OFFERS_APPLIED
                        + ", " + OrderItemsEntry.TABLE_NAME + "." + OrderItemsEntry.COLUMN_TOTAL_PRICE
                        + ", " + OrderItemsEntry.TABLE_NAME + "." + OrderItemsEntry.COLUMN_EDITED_PRICE
                        + ", " + OrderItemsEntry.TABLE_NAME + "." + OrderItemsEntry.COLUMN_PRODUCT_ID
                        + " FROM " + OrderItemsEntry.TABLE_NAME
                        + " WHERE " + OrderItemsEntry.TABLE_NAME + "." + OrderItemsEntry.COLUMN_ORDER_ID
                        + " = " + orderId + " ) "
                        + " AS " + TEMP_TABLE + " ON "
                        + ProductsEntry.TABLE_NAME + "." + ProductsEntry.COLUMN_PRODUCT_ID + " = "
                        + TEMP_TABLE + "." + ProductsEntry.COLUMN_PRODUCT_ID + ";";

                retCursor = db.rawQuery(QUERY,null);
                break;
            }
            case SYNC_EXIST_CHECK:{
                retCursor = mOpenHelper.getReadableDatabase().query(SyncStatesEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case TRACKING_CHECK:{
                retCursor = mOpenHelper.getReadableDatabase().query(TrackingEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case TRACKING_CHECK_WITH_RETAILERS:{
                final SQLiteQueryBuilder checkWithRetailersQueryBuilder = new SQLiteQueryBuilder();
                checkWithRetailersQueryBuilder.setTables(TrackingEntry.TABLE_NAME + " LEFT JOIN " +
                        RetailersEntry.TABLE_NAME + " ON " + TrackingEntry.TABLE_NAME  + "." +
                        TrackingEntry.COLUMN_RETAILER_ID + " = " + RetailersEntry.TABLE_NAME + "." +
                        RetailersEntry._ID);
                retCursor = checkWithRetailersQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        Log.w(LOG_TAG,"Uri is " + uri.toString());
        switch (match){
            case USER_INSERT:{
                long _id = db.insert(UserEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = UserEntry.buildUserUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case RETAILER_INSERT:{
                long _id = db.insert(RetailersEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = RetailersEntry.buildRetailerrUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PRODUCT_INSERT:{
                long _id = db.insert(ProductsEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ProductsEntry.buildProductUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PRODUCT_OFFER_INSERT:{
                long _id = db.insert(ProductOffersEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ProductOffersEntry.buildOfferUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case ORDER_INSERT:{
                long _id = db.insert(OrdersEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = OrdersEntry.buildOrderUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRACKING_INSERT:{
                long _id = db.insert(TrackingEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TrackingEntry.buildTrackingUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case USER_LOGIN_STATUS:
                rowsUpdated = db.update(UserEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case RETAILER_UPDATE:{
                rowsUpdated = db.update(RetailersEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            case ORDER_UPDATE:{
                rowsUpdated = db.update(OrdersEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            case TRACKING_UPDATE:{
                rowsUpdated = db.update(TrackingEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch (match) {
            case USER_DELETE:
                rowsDeleted = db.delete(UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case RETAILER_DELETE:
                rowsDeleted = db.delete(RetailersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCTS_DELETE:
                rowsDeleted = db.delete(ProductsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_OFFER_DELETE:
                rowsDeleted = db.delete(ProductOffersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ORDER_ITEM_DELETE:
                rowsDeleted = db.delete(OrderItemsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT_BULK_INSERT:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ProductsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case RETAILER_BULK_INSERT:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(RetailersEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case PRODUCT_OFFER_BULK_INSERT:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ProductOffersEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case ORDER_OFFER_BULK_INSERT:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(OrderOffersEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case ORDER_ITEM_BULK_INSERT:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(OrderItemsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case SYNC_INTIAL_INSERT:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(SyncStatesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }
}