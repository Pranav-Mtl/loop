package com.aggregator.loop;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.BL.SendCommentBL;
import com.aggregator.BL.TripFeedbackBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;


public class TripFeedback extends AppCompatActivity implements View.OnClickListener {
    TextView date,pick,drop,rate;
    ProgressDialog pd;
    TripFeedbackBL tripFeedbackBL;
    RatingBar ratingBar;
    LinearLayout llIssues;
    Button btnFeedback;
    int rateBar;
    CheckBox one,two,three,four,five,six;
    EditText comment;
    String userRunID;
    SendCommentBL sendCommentBL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_feedback);
        pd=new ProgressDialog(TripFeedback.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pd=new ProgressDialog(TripFeedback.this);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        date=(TextView)findViewById(R.id.date);
        pick=(TextView)findViewById(R.id.pick);
        drop=(TextView)findViewById(R.id.drop);
        rate=(TextView)findViewById(R.id.rate);
        ratingBar= (RatingBar) findViewById(R.id.feedback_rating);
        llIssues= (LinearLayout) findViewById(R.id.ll_trip_issues);
        btnFeedback= (Button) findViewById(R.id.feedback_done);
        one=(CheckBox)findViewById(R.id.checkbox1);
        two=(CheckBox)findViewById(R.id.checkbox2);
        three=(CheckBox)findViewById(R.id.checkbox3);
        four=(CheckBox)findViewById(R.id.checkbox4);
        five=(CheckBox)findViewById(R.id.checkbox5);
        six=(CheckBox)findViewById(R.id.checkbox6);
        comment=(EditText)findViewById(R.id.comment);
        sendCommentBL=new SendCommentBL();

        final Drawable d = btnFeedback.getBackground();

        userRunID=getIntent().getExtras().get("RunID").toString();

        btnFeedback.setBackgroundColor(Color.parseColor("#e4e4e4"));

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

               btnFeedback.setBackground(d);

                rateBar = (int) ratingBar.getRating();

                if (rateBar < 4) {
                    llIssues.setVisibility(View.VISIBLE);
                } else {
                    llIssues.setVisibility(View.GONE);
                }
            }
        });


        try{
            tripFeedbackBL=new TripFeedbackBL();
            new FetchRecord().execute(userRunID);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalString=null;
                String text=null;
                if(one.isChecked()) {
                    String check1 = one.getText().toString();
                    finalString=check1;
                }
                if(two.isChecked()) {
                    String check2 = two.getText().toString();
                            if(finalString!=null&&check2!=null)
                              {
                                  System.out.println("above bothe are null");
                               finalString = finalString + "," + check2;
                               }
                    else
                            {
                                finalString = check2;
                            }
                }
                 if(three.isChecked()) {
                    String check3 = three.getText().toString();
                     if(finalString!=null&&check3!=null)
                     {
                         finalString = finalString + "," + check3;
                     }
                     else
                     {
                         finalString = check3;
                     }






                }
                  if(four.isChecked()) {
                    String check4 = four.getText().toString();
                      if(finalString!=null&&check4!=null)
                      {
                          finalString = finalString + "," + check4;
                      }
                      else
                      {
                          finalString = check4;
                      }
                }
                 if(five.isChecked()) {
                    String check5 = five.getText().toString();
                     if(finalString!=null&&check5!=null)
                     {
                         finalString = finalString + "," + check5;
                     }
                     else
                     {
                         finalString = check5;
                     }
                }
                 if(six.isChecked()) {
                    String check6 = six.getText().toString();
                     if(finalString!=null&&check6!=null)
                     {
                         finalString = finalString + "," + check6;
                     }
                     else
                     {
                         finalString = check6;
                     }
                }
                text=comment.getText().toString();
                if(text!=null){
                        if(finalString==null)
                            {
                              finalString = "No Issues" + "," + text;
                             }
                        else {
                          finalString = finalString + "," + text;
                         }

                }
               String rating=String.valueOf(rateBar);
                String id= Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_User_id);
                System.out.println("checkboxex selected by the user------>"+finalString);
                new SendComment().execute(id,rating,finalString,userRunID);



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

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    private class FetchRecord extends AsyncTask<String ,String ,String>
    {


        @Override
        protected void onPreExecute() {
            pd.show();
            pd.setMessage("Loading...");
            pd.setCancelable(false);
        }


        @Override
        protected String doInBackground(String... params) {
            tripFeedbackBL.sendId(params[0]);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {

            try
            {
                pick.setText(Constant.pickPoint);
                drop.setText(Constant.dropPoint);
                date.setText(Constant.date);
                rate.setText(Constant.rate);
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                pd.dismiss();
            }


        }
    }


    private class SendComment extends AsyncTask<String ,String ,String>
    {


        @Override
        protected void onPreExecute() {
            pd.show();
            pd.setMessage("Loading...");
            pd.setCancelable(false);

        }


        @Override
        protected String doInBackground(String... params) {
            String result=sendCommentBL.sendComment(params[0], params[1], params[2],params[3]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equals(Constant.WS_RESULT_SUCCESS)) {
                    Toast.makeText(getApplicationContext(), "Feedback Sent Successfully", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "You have already sent feedback for this trip.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
            finally {
               pd.dismiss();
            }

        }
    }





}
