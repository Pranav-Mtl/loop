package com.aggregator.loop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aggregator.BL.HelpActivityBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;


public class HelpActivity extends ActionBarActivity {
    EditText message;
    Button submit;
    ProgressDialog pd;
    HelpActivityBL helpActivityBL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        helpActivityBL=new HelpActivityBL();
        message=(EditText)findViewById(R.id.message);
        submit=(Button)findViewById(R.id.submitBtn);
        pd=new ProgressDialog(HelpActivity.this);
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

}
