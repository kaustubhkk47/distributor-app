package com.bazara2z.distributorhelper.OrderSummaryPackage;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.bazara2z.distributorhelper.BuildOrderPackage.BuildOrderModel;
import com.bazara2z.distributorhelper.Data.DistributorContract.*;
import com.bazara2z.distributorhelper.MainActivityPackage.MainActivity;
import com.bazara2z.distributorhelper.R;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_order_summary, container, false);

        mListView = (ListView) rootView.findViewById(R.id.order_summary_list_view);

        orderSummaryAdapter = new OrderSummaryAdapter(getActivity(), buildOrderModelList);

        modifiedPrice = (Button) rootView.findViewById(R.id.confirm_order_modified_price_button);

        //modifiedPrice.setText(String.valueOf(orderSummaryModel.getModifiedPrice()));
        /*
        Log.w(LOG_TAG, "Modified price is :" + orderSummaryModel.getModifiedPrice());
        Log.w(LOG_TAG, "Product count is :" +orderSummaryModel.getProductCount());
*/
        modifiedPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptView = li.inflate(R.layout.order_summary_price_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setView(promptView);

                final EditText userInput = (EditText) promptView
                        .findViewById(R.id.orderSummaryDialogUserInput);


                Log.w(LOG_TAG, "Button clicked");

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

                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                alertDialog.show();


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
        modifiedPrice.setText(String.valueOf(orderSummaryModel.getModifiedPrice()));
        totalPrice.setText(String.valueOf(orderSummaryModel.getTotalPrice()));
        quantity.setText(String.valueOf(orderSummaryModel.getProductCount()));

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

            Uri insertedUri = context.getContentResolver().insert(OrdersEntry.INSERT_URI, orderValues);

            orderId = parseIdFromUri(insertedUri);
        }
        else {
            orderId = orderSummaryModel.getOrderId();

            orderValues.put(OrdersEntry.COLUMN_TOTAL_PRICE, orderSummaryModel.getTotalPrice());
            orderValues.put(OrdersEntry.COLUMN_MODIFIED_PRICE, orderSummaryModel.getModifiedPrice());
            orderValues.put(OrdersEntry.COLUMN_UPLOAD_SYNC_STATUS, 0);
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

                cVVector.add(orderItemValues);
            }

            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            context.getContentResolver().bulkInsert(OrderItemsEntry.BULK_INSERT_URI, cvArray);

            Log.w(LOG_TAG,"Inserted order items");
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
}
