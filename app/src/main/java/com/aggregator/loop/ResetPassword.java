package com.aggregator.loop;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aggregator.BL.SendEmail;

import java.util.regex.Pattern;


public class ResetPassword extends AppCompatActivity {
    EditText emailId;
    Button submit;
    String email;
    SendEmail sendEmail;
    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
    );

    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        sendEmail=new SendEmail();
        emailId=(EditText)findViewById(R.id.emailId);

        back= (ImageButton) findViewById(R.id.reset_back);

        submit=(Button)findViewById(R.id.submitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailId.getText().toString();
                if (email.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Enter valid Email", Toast.LENGTH_LONG).show();
                } else if (!checkEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Please Enter valid Email", Toast.LENGTH_LONG).show();
                } else if (checkEmail(email)) {
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

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }



    private class Email extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            sendEmail.getData(params[0]);
            return "pp";
        }

        @Override
        protected void onPostExecute(String s) {
            if(sendEmail.status==1)
            {
                Toast.makeText(getApplicationContext(),"Temporary Password has been sent at Email-id",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),SignIn.class));
            }
            else {
                Toast.makeText(getApplicationContext(),"Email-id doesn't exist",Toast.LENGTH_LONG).show();
            }




        }
    }






}
