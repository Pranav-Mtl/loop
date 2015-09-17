package com.aggregator.loop;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aggregator.BL.PromoCodeBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;

public class PromoCode extends AppCompatActivity implements View.OnClickListener {

    EditText etPromocode;
    Button btnVerify;
    String userID;
    PromoCodeBL objPromoCodeBL;
    ProgressDialog mPrograProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        etPromocode= (EditText) findViewById(R.id.promocode);
        btnVerify= (Button) findViewById(R.id.promocode_done);
        btnVerify.setOnClickListener(this);

        mPrograProgressDialog=new ProgressDialog(PromoCode.this);

        objPromoCodeBL=new PromoCodeBL();

        userID= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SHARED_PREFERENCE_User_id);


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
            case R.id.promocode_done:

                String text=etPromocode.getText().toString();
                if(text.length()==0){
                    etPromocode.setError("Enter PromoCode");
                }
                else
                {
                    new ApplyCode().execute(userID,text);
                }
                break;
        }
    }

    private class ApplyCode extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            mPrograProgressDialog.show();
            mPrograProgressDialog.setMessage("Loading...");
            mPrograProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String result=objPromoCodeBL.sendCode(params[0],params[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if(s.equals(Constant.WS_RESULT_SUCCESS)){
                    Toast.makeText(getApplicationContext(),"Loop Credit added successfully",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid PromoCode",Toast.LENGTH_LONG).show();
                    etPromocode.setError("Invalid Promo code");
                }
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception e){

            }
            finally {
                mPrograProgressDialog.dismiss();
            }
        }
    }
}
