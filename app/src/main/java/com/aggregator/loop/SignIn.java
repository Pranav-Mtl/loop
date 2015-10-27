package com.aggregator.loop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.BE.BookingBE;
import com.aggregator.BL.BookingBL;
import com.aggregator.BL.SignInBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.appsee.Appsee;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail,etPassword;

    Button btnDone,tvSignUp,tvForgot;

    String txtEmail,txtPassword;
    ProgressDialog mProgressDialog;
    SignInBL objSignInBL;
    String loginType;

    String bookingRouteId;
    String bookingSource;
    String bookingDestination;
    String bookingDate;
    String bookingTime;
    String bookingPrice;

    BookingBE objBookingBE;
    BookingBL objBookingBL;

    int xx,yy;

    GoogleCloudMessaging gcmObj;

    Context applicationContext;
    String gcmID;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        applicationContext=getApplicationContext();
        Display display = getWindowManager().getDefaultDisplay();

        // Point size = new Point();
        // display.getSize(size);
        int width = display.getWidth();
        int height = display.getHeight();

        // System.out.println("width" + width + "height" + height);

        if(width>=700 && height>=1000)
        {
            xx=500;
            yy=500;
        }
        else
        {
            xx=400;
            yy=500;
        }

        Appsee.start("de8395d3ae424245b695b4c9d6642f71");

        mProgressDialog=new ProgressDialog(SignIn.this);

        objSignInBL=new SignInBL();
        objBookingBE=new BookingBE();
        objBookingBL=new BookingBL();



        etEmail= (EditText) findViewById(R.id.signin_email);
        etPassword= (EditText) findViewById(R.id.signin_password);

        tvForgot= (Button) findViewById(R.id.signin_forgot);
        tvSignUp= (Button) findViewById(R.id.signin_signup);

        btnDone= (Button) findViewById(R.id.signin_done);

        btnDone.setOnClickListener(this);
        tvForgot.setOnClickListener(this);


        tvSignUp.setOnClickListener(this);

        gcmID=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_GCM_ID);

        if(gcmID==null){
            if (checkPlayServices()) {
                registerInBackground();
            }
        }

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    String regexStr = "^[0-9]*$";

                    if (etEmail.getText().toString().trim().length() == 0) {
                        etEmail.setError("Required");
                    } else {
                        if (etEmail.getText().toString().trim().matches(regexStr)) {
                            //write code here for success
                            if (etEmail.getText().toString().length() != 10) {
                                etEmail.setError("Invalid Mobile no.");
                            }
                            else
                            {
                                loginType="phone";
                            }
                        } else {
                            // write code for failure
                            if (!Util.isEmailValid(etEmail.getText().toString())) {
                                etEmail.setError("Invalid Email-id");
                            }
                            else
                            {
                                loginType="email";
                            }
                        }


                    }


                }
            }
        });


        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if(etPassword.getText().toString().trim().length()==0)
                    {
                        etPassword.setError("required");
                    }



                }
            }
        });



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

    @Override
    public void onClick(View v) {

        int id=v.getId();

        switch (id)
        {
            case R.id.signin_done:
                validateSignIn();
                break;
            case R.id.signin_signup:
                startActivity(new Intent(SignIn.this,SignUpScreen.class));
                break;
            case R.id.signin_forgot:
                startActivity(new Intent(SignIn.this,ResetPassword.class));
            default:
                break;

        }
    }


    private void validateSignIn()
    {
        txtEmail=etEmail.getText().toString();
        txtPassword=etPassword.getText().toString();

        if(txtEmail.trim().length()==0)
        {
            etEmail.setError("Required");
        }
        else if(txtPassword.trim().length()==0)
        {
            etPassword.setError("Required");
        }
        else
        {
            if(Util.isInternetConnection(SignIn.this))
                     new ValidateDetails().execute(txtEmail,txtPassword,loginType,gcmID);
            else
            {
               showDialogConnection(SignIn.this);

            }
        }
    }

    private class ValidateDetails extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String result=objSignInBL.validateSignInDetails(params[0],params[1],params[2],getApplicationContext(),params[3]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            try
            {
                if(result.equals(Constant.WS_RESULT_SUCCESS)){
                    Toast.makeText(getApplicationContext(),"Successfully Logged In",Toast.LENGTH_LONG).show();
                    Util.setSharedPrefrenceValue(SignIn.this, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_LOGIN_STATUS, txtEmail);

                    bookingRouteId=Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_BOOKING_ROUTE_ID);
                    if(bookingRouteId==null)
                    {
                       // mProgressDialog.dismiss();
                        startActivity(new Intent(SignIn.this, RouteNew.class));
                        finish();
                    }
                    else
                    {
                       // mProgressDialog.dismiss();
                        startActivity(new Intent(SignIn.this, RouteNew.class));
                        finish();

                       /* objBookingBE.setRouteID(Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_BOOKING_ROUTE_ID));
                        objBookingBE.setRunID(Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_BOOKING_Run_ID));
                        objBookingBE.setStartPoint(Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_BOOKING_SOURCE_id));
                        objBookingBE.setEndPoint(Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_BOOKING_DESTINATION_id));
                        objBookingBE.setTime(Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_BOOKING_TIME));
                        objBookingBE.setPrice((Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_BOOKING_PRICE)));
                        objBookingBE.setUserID(Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_User_id));
                        objBookingBE.setLoopCredit(Util.getSharedPrefrenceValue(SignIn.this,Constant.SHARED_PREFERENCE_BOOKING_LOOP_CREDIT));

                        booking();*/
                    }

                }
                else if(result.equals("verify")){
                    startActivity(new Intent(getApplicationContext(),OTP.class).putExtra("Mobile",etEmail.getText().toString()).putExtra("Previous","Login"));
                    finish();
                }
                else
                {
                    Snackbar
                                .make(findViewById(R.id.login_root),
                                        "Wrong Mobile or Password",
                                        Snackbar.LENGTH_LONG).setText("Wrong Mobile or Password")
                                .show();
                        etPassword.setError("Wrong Mobile or password");

                }

            }
            catch (NullPointerException e){
               showDialogResponse(SignIn.this);
            }
            catch (Exception e)
            {

            }
            finally {
                mProgressDialog.dismiss();
            }
        }
    }


    private void booking()
    {
        /*objBookingBE.setRouteID(bookingRouteId);
        objBookingBE.setRouteSource(bookingSource);
        objBookingBE.setRouteDestination(bookingDestination);

        objBookingBE.setRouteCost(bookingPrice);
        objBookingBE.setRouteTime(bookingTime);

        objBookingBE.setEmailId(txtEmail);

        final Calendar c = Calendar.getInstance();
        int yy,mm,dd;
        yy = c.get(Calendar.YEAR);
        mm = c.get(Calendar.MONTH);
        dd = c.get(Calendar.DAY_OF_MONTH);

        objBookingBE.setRoutedate(yy+"-"+mm+"-"+dd);*/

        new InsertBooking().execute();
    }

    private class InsertBooking extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {

            String result=objBookingBL.insertBooking(objBookingBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                if(!s.equals(Constant.WS_RESULT_FAILURE)){
                    Toast.makeText(getApplicationContext(),"Ride Successfully Booked",Toast.LENGTH_LONG).show();
                    Util.setSharedPrefrenceValue(SignIn.this, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_BOOKING_Run_ID, null);
                    Util.setSharedPrefrenceValue(SignIn.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_ROUTE_ID,null);
                    Util.setSharedPrefrenceValue(SignIn.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_SOURCE_id,null);
                    Util.setSharedPrefrenceValue(SignIn.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_DESTINATION_id,null);
                    Util.setSharedPrefrenceValue(SignIn.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_PRICE,null);
                    Util.setSharedPrefrenceValue(SignIn.this, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_BOOKING_TIME, null);
                    Util.setSharedPrefrenceValue(SignIn.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_LOOP_CREDIT,null);
                    startActivity(new Intent(SignIn.this,TicketScreen.class).putExtra("BookingID",s).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                else{

                }


            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                mProgressDialog.dismiss();
            }

        }
    }

    private void showDialogResponse(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText("D'oh!");
        tvMsg.setText("Sorry, something didn't quite work.");
        btnClosePopup.setText("Cancel");
        btnsave.setText("Try again?");


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           new ValidateDetails().execute(txtEmail,txtPassword,loginType);
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

    private void showDialogConnection(final Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText("No Internet");
        tvMsg.setText("Looks like you have no or very slow data connectivity.");
        btnClosePopup.setText("Cancel");
        btnsave.setText("Try again?");


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           new ValidateDetails().execute(txtEmail,txtPassword,loginType);
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

                finish();
            }
            return false;
        } else {

        }
        return true;
    }
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(applicationContext);
                    }
                    gcmID = gcmObj
                            .register(Constant.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + gcmID;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(gcmID)) {
                    Util.setSharedPrefrenceValue(applicationContext,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_GCM_ID,gcmID);
                } else {
                  /*  Toast.makeText(
                            applicationContext,
                            "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();*/
                }
            }
        }.execute(null, null, null);
    }
}
