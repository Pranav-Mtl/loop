package com.aggregator.loop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.BE.BookingBE;
import com.aggregator.BL.BookingBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingNew extends AppCompatActivity implements View.OnClickListener{


    Spinner spnTimePicker,spnPick,spnDrop;
    Button btnDone;
    TextView tvPrice;
    ImageButton btnSwipe,btnBack;

    FloatingActionButton back;

    BookingBL objBookingBL;
    BookingBE objBookingBE;
    List listPickTime=new ArrayList<>();
    List listRun=new ArrayList<>();

    int pickPointPosition,dropPointPosition;

    int dropStart;

    String loginID;

    int pickPos;
    int dropPos;


    String routeId="1";
    String startPointID,endPointID,runID;

    double startPointDist,endPointDist;

    int HH,MM,SS;
    SimpleDateFormat sdf = new SimpleDateFormat("KK:mm");
    DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss");
    String currentTime;

    Date dtCurrent,dtArray;

    boolean pickselected,dropselected,timeSelected;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_new);

        spnTimePicker= (Spinner) findViewById(R.id.booking_timepicker);
        tvPrice= (TextView) findViewById(R.id.booking_price);


        spnPick= (Spinner) findViewById(R.id.sp_pick);
        spnDrop= (Spinner) findViewById(R.id.sp_drop);

        btnDone= (Button) findViewById(R.id.booking_screen_btn);
        btnSwipe= (ImageButton) findViewById(R.id.booking_swap_button);
        back= (FloatingActionButton) findViewById(R.id.booking_float);



        btnDone.setEnabled(false);

        objBookingBL=new BookingBL();
        objBookingBE=new BookingBE();

        mProgressDialog=new ProgressDialog(BookingNew.this);

        loginID=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_User_id);

        btnSwipe.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        back.setOnClickListener(this);

        /* spinner pick point selected listener */

        spnPick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerPickSelected(position);
                pickPos = position;
                pickselected = true;
                startPointID = Constant.pointID[position];
                startPointDist = Constant.pointDistance[position];
                Log.d("Pick Point ID", startPointID);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* spinner drop point selected listener */

        spnDrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int pos = dropPointPosition;

                dropPos=position;

                endPointID=Constant.pointID[pos];
                dropselected=true;
                endPointDist=Constant.pointDistance[pos];

                double price=calculatePrice();
                Log.d("Drop Point ID", endPointID);

                tvPrice.setText(price+"");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnTimePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    //runID=Constant.pointRunArray[position-1];
                    runID=listRun.get(position-1).toString();
                    timeSelected=true;
                    if(pickselected)
                    {
                        if(dropselected)
                        {
                            btnDone.setEnabled(true);
                        }
                    }
                    Log.d("Selected Run ID",runID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        routeId=getIntent().getExtras().get("RouteId").toString();

        // CALL WEB SERVICE TO GET JSON OF  SELECTED ROUTE.

        if(Util.isInternetConnection(BookingNew.this)){
           new GetSelectedRoute().execute(routeId);
        }
        else{
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(BookingNew.this);

            alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);

            alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);

            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });

            alertDialog2.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog

                            dialog.cancel();
                        }
                    });


            alertDialog2.show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.booking_screen_btn:

                if(loginID==null)
                {
                    Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_SOURCE_id,startPointID);
                    Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_DESTINATION_id,endPointID);
                    Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_ROUTE_ID,routeId);
                    Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_Run_ID,runID);
                    Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_TIME,spnTimePicker.getSelectedItem().toString());
                    Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_PRICE,tvPrice.getText().toString());

                    startActivity(new Intent(BookingNew.this, SignUpScreen.class));
                }
                else {

                    objBookingBE.setUserID(loginID);
                    objBookingBE.setRouteID(routeId);
                    objBookingBE.setStartPoint(startPointID);
                    objBookingBE.setEndPoint(endPointID);
                    objBookingBE.setRunID(runID);
                    objBookingBE.setPrice(tvPrice.getText().toString());
                    objBookingBE.setTime(spnTimePicker.getSelectedItem().toString());

                    new InsertBooking().execute();
                }
                break;
            case R.id.booking_swap_button:
                swapButtonClicked();
                break;
            case R.id.booking_float:
                finish();
                break;
        }
    }

    private void spinnerPickSelected(int pos){


        updateDropPoint(pos);   /* call method to update drop points */
        updateTimeDropDown(pos);

        Log.d("Pick Point Position-->",pos+"");
    }
    private void spinnerDropSelected(){

        dropPointPosition=spnDrop.getSelectedItemPosition();

        Log.d("Drop Point Position-->",dropPointPosition+"");
    }


    private class GetSelectedRoute extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result = objBookingBL.getRoutesDetail(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {

                setPickPoint(Constant.pointName);  // Initialize Pick Point DropDown
                setDropPoint(Constant.pointName);  // Initialize Drop Point DropDown
                String ss[]=Constant.pointRun[0].split(",");
                Constant.pointRunArray=new String[ss.length];
                for(int i=0;i<ss.length;i++)
                {
                    Constant.pointRunArray[i]=ss[i];
                }

            }catch (NullPointerException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                mProgressDialog.dismiss();
            }

        }
    }

    /* calculate trip price between start point and end point  */

    /*

    * Formula for price calculation: (route.fixed_price)+(route.per_km_price * route_point_distance_price.distance).

    */

    private double calculatePrice(){
        Double price=0.0;
        Double distance=endPointDist-startPointDist;

        distance=Math.abs(distance);

        Log.d("Calculated Distance",distance+"");

        price=(Constant.routeFixedPrice)+(Constant.routePerKmPrice * distance);

        Log.d("Calculated Price", price + "");
        return price;
    }



/* update drop points  on pick point selection */

    private void updateDropPoint(int pickPointPos){
        List listDropPoint=new ArrayList<>();

        int counter=0;
        dropPointPosition=pickPointPos;
        Log.d("Pick Point",dropPointPosition+"");
        Log.d("Total Record",Constant.pointName.length+"");
        for(int i=dropPointPosition+1;i<Constant.pointName.length;i++){
            listDropPoint.add(Constant.pointName[i]);
            counter++;
        }
        Log.d("Total Counter",counter+"");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookingNew.this,R.layout.spinner_item,listDropPoint);
        spnDrop.setAdapter(adapter);
        spnDrop.setSelection(counter - 1);
        dropPos=counter-1;
    }

    /* set pick points on page load */

    private void setPickPoint(String pointName[]){

       // spinnerPickSelected(position);
        pickPos = 0;
        pickselected = true;
        startPointID = Constant.pointID[0];
        startPointDist = Constant.pointDistance[0];

        List listPickPoint=new ArrayList<>();


        for(int i=0;i<pointName.length-1;i++){
            listPickPoint.add(pointName[i]);
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookingNew.this,R.layout.spinner_item,listPickPoint);
        spnPick.setAdapter(adapter);
        spnPick.setSelection(0);
    }

    /* set drop points on page load */

    private void setDropPoint(String pointName[]){
        List listDropPoint=new ArrayList<>();
        dropPointPosition=1;


        for(int i=1;i<pointName.length;i++){
            listDropPoint.add(pointName[i]);
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookingNew.this,R.layout.spinner_item,listDropPoint);
        spnDrop.setAdapter(adapter);
        //spnDrop.setSelection(pointName.length-1);
    }

    /* update time drop down on each pick point selection */

    private void updateTimeDropDown(int pos){
        List listPickTime=new ArrayList<>();

        listPickTime.add("Select Time");

        listRun.clear();

        String timeArray[]=Constant.pointTime[pos].split(",");

       dtCurrent=getCurrentTime();

        Log.d("Current Time--",dtCurrent+"");

        for(int i=0;i<timeArray.length;i++){
            try {
                dtArray = dateFormat.parse(timeArray[i]);
                //String format=dateFormat.format(dd);
                Log.d(" Time Array --",dtArray+"");
                if (dtArray.compareTo(dtCurrent)>0){

                    Log.d("COMPARE-->", "UNDER IF");
                    listPickTime.add(timeArray[i]);
                    listRun.add(Constant.pointRunArray[i]);
                }
                else
                {
                    Log.d("COMPARE-->", "OUTSIDE  IF");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }



        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookingNew.this,R.layout.spinner_item,listPickTime);
        spnTimePicker.setAdapter(adapter);
    }

    /* return current date and time */
    private Date getCurrentTime(){
        try {
            Date date = new Date();
            String formattedDateTime = dateFormat.format(date);

           dtCurrent = dateFormat.parse(formattedDateTime);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return dtCurrent;
    }

    /* swap route i.e change direction to UP/DOWN  */
    private void swapButtonClicked(){
        pickPos=spnPick.getSelectedItemPosition();
        dropPos=spnDrop.getSelectedItemPosition();
        if(Constant.swapRoute){
            objBookingBL.parseSubJson(Constant.jsonUP);
            setPickPoint(Constant.pointName);  // Initialize Pick Point DropDown
            setDropPoint(Constant.pointName);  // Initialize Drop Point DropDown
            String ss[]=Constant.pointRun[0].split(",");
            Constant.pointRunArray=new String[ss.length];
            for(int i=0;i<ss.length;i++)
            {
                Constant.pointRunArray[i]=ss[i];
            }
            Constant.swapRoute=false;
        }
        else
        {
            objBookingBL.parseSubJson(Constant.jsonDOWN);
            setPickPoint(Constant.pointName);  // Initialize Pick Point DropDown
            setDropPoint(Constant.pointName);  // Initialize Drop Point DropDown
            String ss[]=Constant.pointRun[0].split(",");
            Constant.pointRunArray=new String[ss.length];
            for(int i=0;i<ss.length;i++)
            {
                Constant.pointRunArray[i]=ss[i];
            }
            Constant.swapRoute=true;

         }

        String compareValue = "some value";


    }


    private class InsertBooking extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            String result=objBookingBL.insertBooking(objBookingBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                if(!s.equals(Constant.WS_RESULT_FAILURE)){
                    Log.d("Booking id-->", s);
                    Toast.makeText(getApplicationContext(), "Ride Successfully Booked", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(BookingNew.this, TicketScreen.class).putExtra("BookingID", s).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                mProgressDialog.dismiss();
            }

        }
    }

}
