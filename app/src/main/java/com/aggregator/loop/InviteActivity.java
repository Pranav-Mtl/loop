package com.aggregator.loop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.Adapters.DrawerAdapter;
import com.aggregator.BE.InviteActivityBE;
import com.aggregator.BL.InviteActivityBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.appsee.Appsee;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


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

    String msgStart;
    String msgTwitter;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    int xx,yy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        Appsee.start("de8395d3ae424245b695b4c9d6642f71");

        Display display = getWindowManager().getDefaultDisplay();

        int width = display.getWidth();
        int height = display.getHeight();

        // System.out.println("width" + width + "height" + height);

        if(width>=700 && height>=1000)
        {
            xx=500;
            yy=500;
        }
        else
        {
            xx=400;
            yy=500;
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);


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

            if(Util.isInternetConnection(InviteActivity.this)){
                new SharePromocode().execute(userId);
            }
            else
            {
                showDialogConnection(this);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }





        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setPackage("com.whatsapp");
                shareIntent.setType("image/png");
                Uri uri = Uri.parse("android.resource://com.aggregator.loop/"+R.drawable.share);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.putExtra(Intent.EXTRA_TEXT,msgStart);
                startActivity(Intent.createChooser(shareIntent, "Send your image"));

            }
        });



        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("image/png");
                Uri uri = Uri.parse("android.resource://com.aggregator.loop/"+R.drawable.share);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.putExtra(Intent.EXTRA_TEXT,msgStart);
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
                }*/


                Uri uri = Uri.parse("http://i61.tinypic.com/2rlyq0p.png");

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Loop")
                            .setContentDescription(msgStart)
                            .setContentUrl(Uri.parse("http://www.goinloop.com"))
                            .setImageUrl(uri)
                            .build();

                    shareDialog.show(linkContent);
                }
            }
        });


        twitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //String msg = tweetText.getText().toString();
               // Uri uri = Uri.parse("android.resource://com.aggregator.loop/"+R.drawable.share);

                if(checkTwitter()){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/*");

                    intent.putExtra(Intent.EXTRA_TEXT,msgTwitter);
                    intent.setPackage("com.twitter.android");
                    startActivity(intent);

                }
                else {
                    String tweetUrl =
                            String.format("https://twitter.com/intent/tweet?text=%s",
                                    urlEncode(msgStart));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
                    startActivity(intent);
                    //Toast.makeText(getApplicationContext(),"You need to install twitter app first.",Toast.LENGTH_SHORT).show();
                }



            }

        });


        smsText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //String msg = tweetText.getText().toString();
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("sms_body",msgStart);
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
                        } else if (position == 10) {
                            startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                        }
                        else if (position == 7) {
                            startActivity(new Intent(getApplicationContext(), AddLoopCredit.class));
                        }
                        else if (position == 8) {
                            Util.rateUs(getApplicationContext());
                        }else if (position == 9) {
                            startActivity(new Intent(getApplicationContext(), Tutorial.class));
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
                    msgStart="Hey! have you tried Loop? Smarter and cheaper option for daily commute. Get FREE rides worth Rs"+Math.round(Double.valueOf(objInviteActivityBE.getReferralValue()))+" by signing-up with referral code:"+objInviteActivityBE.getReferralCode() +". Click: http://tinyurl.com/Goinloop to get the Loop app.";
                    msgTwitter="Try Loop - smarter & cheaper way to travel.FREE rides worth Rs"+Math.round(Double.valueOf(objInviteActivityBE.getReferralValue()))+" on sign-up with referral code:"+objInviteActivityBE.getReferralCode() +". App: http://tinyurl.com/Goinloop";
                   // msgStart="Try out Loop - awesome bus service! use my referral code "+objInviteActivityBE.getReferralCode() +"to get "+Math.round(Double.valueOf(objInviteActivityBE.getReferralValue()))+" loop credit on registration";

                  // msgTwitter="Try Loop - smarter & cheaper way to travel.FREE rides worth Rs.100 on sign-up with referral code: J8636.App: http://tinyurl.com/pzxgpky";
                    shareBtn.setText(objInviteActivityBE.getReferralCode());
                    promocode="Your Referral Code for loop is :"+objInviteActivityBE.getReferralCode();
                    tvText.setText("They get free rides worth \u20B9"+Math.round(Double.valueOf(objInviteActivityBE.getReferralValue()))+". \n"+"And, so do you!");

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Referral code is not available.Please come back later.", Toast.LENGTH_LONG).show();
                    finish();

                }

            }

            catch (NullPointerException e){
                showDialogResponse(InviteActivity.this);
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

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            Log.d("TAG", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
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

                                           new SharePromocode().execute(userId);
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

                                           new SharePromocode().execute(userId);
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

}
