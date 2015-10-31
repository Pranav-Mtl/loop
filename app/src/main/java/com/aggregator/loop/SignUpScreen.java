package com.aggregator.loop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.BE.SignUpBE;
import com.aggregator.BL.ApplyReferralBL;
import com.aggregator.BL.SignUpBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.appsee.Appsee;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class SignUpScreen extends AppCompatActivity implements View.OnClickListener {

    private TextView tvFName,tvLName,tvEmail,tvPassword,tvCPassword;
    EditText tvMobileNo;
    ImageButton btnBack;
    String txtFName,txtLName,txtEmail,txtPassword,txtCPassword,txtMobile;
    String payTM="",promoCode="",user_credit="",user_balance="";
    SignUpBL objSignUpBL;
    SignUpBE objSignUpBE;
    int count=0;
    Button tvSignIn,btnDone,btnReferrer;
    ProgressDialog mProgressDialog;
    TextView tvApplied;

    GoogleCloudMessaging gcmObj;

    Context applicationContext;
    String gcmID;

    int xx,yy;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Appsee.start("de8395d3ae424245b695b4c9d6642f71");

        applicationContext=getApplicationContext();

        Display display = getWindowManager().getDefaultDisplay();

        // Point size = new Point();
        // display.getSize(size);
        int width = display.getWidth();
        int height = display.getHeight();

        // System.out.println("width" + width + "height" + height);

        if(width>=1000 && height>=1500){
            xx=700;
            yy=800;
        }
        else if(width>=700 && height>=1000)
        {
            xx=500;
            yy=500;
        }
        else
        {
            xx=400;
            yy=500;
        }


        tvFName= (TextView) findViewById(R.id.signup_name);
        //tvLName= (TextView) findViewById(R.id.signup_lastname);
        tvEmail= (TextView) findViewById(R.id.signup_email);
        tvPassword= (TextView) findViewById(R.id.signup_password);
        tvCPassword= (TextView) findViewById(R.id.signup_cpassword);
        tvMobileNo= (EditText) findViewById(R.id.signup_mobile);
        tvSignIn= (Button) findViewById(R.id.signup_signin);
        tvApplied= (TextView) findViewById(R.id.signup_applied);

        //btnBack= (ImageButton) findViewById(R.id.signup_back);
        btnDone= (Button) findViewById(R.id.signup_done);
        btnReferrer= (Button) findViewById(R.id.signup_promo);

        objSignUpBL=new SignUpBL();
        objSignUpBE=new SignUpBE();

        mProgressDialog=new ProgressDialog(SignUpScreen.this);

        btnDone.setOnClickListener(this);
        //btnBack.setOnClickListener(this);
        btnReferrer.setOnClickListener(this);

        btnDone.setVisibility(View.VISIBLE);

        tvSignIn.setOnClickListener(this);




        tvMobileNo.setText("+91", TextView.BufferType.EDITABLE);
        tvMobileNo.setSelection(tvMobileNo.getText().length());

       /* tvMobileNo.setText("+374");
        Selection.setSelection(tvMobileNo.getText(), tvMobileNo.getText().length());*/

   /*     String code = "+91";
        tvMobileNo.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        tvMobileNo.setCompoundDrawablePadding(code.length()*10);*/


        // for name validation

        gcmID=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_GCM_ID);

        if(gcmID==null){
            if (checkPlayServices()) {
                registerInBackground();
            }
        }


        tvFName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if (tvFName.getText().toString().trim().length() == 0) {
                        tvFName.setError("required");
                    }
                    else
                        count++;

                    if(count==4)
                    {
                        btnDone.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        // for last name validation

/*
        tvLName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if (tvLName.getText().toString().trim().length() == 0) {
                        tvLName.setError("required");
                    }
                    else
                        count++;

                    if(count==6)
                    {
                        btnDone.setVisibility(View.VISIBLE);
                    }

                }
            }
        });


*/

        // for email-id validation

        tvEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if (tvEmail.getText().toString().trim().length() == 0) {
                        tvEmail.setError("required");
                    } else if (!Util.isEmailValid(tvEmail.getText().toString())) {
                        tvEmail.setError("Invalid Email-id");
                    }
                    else
                        count++;

                    if(count==4)
                    {
                        btnDone.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        // for mobile no validation

        tvMobileNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if(tvMobileNo.getText().toString().trim().length()==0)
                    {
                        tvMobileNo.setError("required");
                    }
                    else if (tvMobileNo.getText().toString().trim().length()!=13)
                    {
                        tvMobileNo.setError("Invalid Mobile No.");
                    }
                    else
                        count++;

                    if(count==4)
                    {
                        btnDone.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        // for password validation

        tvPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if(tvPassword.getText().toString().trim().length()==0)
                    {
                        tvPassword.setError("required");
                    }
                    else
                        count++;

                    if(count==4)
                    {
                        btnDone.setVisibility(View.VISIBLE);
                    }


                }
            }
        });

        // for confirm password validation

        tvCPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if(!tvCPassword.getText().toString().equals(tvPassword.getText().toString()))
                    {
                        tvCPassword.setError("Password Mismatch");
                    }
                    else
                        count++;

                    if(count==4)
                    {
                        btnDone.setVisibility(View.VISIBLE);
                    }


                }
            }
        });

        tvMobileNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Clear focus here from edittext
                    tvMobileNo.clearFocus();
                }
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();

        switch (id)
        {
            case R.id.signup_done:
                onDoneButtonClick();
                break;
            case R.id.signup_signin:
                startActivity(new Intent(getApplicationContext(),SignIn.class));
                break;
            case R.id.signup_promo:
                //initiatePopupWindow();
                showDialog(this);
                break;
            default:
                break;
        }
    }

    void onDoneButtonClick()
    {
        boolean flag=true;
        txtFName=tvFName.getText().toString();
//        txtLName=tvLName.getText().toString();
        txtEmail=tvEmail.getText().toString();
        txtPassword=tvPassword.getText().toString();
        txtCPassword=tvCPassword.getText().toString();
        txtMobile=tvMobileNo.getText().toString();


        if(txtFName.trim().length()==0)
        {
            tvFName.setError("Required");
            flag=false;
        }
        if(txtEmail.trim().length()==0)
        {
            tvEmail.setError("Required");
            flag=false;
        }
        if(txtPassword.trim().length()==0)
        {
            tvPassword.setError("Required");
            flag=false;
        }
        if(txtCPassword.trim().length()==0)
        {
            tvCPassword.setError("Required");
            flag=false;
        }
        if(txtMobile.trim().length()==0)
        {
            tvMobileNo.setError("Required");
            flag=false;
        }
        if(flag)
        {
            if(!Util.isEmailValid(txtEmail))
            {
                tvEmail.setError("Invalid Email-Id");
            }
            else if(!txtPassword.equals(txtCPassword)){
                tvCPassword.setError("Password Mismatch");
            }
            else if(txtMobile.trim().length()!=13){
                tvMobileNo.setError("Invalid Mobile No.");
            }
            else {
                if (Util.isInternetConnection(SignUpScreen.this)) {

                    txtMobile = txtMobile.replaceAll("\\s+", "");

                    txtMobile=txtMobile.substring(3);


                    String strOTP=Util.generatePIN();

                    Constant.OTP=strOTP;

                    //http://appslure.in/loop/webservices/signup.php?name=name&email=abcd@gmail.com&password=name&phone_no=9876543210&paytm=paytm&promocode=promocode&otp=otp

                    new InsertData().execute(txtFName,txtEmail,txtPassword,txtMobile, payTM, promoCode,Constant.OTP,user_credit,user_balance,gcmID);
                }
                else {

                    showDialogConnection(this);

                }
            }

        }

    }


    private class InsertData extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {

            String result=objSignUpBL.insertSignUpDetails(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try
            {
                if(result.equalsIgnoreCase(Constant.WS_RESULT_SUCCESS)){
                    //Toast.makeText(getApplicationContext(),"You are successfully registered",Toast.LENGTH_LONG).show();

                    Util.setSharedPrefrenceValue(SignUpScreen.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_EMAIL,txtEmail);
                    Util.setSharedPrefrenceValue(SignUpScreen.this, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_PHONE, txtMobile);
                    Intent intent = new Intent(getApplicationContext(), OTP.class);
                    intent.putExtra("Mobile",txtMobile);
                    intent.putExtra("Previous","SignUp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    Snackbar
                            .make(findViewById(R.id.signup_root),
                                    "Mobile no. already exists",
                                    Snackbar.LENGTH_LONG).setText("Mobile no. already exists")
                            .show();


                    tvMobileNo.setError("Mobile no. already exists");
                    //Toast.makeText(getApplicationContext(),"Email address already exists",Toast.LENGTH_LONG).show();
                }
            }
            catch (NullPointerException e)
            {
               showDialogResponse(SignUpScreen.this);
            }
            finally {
                mProgressDialog.dismiss();
            }
        }
    }

    PopupWindow pwindo;
    private void initiatePopupWindow() {
        final EditText edittext;
        TextView tvTitle;
        Button btnClosePopup,btnsave;
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) SignUpScreen.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.signup_referrer_code,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 500, 550, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            edittext= (EditText) layout.findViewById(R.id.signup_referrer);
            btnClosePopup = (Button) layout.findViewById(R.id.referrer_cancel);
            btnsave= (Button) layout.findViewById(R.id.referrer_add);




            btnClosePopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                    pwindo.dismiss();
                }
            });

            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String subcategory=edittext.getText().toString();
                    if(subcategory.trim().length()>0) {
                        promoCode=subcategory;
                    }
                    pwindo.dismiss();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog(final Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate
        final EditText edittext;
        final TextView tvMsg;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.signup_referrer_code);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;



        edittext= (EditText) dialog.findViewById(R.id.signup_referrer);
        btnClosePopup = (Button) dialog.findViewById(R.id.referrer_cancel);
        btnsave= (Button) dialog.findViewById(R.id.referrer_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_msg);


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subcategory = edittext.getText().toString();
                if (subcategory.trim().length() > 0) {

                    try {
                        String result = new ApplyReferral().execute(subcategory).get();

                        if(result.equals(Constant.WS_RESULT_SUCCESS)){
                            promoCode = subcategory;
                            tvApplied.setVisibility(View.VISIBLE);
                            btnReferrer.setEnabled(false);
                            Toast.makeText(context,"Referral code applied",Toast.LENGTH_SHORT).show();

                            /* call google analytics*/
                            try {
                                Application.tracker().setScreenName("Register Screen");
                                Application.tracker().send(new HitBuilders.EventBuilder()
                                        .setLabel("Not logged-in")
                                        .setCategory("Ref pop up")
                                        .setAction("Apply Button")
                                        .setValue(Integer.valueOf(promoCode))
                                        .build());
                                // AffleInAppTracker.inAppTrackerViewName(getApplicationContext(), "Landing Screen", "App First Screen", "APP Open", null);

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                        else {
                            tvMsg.setText("Invalid referral code!");
                        }

                    }
                    catch (Exception e){

                    }

                    }
                 //   dialog.dismiss();

                }
            }

            );


            dialog.show();
        }

        private class ApplyReferral extends AsyncTask<String,String,String>{
        ApplyReferralBL objApplyReferralBL=new ApplyReferralBL();
        @Override
        protected String doInBackground(String... params) {
            String result=objApplyReferralBL.applyReferralCode(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
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

                                           new InsertData().execute(txtFName,txtEmail,txtPassword,txtMobile, payTM, promoCode,Constant.OTP,user_credit,user_balance);
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

                                           new InsertData().execute(txtFName,txtEmail,txtPassword,txtMobile, payTM, promoCode,Constant.OTP,user_credit,user_balance);
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
