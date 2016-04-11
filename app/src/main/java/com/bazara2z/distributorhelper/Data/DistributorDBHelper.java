package com.bazara2z.distributorhelper.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.bazara2z.distributorhelper.Data.DistributorContract.*;

/**
 * Created by Maddy on 10-04-2016.
 */
public class DistributorDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "distributor_helper.db";

    public DistributorDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String TYPE_INTEGER = " INTEGER ";
    public static final String TYPE_INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY ";
    public static final String AUTO_INCREMENT = " AUTOINCREMENT ";
    public static final String TYPE_TEXT = " TEXT ";
    public static final String TYPE_REAL = " REAL ";
    public static final String TYPE_UNIQUE = " UNIQUE ";
    public static final String IF_NOT_EXISTS = " IF NOT EXISTS ";
    public static final String COMMA_SEP = " , ";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + IF_NOT_EXISTS + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + TYPE_INTEGER_PRIMARY_KEY + COMMA_SEP +
                UserEntry.COLUMN_PHONE_NUMBER + TYPE_TEXT + COMMA_SEP +
                UserEntry.COLUMN_PASSWORD + TYPE_TEXT + COMMA_SEP +
                UserEntry.COLUMN_USER_NAME + TYPE_TEXT + COMMA_SEP +
                UserEntry.COLUMN_LOGIN_STATUS + TYPE_INTEGER + COMMA_SEP +
                UserEntry.COLUMN_TOKEN + TYPE_TEXT +
                " );";

        final String SQL_CREATE_RETAILERS_TABLE = "CREATE TABLE "+ IF_NOT_EXISTS  + RetailersEntry.TABLE_NAME + " (" +
                RetailersEntry._ID + TYPE_INTEGER_PRIMARY_KEY + COMMA_SEP +
                RetailersEntry.COLUMN_RETAILER_ID + TYPE_INTEGER + TYPE_UNIQUE + COMMA_SEP +
                RetailersEntry.COLUMN_SHOP_NAME + TYPE_TEXT + COMMA_SEP +
                RetailersEntry.COLUMN_FIRST_NAME + TYPE_TEXT + COMMA_SEP +
                RetailersEntry.COLUMN_ADDRESS_LINE_1 + TYPE_TEXT + COMMA_SEP +
                RetailersEntry.COLUMN_ADDRESS_LINE_2 + TYPE_TEXT + COMMA_SEP +
                RetailersEntry.COLUMN_LANDMARK + TYPE_TEXT + COMMA_SEP +
                RetailersEntry.COLUMN_PINCODE + TYPE_TEXT + COMMA_SEP +
                RetailersEntry.COLUMN_PHONE_NUMBER + TYPE_TEXT + COMMA_SEP +
                RetailersEntry.COLUMN_LOCATION_PRESENT + TYPE_INTEGER + COMMA_SEP +
                RetailersEntry.COLUMN_RETAILER_LATITUDE + TYPE_REAL + COMMA_SEP +
                RetailersEntry.COLUMN_RETAILER_EDITED + TYPE_INTEGER + COMMA_SEP +
                RetailersEntry.COLUMN_RETAILER_LONGITUDE + TYPE_REAL + COMMA_SEP +
                RetailersEntry.COLUMN_UPLOAD_SYNC_STATUS + TYPE_INTEGER +
                " );";

        final String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE "+ IF_NOT_EXISTS  + ProductsEntry.TABLE_NAME + " (" +
                ProductsEntry._ID + TYPE_INTEGER_PRIMARY_KEY + COMMA_SEP +
                ProductsEntry.COLUMN_PRODUCT_ID + TYPE_INTEGER + TYPE_UNIQUE + COMMA_SEP +
                ProductsEntry.COLUMN_PRODUCT_NAME + TYPE_TEXT + COMMA_SEP +
                ProductsEntry.COLUMN_PRICE_PER_UNIT + TYPE_REAL +
                " );";

        final String SQL_CREATE_ORDER_OFFERS_TABLE = "CREATE TABLE " + IF_NOT_EXISTS + OrderOffersEntry.TABLE_NAME + " (" +
                OrderOffersEntry._ID + TYPE_INTEGER_PRIMARY_KEY + COMMA_SEP +
                OrderOffersEntry.COLUMN_OFFER_ID + TYPE_INTEGER + COMMA_SEP +
                OrderOffersEntry.COLUMN_OFFER_NAME + TYPE_TEXT + COMMA_SEP +
                OrderOffersEntry.COLUMN_DISCOUNT_PERCENT + TYPE_REAL +
                " );";

        final String SQL_CREATE_PRODUCT_OFFERS_TABLE = "CREATE TABLE " + IF_NOT_EXISTS + ProductOffersEntry.TABLE_NAME + " (" +
                ProductOffersEntry._ID + TYPE_INTEGER_PRIMARY_KEY + COMMA_SEP +
                ProductOffersEntry.COLUMN_OFFER_ID + TYPE_INTEGER + COMMA_SEP +
                ProductOffersEntry.COLUMN_OFFER_TYPE_NAME + TYPE_TEXT + COMMA_SEP +
                ProductOffersEntry.COLUMN_OFFER_TYPE + TYPE_INTEGER + COMMA_SEP +
                ProductOffersEntry.COLUMN_PRODUCT_ID + TYPE_INTEGER + COMMA_SEP +
                ProductOffersEntry.COLUMN_MINIMUM_ORDER_QUANTITY + TYPE_INTEGER + COMMA_SEP +
                ProductOffersEntry.COLUMN_DISCOUNT_PERCENT + TYPE_REAL + COMMA_SEP +
                ProductOffersEntry.COLUMN_X_COUNT + TYPE_INTEGER + COMMA_SEP +
                ProductOffersEntry.COLUMN_Y_COUNT + TYPE_INTEGER + COMMA_SEP +
                ProductOffersEntry.COLUMN_Y_NAME + TYPE_TEXT +
                " );";

        final String SQL_CREATE_ORDERS_TABLE = "CREATE TABLE "+ IF_NOT_EXISTS  + OrdersEntry.TABLE_NAME + " (" +
                OrdersEntry._ID + TYPE_INTEGER_PRIMARY_KEY + AUTO_INCREMENT + COMMA_SEP +
                OrdersEntry.COLUMN_ORDER_ID + TYPE_INTEGER + COMMA_SEP +
                OrdersEntry.COLUMN_PRODUCT_COUNT + TYPE_INTEGER + COMMA_SEP +
                OrdersEntry.COLUMN_TOTAL_PRICE + TYPE_REAL + COMMA_SEP +
                OrdersEntry.COLUMN_MODIFIED_PRICE + TYPE_REAL + COMMA_SEP +
                OrdersEntry.COLUMN_UPLOAD_SYNC_STATUS + TYPE_INTEGER + COMMA_SEP +
                OrdersEntry.COLUMN_ORDER_OFFER_APPLIED + TYPE_INTEGER + COMMA_SEP +
                OrdersEntry.COLUMN_ORDER_OFFER_ID + TYPE_INTEGER + COMMA_SEP +
                OrdersEntry.COLUMN_ORDER_CREATION_TIME + TYPE_INTEGER + COMMA_SEP +
                OrdersEntry.COLUMN_ORDER_UPDATION_TIME + TYPE_INTEGER + COMMA_SEP +
                OrdersEntry.COLUMN_RETAILER_ID + TYPE_INTEGER +
                " );";

        final String SQL_CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE " + IF_NOT_EXISTS + OrderItemsEntry.TABLE_NAME + " (" +
                OrderItemsEntry._ID + TYPE_INTEGER_PRIMARY_KEY + AUTO_INCREMENT + COMMA_SEP +
                OrderItemsEntry.COLUMN_QUANTITY + TYPE_REAL + COMMA_SEP +
                OrderItemsEntry.COLUMN_PRODUCT_ID + TYPE_INTEGER + COMMA_SEP +
                OrderItemsEntry.COLUMN_OFFERS_APPLIED + TYPE_TEXT + COMMA_SEP +
                OrderItemsEntry.COLUMN_TOTAL_PRICE + TYPE_REAL + COMMA_SEP +
                OrderItemsEntry.COLUMN_EDITED_PRICE + TYPE_REAL + COMMA_SEP +
                OrderItemsEntry.COLUMN_FREE_UNITS + TYPE_INTEGER + COMMA_SEP +
                OrderItemsEntry.COLUMN_ORDER_ID + TYPE_INTEGER +
                " );";

        final String SQL_CREATE_TRACKING_TABLE = "CREATE TABLE "+ IF_NOT_EXISTS  + TrackingEntry.TABLE_NAME + " (" +
                TrackingEntry._ID + TYPE_INTEGER_PRIMARY_KEY + AUTO_INCREMENT + COMMA_SEP +
                TrackingEntry.COLUMN_TRACKED_TIME + TYPE_INTEGER + COMMA_SEP +
                TrackingEntry.COLUMN_TRACKED_LATITUDE + TYPE_REAL + COMMA_SEP +
                TrackingEntry.COLUMN_TRACKED_LONGITUDE + TYPE_REAL + COMMA_SEP +
                TrackingEntry.COLUMN_UPLOAD_SYNC_STATUS + TYPE_INTEGER +  COMMA_SEP +
                TrackingEntry.COLUMN_RETAILER_ID + TYPE_INTEGER +
                " );";

        final String SQL_CREATE_SYNC_STATES_TABLE = "CREATE TABLE "+ IF_NOT_EXISTS  + DistributorContract.SyncStatesEntry.TABLE_NAME + " (" +
                DistributorContract.SyncStatesEntry._ID + TYPE_INTEGER_PRIMARY_KEY + COMMA_SEP +
                DistributorContract.SyncStatesEntry.COLUMN_SYNC_ENTRY + TYPE_INTEGER + COMMA_SEP +
                DistributorContract.SyncStatesEntry.COLUMN_SYNC_STATUS + TYPE_INTEGER + COMMA_SEP +
                DistributorContract.SyncStatesEntry.COLUMN_LAST_SYNCED_TIME + TYPE_INTEGER +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RETAILERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ORDER_OFFERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCT_OFFERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ORDERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ORDER_ITEMS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRACKING_TABLE);
        //sqLiteDatabase.execSQL(SQL_CREATE_SYNC_STATES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RetailersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProductsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrderOffersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProductOffersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrdersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrderItemsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrackingEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SyncStatesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
