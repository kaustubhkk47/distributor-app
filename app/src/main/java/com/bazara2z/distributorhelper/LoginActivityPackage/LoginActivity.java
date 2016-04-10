package com.bazara2z.distributorhelper.LoginActivityPackage;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bazara2z.distributorhelper.Data.DistributorContract.*;
import com.bazara2z.distributorhelper.MainActivityPackage.MainActivity;
import com.bazara2z.distributorhelper.Miscellaneous.Validation;
import com.bazara2z.distributorhelper.R;

import com.bazara2z.distributorhelper.SyncAdapter.SyncFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A login screen that offers login via phone number and password for salesmen
 */

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = "LOGIN ACTIVITY";
    private static final String DUMMY_CREDENTIALS_1 = "9876543210";
    private static final String DUMMY_PASSWORD_1 = "123456";
    private static final String DUMMY_CREDENTIALS_2 = "9999999999";
    private static final String DUMMY_PASSWORD_2 = "111111";

    private String mPhoneNumber;
    private String mPassword;
    private int internalLogin = 0;
    private Context mContext;
    private SyncFunctions syncFunctions;

    // UI references.
    private EditText mPhoneNoView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private String mToken = "";

    private final String mUserName = "Test User";

    Validation validation = new Validation();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        mContext = getApplicationContext();
        syncFunctions = new SyncFunctions(mContext);

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

        // Reset errors.
        mPhoneNoView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phoneno = mPhoneNoView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !validation.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid phoneno.
        if (!validation.isPhoneNoValid(phoneno)) {
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

            mPhoneNumber = phoneno;
            mPassword = password;

            checkInternalLogin();

            //mAuthTask = new UserLoginTask(phoneno, password, getApplicationContext());
            //mAuthTask.execute((Void) null);
        }
    }

    public  void checkInternalLogin(){

        String[] columns = {UserEntry.COLUMN_PHONE_NUMBER, UserEntry.COLUMN_PASSWORD};

        Cursor cursor = mContext.getContentResolver().query(UserEntry.CHECK_URI, columns, null, null, null);

        if (cursor.getCount() > 0) {

            cursor.moveToNext();
            //Log.w(LOG_TAG, "getString(1)" + cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PHONE_NUMBER)));
            //Log.w(LOG_TAG, "getString(2)"+ cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PASSWORD)) );
            Log.w(LOG_TAG, "Got cursor inside AsyncTask");
            if (cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PHONE_NUMBER)).equals(mPhoneNumber)
                    && cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PASSWORD)).equals(mPassword)) {

                internalLogin = 1;
                Toast.makeText(getApplicationContext(), R.string.successful_login_toast, Toast.LENGTH_LONG).show();
                ContentValues userValues = new ContentValues();

                userValues.put(UserEntry.COLUMN_LOGIN_STATUS, 1);

                String selection = null;
                String[] selectionArgs = null;

                int editedrows = mContext.getContentResolver().update(UserEntry.LOGIN_STATUS_URI, userValues,selection,selectionArgs);


                syncFunctions.getUnsyncedRetailerData();

                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);

                finish();
                Log.w(LOG_TAG, "Logged in via database this time");

                showProgress(false);

                cursor.close();
                return;
            }
            else {
                connectToNetwork();
                cursor.close();
                return;
            }
        }
        connectToNetwork();
        cursor.close();

    }

    public void connectToNetwork() {

        RequestQueue queue = Volley.newRequestQueue(this);

        Log.w(LOG_TAG, this.toString());

        StringRequest postRequest = new StringRequest(Request.Method.POST, syncFunctions.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String statusCode = jsonResponse.getString("statusCode");

                            if (statusCode.equals(syncFunctions.CORRECT_RESPONSE_CODE)) {

                                String body = jsonResponse.getString("body");

                                JSONObject bodyJSON = new JSONObject(body);

                                mToken = bodyJSON.getString("token");
                                Log.w(LOG_TAG, mToken);

                                // TODO: set username via API

                                loginSuccess();
                                return;
                            } else {
                                setIncorrectCredentialsError();
                                return;
                            }

                        } catch (JSONException e) {

                            setNetworkError();
                            return;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(LOG_TAG, error.toString());
                        setNetworkError();
                        return;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put(syncFunctions.KEY_MOBILE_NUMBER, mPhoneNumber);
                params.put(syncFunctions.KEY_PASSWORD, mPassword);
                return params;
            }

        };

        queue.add(postRequest);

    }

    public void setIncorrectCredentialsError(){
        showProgress(false);
        mPasswordView.setError(getString(R.string.error_incorrect_credentials));
        mPasswordView.requestFocus();
    }

    public void setNetworkError(){
        showProgress(false);
        mPasswordView.setError(getString(R.string.error_network));
        mPasswordView.requestFocus();
    }

    public void showProgress(final boolean show) {

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);

    }

    public void loginSuccess(){

        //Delete old user from DB
        String selection = null;
        String[] selectionArgs = null;

        int deletedrows = mContext.getContentResolver().delete(UserEntry.DELETE_URI, selection ,selectionArgs);
        Log.w(LOG_TAG, "Deleted"+ deletedrows + " user from database");

        //Insert new user into user table
        ContentValues userValues = new ContentValues();
        userValues.put(UserEntry.COLUMN_PHONE_NUMBER, mPhoneNumber);
        userValues.put(UserEntry.COLUMN_PASSWORD, mPassword);
        userValues.put(UserEntry.COLUMN_LOGIN_STATUS, 1);
        userValues.put(UserEntry.COLUMN_USER_NAME, mUserName);
        userValues.put(UserEntry.COLUMN_TOKEN, mToken);

        Uri insertedUri = mContext.getContentResolver().insert(UserEntry.INSERT_URI, userValues);

        Log.w(LOG_TAG, "Inserted new buyer into database");
        Log.w(LOG_TAG, insertedUri.toString());

        syncFunctions.getUnsyncedRetailerData();

        Toast.makeText(getApplicationContext(), R.string.successful_login_toast, Toast.LENGTH_LONG).show();

        //------------------------------------------------------------------------------------------

        mPhoneNoView.getText().clear();
        mPasswordView.getText().clear();

        //------------------------------------------------------------------------------------------

        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
        showProgress(false);

    }

}



