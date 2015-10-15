package com.aggregator.loop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aggregator.Constant.Constant;
import com.google.android.gms.analytics.HitBuilders;

public class AddLoopCredit extends AppCompatActivity implements View.OnClickListener {

    Button etCredit50,etCredit100,etCredit200;
    EditText etAmount;
    Button btnAddCredit;
    TextView tvCurrentCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loop_credit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        etCredit50= (Button) findViewById(R.id.credit_fifty);
        etCredit100= (Button) findViewById(R.id.credit_hundred);
        etCredit200= (Button) findViewById(R.id.credit_two_hundred);
        etAmount= (EditText) findViewById(R.id.credit_amount);
        tvCurrentCredit= (TextView) findViewById(R.id.credit_current);

        tvCurrentCredit.setText("Current balance: "+Constant.currentLoopCredit);

        btnAddCredit= (Button) findViewById(R.id.btn_buy_credit);

        etCredit50.setOnClickListener(this);
        etCredit100.setOnClickListener(this);
        etCredit200.setOnClickListener(this);
        btnAddCredit.setOnClickListener(this);


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
            case R.id.btn_buy_credit:
                if(etAmount.getText().length()==0){
                    etAmount.setError("required.");
                }
                else if(Integer.valueOf(etAmount.getText().toString())>2000 || Integer.valueOf(etAmount.getText().toString())<50){
                    etAmount.setError("amount must be b/w 50-2000");
                }
                else {
                    Log.d("AMOUNT",etAmount.getText().toString());

                     /* call google analytics*/

                    try {
                        Application.tracker().setScreenName("Buy Loop Credit");
                        Application.tracker().send(new HitBuilders.EventBuilder()
                                .setLabel("Logged-in")
                                .setCategory("Buy Loop Credit")
                                .setAction("Buy Button")
                                .setValue(Integer.valueOf(etAmount.getText().toString()))

                                .build());
                        // AffleInAppTracker.inAppTrackerViewName(getApplicationContext(), "Landing Screen", "App First Screen", "APP Open", null);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(getApplicationContext(),WebViewLoop.class).putExtra("Amount",etAmount.getText().toString()));
                }

                break;
            case R.id.credit_fifty:
                etAmount.setText("50");
                etAmount.setSelection(etAmount.getText().length());
                break;
            case R.id.credit_hundred:
                etAmount.setText("100");
                etAmount.setSelection(etAmount.getText().length());
                break;
            case R.id.credit_two_hundred:
                etAmount.setText("200");
                etAmount.setSelection(etAmount.getText().length());
                break;
        }
    }
}
