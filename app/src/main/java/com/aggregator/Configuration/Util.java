package com.aggregator.Configuration;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.aggregator.Constant.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Balram on 8/19/2015.
 */


public class Util {

    public static void  setSharedPrefrenceValue(Object objCurrentClassReference, String strPrefenceFileName,
                                                 String strKey, String strValue) {

        SharedPreferences sharedPreferencesForStoringData = ((ContextWrapper) objCurrentClassReference)
                .getSharedPreferences(strPrefenceFileName, 0);
        SharedPreferences.Editor sharedPrefencesEditor = sharedPreferencesForStoringData
                .edit();

        //  ArrayList< String> obj=new  ArrayList< String>();
        // LinkedHashSet<String> obj = new LinkedHashSet<String>();


        sharedPrefencesEditor.putString(strKey, strValue);
        //sharedPrefencesEditor.putStringSet("abc", obj);
        sharedPrefencesEditor.commit();


    }




    public static String  getSharedPrefrenceValue(Object objCurrentClassReference,String key) {

        SharedPreferences settings = ((ContextWrapper) objCurrentClassReference)
                .getSharedPreferences(
                        Constant.PREFS_NAME, 1);

        String value=settings.getString(key, null);

        return value;




    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;


        Pattern pattern = Pattern.compile(Constant.STREMAILADDREGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;

    }

    public static boolean isInternetConnection(Context context) {
        // TODO Auto-generated method stub

        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();
        boolean statusInternet;

        if(nf != null && nf.isConnected()==true )
        {
            Log.i("Info:", "Network Available.");
            statusInternet=true;
        }
        else
        {
            Log.i("Info:","Network Not Available.");
            statusInternet=false;

        }
        return statusInternet;
    }


    public static String generatePIN()
    {

        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int)(Math.random()*9000)+1000;

        return String.valueOf(randomPIN);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}
