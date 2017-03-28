package com.bazara2z.distributorhelper.AddRetailerPackage;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bazara2z.distributorhelper.Data.DistributorContract;
import com.bazara2z.distributorhelper.Data.DistributorContract.*;
import com.bazara2z.distributorhelper.MainActivityPackage.MainActivity;
import com.bazara2z.distributorhelper.Miscellaneous.MiscellaneousValues;
import com.bazara2z.distributorhelper.Miscellaneous.Validation;
import com.bazara2z.distributorhelper.R;
import com.bazara2z.distributorhelper.SyncAdapter.SyncFunctions;
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


public class AddRetailer extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final String LOG_TAG = "AddRetailerActivity";

    private EditText mShopName;
    private EditText mFirstName;
    private EditText mPhoneNumber;
    private EditText mAddressLine1;
    private EditText mAddressLine2;
    private EditText mLandmark;
    private EditText mPincode;

    private String shopName;
    private String firstName;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String pincode;

    private int retailerId = 1;
    int mRetailerEdited = 0;

    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private int locationPresent = 0;

    private int uploadSyncStatus = 0;

    private Context mContext;

    private Button mAddRetailerButton;

    private Location mLastLocation;

    private SyncFunctions syncFunctions;

    GoogleApiClient mGoogleApiClient = null;
    LocationRequest mLocationRequest = null;
    private int yesPressed = 0;

    Validation validation = new Validation();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_retailer);

        mContext = getApplicationContext();
        syncFunctions = new SyncFunctions(mContext);

        mShopName = (EditText) findViewById(R.id.retailer_shop_name);
        mFirstName = (EditText) findViewById(R.id.retailer_first_name);
        mPhoneNumber = (EditText) findViewById(R.id.retailer_phone_number);
        mAddressLine1 = (EditText) findViewById(R.id.retailer_address_line1);
        mAddressLine2 = (EditText) findViewById(R.id.retailer_address_line2);
        mLandmark = (EditText) findViewById(R.id.retailer_landmark);
        mPincode = (EditText) findViewById(R.id.retailer_pincode);

        mAddRetailerButton = (Button) findViewById(R.id.add_retailer_button);

        mAddRetailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAddRetailer();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_retailer, menu);
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

    public void attemptAddRetailer(){

        mShopName.setError(null);
        mFirstName.setError(null);
        mPhoneNumber.setError(null);

        mAddressLine1.setError(null);

        mPincode.setError(null);

        shopName = mShopName.getText().toString();
        firstName = mFirstName.getText().toString();
        phoneNumber = mPhoneNumber.getText().toString();
        addressLine1 = mAddressLine1.getText().toString();
        addressLine2 = mAddressLine2.getText().toString();
        landmark = mLandmark.getText().toString();
        pincode = mPincode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(pincode) || !validation.isPincodeValid(pincode)) {
            mPincode.setError(getString(R.string.error_invalid_pincode));
            focusView = mPincode;
            cancel = true;
        }

        if (TextUtils.isEmpty(addressLine1)) {
            mAddressLine1.setError(getString(R.string.error_field_required));
            focusView = mAddressLine1;
            cancel = true;
        }

        if (TextUtils.isEmpty(phoneNumber) || !validation.isPhoneNoValid(phoneNumber)) {
            mPhoneNumber.setError(getString(R.string.error_invalid_phonenumber));
            focusView = mPhoneNumber;
            cancel = true;
        }

        if (TextUtils.isEmpty(firstName)) {
            mFirstName.setError(getString(R.string.error_field_required));
            focusView = mFirstName;
            cancel = true;
        }

        if (TextUtils.isEmpty(shopName)) {
            mShopName.setError(getString(R.string.error_field_required));
            focusView = mShopName;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            LocationConfirmAlert(getString(R.string.location_alert_title), getString(R.string.location_alert_message));
        }

    }

    public void LocationConfirmAlert(String title, String message){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(title);

        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Call async task to get retailer location and save retailer details to DB
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

                                yesPressed = 1;
                                Log.w(LOG_TAG, " Location status setting success ");
                                addRetailerToDB();

                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the user
                                // a dialog.

                                try {
                                    // Show the dialog by calling startResolutionForResult() and check the result in onActivityResult().
                                    status.startResolutionForResult(AddRetailer.this, MiscellaneousValues.REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }


                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won'OffersFragment show the dialog.
                                Log.w(LOG_TAG, " Location status setting settings change unavailable ");
                                addRetailerToDB();

                                break;
                            default:
                                addRetailerToDB();

                        }
                    }
                });

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Call async task to save retailer details to DB
                addRetailerToDB();

            }
        });

        alertDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case MiscellaneousValues.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(LOG_TAG, "User agreed to make required location settings changes.");
                        yesPressed = 1;
                        Log.w(LOG_TAG, " Location status setting success ");
                        addRetailerToDB();

                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(LOG_TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
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

    public void addRetailerToDB(){

        if(yesPressed == 1){

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(MiscellaneousValues.LOCATION_INTERVAL);
            mLocationRequest.setFastestInterval(MiscellaneousValues.LOCATION_FASTEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            if (ContextCompat.checkSelfPermission(AddRetailer.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(AddRetailer.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }



            //mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
                Log.w(LOG_TAG, "Latitude is :" + latitude.toString());
                Log.w(LOG_TAG, "Longitude is :" + longitude.toString());
                locationPresent = 1;
            }
        }


        retailerId = 0;
        ContentValues retailerValues = new ContentValues();

        retailerValues.put(RetailersEntry.COLUMN_RETAILER_ID, retailerId);
        retailerValues.put(RetailersEntry.COLUMN_SHOP_NAME, shopName);
        retailerValues.put(RetailersEntry.COLUMN_FIRST_NAME, firstName);

        retailerValues.put(RetailersEntry.COLUMN_PHONE_NUMBER, phoneNumber);

        retailerValues.put(RetailersEntry.COLUMN_ADDRESS_LINE_1, addressLine1);
        retailerValues.put(RetailersEntry.COLUMN_ADDRESS_LINE_2, addressLine2);
        retailerValues.put(RetailersEntry.COLUMN_LANDMARK, landmark);
        retailerValues.put(RetailersEntry.COLUMN_PINCODE, pincode);

        retailerValues.put(RetailersEntry.COLUMN_LOCATION_PRESENT, locationPresent);
        retailerValues.put(RetailersEntry.COLUMN_RETAILER_LATITUDE, latitude);
        retailerValues.put(RetailersEntry.COLUMN_RETAILER_LONGITUDE, longitude);

        retailerValues.put(RetailersEntry.COLUMN_UPLOAD_SYNC_STATUS, uploadSyncStatus);
        retailerValues.put(RetailersEntry.COLUMN_RETAILER_EDITED, mRetailerEdited);


        Uri insertedUri = mContext.getContentResolver().insert(RetailersEntry.INSERT_URI, retailerValues);

        Log.w(LOG_TAG, "New retailer added at" + insertedUri.toString());

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        Toast.makeText(getApplicationContext(), R.string.successful_retailer_added_toast, Toast.LENGTH_LONG).show();

        syncFunctions.getUnsyncedRetailerData();

        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onStop(){
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }




}
