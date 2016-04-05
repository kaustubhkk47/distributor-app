package com.bazara2z.distributorhelper.SelectRetailerPackage;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.bazara2z.distributorhelper.BuildOrderPackage.BuildOrder;
import com.bazara2z.distributorhelper.Data.DistributorContract.*;
import com.bazara2z.distributorhelper.Miscellaneous.MiscellaneousValues;
import com.bazara2z.distributorhelper.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class SelectRetailer extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String LOG_TAG = "ProductsDisplayActivity";

    List<SelectRetailerModel> selectRetailerModelList;
    SelectRetailerAdapter selectRetailerAdapter;
    ListView mListView;

    GoogleApiClient mGoogleApiClient = null;
    LocationRequest mLocationRequest = null;

    //TODO: CHANGE RETAILER UPLOAD SYNC STATUS TO 0 IF LOCATION IS PICKED UP

    private Double mLatitude = 0.0;
    private Double mLongitude = 0.0;
    private int mLocationPresent = 1;
    public int gotLocation = 0;
    private Location mLastLocation;

    int clickedRetailerId;

    int newOrder = 1;
    int orderId = 0;

    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_retailer);

        mContext = getApplicationContext();

        selectRetailerModelList = getAllRetailers();

        mListView = (ListView) findViewById(R.id.select_retailer_ListView);

        selectRetailerAdapter = new SelectRetailerAdapter(this, selectRetailerModelList);

        mListView.setAdapter(selectRetailerAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //here you can use the position to determine what checkbox to check
                //this assumes that you have an array of your checkboxes as well. called checkbox

                SelectRetailerModel selectRetailerModel = selectRetailerModelList.get(position);

                clickedRetailerId = selectRetailerModel.getRetailerId();

                checkNewOrder();

                getLocation();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_retailer, menu);
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



    public List<SelectRetailerModel> getAllRetailers(){
        ArrayList<SelectRetailerModel> mSelectRetailerModelList = new ArrayList<SelectRetailerModel>();


        String[] columns = {RetailersEntry._ID, RetailersEntry.COLUMN_SHOP_NAME, RetailersEntry.COLUMN_PHONE_NUMBER,
        RetailersEntry.COLUMN_ADDRESS_LINE_1, RetailersEntry.COLUMN_ADDRESS_LINE_2, RetailersEntry.COLUMN_LANDMARK};

        Cursor cursor = mContext.getContentResolver().query(RetailersEntry.CHECK_URI, columns, null, null, null);

        SelectRetailerModel selectRetailerModel;

        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                selectRetailerModel = new SelectRetailerModel();
                selectRetailerModel.setId(i);
                selectRetailerModel.setRetailerId(cursor.getInt(cursor.getColumnIndex(RetailersEntry._ID)));
                selectRetailerModel.setRetailerName(cursor.getString(cursor.getColumnIndex(RetailersEntry.COLUMN_SHOP_NAME)));
                selectRetailerModel.setPhoneNumber(cursor.getString(cursor.getColumnIndex(RetailersEntry.COLUMN_PHONE_NUMBER)));
                selectRetailerModel.setAddressLine1(cursor.getString(cursor.getColumnIndex(RetailersEntry.COLUMN_ADDRESS_LINE_1)));
                selectRetailerModel.setAddressLine2(cursor.getString(cursor.getColumnIndex(RetailersEntry.COLUMN_ADDRESS_LINE_2)));
                selectRetailerModel.setLandmark(cursor.getString(cursor.getColumnIndex(RetailersEntry.COLUMN_LANDMARK)));

                mSelectRetailerModelList.add(selectRetailerModel);
            }
        }

        cursor.close();

        //Log.w(LOG_TAG, "The size of the list is " + mProductDisplayModelList.size());

        return mSelectRetailerModelList;
    }

    public void getLocation(){
        addLocationListener();

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
            Log.w(LOG_TAG," mGoogleApiClient connected ");
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MiscellaneousValues.LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(MiscellaneousValues.LOCATION_FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        getLocationFinal();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.

                        try {
                            // Show the dialog by calling startResolutionForResult() and check the result in onActivityResult().
                            status.startResolutionForResult(SelectRetailer.this, MiscellaneousValues.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won'OffersFragment show the dialog.
                        Log.w(LOG_TAG, " Location status setting settings change unavailable ");


                        break;
                    default:

                }
            }
        });
    }

    public void getLocationFinal(){

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MiscellaneousValues.LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(MiscellaneousValues.LOCATION_FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(SelectRetailer.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SelectRetailer.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        if (mLastLocation != null) {
            mLatitude = mLastLocation.getLatitude();
            mLongitude = mLastLocation.getLongitude();
            Log.w(LOG_TAG, "Got retailer location");
            writeTrackingEntry();
            checkRetailerLocation();
            if (mLocationPresent == 0){
                LocationConfirmAlert(getString(R.string.location_alert_title), getString(R.string.location_alert_message));
            }
            else {
                if (newOrder == 0){
                    String title = "Order already placed";
                    String message = "Do you want to edit order?";
                    editOrderConfirmAlert(title, message);
                }
                else {
                    passIntent();
                }
            }

        }
        else {
            if (newOrder == 0){
                String title = "Order already placed";
                String message = "Do you want to edit order?";
                editOrderConfirmAlert(title, message);
            }
            else {
                passIntent();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().

            case MiscellaneousValues.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getLocationFinal();
                        break;
                    case Activity.RESULT_CANCELED:
                        String title = "GPS is off";
                        String message = "Please switch GPS on";
                        showAlertDialog(title , message);
                        break;
                }
                break;
        }
    }

    public void writeTrackingEntry(){

        ContentValues trackingValues = new ContentValues();

        Long trackedTime = System.currentTimeMillis();

        Log.w(LOG_TAG, "Tracked time was " + trackedTime);

        trackingValues.put(TrackingEntry.COLUMN_RETAILER_ID, clickedRetailerId);
        trackingValues.put(TrackingEntry.COLUMN_TRACKED_LATITUDE, mLatitude);
        trackingValues.put(TrackingEntry.COLUMN_TRACKED_LONGITUDE, mLongitude);
        trackingValues.put(TrackingEntry.COLUMN_TRACKED_TIME, trackedTime);
        trackingValues.put(TrackingEntry.COLUMN_UPLOAD_SYNC_STATUS, 0);

        Uri insertedUri = mContext.getContentResolver().insert(TrackingEntry.INSERT_URI, trackingValues);

        Log.w(LOG_TAG, "Wrote tracking entry");
    }

    public void checkRetailerLocation(){
        String[] columns = {RetailersEntry.COLUMN_LOCATION_PRESENT};
        String selection = RetailersEntry._ID +" = " + clickedRetailerId;

        Cursor cursor = mContext.getContentResolver().query(RetailersEntry.CHECK_URI, columns,selection,null,null);

        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            if(cursor.getInt(cursor.getColumnIndex(RetailersEntry.COLUMN_LOCATION_PRESENT)) == 0){
                mLocationPresent = 0;
                Log.w(LOG_TAG, "Retailer location not present");
            }
        }
        else {
        }
        cursor.close();
    }

    public void updateRetailerLocation(){
        ContentValues locationValues = new ContentValues();

        String selection = RetailersEntry.COLUMN_RETAILER_ID +" = " + clickedRetailerId;

        locationValues.put(RetailersEntry.COLUMN_LOCATION_PRESENT, 1);
        locationValues.put(RetailersEntry.COLUMN_RETAILER_LATITUDE, mLatitude);
        locationValues.put(RetailersEntry.COLUMN_RETAILER_LONGITUDE, mLongitude);

        String[] selectionArgs = null;

        int editedrows = mContext.getContentResolver().update(RetailersEntry.UPDATE_URI, locationValues,selection,selectionArgs);

    }

    public void passIntent(){
        Intent intent = new Intent(mContext, BuildOrder.class);
        intent.putExtra(RetailersEntry.COLUMN_RETAILER_ID, clickedRetailerId);
        intent.putExtra(OrdersEntry._ID, orderId);
        intent.putExtra("NewOrder", newOrder);
        startActivity(intent);
        finish();
    }

    public void LocationConfirmAlert(String title, String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        Log.w(LOG_TAG, "LocationConfirmAlert should be fired");

        alertDialog.setTitle(title);

        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                updateRetailerLocation();
                if (newOrder == 0){
                    String title = "Order already placed";
                    String message = "Do you want to edit order?";
                    editOrderConfirmAlert(title, message);
                }
                else {
                    passIntent();
                }
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (newOrder == 0){
                    String title = "Order already placed";
                    String message = "Do you want to edit order?";
                    editOrderConfirmAlert(title, message);
                }
                else {
                    passIntent();
                }
            }
        });

        alertDialog.show();
    }

    public void editOrderConfirmAlert(String title, String message){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                passIntent();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();

    }

    private void addLocationListener(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).
                    addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }


    @Override
    public void onConnected(Bundle connectionHint) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(LOG_TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(LOG_TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    public void showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

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

    @Override
    public void onStop(){
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void checkNewOrder(){

        Calendar cal = Calendar.getInstance();

        Long nowTime = cal.getTimeInMillis();

        cal.setTimeInMillis(nowTime);
        cal.set(Calendar.HOUR_OF_DAY, 0); //set hours to zero
        cal.set(Calendar.MINUTE, 0); // set minutes to zero
        cal.set(Calendar.SECOND, 0); //set seconds to zero

        Long dayStartTime = cal.getTimeInMillis();

        cal.add(Calendar.DATE, 1);

        Long dayEndTime = cal.getTimeInMillis();

        String[] columns = {OrdersEntry._ID};
        String selection = OrdersEntry.COLUMN_RETAILER_ID +" = " + clickedRetailerId + " AND " +
                OrdersEntry.COLUMN_ORDER_CREATION_TIME + " >= " + dayStartTime + " AND " +
                OrdersEntry.COLUMN_ORDER_CREATION_TIME + " < " + dayEndTime;

        Cursor cursor = getApplicationContext().getContentResolver().query(OrdersEntry.CHECK_URI, columns, selection, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            orderId = cursor.getInt(cursor.getColumnIndex(OrdersEntry._ID));
            newOrder = 0;

        }

    }
}
