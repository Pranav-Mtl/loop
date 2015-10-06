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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aggregator.Adapters.DrawerAdapter;
import com.aggregator.BL.SuggestRouteBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;

public class SuggestRoute extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;

    DrawerAdapter drawerAdapter;
    View _itemColoured;

    ProgressDialog mProgressDialog;

    SuggestRouteBL objSuggestRouteBL;

    String userID;
    EditText etMessage;
    Button btnDone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_route);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        mProgressDialog=new ProgressDialog(SuggestRoute.this);
        objSuggestRouteBL=new SuggestRouteBL();
        userID= Util.getSharedPrefrenceValue(SuggestRoute.this,Constant.SHARED_PREFERENCE_User_id);

        etMessage= (EditText) findViewById(R.id.suggestion_message);
        btnDone= (Button) findViewById(R.id.submitBtn);

        btnDone.setOnClickListener(this);


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
                            startActivity(new Intent(getApplicationContext(), PromoCode.class));
                        } else if (position == 4) {
                            startActivity(new Intent(getApplicationContext(), InviteActivity.class));
                        } else if (position == 9) {
                            startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                        } else if (position == 7) {

                        } else if (position == 6) {
                            Drawer.closeDrawers();

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
           case R.id.submitBtn:

               if(etMessage.getText().toString().length()!=0){
                   new SendMessage().execute(userID,etMessage.getText().toString());
               }
               else
               etMessage.setError("Required...");
               break;
        }
    }


    private class SendMessage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            String result=objSuggestRouteBL.sendMessage(params[0], params[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            try{

                if(s.equals(Constant.WS_RESULT_SUCCESS))
                {
                    Toast.makeText(getApplicationContext(), "Thanks. Your suggestion has been sent to loop team.", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_LONG).show();
                }

            }
            catch (NullPointerException e){

            }
            catch (Exception e){

            }
            finally {
                mProgressDialog.dismiss();
            }
        }
    }

}
