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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.aggregator.Adapters.CardAdapter;
import com.aggregator.Adapters.DrawerAdapter;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.appsee.Appsee;

public class TripHistory extends AppCompatActivity {

    RecyclerView recList;
    CardAdapter cd;

    String userId;

    com.twotoasters.android.support.v7.widget.RecyclerView mRecyclerView;                           // Declaring RecyclerView
    com.twotoasters.android.support.v7.widget.RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    com.twotoasters.android.support.v7.widget.RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;

    DrawerAdapter drawerAdapter;

    View _itemColoured;

    ProgressDialog mProgressDialog;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Appsee.start("de8395d3ae424245b695b4c9d6642f71");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressDialog=new ProgressDialog(TripHistory.this);
        mRecyclerView = (com.twotoasters.android.support.v7.widget.RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);

        drawerAdapter = new DrawerAdapter(Constant.TITLES,Constant.ICONS, Constant.NAME, Constant.LoopCredit,Constant.PayTMWalet, getApplicationContext());       // Creating the Adapter of com.example.balram.sampleactionbar.MyAdapter class(which we are going to see in a bit)

        // And passing the titles,icons,header view name, header view email,
        // and header  view profile picture
        mRecyclerView.setAdapter(drawerAdapter);

        mLayoutManager = new com.twotoasters.android.support.v7.widget.LinearLayoutManager(this);                 // Creating a layout Manager
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




        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);

       final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);



        userId= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SHARED_PREFERENCE_User_id);

        if(Util.isInternetConnection(TripHistory.this)) {
            new GetHistory().execute(userId);
        }
        else{
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(TripHistory.this);

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
                            Drawer.closeDrawers();
                        } else if (position == 3) {
                            startActivity(new Intent(getApplicationContext(), PromoCode.class));
                        } else if (position == 4) {
                            startActivity(new Intent(getApplicationContext(), InviteActivity.class));
                        } else if (position == 9) {
                            startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                        } else if (position == 7) {

                        }else if (position == 6) {
                            startActivity(new Intent(getApplicationContext(),SuggestRoute.class));
                        }

                    }


                }));


        recList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();

                Log.d("visible item count",visibleItemCount+"");
                Log.d("total item count",totalItemCount+"");
                Log.d("past visible item",pastVisiblesItems+"");

                if (loading) {
                    if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        Log.v("...", "Last Item Wow !");
                    }
                }


            }
        });


    }

   private class GetHistory extends AsyncTask<String,String,String>{

       @Override
       protected void onPreExecute() {
           mProgressDialog.show();
           mProgressDialog.setMessage("Loading...");
           mProgressDialog.setCancelable(false);
       }

       @Override
       protected String doInBackground(String... params) {
           cd=new CardAdapter(TripHistory.this,params[0]);
           return "";
       }

       @Override
       protected void onPostExecute(String s) {
           try {
               recList.setAdapter(cd);
           } catch (NullPointerException e){
              NoResponseServer();
           }
           finally {
               mProgressDialog.dismiss();
           }
       }
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
                        new GetHistory().execute(userId);
                    }
                });


        final AlertDialog alert = builder.create();
        alert.show();
    }
}
