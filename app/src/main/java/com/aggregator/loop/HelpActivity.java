package com.aggregator.loop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aggregator.Adapters.DrawerAdapter;
import com.aggregator.BL.HelpActivityBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;


public class HelpActivity extends AppCompatActivity {
    EditText message;
    Button submit;
    ProgressDialog pd;
    HelpActivityBL helpActivityBL;


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
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        helpActivityBL=new HelpActivityBL();
        message=(EditText)findViewById(R.id.message);
        submit=(Button)findViewById(R.id.submitBtn);
        pd=new ProgressDialog(HelpActivity.this);

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





        final String userId= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SHARED_PREFERENCE_User_id);
        if(userId==null)
        {
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();

                new SendMessage().execute(userId, msg);
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
                                startActivity(new Intent(getApplicationContext(), LoopProfile.class));
                            } else if (position == 1) {

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
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class SendMessage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            pd.show();
            pd.setMessage("Loading...");
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            String result=helpActivityBL.sendMessage(params[0], params[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            try{
                Log.d("Help Result--->",s);
                if(s.equals("1"))
                {
                    Toast.makeText(getApplicationContext(), "Thanks. Your query has been sent to loop team.", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "please try again", Toast.LENGTH_LONG).show();
                }


            }
            catch (NullPointerException e){

            }
            catch (Exception e){

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
}
