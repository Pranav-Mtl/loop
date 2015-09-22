package com.aggregator.loop;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.Adapters.DrawerAdapter;
import com.aggregator.BE.InviteActivityBE;
import com.aggregator.BL.InviteActivityBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;

import java.util.List;


public class InviteActivity extends AppCompatActivity {

    String promocode="";
    Button shareBtn;
    TextView whatsapp,facebook,twitter,smsText;
    InviteActivityBL inviteActivityBL;
    InviteActivityBE objInviteActivityBE;

    String userId;

    ProgressDialog mProgressDialog;
    TextView tvText;

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
        setContentView(R.layout.activity_invite);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        inviteActivityBL=new InviteActivityBL();
        objInviteActivityBE=new InviteActivityBE();

        mProgressDialog=new ProgressDialog(InviteActivity.this);

        shareBtn=(Button)findViewById(R.id.shareBtn);
        whatsapp=(TextView)findViewById(R.id.whatsapp);
        facebook=(TextView)findViewById(R.id.facebook);
        twitter=(TextView)findViewById(R.id.twitterthis);
        smsText=(TextView)findViewById(R.id.sms);
        tvText= (TextView) findViewById(R.id.inviteText);

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


        userId= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SHARED_PREFERENCE_User_id);

        try{

            new SharePromocode().execute(userId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }





        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, promocode);
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    ex.printStackTrace();
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }
            }
        });



        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, promocode);
                PackageManager pm = v.getContext().getPackageManager();
                List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
                for (final ResolveInfo app : activityList) {
                    if ((app.activityInfo.name).contains("facebook")) {
                        final ActivityInfo activity = app.activityInfo;
                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                        shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        shareIntent.setComponent(name);
                        v.getContext().startActivity(shareIntent);
                        break;
                    }
                }
            }
        });


        twitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //String msg = tweetText.getText().toString();

                if(checkTwitter()){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, promocode);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_STREAM, "");
                    intent.setPackage("com.twitter.android");
                    startActivity(intent);

                }
                else {
                    Toast.makeText(getApplicationContext(),"You need to install twitter app first.",Toast.LENGTH_SHORT).show();
                }



            }

        });


        smsText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //String msg = tweetText.getText().toString();
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("sms_body",promocode);
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);


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
                            Drawer.closeDrawers();
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


    private class SharePromocode extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
           String result= inviteActivityBL.getPromocode(params[0],objInviteActivityBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            try
            {
                if(s.equals(Constant.WS_RESULT_SUCCESS))
                {
                    //Toast.makeText(getApplicationContext(),"Succefully shared",Toast.LENGTH_LONG).show();
                    shareBtn.setText(objInviteActivityBE.getReferralCode());
                    promocode="Your Referral Code  for loop is :"+objInviteActivityBE.getReferralCode();
                    tvText.setText(" They get free rides worth ₹"+objInviteActivityBE.getReferralValue()+". And so do you !");

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Referral code is not available.Please come back later.", Toast.LENGTH_LONG).show();
                    finish();

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


    public boolean checkTwitter()
    {
        boolean flag=false;
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.twitter.android", 0);
            flag= true;
        } catch (PackageManager.NameNotFoundException e) {
            flag= false;
        }
        return flag;
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
