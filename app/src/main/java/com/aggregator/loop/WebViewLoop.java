package com.aggregator.loop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.aggregator.BE.AddCreditBE;
import com.aggregator.BL.GetUrlBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;

import java.net.URLDecoder;

public class WebViewLoop extends AppCompatActivity implements View.OnClickListener{

    private WebView webView;

    GetUrlBL objGetUrlBL;

    ProgressDialog mProgressDialog;

    String addToURL="?embed=form";

    String userID;

    String amount;

    Button btnDone;

    AddCreditBE objAddLoopCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mProgressDialog=new ProgressDialog(WebViewLoop.this);
        objGetUrlBL=new GetUrlBL();

        webView = (WebView) findViewById(R.id.loop_webView);
        btnDone= (Button) findViewById(R.id.webview_done);

        btnDone.setOnClickListener(this);

        amount=getIntent().getExtras().get("Amount").toString();

        objAddLoopCredit=new AddCreditBE();

        userID= Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_User_id);

       // startWebView("http://imojo.in/55kww");

        new GetURLToLoad().execute(amount,userID);

    }


    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                try{
                    if (progressDialog == null) {
                        // in standard case YourActivity.this
                        progressDialog = new ProgressDialog(WebViewLoop.this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                    }

                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });


        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);


        //Load url in webview
        webView.loadUrl(url);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            try {

                //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
                Log.d("URL RETURN", webView.getUrl());

                String url = webView.getUrl();

                String ss[] = url.split("&");

                String keyValue[] = ss[0].split("=");
                String param_key = URLDecoder.decode(keyValue[0]);
// the	tracking	id	value	(i.e.	12345)	is
                // stored	in	param_value


                String param_value =
                        URLDecoder.decode(keyValue[1]);

                Log.d("Key", param_key);
                Log.d("Paymenti id", param_value);

                String status[] = ss[1].split("=");

                String statusKey = URLDecoder.decode(status[0]);
                String statusValue = URLDecoder.decode(status[1]);

                Log.d("STATUS Key", statusKey);
                Log.d("STATUS VALUE", statusValue);

                if (statusValue.equals("success")) {
                    Toast.makeText(getApplicationContext(), amount + " loop credited to your account.", Toast.LENGTH_SHORT).show();
                    double amt = Double.valueOf(Constant.amount) + Double.valueOf(amount);
                    Constant.amount = amt + "";
                    Constant.LoopCredit = Constant.LoopCreditText + Constant.amount;
                    startActivity(new Intent(getApplicationContext(), RouteNew.class));
                    // finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Loop payment failed. Please try again.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                finish();
            }
            catch (Exception e){
                e.printStackTrace();
                finish();
            }



            /*for	(String	referrerValue	:	ss) {
                String keyValue[] =
                        referrerValue.split("=");
// the	key,	tracking_id	in	this	case,	is
                //	stored	in	param_key
                String param_key = URLDecoder.decode(keyValue[0]);
// the	tracking	id	value	(i.e.	12345)	is
                // stored	in	param_value


                String param_value =
                        URLDecoder.decode(keyValue[1]);
                //	do	something	with
                //	the	param	value
                System.out.println("KEY" + param_key);
                System.out.println("VALUE" + param_value);

                Log.d("Key", param_key);
                Log.d("Value", param_value);
            }

*/


                onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.webview_done:

                try {

                    //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
                    Log.d("URL RETURN", webView.getUrl());

                    String url = webView.getUrl();

                    String ss[] = url.split("&");

                    String keyValue[] = ss[0].split("=");
                    String param_key = URLDecoder.decode(keyValue[0]);
// the	tracking	id	value	(i.e.	12345)	is
                    // stored	in	param_value


                    String param_value =
                            URLDecoder.decode(keyValue[1]);

                    Log.d("Key", param_key);
                    Log.d("Paymenti id", param_value);

                    String status[] = ss[1].split("=");

                    String statusKey = URLDecoder.decode(status[0]);
                    String statusValue = URLDecoder.decode(status[1]);

                    Log.d("STATUS Key", statusKey);
                    Log.d("STATUS VALUE", statusValue);

                    if (statusValue.equals("success")) {
                        Toast.makeText(getApplicationContext(), amount + " loop credited to your account.", Toast.LENGTH_SHORT).show();
                        double amt = Double.valueOf(Constant.amount) + Double.valueOf(amount);
                        Constant.amount = amt + "";
                        Constant.LoopCredit = Constant.LoopCreditText + Constant.amount;
                        startActivity(new Intent(getApplicationContext(), RouteNew.class));
                        // finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Loop payment failed. Please try again.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                    finish();
                }
                catch (Exception e){
                    e.printStackTrace();
                    finish();
                }



                onBackPressed();
                break;
        }
    }

    private class GetURLToLoad extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String URL=objGetUrlBL.getURL(params[0],params[1],objAddLoopCredit);
            return URL;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                s=s+addToURL+"&data_name="+objAddLoopCredit.getUserName()+"&data_email="+objAddLoopCredit.getEmailID()+"&data_phone="+objAddLoopCredit.getMobileNo()+"&data_readonly=data_\n" +
                        "name&data_readonly=data_phone";
                startWebView(s);
            }
            catch (Exception e){

            }finally {
                mProgressDialog.dismiss();
            }

        }
    }
}
