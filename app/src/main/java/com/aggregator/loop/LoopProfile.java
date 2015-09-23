package com.aggregator.loop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aggregator.Adapters.DrawerAdapter;
import com.aggregator.BL.GettingProfileInformation;
import com.aggregator.BL.LoopProfileBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.appsee.Appsee;
import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;


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

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;

    DrawerAdapter drawerAdapter;

    View _itemColoured;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loop_profile);

        Appsee.start("de8395d3ae424245b695b4c9d6642f71");

        objLoopProfileBL=new LoopProfileBL();
        gettingProfileInformation=new GettingProfileInformation();
        pd=new ProgressDialog(LoopProfile.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        userEmail.setEnabled(false);
        userMobileNum.setEnabled(false);


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);

        drawerAdapter = new DrawerAdapter(Constant.TITLES,Constant.ICONS, Constant.NAME, Constant.LoopCredit,Constant.PayTMWalet, getApplicationContext());       // Creating the Adapter of com.example.balram.sampleactionbar.MyAdapter class(which we are going to see in a bit)

        // And passing the titles,icons,header view name, header view email,
        // and header  view profile picture
        mRecyclerView.setAdapter(drawerAdapter);

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();



        try {

            if(Util.isInternetConnection(LoopProfile.this)){
                new GettingProfileRecord().execute(userId);
            }
            else{
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(LoopProfile.this);

                alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);

                alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);

                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                startActivity(new Intent(Settings.ACTION_SETTINGS));
                            }
                        });

                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                dialog.cancel();
                            }
                        });


                alertDialog2.show();
            }

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
                Intent intent = new Intent(getApplicationContext(), RouteNew.class);
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
                userName.setSelection(userName.getText().length());
                userName.setFocusable(true);



            }
        });

        mobileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail.setEnabled(true);
                saveBtn.setVisibility(View.VISIBLE);
                userEmail.setSelection(userEmail.getText().length());
                userEmail.setFocusable(true);


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
                if(newPassword.length()==0 )
                {
                    newPass.setError("Required");
                }
                else {
                    if (newPassword.equals(confirmPassword)) {
                        new UpdateProfile().execute(name, email, mobileNumber, password);
                    } else {
                        Toast.makeText(getApplicationContext(), "New password and Confirm password must be same", Toast.LENGTH_LONG).show();
                    }
                }
            }

            else
            {
                Toast.makeText(getApplicationContext(), "Old password didn't matched", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            if(email.length()==0){
                userEmail.setError("Required");
                Toast.makeText(getApplicationContext(), "Please enter Email-id.", Toast.LENGTH_SHORT).show();
            }
            else {
                new UpdateProfile().execute(name, email, mobileNumber, Constant.password);
            }


        }




}
});


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        if (position != 0) {
                            if (_itemColoured != null) {
                                _itemColoured.setBackgroundColor(Color.parseColor("#66daae"));
                                _itemColoured.invalidate();
                            }
                            _itemColoured = view;
                            view.setBackgroundColor(Color.parseColor("#1fc796"));
                        }

                        if (position == 0) {
                            Drawer.closeDrawers();
                        } else if (position == 1) {
                            startActivity(new Intent(getApplicationContext(), RouteNew.class));
                        } else if (position == 2) {
                            startActivity(new Intent(getApplicationContext(), TripHistory.class));
                        } else if (position == 3) {
                            startActivity(new Intent(getApplicationContext(), PromoCode.class));
                        } else if (position == 4) {
                            startActivity(new Intent(getApplicationContext(), InviteActivity.class));
                        } else if (position == 8) {
                            startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                        } else if (position == 7) {

                        } else if (position == 6) {
                            //startActivity(new Intent(getApplicationContext(),TripFeedback.class));
                        }

                    }


                }));

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
               NoResponseServer();
            }
            catch (Exception e){
               NoResponseServer();
            }
            finally {
                pd.dismiss();
            }




        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerAdapter.notifyDataSetChanged();
    }


    private void NoResponseServer()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Constant.ERR_NO_SERVER_RESPONSE)
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        new GettingProfileRecord().execute(userId);
                    }
                });


        final AlertDialog alert = builder.create();
        alert.show();
    }
}

