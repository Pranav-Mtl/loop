package com.aggregator.loop;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aggregator.Adapters.TripHistoryAdapter;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;

public class TripHistory extends AppCompatActivity {

    ListView lvTrips;

    TripHistoryAdapter objTripHistoryAdapter;

    ProgressDialog mProgressDialog;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        lvTrips= (ListView) findViewById(R.id.trips_lv);

        mProgressDialog=new ProgressDialog(TripHistory.this);

        userID= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SHARED_PREFERENCE_User_id);

        new GetTripHistory().execute(userID);
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
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetTripHistory extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            objTripHistoryAdapter=new TripHistoryAdapter(getApplicationContext(),params[0]);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
          try {
              lvTrips.setAdapter(objTripHistoryAdapter);

              lvTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      Log.d("CLICKED--->", view.getId()+"");

                      objTripHistoryAdapter.notifyDataSetChanged();
                  }
              });
          }
          catch (Exception e){
              e.printStackTrace();
          }
            finally {
              mProgressDialog.dismiss();
          }
        }
    }
}
