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
import com.bazara2z.distributorhelper.OrderSummaryPackage.OrderSummaryFragment;
import com.bazara2z.distributorhelper.OrderSummaryPackage.OrderSummaryModel;
import com.bazara2z.distributorhelper.R;

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

        if (newOrder == 1) {
            buildOrderModelList = getAllProducts();
        }
        else {
            //TODO: get build Order Model List
            buildOrderModelList = getAllProducts();
        }

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

        Context mContext = getApplicationContext();

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
    }

    public void refreshOrderSummaryModel(){
        int productCount = 0;
        double totalPrice = 0.0;

        int listSize = buildOrderModelList.size();

        BuildOrderModel buildOrderModel;

        orderSummaryModelList.clear();

        for (int i = 0; i < listSize; i++){
            buildOrderModel = buildOrderModelList.get(i);
            if(buildOrderModel.getQuantity() > 0){
                productCount = productCount + 1;
                totalPrice = totalPrice + buildOrderModel.getTotalPrice();
                orderSummaryModelList.add(buildOrderModel);
            }
        }

        orderSummaryModel.setProductCount(productCount);
        orderSummaryModel.setTotalPrice(totalPrice);
        orderSummaryModel.setModifiedPrice(totalPrice);
        /*
        Log.w(LOG_TAG, "Product count refreshed to " + productCount);
        Log.w(LOG_TAG, "Total price refreshed to " + totalPrice);
        */
        //Log.w(LOG_TAG, "Size of OrderSummaryModelList is " + orderSummaryModelList.size());
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
        String selection = RetailersEntry.COLUMN_RETAILER_ID +" = " + retailerId;

        String retailerName;

        Cursor cursor = getApplicationContext().getContentResolver().query(RetailersEntry.CHECK_URI, columns, selection, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            retailerName = cursor.getString(cursor.getColumnIndex(RetailersEntry.COLUMN_SHOP_NAME));
            setTitle(retailerName);
        }
    }
}
