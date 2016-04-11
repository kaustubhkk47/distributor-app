package com.bazara2z.distributorhelper.SplashScreenPackage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.bazara2z.distributorhelper.Data.DistributorContract;
import com.bazara2z.distributorhelper.LoginActivityPackage.LoginActivity;
import com.bazara2z.distributorhelper.MainActivityPackage.MainActivity;
import com.bazara2z.distributorhelper.SyncAdapter.SyncFunctions;

import java.util.Vector;

/**
 * Created by Maddy on 08-03-2016.
 */
public class SplashScreen extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT_MAIN = 2000;
    private static int SPLASH_TIME_OUT_LOGIN = 1000;
    public static String LOG_TAG = "SplashScreenActivity";
    private SyncFunctions syncFunctions;
    private Context mContext;
    int checkLogin = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkLogin();
        mContext = getApplicationContext();
        syncFunctions = new SyncFunctions(mContext);

        //setContentView(R.layout.activity_splash_screen);

        if (checkLogin == 1) {
            syncFunctions.getUnsyncedRetailerData();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT_MAIN);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT_LOGIN);
        }

    }

    public void checkLogin(){
        String[] columns = {DistributorContract.UserEntry.COLUMN_LOGIN_STATUS};

        Context mContext = getApplicationContext();

        ContentResolver contentResolver = mContext.getContentResolver();

        Cursor cursor = contentResolver.query(DistributorContract.UserEntry.CHECK_URI, columns,null,null,null);

        if (cursor.getCount() > 0) {

            cursor.moveToNext();
            if(cursor.getString(cursor.getColumnIndex(DistributorContract.UserEntry.COLUMN_LOGIN_STATUS)).equals("1")){

                checkLogin = 1;

            }
        }
        cursor.close();
    }


}
