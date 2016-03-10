package com.bazara2z.distributorhelper.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Maddy on 27-02-2016.
 */
public class DistributorContract {

    public static final String CONTENT_AUTHORITY = "com.bazara2z.distributorhelper";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_USER = "user";
    public static final String PATH_RETAILERS = "retailers";
    public static final String PATH_PRODUCTS = "products";
    public static final String PATH_OFFERS = "offers";
    public static final String PATH_ORDERS = "orders";
    public static final String PATH_ORDER_ITEMS = "order_items";
    public static final String PATH_TRACKING = "tracking";
    public static final String PATH_SYNC_STATES = "sync_states";

    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String INSERT = "insert";
        public static final String CHECK = "check";
        public static final String LOGIN_STATUS = "login_status";
        public static final String DELETE = "delete";


        public static final Uri INSERT_URI = CONTENT_URI.buildUpon().appendPath(INSERT).build();
        public static final Uri CHECK_URI = CONTENT_URI.buildUpon().appendPath(CHECK).build();
        public static final Uri LOGIN_STATUS_URI = CONTENT_URI.buildUpon().appendPath(LOGIN_STATUS).build();
        public static final Uri DELETE_URI = CONTENT_URI.buildUpon().appendPath(DELETE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        // Table name
        public static final String TABLE_NAME = "user";

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_PHONE_NUMBER = "phone_number";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_LOGIN_STATUS = "login_status";

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class RetailersEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RETAILERS).build();

        public static final String INSERT = "insert";
        public static final String CHECK = "check";
        public static final String UPDATE_LOCATION = "update_location";
        public static final String BULK_INSERT = "bulk_insert";

        public static final Uri INSERT_URI = CONTENT_URI.buildUpon().appendPath(INSERT).build();
        public static final Uri CHECK_URI = CONTENT_URI.buildUpon().appendPath(CHECK).build();
        public static final Uri UPDATE_LOCATION_URI = CONTENT_URI.buildUpon().appendPath(UPDATE_LOCATION).build();
        public static final Uri BULK_INSERT_URI = CONTENT_URI.buildUpon().appendPath(BULK_INSERT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RETAILERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RETAILERS;

        public static final String TABLE_NAME = "retailers";

        public static final String COLUMN_RETAILER_ID = "retailer_id";
        public static final String COLUMN_SHOP_NAME = "shop_name";
        public static final String COLUMN_ADDRESS_LINE1 = "address_line1";
        public static final String COLUMN_ADDRESS_LINE2 = "address_line2";
        public static final String COLUMN_PINCODE = "pincode";
        public static final String COLUMN_PHONE_NUMBER = "phone_number";
        public static final String COLUMN_LOCATION_PRESENT = "location_present";
        public static final String COLUMN_RETAILER_LATITUDE = "retailer_latitude";
        public static final String COLUMN_RETAILER_LONGITUDE = "retailer_longitude";
        public static final String COLUMN_UPLOAD_SYNC_STATUS = "upload_sync_status";

        public static Uri buildRetailerrUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class ProductsEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRODUCTS).build();

        public static final String INSERT = "insert";
        public static final String CHECK = "check";
        public static final String DISPLAY = "display";
        public static final String BULK_INSERT = "bulk_insert";
        public static final String PRODUCTS_WITH_QUANTITY = "products_with_uantity";

        public static final Uri INSERT_URI = CONTENT_URI.buildUpon().appendPath(INSERT).build();
        public static final Uri CHECK_URI = CONTENT_URI.buildUpon().appendPath(CHECK).build();
        public static final Uri DISPLAY_URI = CONTENT_URI.buildUpon().appendPath(DISPLAY).build();
        public static final Uri BULK_INSERT_URI = CONTENT_URI.buildUpon().appendPath(BULK_INSERT).build();
        public static final Uri PRODUCTS_WITH_QUANTITY_URI = CONTENT_URI.buildUpon().appendPath(PRODUCTS_WITH_QUANTITY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public static final String TABLE_NAME = "products";

        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_PRICE_PER_UNIT = "price_per_unit";


        public static Uri buildProductUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class OffersEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_OFFERS).build();

        public static final String INSERT = "insert";
        public static final String CHECK = "check";
        public static final String VIEW_WITH_PRODUCTS = "view_with_products";
        public static final String BULK_INSERT = "bulk_insert";

        public static final Uri INSERT_URI = CONTENT_URI.buildUpon().appendPath(INSERT).build();
        public static final Uri CHECK_URI = CONTENT_URI.buildUpon().appendPath(CHECK).build();
        public static final Uri VIEW_WITH_PRODUCTS_URI = CONTENT_URI.buildUpon().appendPath(VIEW_WITH_PRODUCTS).build();
        public static final Uri BULK_INSERT_URI = CONTENT_URI.buildUpon().appendPath(BULK_INSERT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OFFERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OFFERS;

        public static final String TABLE_NAME = "offers";

        public static final String COLUMN_OFFER_ID = "offer_id";
        public static final String COLUMN_OFFER_DETAILS = "offer_details";
        public static final String COLUMN_PRODUCT_ID = "product_id";

        public static Uri buildOfferUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class OrdersEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ORDERS).build();

        public static final String CHECK = "check";
        public static final String INSERT = "insert";
        public static final String DISPLAY_ORDERS = "display_orders";
        public static final String UPDATE = "update";

        public static final Uri CHECK_URI = CONTENT_URI.buildUpon().appendPath(CHECK).build();
        public static final Uri INSERT_URI = CONTENT_URI.buildUpon().appendPath(INSERT).build();
        public static final Uri DISPLAY_ORDERS_URI = CONTENT_URI.buildUpon().appendPath(DISPLAY_ORDERS).build();
        public static final Uri UPDATE_URI = CONTENT_URI.buildUpon().appendPath(UPDATE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ORDERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ORDERS;

        public static final String TABLE_NAME = "orders";
        public static final String COLUMN_PRODUCT_COUNT = "product_count";
        public static final String COLUMN_TOTAL_PRICE = "total_price";
        public static final String COLUMN_MODIFIED_PRICE = "modified_price";
        public static final String COLUMN_UPLOAD_SYNC_STATUS = "upload_sync_status";
        public static final String COLUMN_RETAILER_ID = "retailer_id";
        public static final String COLUMN_ORDER_CREATION_TIME = "order_creation_time";
        public static final String COLUMN_ORDER_UPDATION_TIME = "order_updation_time";

        public static Uri buildOrderUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class OrderItemsEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ORDER_ITEMS).build();

        public static final String INSERT = "insert";
        public static final String BULK_INSERT = "bulk_insert";
        public static final String DELETE = "delete";


        public static final Uri INSERT_URI = CONTENT_URI.buildUpon().appendPath(INSERT).build();
        public static final Uri BULK_INSERT_URI = CONTENT_URI.buildUpon().appendPath(BULK_INSERT).build();
        public static final Uri DELETE_URI = CONTENT_URI.buildUpon().appendPath(DELETE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ORDER_ITEMS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ORDER_ITEMS;

        public static final String TABLE_NAME = "orderItems";


        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_ORDER_ID = "order_id";

    }

    public static final class TrackingEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRACKING).build();

        public static final String INSERT = "insert";

        public static final Uri INSERT_URI = CONTENT_URI.buildUpon().appendPath(INSERT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRACKING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRACKING;

        public static final String TABLE_NAME = "tracking";

        public static final String COLUMN_TRACKED_TIME = "tracked_time";
        public static final String COLUMN_TRACKED_LATITUDE = "tracked_latitude";
        public static final String COLUMN_TRACKED_LONGITUDE = "tracked_longitude";
        public static final String COLUMN_RETAILER_ID = "retailer_id";

        public static Uri buildTrackingUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

    public static final class SyncStatesEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SYNC_STATES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SYNC_STATES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SYNC_STATES;

        public static final String TABLE_NAME = "sync_states";

        public static final String COLUMN_SYNC_STATUS = "sync_status";
        public static final String COLUMN_LAST_SYNCED_TIME = "last_synced_time";


    }
}
