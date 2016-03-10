package com.bazara2z.distributorhelper.LoginActivityPackage;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bazara2z.distributorhelper.Data.DistributorContract.*;
import com.bazara2z.distributorhelper.MainActivityPackage.MainActivity;
import com.bazara2z.distributorhelper.R;

import java.util.Vector;


/**
 * A login screen that offers login via phone number and password for salesmen
 */

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = "LOGIN ACTIVITY";
    private static final String DUMMY_CREDENTIALS_1 = "9876543210";
    private static final String DUMMY_PASSWORD_1 = "123456";
    private static final String DUMMY_CREDENTIALS_2 = "9999999999";
    private static final String DUMMY_PASSWORD_2 = "111111";

    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mPhoneNoView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    //private DistributorProvider distributorProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        insertSampleData();

        setContentView(R.layout.activity_login);

        // Set up the login form.
        mPhoneNoView = (EditText) findViewById(R.id.phonenumber);

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    public void attemptLogin() {

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPhoneNoView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phoneno = mPhoneNoView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid phoneno.
        if (!isPhoneNoValid(phoneno)) {
            mPhoneNoView.setError(getString(R.string.error_invalid_phonenumber));
            focusView = mPhoneNoView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don'OffersFragment attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            focusView = mLoginFormView;
            focusView.requestFocus();
            mAuthTask = new UserLoginTask(phoneno, password, getApplicationContext());
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPhoneNoValid(String phoneno) {
        if (phoneno.matches("[0-9]+") && phoneno.length() == 10 && phoneno.substring(0, 1).matches("9|8|7")){
            return true;
        }
        return false;
    }

    private boolean isPasswordValid(String password) {

        return true;
    }


    public void showProgress(final boolean show) {

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);

    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPhoneNumber;
        private final String mPassword;
        private Context mContext;
        private int internalLogin = 0;
        private final String mUserName = "Test User";
        private int mUserId = 1;

        UserLoginTask(String phoneNumber, String password, Context context) {
            mPhoneNumber = phoneNumber;
            mPassword = password;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // TODO: Write entire code for login check
            /** First check if the credentials match those stored inside internal database
             * If they do, move forward to main activity and check if DB is synced
             * If they don'OffersFragment, check network access and authenticate on network
             * Sync database accordingly
             */


            String[] columns = {UserEntry.COLUMN_PHONE_NUMBER,UserEntry.COLUMN_PASSWORD};

            Cursor cursor = mContext.getContentResolver().query(UserEntry.CHECK_URI, columns,null,null,null);

            if (cursor.getCount() > 0) {

                cursor.moveToNext();
                //Log.w(LOG_TAG, "getString(1)" + cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PHONE_NUMBER)));
                //Log.w(LOG_TAG, "getString(2)"+ cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PASSWORD)) );
                Log.w(LOG_TAG,"Got cursor inside AsyncTask");
                if(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PHONE_NUMBER)).equals(mPhoneNumber)
                        && cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PASSWORD)).equals(mPassword)){

                    internalLogin = 1;
                    //Toast.makeText(getApplicationContext(), R.string.successful_login_toast, Toast.LENGTH_SHORT).show();

                    cursor.close();
                    return true;
                }
            }
            cursor.close();


            try {
                //Use Volley to connect to the network
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }


            if(DUMMY_CREDENTIALS_1.equals(mPhoneNumber) && DUMMY_PASSWORD_1.equals(mPassword)){
                return true;
            }
            if(DUMMY_CREDENTIALS_2.equals(mPhoneNumber) && DUMMY_PASSWORD_2.equals(mPassword)){
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            // TODO: Write code to go to Home Activity and download DB

            mAuthTask = null;


            if (success) {

                if(internalLogin == 0) {

                    //Delete old user from DB

                    String selection = null;
                    String[] selectionArgs = null;

                    int deletedrows = mContext.getContentResolver().delete(UserEntry.DELETE_URI, selection ,selectionArgs);
                    Log.w(LOG_TAG, "Deleted"+ deletedrows + " user from database");

                    //Insert new user into user table
                    ContentValues userValues = new ContentValues();
                    userValues.put(UserEntry.COLUMN_USER_ID, mUserId);
                    userValues.put(UserEntry.COLUMN_PHONE_NUMBER, mPhoneNumber);
                    userValues.put(UserEntry.COLUMN_PASSWORD, mPassword);
                    userValues.put(UserEntry.COLUMN_LOGIN_STATUS, 1);
                    userValues.put(UserEntry.COLUMN_USER_NAME, mUserName);

                    Uri insertedUri = mContext.getContentResolver().insert(UserEntry.INSERT_URI, userValues);

                    Log.w(LOG_TAG, "Inserted new buyer into database");
                    Log.w(LOG_TAG, insertedUri.toString());

                    Toast.makeText(getApplicationContext(), R.string.successful_login_toast, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();

                    // TODO: Call SyncAdapter to fetch data from server
                }
                else if(internalLogin == 1){
                    Toast.makeText(getApplicationContext(), "Hey you logged in via DB this time", Toast.LENGTH_LONG).show();
                    ContentValues userValues = new ContentValues();

                    userValues.put(UserEntry.COLUMN_LOGIN_STATUS, 1);

                    String selection = null;
                    String[] selectionArgs = null;

                    int editedrows = mContext.getContentResolver().update(UserEntry.LOGIN_STATUS_URI, userValues,selection,selectionArgs);

                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);

                    finish();
                    Log.w(LOG_TAG, "Logged in via database this time");
                }

                showProgress(false);

            } else {
                showProgress(false);
                mPasswordView.setError(getString(R.string.error_incorrect_credentials));
                mPasswordView.requestFocus();
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public void insertSampleData(){

        Context mContext = getApplicationContext();

        //---------------------------------------------------------
        // INSERT RETAILERS

        int retailersCount = 1;

        String[] retailerColumns = {RetailersEntry.COLUMN_RETAILER_ID};

        Cursor retailerCursor = mContext.getContentResolver().query(RetailersEntry.CHECK_URI, retailerColumns,null,null,null);

        if (retailerCursor.getCount() > 0) {
            retailersCount = 0;
        }

        retailerCursor.close();

        if (retailersCount == 1) {

            Vector<ContentValues> cVVector = new Vector<ContentValues>(5);

            for (int i = 1; i <= 5; i = i + 1) {

                int mRetailerId = i;
                String mShopName = "Retailer Shop " + String.valueOf(i);
                String mAddressLine1 = "Address Line 1";
                String mAddressLine2 = "Address Line 2";
                String mPhoneNumber = "9999999999";
                String mPincode = "111111";
                int mLocationPresent = 0;
                double mLatitude = 0.0;
                double mLongitude = 0.0;
                int mUploadSyncStatus = 0;

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
                retailerValues.put(RetailersEntry.COLUMN_PHONE_NUMBER, mPhoneNumber);

                //Uri insertedUri = mContext.getContentResolver().insert(RetailersEntry.INSERT_URI, retailerValues);

                cVVector.add(retailerValues);
            }

            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            mContext.getContentResolver().bulkInsert(RetailersEntry.BULK_INSERT_URI, cvArray);

            Log.w(LOG_TAG,"Inserted retailers");
        }
        //---------------------------------------------------------
        // INSERT PRODUCTS

        int productsCount = 1;

        String[] productColumns = {ProductsEntry.COLUMN_PRODUCT_ID};

        Cursor productsCursor = mContext.getContentResolver().query(ProductsEntry.CHECK_URI, productColumns,null,null,null);

        if (productsCursor.getCount() > 0) {
            productsCount = 0;
        }

        productsCursor.close();

        if (productsCount == 1) {

            Vector<ContentValues> cVVector = new Vector<ContentValues>(20);

            for (int i = 1; i <= 20; i = i + 1) {

                int mProductId = i;
                String mProductName = "Product " + String.valueOf(i);
                double mPricePerUnit = 10.0;

                ContentValues productValues = new ContentValues();

                productValues.put(ProductsEntry.COLUMN_PRODUCT_ID, mProductId);
                productValues.put(ProductsEntry.COLUMN_PRODUCT_NAME, mProductName);
                productValues.put(ProductsEntry.COLUMN_PRICE_PER_UNIT, mPricePerUnit);

                //Uri insertedUri = mContext.getContentResolver().insert(ProductsEntry.INSERT_URI, productValues);

                cVVector.add(productValues);
            }

            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            mContext.getContentResolver().bulkInsert(ProductsEntry.BULK_INSERT_URI, cvArray);

            Log.w(LOG_TAG,"Inserted products");
        }

        //---------------------------------------------------------
        // INSERT OFFERS

        int offersCount = 1;

        String[] offerColumns = {OffersEntry.COLUMN_OFFER_ID};

        Cursor offerCursor = mContext.getContentResolver().query(OffersEntry.CHECK_URI, offerColumns,null,null,null);

        if (offerCursor.getCount() > 0) {
            offersCount = 0;
        }

        offerCursor.close();

        if (offersCount == 1) {

            Vector<ContentValues> cVVector = new Vector<ContentValues>(10);

            for (int i = 1; i <= 10; i = i + 1) {

                int mProductId = i;
                String mOfferDetails = "Discount of 10%";
                int mOfferId = i;

                ContentValues offerValues = new ContentValues();

                offerValues.put(OffersEntry.COLUMN_OFFER_ID, mOfferId);
                offerValues.put(OffersEntry.COLUMN_OFFER_DETAILS, mOfferDetails);
                offerValues.put(OffersEntry.COLUMN_PRODUCT_ID, mProductId);

                //Uri insertedUri = mContext.getContentResolver().insert(OffersEntry.INSERT_URI, offerValues);

                cVVector.add(offerValues);
            }

            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            mContext.getContentResolver().bulkInsert(OffersEntry.BULK_INSERT_URI, cvArray);

            Log.w(LOG_TAG,"Inserted offers");
        }
    }

}

