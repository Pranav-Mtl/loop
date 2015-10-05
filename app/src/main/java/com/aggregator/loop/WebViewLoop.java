package com.aggregator.loop;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aggregator.BL.GetUrlBL;

public class WebViewLoop extends AppCompatActivity {

    private WebView webView;

    GetUrlBL objGetUrlBL;

    ProgressDialog mProgressDialog;

    String addToURL="?embed=foam";

    String amount;

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

        amount=getIntent().getExtras().get("Amount").toString();

       // startWebView("http://imojo.in/55kww");

        new GetURLToLoad().execute(amount);

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
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(WebViewLoop.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                try{

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

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
            Log.d("URL RETURN", webView.getUrl());
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            String URL=objGetUrlBL.getURL(params[0]);
            return URL;
        }

        @Override
        protected void onPostExecute(String s) {

           /*
*//*
           *//* Log.d("URL",s);*/

            try {
                s=s+addToURL;
                startWebView(s);
            }
            catch (Exception e){

            }finally {
                mProgressDialog.dismiss();
            }

        }
    }
}
