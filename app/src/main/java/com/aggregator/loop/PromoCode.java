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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aggregator.Adapters.DrawerAdapter;
import com.aggregator.BL.PromoCodeBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.appsee.Appsee;
import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;

public class PromoCode extends AppCompatActivity implements View.OnClickListener {

    EditText etPromocode;
    Button btnVerify;
    String userID;
    PromoCodeBL objPromoCodeBL;
    ProgressDialog mPrograProgressDialog;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;

    DrawerAdapter drawerAdapter;

    View _itemColoured;
    RadioGroup rg;
    RadioButton rbPromo,rbReferrer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code);

        Appsee.start("de8395d3ae424245b695b4c9d6642f71");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        etPromocode= (EditText) findViewById(R.id.promocode);
        btnVerify= (Button) findViewById(R.id.promocode_done);

        btnVerify.setOnClickListener(this);

        mPrograProgressDialog=new ProgressDialog(PromoCode.this);

        objPromoCodeBL=new PromoCodeBL();

        userID= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SHARED_PREFERENCE_User_id);

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
                            startActivity(new Intent(getApplicationContext(), LoopProfile.class));
                        } else if (position == 1) {
                            startActivity(new Intent(getApplicationContext(), RouteNew.class));
                        } else if (position == 2) {
                            startActivity(new Intent(getApplicationContext(), TripHistory.class));
                        } else if (position == 3) {
                            Drawer.closeDrawers();
                        } else if (position == 4) {
                            startActivity(new Intent(getApplicationContext(), InviteActivity.class));
                        } else if (position == 9) {
                            startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                        } else if (position == 7) {

                        } else if (position == 6) {
                            startActivity(new Intent(getApplicationContext(),SuggestRoute.class));
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
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.promocode_done:
                String text=etPromocode.getText().toString();

                if(text.length()==0){
                    etPromocode.setError("Enter PromoCode");
                }
                else
                {
                    if(Util.isInternetConnection(PromoCode.this)) {
                        new ApplyCode().execute(userID, text);
                    }
                    else{
                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(PromoCode.this);

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
                break;
        }
    }


    private class ApplyCode extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            mPrograProgressDialog.show();
            mPrograProgressDialog.setMessage("Loading...");
            mPrograProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String result=objPromoCodeBL.sendCode(params[0],params[1]);
            return result;
        }
        
        @Override
        protected void onPostExecute(String s) {
            try {
                if(s.equals(Constant.WS_RESULT_SUCCESS)){
                    Log.d("Loop Credit",Constant.LoopCreditUsed);
                    Toast.makeText(getApplicationContext(),"₹"+Constant.LoopCreditUsed+" Loop Credited to your account. ",Toast.LENGTH_LONG).show();
                    finish();
                }
                else if(s.equals("used"))
                {
                    Toast.makeText(getApplicationContext(),"Referral code can be applied only once and before first booking.",Toast.LENGTH_LONG).show();
                    //etPromocode.setError("The promo/referral code is invalid.");
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"The promo/referral code is invalid.",Toast.LENGTH_LONG).show();
                    etPromocode.setError("The promo/referral code is invalid.");
                }


            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception e){

            }
            finally {
                mPrograProgressDialog.dismiss();
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
}
