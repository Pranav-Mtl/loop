package com.aggregator.loop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.Adapters.DrawerAdapter;
import com.aggregator.BL.SendCommentBL;
import com.aggregator.BL.TripFeedbackBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.appsee.Appsee;
import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;


public class TripFeedback extends AppCompatActivity implements View.OnClickListener {
    TextView date,pick,drop,rate;
    ProgressDialog pd;
    TripFeedbackBL tripFeedbackBL;
    RatingBar ratingBar;
    LinearLayout llIssues;
    Button btnFeedback;
    int rateBar;
    CheckBox one,two,three,four,five,six;
    EditText comment;
    String userRunID;
    SendCommentBL sendCommentBL;

    String usedID;

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
        setContentView(R.layout.activity_trip_feedback);

        Appsee.start("de8395d3ae424245b695b4c9d6642f71");

        pd=new ProgressDialog(TripFeedback.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pd=new ProgressDialog(TripFeedback.this);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        date=(TextView)findViewById(R.id.date);
        pick=(TextView)findViewById(R.id.pick);
        drop=(TextView)findViewById(R.id.drop);
        rate=(TextView)findViewById(R.id.rate);
        ratingBar= (RatingBar) findViewById(R.id.feedback_rating);
        llIssues= (LinearLayout) findViewById(R.id.ll_trip_issues);
        btnFeedback= (Button) findViewById(R.id.feedback_done);
        one=(CheckBox)findViewById(R.id.checkbox1);
        two=(CheckBox)findViewById(R.id.checkbox2);
        three=(CheckBox)findViewById(R.id.checkbox3);
        four=(CheckBox)findViewById(R.id.checkbox4);
        five=(CheckBox)findViewById(R.id.checkbox5);
        six=(CheckBox)findViewById(R.id.checkbox6);
        comment=(EditText)findViewById(R.id.comment);
        sendCommentBL=new SendCommentBL();

        final Drawable d = btnFeedback.getBackground();


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


        usedID=Util.getSharedPrefrenceValue(TripFeedback.this,Constant.SHARED_PREFERENCE_User_id);

        userRunID=getIntent().getExtras().get("RunID").toString();

        btnFeedback.setBackgroundColor(Color.parseColor("#e4e4e4"));

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

               btnFeedback.setBackground(d);

                rateBar = (int) ratingBar.getRating();

                if (rateBar < 4) {
                    llIssues.setVisibility(View.VISIBLE);
                } else {
                    llIssues.setVisibility(View.GONE);
                }
            }
        });


        try{
            tripFeedbackBL=new TripFeedbackBL();
            if(Util.isInternetConnection(TripFeedback.this)){
                new FetchRecord().execute(userRunID,usedID);
            }
            else{
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(TripFeedback.this);

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
        catch(Exception e)
        {
            e.printStackTrace();
        }


        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalString=null;
                String text=null;
                if(one.isChecked()) {
                    String check1 = one.getText().toString();
                    finalString=check1;
                }
                if(two.isChecked()) {
                    String check2 = two.getText().toString();
                            if(finalString!=null&&check2!=null)
                              {
                                  System.out.println("above bothe are null");
                               finalString = finalString + "," + check2;
                               }
                    else
                            {
                                finalString = check2;
                            }
                }
                 if(three.isChecked()) {
                    String check3 = three.getText().toString();
                     if(finalString!=null&&check3!=null)
                     {
                         finalString = finalString + "," + check3;
                     }
                     else
                     {
                         finalString = check3;
                     }

                }
                  if(four.isChecked()) {
                    String check4 = four.getText().toString();
                      if(finalString!=null&&check4!=null)
                      {
                          finalString = finalString + "," + check4;
                      }
                      else
                      {
                          finalString = check4;
                      }
                }
                 if(five.isChecked()) {
                    String check5 = five.getText().toString();
                     if(finalString!=null&&check5!=null)
                     {
                         finalString = finalString + "," + check5;
                     }
                     else
                     {
                         finalString = check5;
                     }
                }
                 if(six.isChecked()) {
                    String check6 = six.getText().toString();
                     if(finalString!=null&&check6!=null)
                     {
                         finalString = finalString + "," + check6;
                     }
                     else
                     {
                         finalString = check6;
                     }
                }
                text=comment.getText().toString();
                if(text!=null){
                        if(finalString==null)
                            {
                              finalString = "No Issues" + "," + text;
                             }
                        else {
                          finalString = finalString + "," + text;
                         }

                }
               String rating=String.valueOf(rateBar);
                String id= Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_User_id);
                System.out.println("checkboxex selected by the user------>"+finalString);
                new SendComment().execute(id,rating,finalString,userRunID);



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
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    private class FetchRecord extends AsyncTask<String ,String ,String>
    {


        @Override
        protected void onPreExecute() {
            pd.show();
            pd.setMessage("Loading...");
            pd.setCancelable(false);
        }


        @Override
        protected String doInBackground(String... params) {
            String result=tripFeedbackBL.sendId(params[0], params[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            try
            {
                pick.setText(Constant.pickPoint);
                drop.setText(Constant.dropPoint);
                date.setText(Constant.date);
                rate.setText("Cost : ₹ "+Constant.totalAmount+" (Loop Credit: ₹ "+Constant.rateCredit+", Paytm: ₹ "+Constant.rate+")");

                if(s.equalsIgnoreCase("y")){
                    ratingBar.setRating(Constant.feedback_rating);
                    ratingBar.setIsIndicator(true);
                    btnFeedback.setVisibility(View.INVISIBLE);
                    llIssues.setVisibility(View.GONE);
                    comment.setEnabled(false);
                }
            }
            catch (NullPointerException e)
            {
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


    private class SendComment extends AsyncTask<String ,String ,String>
    {


        @Override
        protected void onPreExecute() {
            pd.show();
            pd.setMessage("Loading...");
            pd.setCancelable(false);

        }


        @Override
        protected String doInBackground(String... params) {
            String result=sendCommentBL.sendComment(params[0], params[1], params[2],params[3]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equals(Constant.WS_RESULT_SUCCESS)) {
                    Toast.makeText(getApplicationContext(), "Feedback Sent Successfully", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "You have already sent feedback for this trip.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            catch (NullPointerException e){
                e.printStackTrace();
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
                        new FetchRecord().execute(userRunID,usedID);
                    }
                });


        final AlertDialog alert = builder.create();
        alert.show();
    }
}
