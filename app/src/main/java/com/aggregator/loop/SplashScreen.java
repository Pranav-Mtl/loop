package com.aggregator.loop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.google.android.gms.analytics.HitBuilders;

public class SplashScreen extends AppCompatActivity {

    int SPLASH_TIME = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final String appRun= Util.getSharedPrefrenceValue(SplashScreen.this, Constant.SHARED_PREFERENCE_CHECK_APP_RUN);

         /* call google analytics*/

        if(Util.isInternetConnection(SplashScreen.this)) {
            try {
                Application.tracker().setScreenName("Splash Screen");
                Application.tracker().send(new HitBuilders.EventBuilder()
                        .setLabel("Splash Screen")
                        .setCategory("Splash Screen")
                        .setAction("App Launch")
                        .build());

                // AffleInAppTracker.inAppTrackerViewName(getApplicationContext(), "Landing Screen", "App First Screen", "APP Open", null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if(appRun==null) {
                    startActivity(new Intent(getApplicationContext(), Tutorial.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                else {
                    startActivity(new Intent(getApplicationContext(), RouteNew.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        }, SPLASH_TIME);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
