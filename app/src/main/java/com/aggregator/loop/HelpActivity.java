package com.aggregator.loop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
        helpActivityBL=new HelpActivityBL();
        message=(EditText)findViewById(R.id.message);
        submit=(Button)findViewById(R.id.submitBtn);
        pd=new ProgressDialog(HelpActivity.this);
        String userId= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SHARED_PREFERENCE_User_id);
        if(userId==null)
        {
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                String userId = "11";
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
        if (id == R.id.action_settings) {
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
            helpActivityBL.sendMessage(params[0],params[1]);
            return "pp";
        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            if(helpActivityBL.result==1)
            {
                Toast.makeText(getApplicationContext(), "Your query has been mailed to loop team", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "please try again", Toast.LENGTH_LONG).show();
            }



        }
    }

}
