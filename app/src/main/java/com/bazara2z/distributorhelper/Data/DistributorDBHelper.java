package com.bazara2z.distributorhelper.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.bazara2z.distributorhelper.Data.DistributorContract.*;

/**
 * Created by Maddy on 28-02-2016.
 */
public class DistributorDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "distributor.db";

    public DistributorDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String TYPE_INTEGER = " INTEGER NOT NULL ";
    public static final String TYPE_INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY ";
    public static final String AUTO_INRCREMENT = " AUTOINCREMENT ";
    public static final String TYPE_TEXT = " TEXT NOT NULL ";
    public static final String TYPE_REAL = " REAL NOT NULL ";
    public static final String TYPE_UNIQUE = " UNIQUE ";
    public static final String IF_NOT_EXISTS = " IF NOT EXISTS ";
    public static final String COMMA_SEP = " , ";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + IF_NOT_EXISTS + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + TYPE_INTEGER_PRIMARY_KEY + COMMA_SEP +
                UserEntry.COLUMN_USER_ID + TYPE_INTEGER + COMMA_SEP +
                UserEntry.COLUMN_PHONE_NUMBER + TYPE_TEXT + COMMA_SEP +
                UserEntry.COLUMN_PASSWORD + TYPE_TEXT + COMMA_SEP +
                UserEntry.COLUMN_USER_NAME + TYPE_TEXT + COMMA_SEP +
                UserEntry.COLUMN_LOGIN_STATUS + TYPE_INTEGER +
                " );";

        final String SQL_CREATE_RETAILERS_TABLE = "CREATE TABLE "+ IF_NOT_EXISTS  + RetailersEntry.TABLE_NAME + " (" +
                RetailersEntry._ID + TYPE_INTEGER_PRIMARY_KEY + COMMA_SEP +
                RetailersEntry.COLUMN_RETAILER_ID + TYPE_INTEGER + TYPE_UNIQUE + COMMA_SEP +
                RetailersEntry.COLUMN_SHOP_NAME + TYPE_TEXT + COMMA_SEP +
                RetailersEntry.COLUMN_ADDRESS_LINE1 + TYPE_TEXT + COMMA_SEP +
                RetailersEntry.COLUMN_ADDRESS_LINE2 + TYPE_TEXT + COMMA_SEP +
                RetailersEntry.COLUMN_PHONE_NUMBER + TYPE_TEXT + COMMA_SEP +
                RetailersEntry.COLUMN_PINCODE + TYPE_TEXT + COMMA_SEP +
                RetailersEntry.COLUMN_LOCATION_PRESENT + TYPE_INTEGER + COMMA_SEP +
                RetailersEntry.COLUMN_RETAILER_LATITUDE + TYPE_REAL + COMMA_SEP +
                RetailersEntry.COLUMN_RETAILER_LONGITUDE + TYPE_REAL + COMMA_SEP +
                RetailersEntry.COLUMN_UPLOAD_SYNC_STATUS + TYPE_INTEGER +
                " );";

        final String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE "+ IF_NOT_EXISTS  + ProductsEntry.TABLE_NAME + " (" +
                ProductsEntry._ID + TYPE_INTEGER_PRIMARY_KEY + COMMA_SEP +
                ProductsEntry.COLUMN_PRODUCT_ID + TYPE_INTEGER + TYPE_UNIQUE + COMMA_SEP +
                ProductsEntry.COLUMN_PRODUCT_NAME + TYPE_TEXT + COMMA_SEP +
                ProductsEntry.COLUMN_PRICE_PER_UNIT + TYPE_REAL +
                " );";

        final String SQL_CREATE_OFFERS_TABLE = "CREATE TABLE " + IF_NOT_EXISTS + OffersEntry.TABLE_NAME + " (" +
                OffersEntry._ID + TYPE_INTEGER_PRIMARY_KEY + COMMA_SEP +
                OffersEntry.COLUMN_OFFER_ID + TYPE_INTEGER + COMMA_SEP +
                OffersEntry.COLUMN_OFFER_DETAILS + TYPE_TEXT + COMMA_SEP +
                OffersEntry.COLUMN_PRODUCT_ID + TYPE_INTEGER + COMMA_SEP +
                " FOREIGN KEY (" + OffersEntry.COLUMN_PRODUCT_ID + ") REFERENCES " +
                ProductsEntry.TABLE_NAME + " (" + ProductsEntry.COLUMN_PRODUCT_ID + ")" +
                " );";

        final String SQL_CREATE_ORDERS_TABLE = "CREATE TABLE "+ IF_NOT_EXISTS  + OrdersEntry.TABLE_NAME + " (" +
                OrdersEntry._ID + TYPE_INTEGER_PRIMARY_KEY + AUTO_INRCREMENT + COMMA_SEP +
                OrdersEntry.COLUMN_PRODUCT_COUNT + TYPE_INTEGER + COMMA_SEP +
                OrdersEntry.COLUMN_TOTAL_PRICE + TYPE_REAL + COMMA_SEP +
                OrdersEntry.COLUMN_MODIFIED_PRICE + TYPE_REAL + COMMA_SEP +
                OrdersEntry.COLUMN_UPLOAD_SYNC_STATUS + TYPE_INTEGER + COMMA_SEP +
                OrdersEntry.COLUMN_ORDER_CREATION_TIME + TYPE_INTEGER + COMMA_SEP +
                OrdersEntry.COLUMN_ORDER_UPDATION_TIME + TYPE_INTEGER + COMMA_SEP +
                OrdersEntry.COLUMN_RETAILER_ID + TYPE_INTEGER + COMMA_SEP +
                " FOREIGN KEY (" + OrdersEntry.COLUMN_RETAILER_ID + ") REFERENCES " +
                RetailersEntry.TABLE_NAME + " (" + RetailersEntry.COLUMN_RETAILER_ID + ")" +
                " );";

        final String SQL_CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE " + IF_NOT_EXISTS + OrderItemsEntry.TABLE_NAME + " (" +
                OrderItemsEntry._ID + TYPE_INTEGER_PRIMARY_KEY + AUTO_INRCREMENT + COMMA_SEP +
                OrderItemsEntry.COLUMN_QUANTITY + TYPE_REAL + COMMA_SEP +
                OrderItemsEntry.COLUMN_PRODUCT_ID + TYPE_INTEGER + COMMA_SEP +
                OrderItemsEntry.COLUMN_ORDER_ID + TYPE_INTEGER + COMMA_SEP +
                " FOREIGN KEY (" + OrderItemsEntry.COLUMN_PRODUCT_ID + ") REFERENCES " +
                ProductsEntry.TABLE_NAME + " (" + ProductsEntry.COLUMN_PRODUCT_ID + ")" + COMMA_SEP +
                " FOREIGN KEY (" + OrderItemsEntry.COLUMN_ORDER_ID + ") REFERENCES " +
                OrdersEntry.TABLE_NAME + " (" + OrdersEntry._ID + ")" +
                " );";

        final String SQL_CREATE_TRACKING_TABLE = "CREATE TABLE "+ IF_NOT_EXISTS  + TrackingEntry.TABLE_NAME + " (" +
                TrackingEntry._ID + TYPE_INTEGER_PRIMARY_KEY + AUTO_INRCREMENT + COMMA_SEP +
                TrackingEntry.COLUMN_TRACKED_TIME + TYPE_INTEGER + COMMA_SEP +
                TrackingEntry.COLUMN_TRACKED_LATITUDE + TYPE_REAL + COMMA_SEP +
                TrackingEntry.COLUMN_TRACKED_LONGITUDE + TYPE_REAL + COMMA_SEP +
                TrackingEntry.COLUMN_RETAILER_ID + TYPE_INTEGER + COMMA_SEP +
                " FOREIGN KEY (" + TrackingEntry.COLUMN_RETAILER_ID + ") REFERENCES " +
                RetailersEntry.TABLE_NAME + " (" + RetailersEntry.COLUMN_RETAILER_ID + ")" +
                " );";

        final String SQL_CREATE_SYNC_STATES_TABLE = "CREATE TABLE "+ IF_NOT_EXISTS  + SyncStatesEntry.TABLE_NAME + " (" +
                SyncStatesEntry._ID + TYPE_INTEGER_PRIMARY_KEY + COMMA_SEP +
                SyncStatesEntry.COLUMN_SYNC_STATUS + TYPE_INTEGER + COMMA_SEP +
                SyncStatesEntry.COLUMN_LAST_SYNCED_TIME + TYPE_INTEGER +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RETAILERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_OFFERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ORDERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ORDER_ITEMS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRACKING_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SYNC_STATES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RetailersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProductsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OffersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrdersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrderItemsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrackingEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SyncStatesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
