package com.aggregator.loop;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.BL.InviteActivityBL;

import java.util.Date;
import java.util.List;
import java.util.Random;


public class InviteActivity extends AppCompatActivity {

    String promocode="";
    Button shareBtn;
    TextView whatsapp,facebook,twitter,smsText;
    InviteActivityBL inviteActivityBL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        inviteActivityBL=new InviteActivityBL();
        shareBtn=(Button)findViewById(R.id.shareBtn);
        whatsapp=(TextView)findViewById(R.id.whatsapp);
        facebook=(TextView)findViewById(R.id.facebook);
        twitter=(TextView)findViewById(R.id.twitterthis);
        smsText=(TextView)findViewById(R.id.sms);
        Random random = new Random((new Date()).getTime());

        char[] values = {'a','b','c','d','e','f','g','h','i','j',
                'k','l','m','n','o','p','q','r','s','t',
                'u','v','w','x','y','z','0','1','2','3',
                '4','5','6','7','8','9'};

        String out = "";

        for (int i=0;i<4;i++) {
            int idx=random.nextInt(values.length);
            out += values[idx];
        }
        promocode=out;
        shareBtn.setText(promocode);

        promocode="Your promo code for loop is: "+promocode;


        try{
            String userId="8";
            String referalValue="50";
            new SharePromocode().execute(userId,promocode,referalValue).get();
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

        }

        @Override
        protected String doInBackground(String... params) {
           String result= inviteActivityBL.getPromocode(params[0], params[1], params[2]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {


            if(s.equals("success"))
            {
                //Toast.makeText(getApplicationContext(),"Succefully shared",Toast.LENGTH_LONG).show();

            }
            else
            {
                //Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
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




}
