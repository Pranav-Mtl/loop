package com.aggregator.loop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aggregator.BE.BookingBE;
import com.aggregator.BL.BookingBL;
import com.aggregator.BL.SignInBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

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
                     new ValidateDetails().execute(txtEmail,txtPassword,loginType);
            else
            {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(SignIn.this);

// Setting Dialog Title
                alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);

// Setting Dialog Message
                alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);

// Setting Icon to Dialog


// Setting Positive "Yes" Btn
                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                startActivity(new Intent(Settings.ACTION_SETTINGS));
                            }
                        });
// Setting Negative "NO" Btn
                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                dialog.cancel();
                            }
                        });

// Showing Alert Dialog
                alertDialog2.show();

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

            String result=objSignInBL.validateSignInDetails(params[0],params[1],params[2],getApplicationContext());
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
                        startActivity(new Intent(SignIn.this,RouteNew.class));
                    }
                    else
                    {
                        mProgressDialog.dismiss();

                        objBookingBE.setRouteID(Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_BOOKING_ROUTE_ID));
                        objBookingBE.setRunID(Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_BOOKING_Run_ID));
                        objBookingBE.setStartPoint(Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_BOOKING_SOURCE_id));
                        objBookingBE.setEndPoint(Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_BOOKING_DESTINATION_id));
                        objBookingBE.setTime(Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_BOOKING_TIME));
                        objBookingBE.setPrice((Util.getSharedPrefrenceValue(SignIn.this, Constant.SHARED_PREFERENCE_BOOKING_PRICE)));
                        objBookingBE.setUserID(Util.getSharedPrefrenceValue(SignIn.this,Constant.SHARED_PREFERENCE_User_id));

                        booking();
                    }


                }
                else
                {
                    if(loginType.equals("email")) {
                        Snackbar
                                .make(findViewById(R.id.login_root),
                                        "Wrong Email or Password",
                                        Snackbar.LENGTH_LONG).setText("Wrong Email or Password")
                                .show();
                        etPassword.setError("Wrong Email or Password");
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
                    Util.setSharedPrefrenceValue(SignIn.this, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_BOOKING_Run_ID,null);
                    Util.setSharedPrefrenceValue(SignIn.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_ROUTE_ID,null);
                    Util.setSharedPrefrenceValue(SignIn.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_SOURCE_id,null);
                    Util.setSharedPrefrenceValue(SignIn.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_DESTINATION_id,null);
                    Util.setSharedPrefrenceValue(SignIn.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_PRICE,null);
                    Util.setSharedPrefrenceValue(SignIn.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_TIME,null);
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
}
