package com.bazara2z.distributorhelper.OrderSummaryPackage;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.bazara2z.distributorhelper.BuildOrderPackage.BuildOrderModel;
import com.bazara2z.distributorhelper.Data.DistributorContract;
import com.bazara2z.distributorhelper.Data.DistributorContract.*;
import com.bazara2z.distributorhelper.MainActivityPackage.MainActivity;
import com.bazara2z.distributorhelper.OffersPackage.ProductOffersModel;
import com.bazara2z.distributorhelper.R;
import com.bazara2z.distributorhelper.SyncAdapter.SyncFunctions;


public class OrderSummaryFragment extends Fragment {

    private final String LOG_TAG = "OrderSummaryFragment";

    private OnFragmentInteractionListener mListener;

    List<BuildOrderModel> buildOrderModelList;
    OrderSummaryAdapter orderSummaryAdapter;
    ListView mListView;
    Button orderConfirmButton;
    TextView totalPrice;
    TextView quantity;
    Button modifiedPrice;
    OrderSummaryModel orderSummaryModel;
    Context context;
    Context mContext;
    CheckBox checkBox;

    private SyncFunctions syncFunctions;

    public static OrderSummaryFragment newInstance() {
        List<BuildOrderModel> buildOrderModelList = new ArrayList<BuildOrderModel>();
        OrderSummaryModel orderSummaryModel = new OrderSummaryModel();
        OrderSummaryFragment fragment = new OrderSummaryFragment(buildOrderModelList,orderSummaryModel);
        return fragment;
    }

    public OrderSummaryFragment(List<BuildOrderModel> buildOrderModelList, OrderSummaryModel orderSummaryModel) {
        this.buildOrderModelList = buildOrderModelList;
        this.orderSummaryModel = orderSummaryModel;
    }

    public OrderSummaryFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        mContext = context.getApplicationContext();
        syncFunctions = new SyncFunctions(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_order_summary, container, false);

        mListView = (ListView) rootView.findViewById(R.id.order_summary_list_view);

        orderSummaryAdapter = new OrderSummaryAdapter(getActivity(), buildOrderModelList);

        modifiedPrice = (Button) rootView.findViewById(R.id.confirm_order_modified_price_button);

        checkBox = (CheckBox)  rootView.findViewById(R.id.order_summary_cash_payment_checkBox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    orderSummaryModel.setOrderOfferApplied(1);
                    orderSummaryModel.setOrderOfferId(1);

                    String [] columns = {OrderOffersEntry.COLUMN_DISCOUNT_PERCENT};

                    String selection = OrderOffersEntry.COLUMN_OFFER_ID + " = 1";

                    Cursor cursor = mContext.getContentResolver().query(DistributorContract.OrderOffersEntry.CHECK_URI, columns, selection, null, null);

                    double discountPercent = 0.0;

                    if (cursor.getCount() > 0){
                        cursor.moveToNext();
                        discountPercent = cursor.getDouble(cursor.getColumnIndex(OrderOffersEntry.COLUMN_DISCOUNT_PERCENT));
                    }

                    orderSummaryModel.setDiscountPercent(discountPercent);
                    String checkboxText = "Cash Payment : " + String.format("%.1f",discountPercent) + " % discount applied";
                    checkBox.setText(checkboxText);

                }
                else {
                    orderSummaryModel.setDiscountPercent(0.0);
                    orderSummaryModel.setOrderOfferApplied(0);
                    checkBox.setText("Cash Payment");

                }
                refreshOrderSummaryModel();
                refreshViews();

            }
        });

        //modifiedPrice.setText(String.valueOf(orderSummaryModel.getModifiedPrice()));
        /*
        Log.w(LOG_TAG, "Modified price is :" + orderSummaryModel.getModifiedPrice());
        Log.w(LOG_TAG, "Product count is :" +orderSummaryModel.getProductCount());
*/
        modifiedPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptView = li.inflate(R.layout.price_prompt_order_summary, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setView(promptView);

                final EditText userInput = (EditText) promptView
                        .findViewById(R.id.orderSummaryDialogUserInput);


                //Log.w(LOG_TAG, "Button clicked");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        final String enteredPrice = userInput.getText().toString();

                                        if (isNumberValid(enteredPrice)) {
                                            double newPrice = Double.valueOf(enteredPrice);
                                            orderSummaryModel.setModifiedPrice(newPrice);
                                            modifiedPrice.setText(String.valueOf(orderSummaryModel.getModifiedPrice()));
                                        } else {
                                            Toast.makeText(context, "Please enter valid number", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                Log.w(LOG_TAG, "Alert dialog created");

                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                alertDialog.show();

                Log.w(LOG_TAG, "Alert dialog shown");


            }
        });

        totalPrice = (TextView) rootView.findViewById(R.id.confirm_order_total_price_text_view);
        //Log.w(LOG_TAG, "totalprice is null? " + (totalPrice == null));

        //totalPrice.setText(String.valueOf(orderSummaryModel.getTotalPrice()));

        quantity = (TextView) rootView.findViewById(R.id.confirm_order_total_items_text_view);
        //quantity.setText(String.valueOf(orderSummaryModel.getProductCount()));

        mListView.setAdapter(orderSummaryAdapter);

        orderConfirmButton = (Button) rootView.findViewById(R.id.confirm_order_button);

        orderConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildOrderModelList.size() > 0) {
                    // TODO: Write order to database
                    writeToDB();
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    if (orderSummaryModel.getIsNewOrder() == 1) {
                        Toast.makeText(getActivity().getApplicationContext(), "Hey new order placed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "Hey order edited", Toast.LENGTH_SHORT).show();
                    }
                    getActivity().finish();
                }
                else {
                    String title = "Order is empty";
                    String message = "Please add items to order";
                    showAlertDialog(title, message);
                }
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
        */
    }

    public void refreshViews(){
        modifiedPrice.setText(String.format("%.1f",orderSummaryModel.getModifiedPrice()));
        totalPrice.setText(String.format("%.1f",orderSummaryModel.getTotalPrice()));
        quantity.setText(String.valueOf(orderSummaryModel.getProductCount()));

        if (orderSummaryModel.getOrderOfferApplied() == 1){
            checkBox.setChecked(true);

        }

        orderSummaryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onOrderSummaryFragmentInteraction(Uri uri);
    }

    private boolean isNumberValid(String number) {
        if (number.matches("[0-9]+")){
            return true;
        }
        return false;
    }

    public void writeToDB(){

        ContentValues orderValues = new ContentValues();

        Long nowTime = System.currentTimeMillis();

        int orderId;

        if (orderSummaryModel.getIsNewOrder() == 1) {

            orderValues.put(OrdersEntry.COLUMN_RETAILER_ID, orderSummaryModel.getRetailerId());
            orderValues.put(OrdersEntry.COLUMN_TOTAL_PRICE, orderSummaryModel.getTotalPrice());
            orderValues.put(OrdersEntry.COLUMN_MODIFIED_PRICE, orderSummaryModel.getModifiedPrice());
            orderValues.put(OrdersEntry.COLUMN_UPLOAD_SYNC_STATUS, 0);
            orderValues.put(OrdersEntry.COLUMN_ORDER_CREATION_TIME, nowTime);
            orderValues.put(OrdersEntry.COLUMN_ORDER_UPDATION_TIME, 0.0);
            orderValues.put(OrdersEntry.COLUMN_PRODUCT_COUNT, orderSummaryModel.getProductCount());
            orderValues.put(OrdersEntry.COLUMN_ORDER_ID, 1);
            orderValues.put(OrdersEntry.COLUMN_ORDER_OFFER_APPLIED, orderSummaryModel.getOrderOfferApplied());
            orderValues.put(OrdersEntry.COLUMN_ORDER_OFFER_ID, orderSummaryModel.getOrderOfferId());

            Uri insertedUri = context.getContentResolver().insert(OrdersEntry.INSERT_URI, orderValues);

            orderId = parseIdFromUri(insertedUri);
        }
        else {
            orderId = orderSummaryModel.getOrderId();

            orderValues.put(OrdersEntry.COLUMN_TOTAL_PRICE, orderSummaryModel.getTotalPrice());
            orderValues.put(OrdersEntry.COLUMN_MODIFIED_PRICE, orderSummaryModel.getModifiedPrice());
            orderValues.put(OrdersEntry.COLUMN_ORDER_OFFER_APPLIED, orderSummaryModel.getOrderOfferApplied());
            orderValues.put(OrdersEntry.COLUMN_ORDER_OFFER_ID, orderSummaryModel.getOrderOfferId());

            orderValues.put(OrdersEntry.COLUMN_ORDER_UPDATION_TIME, nowTime);
            orderValues.put(OrdersEntry.COLUMN_PRODUCT_COUNT, orderSummaryModel.getProductCount());

            String selection = OrdersEntry._ID + " = " + orderId;
            String[] selectionArgs = null;

            int editedrows = context.getContentResolver().update(OrdersEntry.UPDATE_URI, orderValues,selection,selectionArgs);


            selection = OrderItemsEntry.COLUMN_ORDER_ID + " = " + orderId;

            int deletedrows = context.getContentResolver().delete(OrderItemsEntry.DELETE_URI, selection ,selectionArgs);
        }

        int orderItems = buildOrderModelList.size();

        if (orderItems > 0) {

            Vector<ContentValues> cVVector = new Vector<ContentValues>(orderItems);
            int tempQuantity;
            int tempProductId;
            BuildOrderModel buildOrderModel;

            for (int i = 0; i < orderItems; i = i + 1) {

                buildOrderModel = buildOrderModelList.get(i);

                ContentValues orderItemValues = new ContentValues();

                tempQuantity = buildOrderModel.getQuantity();
                tempProductId = buildOrderModel.getProductId();

                orderItemValues.put(OrderItemsEntry.COLUMN_QUANTITY, tempQuantity);
                orderItemValues.put(OrderItemsEntry.COLUMN_PRODUCT_ID, tempProductId);
                orderItemValues.put(OrderItemsEntry.COLUMN_ORDER_ID, orderId);
                orderItemValues.put(OrderItemsEntry.COLUMN_TOTAL_PRICE, buildOrderModel.getTotalPrice());
                orderItemValues.put(OrderItemsEntry.COLUMN_EDITED_PRICE, buildOrderModel.getEditedPrice());
                orderItemValues.put(OrderItemsEntry.COLUMN_FREE_UNITS, buildOrderModel.getFreeUnits());

                ArrayList<Integer> arrayList = new ArrayList<>();

                List<ProductOffersModel> productOffersModelList = buildOrderModel.getProductOffers();

                for (int j=0; j<productOffersModelList.size(); j++){
                    ProductOffersModel productOffersModel = productOffersModelList.get(j);
                    if (productOffersModel.getOfferApplied() == 1){
                        arrayList.add(productOffersModel.getOfferId());
                    }
                }

                orderItemValues.put(OrderItemsEntry.COLUMN_OFFERS_APPLIED, arrayList.toString());

                cVVector.add(orderItemValues);
            }

            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            context.getContentResolver().bulkInsert(OrderItemsEntry.BULK_INSERT_URI, cvArray);

            Log.w(LOG_TAG, "Inserted order items");

            syncFunctions.getUnsyncedRetailerData();
        }
    }

    public int parseIdFromUri(Uri insertedUri){
        int id = Integer.valueOf(insertedUri.getLastPathSegment());
        Log.w(LOG_TAG, "Order id is " + id);
        return id;
    }

    public void showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void refreshOrderSummaryModel(){
        int productCount = 0;
        double totalPrice = 0.0;
        double editedPrice = 0.0;

        int listSize = buildOrderModelList.size();

        BuildOrderModel buildOrderModel;

        //orderSummaryModelList.clear();

        for (int i = 0; i < listSize; i++){
            buildOrderModel = buildOrderModelList.get(i);
            if(buildOrderModel.getQuantity() > 0){
                productCount = productCount + 1;
                totalPrice = totalPrice + buildOrderModel.getTotalPrice();
                editedPrice = editedPrice + buildOrderModel.getEditedPrice();
                //orderSummaryModelList.add(buildOrderModel);
            }
        }

        double discountPercent = orderSummaryModel.getDiscountPercent()/100;

        double discountedTotalPrice = totalPrice*(1-discountPercent);
        double discountedEditedPrice = editedPrice*(1-discountPercent);

        orderSummaryModel.setProductCount(productCount);
        orderSummaryModel.setTotalPrice(discountedEditedPrice);
        orderSummaryModel.setModifiedPrice(discountedEditedPrice);
    }

}