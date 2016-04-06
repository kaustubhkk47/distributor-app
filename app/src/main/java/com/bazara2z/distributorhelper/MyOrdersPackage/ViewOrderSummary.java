package com.bazara2z.distributorhelper.MyOrdersPackage;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bazara2z.distributorhelper.BuildOrderPackage.BuildOrderFragment;
import com.bazara2z.distributorhelper.BuildOrderPackage.BuildOrderModel;
import com.bazara2z.distributorhelper.Data.DistributorContract;
import com.bazara2z.distributorhelper.OffersPackage.OffersFragment;
import com.bazara2z.distributorhelper.OrderSummaryPackage.OrderSummaryAdapter;
import com.bazara2z.distributorhelper.OrderSummaryPackage.OrderSummaryFragment;
import com.bazara2z.distributorhelper.OrderSummaryPackage.OrderSummaryModel;
import com.bazara2z.distributorhelper.R;

import java.util.ArrayList;
import java.util.List;

public class ViewOrderSummary extends AppCompatActivity {

    private final String LOG_TAG = "ViewOrderSummaryActivity";

    int retailerId;
    int orderId = 0;

    Context mContext;

    OrderSummaryModel orderSummaryModel;
    OrderSummaryAdapter orderSummaryAdapter;

    ListView mListView;
    TextView totalPrice;
    TextView quantity;
    TextView modifiedPrice;

    List<BuildOrderModel> buildOrderModelList;
    ArrayList<BuildOrderModel> orderSummaryModelList = new ArrayList<BuildOrderModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            retailerId = extras.getInt(DistributorContract.RetailersEntry.COLUMN_RETAILER_ID);
            orderId = extras.getInt(DistributorContract.OrdersEntry._ID);
        }

        buildOrderModelList = getAllProducts();

        populateOrderSummaryModel();

        refreshOrderSummaryModel();

        setContentView(R.layout.activity_view_order_summary);

        setActivityTitle();

        mListView = (ListView) findViewById(R.id.view_order_summary_list_view);

        totalPrice = (TextView) findViewById(R.id.view_order_total_price_text_view);

        quantity = (TextView) findViewById(R.id.view_order_total_items_text_view);

        modifiedPrice = (TextView) findViewById(R.id.view_order_modified_price_text_view);

        modifiedPrice.setText(String.valueOf(orderSummaryModel.getModifiedPrice()));
        totalPrice.setText(String.valueOf(orderSummaryModel.getTotalPrice()));
        quantity.setText(String.valueOf(orderSummaryModel.getProductCount()));

        orderSummaryAdapter = new OrderSummaryAdapter(this , buildOrderModelList);

        mListView.setAdapter(orderSummaryAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_order_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateOrderSummaryModel(){
        orderSummaryModel = new OrderSummaryModel();
        orderSummaryModel.setOrderId(orderId);
        orderSummaryModel.setRetailerId(retailerId);
        orderSummaryModel.setProductCount(0);
        orderSummaryModel.setModifiedPrice(0.0);
        orderSummaryModel.setTotalPrice(0.0);
        orderSummaryModel.setConfirmedStatus(0);
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

    }

    public void setActivityTitle(){
        String[] columns = {DistributorContract.RetailersEntry.COLUMN_SHOP_NAME};
        String selection = DistributorContract.RetailersEntry._ID +" = " + retailerId;

        String retailerName;

        Cursor cursor = getApplicationContext().getContentResolver().query(DistributorContract.RetailersEntry.CHECK_URI, columns, selection, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            retailerName = cursor.getString(cursor.getColumnIndex(DistributorContract.RetailersEntry.COLUMN_SHOP_NAME));
            setTitle(retailerName);
        }
    }

    public List<BuildOrderModel> getAllProducts()
    {
        ArrayList<BuildOrderModel> mBuildOrderModelList = new ArrayList<BuildOrderModel>();

        Cursor cursor;

        String selection = String.valueOf(orderId);
        cursor = mContext.getContentResolver().query(DistributorContract.ProductsEntry.PRODUCTS_WITH_QUANTITY_URI, null, selection, null, null);

        BuildOrderModel buildOrderModel;

        Log.w(LOG_TAG, "The number of products fetched is " + String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                buildOrderModel = new BuildOrderModel();

                Double pricePerUnit = cursor.getDouble(cursor.getColumnIndex(DistributorContract.ProductsEntry.COLUMN_PRICE_PER_UNIT));

                buildOrderModel.setId(i);
                buildOrderModel.setProductName(cursor.getString(cursor.getColumnIndex(DistributorContract.ProductsEntry.COLUMN_PRODUCT_NAME)));

                int quantity = cursor.getInt(cursor.getColumnIndex(DistributorContract.OrderItemsEntry.COLUMN_QUANTITY));
                if (quantity > 0){
                    buildOrderModel.setQuantity(quantity);
                    buildOrderModel.setTotalPrice(quantity*pricePerUnit);
                }
                else {
                    buildOrderModel.setQuantity(0);
                    buildOrderModel.setTotalPrice(0.0);
                }

                buildOrderModel.setProductId(cursor.getInt(cursor.getColumnIndex(DistributorContract.ProductsEntry.COLUMN_PRODUCT_ID)));
                mBuildOrderModelList.add(buildOrderModel);
            }
        }

        cursor.close();

        return mBuildOrderModelList;
    }
}
