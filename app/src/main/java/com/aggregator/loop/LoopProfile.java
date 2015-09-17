package com.aggregator.loop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aggregator.BL.GettingProfileInformation;
import com.aggregator.BL.LoopProfileBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;


public class LoopProfile extends ActionBarActivity {
    ImageButton arroBtn;
    LinearLayout showPassword,btnClick;
    boolean flag=true;
    boolean flag1=true;
    EditText userName,userMobileNum,oldPass,newPass,confirmPass,userEmail;
    ImageView mobileBtn;
    String newPassword,confirmPassword;
    String name,mobileNumber,password;
    ImageButton nameBtn;
    Button saveBtn;
    LoopProfileBL objLoopProfileBL;
    ProgressDialog pd;
    GettingProfileInformation gettingProfileInformation;
    String MobilePattern = "[0-9]{10}";
    ImageButton back;
    Button btnSignOUT;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loop_profile);
        objLoopProfileBL=new LoopProfileBL();
        gettingProfileInformation=new GettingProfileInformation();
        pd=new ProgressDialog(LoopProfile.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        userId= Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_User_id);

        userName=(EditText)findViewById(R.id.userName);
        userMobileNum=(EditText)findViewById(R.id.userMobile);
        oldPass=(EditText)findViewById(R.id.userOldPadd);
        newPass=(EditText)findViewById(R.id.userNewPass);
        confirmPass=(EditText)findViewById(R.id.userConfirmPass);
        nameBtn=(ImageButton)findViewById(R.id.nameBtn);
        mobileBtn=(ImageView)findViewById(R.id.numberBtn);
        showPassword=(LinearLayout)findViewById(R.id.pass_layout);
        arroBtn=(ImageButton)findViewById(R.id.arrow_btn);
        saveBtn=(Button)findViewById(R.id.saveBtn);
        userEmail=(EditText)findViewById(R.id.userEmail);
        btnClick=(LinearLayout)findViewById(R.id.arrow_btn1);

        btnSignOUT= (Button) findViewById(R.id.profile_signout);


        userName.setEnabled(false);
        userMobileNum.setEnabled(false);


        try {
            new GettingProfileRecord().execute(userId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        arroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBtn.setVisibility(View.VISIBLE);
                if (flag) {
                    showPassword.setVisibility(View.VISIBLE);
                    arroBtn.setBackgroundResource(R.drawable.arrow_below);
                    flag = false;
                } else if (!flag) {
                    showPassword.setVisibility(View.GONE);
                    arroBtn.setBackgroundResource(R.drawable.arrow_btn);
                    flag = true;
                }
            }
        });

        btnSignOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_User_id, null);
                Intent intent = new Intent(getApplicationContext(), Routes.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveBtn.setVisibility(View.VISIBLE);

                if(flag1) {
                    showPassword.setVisibility(View.VISIBLE);
                    arroBtn.setBackgroundResource(R.drawable.arrow_below);
                    flag1=false;
                }
                else  if(!flag1)
                {
                    showPassword.setVisibility(View.GONE);
                    arroBtn.setBackgroundResource(R.drawable.arrow_btn);
                    flag1=true;
                }
            }
        });





        nameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setEnabled(true);
                saveBtn.setVisibility(View.VISIBLE);



            }
        });


/* SAVE BUTTON CLICK EVENT */
    saveBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        mobileNumber=userMobileNum.getText().toString();
        String oldpassword=oldPass.getText().toString();
        name=userName.getText().toString();
        newPassword=newPass.getText().toString();
        confirmPassword=confirmPass.getText().toString();
        password=confirmPassword;
        String email=userEmail.getText().toString();

        if(!oldpassword.isEmpty()) {
            if (Constant.password.equals(oldpassword))
            {
                if (newPassword.equals(confirmPassword))
                {
                    new UpdateProfile().execute(name, email, mobileNumber, password);
                }

                else {
                    Toast.makeText(getApplicationContext(), "New password and Confirm password must be same", Toast.LENGTH_LONG).show();
                }
            }

            else
            {
                Toast.makeText(getApplicationContext(), "Old password did not match", Toast.LENGTH_LONG).show();
            }
        }

        else
        {

            new UpdateProfile().execute(name, email, mobileNumber,Constant.password);


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
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* UPDATE PROFILE DATA */
    private class UpdateProfile extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            pd.show();
            pd.setMessage("Loading...");
            pd.setCancelable(false);
        }
        @Override
        protected String doInBackground(String... params) {
            String result=objLoopProfileBL.getProfileData(params[0], params[1], params[2], params[3]);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equals(Constant.WS_RESULT_SUCCESS)) {
                    finish();
                    Toast.makeText(getApplicationContext(), "Profile Updated successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
                }
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                pd.dismiss();
            }



        }
    }

/* FETCHING USER DETAILS*/
    private class GettingProfileRecord extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            pd.show();
            pd.setMessage("Loading...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            gettingProfileInformation.getData(params[0]);
            return "pp";
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                userName.setText(Constant.usrname);
                userMobileNum.setText(Constant.phoneNumber);
                userEmail.setText(Constant.emaiId);
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                pd.dismiss();
            }




        }
    }

}

