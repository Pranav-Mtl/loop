package com.aggregator.loop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.BL.SendEmail;
import com.appsee.Appsee;


public class ResetPassword extends AppCompatActivity {
    EditText emailId;
    Button submit;
    String email;
    SendEmail sendEmail;
    ProgressDialog mProgressDialog;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        Appsee.start("de8395d3ae424245b695b4c9d6642f71");
        sendEmail=new SendEmail();
        emailId=(EditText)findViewById(R.id.emailId);

        emailId.setText("+91", TextView.BufferType.EDITABLE);
        emailId.setSelection(emailId.getText().length());

        back= (ImageButton) findViewById(R.id.reset_back);
        mProgressDialog=new ProgressDialog(ResetPassword.this);

        submit=(Button)findViewById(R.id.submitBtn);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailId.getText().toString();
                if (email.length() == 0) {
                   emailId.setError("Required");
                }
                else if (email.length()!=13) {
                    emailId.setError("Required");
                }
                else {
                    email=email.substring(3);

                    new Email().execute(email);
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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




    private class Email extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=sendEmail.getData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Temporary password sent at Email and Mobile.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), SignIn.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Mobile no. doesn’t exist.", Toast.LENGTH_LONG).show();
                    emailId.setError("Mobile no. doesn't exist.");
                }
            }
            catch (NullPointerException e){
                e.printStackTrace();
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
