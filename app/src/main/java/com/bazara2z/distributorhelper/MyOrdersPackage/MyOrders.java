package com.bazara2z.distributorhelper.MyOrdersPackage;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.bazara2z.distributorhelper.Data.DistributorContract.*;
import com.bazara2z.distributorhelper.OrderSummaryPackage.OrderSummaryModel;
import com.bazara2z.distributorhelper.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyOrders extends AppCompatActivity {

    private final String LOG_TAG = "ProductsDisplayActivity";

    List<OrderSummaryModel> orderSummaryModelList;
    MyOrdersAdapter myOrdersAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        orderSummaryModelList = getAllOrders();

        mListView = (ListView) findViewById(R.id.my_orders_list_view);

        myOrdersAdapter = new MyOrdersAdapter(this, orderSummaryModelList);

        mListView.setAdapter(myOrdersAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    public List<OrderSummaryModel>  getAllOrders(){

        ArrayList<OrderSummaryModel> orderSummaryModelList = new ArrayList<OrderSummaryModel>();

        Context mContext = getApplicationContext();

        Calendar cal = Calendar.getInstance();

        Long nowTime = cal.getTimeInMillis();

        cal.setTimeInMillis(nowTime);
        cal.set(Calendar.HOUR_OF_DAY, 0); //set hours to zero
        cal.set(Calendar.MINUTE, 0); // set minutes to zero
        cal.set(Calendar.SECOND, 0); //set seconds to zero

        Long dayStartTime = cal.getTimeInMillis();

        cal.add(Calendar.DATE, 1);

        Long dayEndTime = cal.getTimeInMillis();

        String selection = OrdersEntry.COLUMN_ORDER_CREATION_TIME + " >= " + dayStartTime + " AND " +
                OrdersEntry.COLUMN_ORDER_CREATION_TIME + " < " + dayEndTime;

        String[] columns = {RetailersEntry.COLUMN_SHOP_NAME, OrdersEntry.TABLE_NAME +"."+ OrdersEntry.COLUMN_RETAILER_ID,
                OrdersEntry.COLUMN_PRODUCT_COUNT, OrdersEntry.COLUMN_TOTAL_PRICE,
                OrdersEntry.TABLE_NAME + "." + OrdersEntry.COLUMN_UPLOAD_SYNC_STATUS,
                OrdersEntry.COLUMN_MODIFIED_PRICE,OrdersEntry.TABLE_NAME + "." + OrdersEntry._ID};

        Cursor cursor = mContext.getContentResolver().query(OrdersEntry.DISPLAY_ORDERS_URI, columns, selection, null, null);

        OrderSummaryModel orderSummaryModel;

        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                orderSummaryModel = new OrderSummaryModel();

                orderSummaryModel.setRetailerName(cursor.getString(cursor.getColumnIndex(RetailersEntry.COLUMN_SHOP_NAME)));
                orderSummaryModel.setRetailerId(cursor.getInt(cursor.getColumnIndex(OrdersEntry.COLUMN_RETAILER_ID)));
                orderSummaryModel.setProductCount(cursor.getInt(cursor.getColumnIndex(OrdersEntry.COLUMN_PRODUCT_COUNT)));
                orderSummaryModel.setTotalPrice(cursor.getDouble(cursor.getColumnIndex(OrdersEntry.COLUMN_TOTAL_PRICE)));
                orderSummaryModel.setModifiedPrice(cursor.getDouble(cursor.getColumnIndex(OrdersEntry.COLUMN_MODIFIED_PRICE)));
                orderSummaryModel.setOrderId(cursor.getInt(cursor.getColumnIndex(OrdersEntry._ID)));
                orderSummaryModel.setIsOrderSynced(cursor.getInt(cursor.getColumnIndex(OrdersEntry.COLUMN_UPLOAD_SYNC_STATUS)));

                orderSummaryModelList.add(orderSummaryModel);
            }
        }

        cursor.close();

        return orderSummaryModelList;

    }

}
