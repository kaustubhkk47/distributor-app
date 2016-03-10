package com.bazara2z.distributorhelper.SplashScreenPackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bazara2z.distributorhelper.Data.DistributorContract;
import com.bazara2z.distributorhelper.LoginActivityPackage.LoginActivity;
import com.bazara2z.distributorhelper.MainActivityPackage.MainActivity;
import com.bazara2z.distributorhelper.R;

/**
 * Created by Maddy on 08-03-2016.
 */
public class SplashScreen extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    public static String LOG_TAG = "SplashScreenActivity";
    int checkLogin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkLogin();

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                
                if (checkLogin == 0){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);

    }

    public void checkLogin(){
        String[] columns = {DistributorContract.UserEntry.COLUMN_LOGIN_STATUS};

        Context mContext = getApplicationContext();

        Cursor cursor = mContext.getContentResolver().query(DistributorContract.UserEntry.CHECK_URI, columns,null,null,null);

        if (cursor.getCount() > 0) {

            cursor.moveToNext();
            if(cursor.getString(cursor.getColumnIndex(DistributorContract.UserEntry.COLUMN_LOGIN_STATUS)).equals("1")){

                checkLogin =1;

                //Toast.makeText(getApplicationContext(), "Hey you were already logged in", Toast.LENGTH_LONG).show();
            }
        }
        cursor.close();
    }
}
