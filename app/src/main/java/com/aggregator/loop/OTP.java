package com.aggregator.loop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.BE.BookingBE;
import com.aggregator.BL.BookingBL;
import com.aggregator.BL.OTPBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.appsee.Appsee;

public class OTP extends AppCompatActivity implements View.OnClickListener {

    Button btnDone,btnResend,btnNot;
    EditText etOTP;
    TextView tvOTPMessage;

    OTPBL objOtpbl;

    ProgressDialog mProgressDialog;

    String emailID;

    String bookingRouteId;
    String bookingSource;
    String bookingDestination;
    String bookingDate;
    String bookingTime;
    String bookingPrice;

    BookingBE objBookingBE;
    BookingBL objBookingBL;

    String mobile;

    String msg="A one time password is being sent as an SMS to +91-";

    int xx,yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Display display = getWindowManager().getDefaultDisplay();

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


        /*ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        etOTP= (EditText) findViewById(R.id.otp);
        btnDone= (Button) findViewById(R.id.otp_done);
        btnResend= (Button) findViewById(R.id.otp_resend);
        tvOTPMessage= (TextView) findViewById(R.id.otp_msg);
        btnNot= (Button) findViewById(R.id.otp_not);

        objOtpbl=new OTPBL();
        objBookingBE=new BookingBE();
        objBookingBL=new BookingBL();

        mProgressDialog=new ProgressDialog(OTP.this);

        mobile=getIntent().getExtras().get("Mobile").toString();
        String status=getIntent().getExtras().get("Previous").toString();

        if(status.equals("Login")){
            String txtOTP=Util.generatePIN();
            Constant.OTP=txtOTP;
            new OTPResend().execute(mobile,txtOTP);
        }

        msg=msg+mobile;

        tvOTPMessage.setText(msg);




        btnDone.setOnClickListener(this);
        btnResend.setOnClickListener(this);
        btnNot.setOnClickListener(this);
//        btnBack.setOnClickListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.otp_done:
                    validate();
                break;
            case R.id.otp_resend:
                String txtOTP=Util.generatePIN();
                Constant.OTP=txtOTP;
                new OTPResend().execute(mobile,txtOTP);
                break;
            case R.id.otp_not:
                startActivity(new Intent(getApplicationContext(),RouteNew.class));
                finish();
                break;

        }
    }

    private void validate()
    {
        String txtOTp=etOTP.getText().toString().trim();

        if(txtOTp.length()==0)
        {
            etOTP.setError("Please enter otp");
        }
        else if(txtOTp.length()!=4)
        {
            etOTP.setError("Invalid OTP");
            //new UpdateStatus().execute(emailID);
        }
        else if(!txtOTp.equals(Constant.OTP)){
            etOTP.setError("Incorrect OTP");
        }
        else
        {
            new UpdateStatus().execute(mobile);
        }

    }

    private class UpdateStatus extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String result=objOtpbl.updateStatus(params[0],getApplicationContext());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                if(s.equals(Constant.WS_RESULT_SUCCESS))
                {
                    Util.setSharedPrefrenceValue(OTP.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_LOGIN_STATUS,emailID);
                    bookingRouteId=Util.getSharedPrefrenceValue(OTP.this, Constant.SHARED_PREFERENCE_BOOKING_ROUTE_ID);
                    if(bookingRouteId==null)
                    {
                        startActivity(new Intent(OTP.this,RouteNew.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(OTP.this, RouteNew.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                       /* objBookingBE.setRouteID(Util.getSharedPrefrenceValue(OTP.this, Constant.SHARED_PREFERENCE_BOOKING_ROUTE_ID));
                        objBookingBE.setRunID(Util.getSharedPrefrenceValue(OTP.this, Constant.SHARED_PREFERENCE_BOOKING_Run_ID));
                        objBookingBE.setStartPoint(Util.getSharedPrefrenceValue(OTP.this, Constant.SHARED_PREFERENCE_BOOKING_SOURCE_id));
                        objBookingBE.setEndPoint(Util.getSharedPrefrenceValue(OTP.this, Constant.SHARED_PREFERENCE_BOOKING_DESTINATION_id));
                        objBookingBE.setTime(Util.getSharedPrefrenceValue(OTP.this, Constant.SHARED_PREFERENCE_BOOKING_TIME));
                        objBookingBE.setPrice((Util.getSharedPrefrenceValue(OTP.this, Constant.SHARED_PREFERENCE_BOOKING_PRICE)));
                        objBookingBE.setUserID(Util.getSharedPrefrenceValue(OTP.this, Constant.SHARED_PREFERENCE_User_id));
                        objBookingBE.setLoopCredit(Util.getSharedPrefrenceValue(OTP.this,Constant.SHARED_PREFERENCE_BOOKING_LOOP_CREDIT));

                        booking();*/
                    }
                }
                else {
                    etOTP.setError("Verification Failed");
                }
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                mProgressDialog.dismiss();
            }


        }
    }

    private void booking()
    {
      /*  objBookingBE.setRouteID(bookingRouteId);
        objBookingBE.setRouteSource(bookingSource);
        objBookingBE.setRouteDestination(bookingDestination);

        objBookingBE.setRouteCost(bookingPrice);
        objBookingBE.setRouteTime(bookingTime);

        objBookingBE.setEmailId(emailID);

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
                    Util.setSharedPrefrenceValue(OTP.this, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_BOOKING_Run_ID, null);
                    Util.setSharedPrefrenceValue(OTP.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_ROUTE_ID,null);
                    Util.setSharedPrefrenceValue(OTP.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_SOURCE_id,null);
                    Util.setSharedPrefrenceValue(OTP.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_DESTINATION_id,null);
                    Util.setSharedPrefrenceValue(OTP.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_PRICE,null);
                    Util.setSharedPrefrenceValue(OTP.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_TIME,null);
                    Util.setSharedPrefrenceValue(OTP.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_LOOP_CREDIT,null);
                    startActivity(new Intent(OTP.this,TicketScreen.class).putExtra("BookingID",s).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

    @Override
    public void onBackPressed() {

    }


    private class OTPResend extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String result=objOtpbl.resendOTP(params[0],params[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                if(s.equals(Constant.WS_RESULT_SUCCESS)){
                    Toast.makeText(getApplicationContext(),"OTP resent",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Problem with OTP Resend ",Toast.LENGTH_LONG).show();
                }
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                mProgressDialog.dismiss();
            }
        }
    }
}
