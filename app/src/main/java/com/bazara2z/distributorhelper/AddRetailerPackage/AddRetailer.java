package com.bazara2z.distributorhelper.AddRetailerPackage;

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

import com.bazara2z.distributorhelper.Data.DistributorContract.*;
import com.bazara2z.distributorhelper.MainActivityPackage.MainActivity;
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


public class AddRetailer extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final String LOG_TAG = "AddRetailerActivity";

    private EditText mShopName;
    private EditText mAddressLine1;
    private EditText mAddressLine2;
    private EditText mPincode;
    private EditText mPhoneNumber;

    private Button mAddRetailerButton;

    private Location mLastLocation;

    private addRetailerToDB addRetailerTask = null;

    private String shopName;
    private String addressLine1;
    private String addressLine2;
    private String pincode;
    private String phoneNumber;

    GoogleApiClient mGoogleApiClient = null;
    LocationRequest mLocationRequest = null;
    private int yesPressed = 0;

    private Double mLatitude = 0.0;
    private Double mLongitude = 0.0;
    private int mLocationPresent = 0;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_retailer);

        mShopName = (EditText) findViewById(R.id.retailer_shop_name);
        mAddressLine1 = (EditText) findViewById(R.id.retailer_address_line1);
        mAddressLine2 = (EditText) findViewById(R.id.retailer_address_line2);
        mPincode = (EditText) findViewById(R.id.retailer_pincode);
        mPhoneNumber = (EditText) findViewById(R.id.retailer_phone_number);

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
        mAddressLine1.setError(null);
        mAddressLine2.setError(null);
        mPincode.setError(null);
        mPhoneNumber.setError(null);

        shopName = mShopName.getText().toString();
        addressLine1 = mAddressLine1.getText().toString();
        addressLine2 = mAddressLine2.getText().toString();
        pincode = mPincode.getText().toString();
        phoneNumber = mPhoneNumber.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(pincode) || !isPincodeValid(pincode)) {
            mPincode.setError(getString(R.string.error_invalid_pincode));
            focusView = mPincode;
            cancel = true;
        }

        if (TextUtils.isEmpty(addressLine1)) {
            mAddressLine1.setError(getString(R.string.error_field_required));
            focusView = mAddressLine1;
            cancel = true;
        }

        if (TextUtils.isEmpty(phoneNumber) || !isPhoneNoValid(phoneNumber)) {
            mPhoneNumber.setError(getString(R.string.error_invalid_phonenumber));
            focusView = mPhoneNumber;
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
            LocationConfirmAlert(getString(R.string.location_alert_title), getString(R.string.location_alert_message),
                    shopName, phoneNumber, addressLine1, addressLine2, pincode);
        }

    }

    private boolean isPincodeValid(String pincode){
        if (pincode.matches("[0-9]+") && pincode.length() == 6){
            return true;
        }
        return false;
    }

    private boolean isPhoneNoValid(String phoneno) {
        if (phoneno.matches("[0-9]+") && phoneno.length() == 10 && phoneno.substring(0, 1).matches("9|8|7")){
            return true;
        }
        return false;
    }

    public void LocationConfirmAlert(String title, String message, final String shopName, final String phoneNumber, final String addressLine1,
                                     final String addressLine2, final String pincode){

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
                mLocationRequest.setInterval(10000);
                mLocationRequest.setFastestInterval(5000);
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
                                addRetailerTask = new addRetailerToDB(shopName, phoneNumber, addressLine1, addressLine2, pincode, getApplicationContext());
                                addRetailerTask.execute((Void) null);

                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the user
                                // a dialog.

                                try {
                                    // Show the dialog by calling startResolutionForResult() and check the result in onActivityResult().
                                    status.startResolutionForResult(AddRetailer.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }


                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won'OffersFragment show the dialog.
                                Log.w(LOG_TAG, " Location status setting settings change unavailable ");
                                addRetailerTask = new addRetailerToDB(shopName,phoneNumber, addressLine1, addressLine2, pincode, getApplicationContext());
                                addRetailerTask.execute((Void) null);
                                break;
                            default:
                                addRetailerTask = new addRetailerToDB(shopName,phoneNumber, addressLine1, addressLine2, pincode, getApplicationContext());
                                addRetailerTask.execute((Void) null);
                        }
                    }
                });

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Call async task to save retailer details to DB
                addRetailerTask = new addRetailerToDB(shopName,phoneNumber, addressLine1, addressLine2, pincode, getApplicationContext());
                addRetailerTask.execute((Void) null);
            }
        });

        alertDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(LOG_TAG, "User agreed to make required location settings changes.");
                        yesPressed = 1;
                        Log.w(LOG_TAG, " Location status setting success ");
                        addRetailerTask = new addRetailerToDB(shopName, phoneNumber, addressLine1, addressLine2, pincode, getApplicationContext());
                        addRetailerTask.execute((Void) null);
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

    public class addRetailerToDB extends AsyncTask<Void, Void, Boolean> {

        private final String mShopName;
        private final String mAddressLine1;
        private final String mAddressLine2;
        private final String mPincode;
        private final String mPhoneNumber;

        private int mRetailerId = 999999;

        private int mUploadSyncStatus = 0;

        private Context mContext;

        addRetailerToDB(String shopName, String phoneNumber, String addressLine1,
                        String addressLine2,String pincode, Context context) {
            mShopName = shopName;
            mAddressLine1 = addressLine1;
            mAddressLine2 = addressLine2;
            mPincode = pincode;
            mContext = context;
            mPhoneNumber = phoneNumber;
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            if(yesPressed == 1){

                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(1000);
                mLocationRequest.setFastestInterval(500);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                if (ContextCompat.checkSelfPermission(AddRetailer.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(AddRetailer.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                }



                //mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (mLastLocation != null) {
                    mLatitude = mLastLocation.getLatitude();
                    mLongitude = mLastLocation.getLongitude();
                    Log.w(LOG_TAG, "Latitude is :" + mLatitude.toString());
                    Log.w(LOG_TAG, "Longitude is :" + mLongitude.toString());
                    mLocationPresent = 1;
                }
            }

            String[] columns = {RetailersEntry.COLUMN_RETAILER_ID};
            String selection = RetailersEntry.COLUMN_RETAILER_ID +" = (SELECT MAX(" +
                    RetailersEntry.COLUMN_RETAILER_ID + ") FROM "+ RetailersEntry.TABLE_NAME + ")";

            Cursor cursor = mContext.getContentResolver().query(RetailersEntry.CHECK_URI, columns,selection,null,null);

            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                mRetailerId = Integer.valueOf(cursor.getString(cursor.getColumnIndex(RetailersEntry.COLUMN_RETAILER_ID))) + 1;
            }
            else {
                mRetailerId = 1;
            }
            cursor.close();

            ContentValues retailerValues = new ContentValues();
            retailerValues.put(RetailersEntry.COLUMN_RETAILER_ID, mRetailerId);
            retailerValues.put(RetailersEntry.COLUMN_SHOP_NAME, mShopName);
            retailerValues.put(RetailersEntry.COLUMN_ADDRESS_LINE1, mAddressLine1);
            retailerValues.put(RetailersEntry.COLUMN_ADDRESS_LINE2, mAddressLine2);
            retailerValues.put(RetailersEntry.COLUMN_PINCODE, mPincode);
            retailerValues.put(RetailersEntry.COLUMN_LOCATION_PRESENT, mLocationPresent);
            retailerValues.put(RetailersEntry.COLUMN_RETAILER_LATITUDE, mLatitude);
            retailerValues.put(RetailersEntry.COLUMN_RETAILER_LONGITUDE, mLongitude);
            retailerValues.put(RetailersEntry.COLUMN_UPLOAD_SYNC_STATUS, mUploadSyncStatus);
            retailerValues.put(RetailersEntry.COLUMN_PHONE_NUMBER, mRetailerId);

            Uri insertedUri = mContext.getContentResolver().insert(RetailersEntry.INSERT_URI, retailerValues);

            Log.w(LOG_TAG, "New retailer added at" + insertedUri.toString());

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            // TODO: Write code to go to Home Activity and download DB

            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }

            Toast.makeText(getApplicationContext(), R.string.successful_retailer_added_toast, Toast.LENGTH_LONG).show();



            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            finish();

        }

        @Override
        protected void onCancelled() {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


}
