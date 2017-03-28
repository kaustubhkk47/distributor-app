package com.bazara2z.distributorhelper.BuildOrderPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bazara2z.distributorhelper.Data.DistributorContract.*;
import com.bazara2z.distributorhelper.MainActivityPackage.MainActivity;
import com.bazara2z.distributorhelper.OffersPackage.OffersFragment;
import com.bazara2z.distributorhelper.OffersPackage.ProductOffersModel;
import com.bazara2z.distributorhelper.OrderSummaryPackage.OrderSummaryFragment;
import com.bazara2z.distributorhelper.OrderSummaryPackage.OrderSummaryModel;
import com.bazara2z.distributorhelper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuildOrder extends AppCompatActivity implements ActionBar.TabListener, OffersFragment.OnFragmentInteractionListener,
        BuildOrderFragment.OnFragmentInteractionListener, OrderSummaryFragment.OnFragmentInteractionListener {

    private final String LOG_TAG = "BuildOrderActivity";

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    int retailerId;

    int newOrder = 1;
    int orderId = 0;

    Context mContext;

    OrderSummaryModel orderSummaryModel;

    List<BuildOrderModel> buildOrderModelList;
    ArrayList<BuildOrderModel> orderSummaryModelList = new ArrayList<BuildOrderModel>();

    BuildOrderFragment buildOrderFragment;
    OffersFragment offersFragment;
    OrderSummaryFragment orderSummaryFragment;

    public void onFragmentInteraction(String position){

    }

    @Override
    public void onOrderSummaryFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            retailerId = extras.getInt(RetailersEntry.COLUMN_RETAILER_ID);
            orderId = extras.getInt(OrdersEntry._ID);
            newOrder = extras.getInt("NewOrder");
        }

        setActivityTitle();

        buildOrderModelList = getAllProducts();

        populateOrderSummaryModel();

        refreshOrderSummaryModel();

        setContentView(R.layout.activity_build_order);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 2){
                    refreshOrderSummaryModel();
                    orderSummaryFragment.refreshViews();
                    //Log.w(LOG_TAG, "Went to tab 2");
                }
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        buildOrderFragment = new BuildOrderFragment(buildOrderModelList);
        offersFragment = new OffersFragment();
        orderSummaryFragment = new OrderSummaryFragment(orderSummaryModelList, orderSummaryModel);

        mViewPager.setCurrentItem(1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_build_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    return offersFragment;
                case 1:
                    return buildOrderFragment;
                case 2:
                    return orderSummaryFragment;

            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.offers_tab).toUpperCase(l);
                case 1:
                    return getString(R.string.products_tab).toUpperCase(l);
                case 2:
                    return getString(R.string.summary_tab).toUpperCase(l);
            }
            return null;
        }
    }

    public List<BuildOrderModel> getAllProducts()
    {
        ArrayList<BuildOrderModel> mBuildOrderModelList = new ArrayList<BuildOrderModel>();

        //Context mContext = getApplicationContext();

        Cursor cursor;

        if (newOrder == 1){

            String[] columns = {ProductsEntry.COLUMN_PRODUCT_NAME, ProductsEntry.COLUMN_PRICE_PER_UNIT,
                    ProductsEntry.COLUMN_PRODUCT_ID};
            cursor = mContext.getContentResolver().query(ProductsEntry.DISPLAY_URI, columns, null, null, null);
        }
        else {
            String selection = String.valueOf(orderId);
            cursor = mContext.getContentResolver().query(ProductsEntry.PRODUCTS_WITH_QUANTITY_URI, null, selection, null, null);
        }
        BuildOrderModel buildOrderModel;

        Log.w(LOG_TAG, "The number of products fetched is " + String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                buildOrderModel = new BuildOrderModel();

                Double pricePerUnit = cursor.getDouble(cursor.getColumnIndex(ProductsEntry.COLUMN_PRICE_PER_UNIT));

                buildOrderModel.setId(i);
                buildOrderModel.setProductName(cursor.getString(cursor.getColumnIndex(ProductsEntry.COLUMN_PRODUCT_NAME)));

                //---------------------------------------------------------------------------------------------

                ArrayList<ProductOffersModel> mProductOffersModelList = new ArrayList<ProductOffersModel>();

                String[] columns1 = {ProductOffersEntry.COLUMN_OFFER_ID, ProductOffersEntry.COLUMN_OFFER_TYPE_NAME,
                        ProductOffersEntry.COLUMN_OFFER_TYPE,ProductOffersEntry.COLUMN_PRODUCT_ID,
                        ProductOffersEntry.COLUMN_MINIMUM_ORDER_QUANTITY,
                        ProductOffersEntry.COLUMN_DISCOUNT_PERCENT, ProductOffersEntry.COLUMN_X_COUNT,
                        ProductOffersEntry.COLUMN_Y_COUNT, ProductOffersEntry.COLUMN_Y_NAME};

                String selection = ProductOffersEntry.COLUMN_PRODUCT_ID + " = " +
                        cursor.getInt(cursor.getColumnIndex(ProductsEntry.COLUMN_PRODUCT_ID));

                Cursor cursor1 = mContext.getContentResolver().query(ProductOffersEntry.CHECK_URI, columns1, selection, null, null);

                ProductOffersModel productOffersModel;

                ArrayList<Integer> offerIds = new ArrayList<>();

                if (newOrder == 0){
                    String offersAppliedString = cursor.getString(cursor.getColumnIndex(OrderItemsEntry.COLUMN_OFFERS_APPLIED));
                    if (offersAppliedString == null){
                        Log.w(LOG_TAG, "Null it is");
                    }
                    //Log.w(LOG_TAG, offersAppliedString);
                    try {
                        JSONObject offersAppliedJSON = new JSONObject(offersAppliedString);
                        JSONArray offersAppliedArray = offersAppliedJSON.getJSONArray(OrderItemsEntry.COLUMN_OFFERS_APPLIED);
                        for (int j = 0; j < offersAppliedArray.length(); j = j + 1){
                            offerIds.add(offersAppliedArray.getInt(j));
                        }
                    }
                    catch (JSONException e){
                        Log.w(LOG_TAG, e.toString());
                    }

                }

                if (cursor1.getCount() > 0){
                    for (int j = 0; j < cursor1.getCount(); j = j + 1) {
                        cursor1.moveToNext();

                        productOffersModel = new ProductOffersModel();
                        productOffersModel.setId(j);
                        productOffersModel.setOfferId(cursor1.getInt(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_OFFER_ID)));
                        productOffersModel.setOfferDetails(cursor1.getString(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_OFFER_TYPE_NAME)));
                        productOffersModel.setOfferType(cursor1.getInt(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_OFFER_TYPE)));
                        productOffersModel.setProductId(cursor1.getInt(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_PRODUCT_ID)));
                        //productOffersModel.setProductName(cursor1.getString(cursor1.getColumnIndex(ProductsEntry.COLUMN_PRODUCT_NAME)));
                        productOffersModel.setMinimumOrderQuantity(cursor1.getInt(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_MINIMUM_ORDER_QUANTITY)));
                        productOffersModel.setDiscountPercent(cursor1.getDouble(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_DISCOUNT_PERCENT)));
                        productOffersModel.setxCount(cursor1.getInt(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_X_COUNT)));
                        productOffersModel.setyCount(cursor1.getInt(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_Y_COUNT)));
                        productOffersModel.setyName(cursor1.getString(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_Y_NAME)));
                        productOffersModel.setOfferApplied(0);

                        if (newOrder == 0){
                            if (offerIds.contains(cursor1.getInt(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_OFFER_ID)))){
                                productOffersModel.setOfferApplied(1);
                            }
                        }

                        mProductOffersModelList.add(productOffersModel);
                    }
                }

                cursor1.close();

                //---------------------------------------------------------------------------------------------

                buildOrderModel.setPricePerUnit(pricePerUnit);
                if (newOrder == 1) {
                    buildOrderModel.setQuantity(0);
                    buildOrderModel.setTotalPrice(0.0);
                }
                else {
                    int quantity = cursor.getInt(cursor.getColumnIndex(OrderItemsEntry.COLUMN_QUANTITY));
                    if (quantity > 0){
                        buildOrderModel.setQuantity(quantity);
                        buildOrderModel.setTotalPrice(quantity*pricePerUnit);
                    }
                    else {
                        buildOrderModel.setQuantity(0);
                        buildOrderModel.setTotalPrice(0.0);
                    }
                }
                buildOrderModel.setProductId(cursor.getInt(cursor.getColumnIndex(ProductsEntry.COLUMN_PRODUCT_ID)));
                buildOrderModel.setProductOffers(mProductOffersModelList);

                mBuildOrderModelList.add(buildOrderModel);
            }
        }

        cursor.close();

        return mBuildOrderModelList;
    }



    public void populateOrderSummaryModel(){
        orderSummaryModel = new OrderSummaryModel();
        orderSummaryModel.setOrderId(orderId);
        orderSummaryModel.setRetailerId(retailerId);
        orderSummaryModel.setProductCount(0);
        orderSummaryModel.setModifiedPrice(0.0);
        orderSummaryModel.setTotalPrice(0.0);
        orderSummaryModel.setConfirmedStatus(0);
        orderSummaryModel.setIsNewOrder(newOrder);
        orderSummaryModel.setOrderOfferApplied(0);
        orderSummaryModel.setDiscountPercent(0.0);

        if (newOrder == 0){
            String[] columns = {OrdersEntry.COLUMN_ORDER_OFFER_APPLIED, OrdersEntry.COLUMN_ORDER_OFFER_ID};
            String selection = OrdersEntry._ID +" = " + orderId;

            Cursor cursor = getApplicationContext().getContentResolver().query(OrdersEntry.CHECK_URI, columns, selection, null, null);

            int orderOfferApplied = 0;
            int orderOfferId = 0;

            if (cursor.getCount() > 0) {
                cursor.moveToNext();

                orderOfferApplied = cursor.getInt(cursor.getColumnIndex(OrdersEntry.COLUMN_ORDER_OFFER_APPLIED));
                orderOfferId = cursor.getInt(cursor.getColumnIndex(OrdersEntry.COLUMN_ORDER_OFFER_APPLIED));

            }
            cursor.close();

            if (orderOfferApplied == 1){
                String[] columns1 = {OrderOffersEntry.COLUMN_DISCOUNT_PERCENT};
                String selection1 = OrderOffersEntry.COLUMN_OFFER_ID + " = " + orderOfferId;

                Cursor cursor1 = getApplicationContext().getContentResolver().query(OrderOffersEntry.CHECK_URI, columns1, selection1, null, null);

                double discountPercent = 0.0;

                if (cursor1.getCount() > 0) {
                    cursor1.moveToNext();

                    discountPercent = cursor1.getDouble(cursor1.getColumnIndex(OrderOffersEntry.COLUMN_DISCOUNT_PERCENT));

                }

                orderSummaryModel.setOrderOfferApplied(1);
                orderSummaryModel.setDiscountPercent(discountPercent);
                orderSummaryModel.setOrderOfferId(orderOfferId);
            }
        }
    }

    public void refreshOrderSummaryModel(){
        int productCount = 0;
        double totalPrice = 0.0;
        double editedPrice = 0.0;

        int listSize = buildOrderModelList.size();

        BuildOrderModel buildOrderModel;

        orderSummaryModelList.clear();

        for (int i = 0; i < listSize; i++){
            buildOrderModel = buildOrderModelList.get(i);
            if(buildOrderModel.getQuantity() > 0){
                productCount = productCount + 1;
                totalPrice = totalPrice + buildOrderModel.getTotalPrice();
                editedPrice = editedPrice + buildOrderModel.getEditedPrice();
                orderSummaryModelList.add(buildOrderModel);
            }
        }

        double discountPercent = orderSummaryModel.getDiscountPercent()/100;

        double discountedTotalPrice = totalPrice*(1-discountPercent);
        double discountedEditedPrice = editedPrice*(1-discountPercent);

        orderSummaryModel.setProductCount(productCount);
        orderSummaryModel.setTotalPrice(discountedEditedPrice);
        orderSummaryModel.setModifiedPrice(discountedEditedPrice);
    }

    @Override
    public void onBackPressed(){
        String title = "Order placement will be cancelled";
        String message = "Do you want to continue?";
        cancelOrderConfirmAlert(title, message);
    }

    public void cancelOrderConfirmAlert(String title, String message){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(title);

        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Clear data from temporary table
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();

    }

    public void setActivityTitle(){
        String[] columns = {RetailersEntry.COLUMN_SHOP_NAME};
        String selection = RetailersEntry._ID +" = " + retailerId;

        String retailerName;

        Cursor cursor = getApplicationContext().getContentResolver().query(RetailersEntry.CHECK_URI, columns, selection, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            retailerName = cursor.getString(cursor.getColumnIndex(RetailersEntry.COLUMN_SHOP_NAME));
            setTitle(retailerName);
        }
    }
}
