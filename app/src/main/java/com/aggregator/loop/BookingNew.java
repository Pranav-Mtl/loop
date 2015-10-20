package com.aggregator.loop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.Adapters.DrawerAdapter;
import com.aggregator.BE.BookingBE;
import com.aggregator.BL.BookingBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.appsee.Appsee;
import com.google.android.gms.analytics.HitBuilders;
import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingNew extends AppCompatActivity implements View.OnClickListener{

    Spinner spnTimePicker,spnPick,spnDrop;
    Button btnDone;
    TextView tvPrice;
    ImageButton btnSwipe,btnBack;

    ImageButton back;

    BookingBL objBookingBL;
    BookingBE objBookingBE;
    List listPickTime=new ArrayList<>();
    List listRun=new ArrayList<>();

    String TITLES[] = {"Book a Ride","Trips","Promos","Invite & Earn","Notifications","Suggest A Route","Rate Us","Tutorial","Help",};

    int ICONS[] = {R.drawable.ic_side_trips,R.drawable.ic_side_bus, R.drawable.ic_side_promo,R.drawable.ic_side_invite_earn,R.drawable.ic_side_notification,R.drawable.ic_side_suggest,R.drawable.ic_side_rate, R.drawable.ic_side_tutorial,R.drawable.ic_side_help};

    int pickPointPosition,dropPointPosition;

    int dropStart;

    String loginID;

    int pickPos;
    int dropPos;

    int price;


    String routeId="1";
    String startPointID,endPointID,runID;

    double startPointDist,endPointDist;

    int HH,MM,SS;
    SimpleDateFormat sdf = new SimpleDateFormat("KK:mm a",Locale.ENGLISH);
    DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss",Locale.ENGLISH);

    SimpleDateFormat dateFormat24 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    String currentTime;

    Date dtCurrent,dtArray;

    boolean pickselected,dropselected,timeSelected;

    ProgressDialog mProgressDialog;

    TextView tvCredit;


    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;

    DrawerAdapter drawerAdapter;

    String totalPrice,loopCredit,paytmCash;

    TextView tvError;

    View _itemColoured;

    RelativeLayout rlDiagram;

    int xx,yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_new);

        Appsee.start("de8395d3ae424245b695b4c9d6642f71");

        Display display = getWindowManager().getDefaultDisplay();

        // Point size = new Point();
        // display.getSize(size);
        int width = display.getWidth();
        int height = display.getHeight();

        // System.out.println("width" + width + "height" + height);

        if(width>=700 && height>=1000)
        {
            xx=500;
            yy=500;
        }
        else
        {
            xx=400;
            yy=500;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spnTimePicker= (Spinner) findViewById(R.id.booking_timepicker);
        tvPrice= (TextView) findViewById(R.id.booking_price);
        tvCredit= (TextView) findViewById(R.id.booking_loop_credit);

        spnPick= (Spinner) findViewById(R.id.sp_pick);
        spnDrop= (Spinner) findViewById(R.id.sp_drop);

        btnDone= (Button) findViewById(R.id.booking_screen_btn);
        btnSwipe= (ImageButton) findViewById(R.id.booking_swap_button);
        back= (ImageButton) findViewById(R.id.booking_float);
        tvError= (TextView) findViewById(R.id.booking_noride);
        rlDiagram= (RelativeLayout) findViewById(R.id.booking_relative_diagram);

        objBookingBL=new BookingBL();
        objBookingBE=new BookingBE();

        mProgressDialog=new ProgressDialog(BookingNew.this);

        loginID=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_User_id);

        if(loginID==null) {
            TITLES = new String[3];
            TITLES[0] = "Book a Ride";
            TITLES[1] = "Tutorial";
            TITLES[2] = "Rate us";
            ICONS = new int[3];
            ICONS[0] = R.drawable.ic_side_bus;
            ICONS[1] = R.drawable.ic_side_tutorial;
            ICONS[2] = R.drawable.ic_side_rate;

            Constant.NAME = "Sign In";
            Constant.LoopCredit = "";
            Constant.PayTMWalet = "";
        }

        btnSwipe.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        back.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);

        drawerAdapter = new DrawerAdapter(TITLES,ICONS, Constant.NAME, Constant.LoopCredit,Constant.PayTMWalet, getApplicationContext());       // Creating the Adapter of com.example.balram.sampleactionbar.MyAdapter class(which we are going to see in a bit)

        // And passing the titles,icons,header view name, header view email,
        // and header  view profile picture
        mRecyclerView.setAdapter(drawerAdapter);

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();



        /* spinner pick point selected listener */

        spnPick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerPickSelected(position);
                pickPos = position;
                pickselected = true;
                timeSelected=false;
                startPointID = Constant.pointID[position];
                startPointDist = Constant.pointDistance[position];
                Log.d("Pick Point ID", startPointID);
                Log.d("Pick Point Dist", startPointDist + "");

                ViewGroup layout = (ViewGroup) findViewById(R.id.booking_relative_diagram);
                View command = layout.findViewById(getResources().getInteger(R.integer.id));

                try {
                    if (command == null) {

                    } else {
                        layout.removeView(command);
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }



                createCustomNew(pickPos,Constant.pointID.length);
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
                dropPos=pickPos+position+1;

                ViewGroup layout = (ViewGroup) findViewById(R.id.booking_relative_diagram);
                View command = layout.findViewById(getResources().getInteger(R.integer.id));
                layout.removeView(command);

                createCustomNew(pickPos,dropPos);

                endPointID=Constant.pointID[dropPos];
                dropselected=true;
                endPointDist=Constant.pointDistance[dropPos];

                 price=calculatePrice();

                Log.d("Drop  Position", position+"");
                Log.d("Drop Down Position",dropPos+"");
                Log.d("Drop Point ID", endPointID);
                Log.d("Drop Point Distance", endPointDist + "");

                Log.d("Amount i Have", Constant.amount + "");

                int pp=Constant.currentLoopCredit-price;
                Log.d("PRICE CATCULATED", pp + "");
                totalPrice=pp+"";

                tvPrice.setText("Loop credit: "+price);

               /* if(pp>0)
                {
                    loopCredit=price+"";
                    paytmCash="0";
                    tvPrice.setText("0");
                    tvCredit.setText("After using ₹"+Math.round(price)+" Loop credit.");
                }
                else
                {
                    loopCredit=Math.round(Double.valueOf(Constant.amount))+"";
                    paytmCash=Math.round(Math.abs(pp))+"";
                    tvPrice.setText(Math.round(Math.abs(pp))+"");
                    if(Math.round(Double.valueOf(Constant.amount))==0){
                        tvCredit.setText("");
                     }
                    else
                        tvCredit.setText("After using "+Math.round(Double.valueOf(Constant.amount))+" Loop credit.");
                }
*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
/*
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.aggregator.loop",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

        spnTimePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    //runID=Constant.pointRunArray[position-1];
                    runID=listRun.get(position).toString();
                    timeSelected=true;
                    if(pickselected)
                    {
                        if(dropselected)
                        {

                        }
                    }
                    Log.d("Selected Run ID",runID);

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

            showDialogConnection(BookingNew.this);
            /*AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(BookingNew.this);

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


            alertDialog2.show();*/
        }

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        if (position != 0) {
                            if (_itemColoured != null) {
                                _itemColoured.setBackgroundColor(Color.parseColor("#66daae"));
                                _itemColoured.invalidate();
                            }
                            _itemColoured = view;
                            view.setBackgroundColor(Color.parseColor("#1fc796"));
                        }

                        if(loginID==null)
                        {
                            if (position == 0) {
                                startActivity(new Intent(getApplicationContext(), SignIn.class));
                            } else if (position == 1) {
                               startActivity(new Intent(getApplicationContext(), RouteNew.class));
                            }
                            else if (position == 2) {
                                startActivity(new Intent(getApplicationContext(), Tutorial.class));
                            }else if (position == 3) {
                                Util.rateUs(getApplicationContext());
                            }
                        }
                        else {

                            if (position == 0) {
                                startActivity(new Intent(getApplicationContext(), LoopProfile.class));
                            } else if (position == 1) {
                                    finish();
                            } else if (position == 2) {
                                startActivity(new Intent(getApplicationContext(), TripHistory.class));
                            } else if (position == 3) {
                                startActivity(new Intent(getApplicationContext(), PromoCode.class));
                            } else if (position == 4) {
                                startActivity(new Intent(getApplicationContext(), InviteActivity.class));
                            } else if (position == 9) {
                                startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                            }else if (position == 7) {
                                Util.rateUs(getApplicationContext());
                            } else if (position == 8) {
                                startActivity(new Intent(getApplicationContext(), Tutorial.class));
                            } else if (position == 6) {
                                startActivity(new Intent(getApplicationContext(),SuggestRoute.class));
                            }
                        }

                    }



                }));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.booking_screen_btn:

                int remainingPrice = Constant.currentLoopCredit - price;

                if (remainingPrice > 0) {  // check whether loop credit is greater than available or not.

                        if (timeSelected) {
                           // String price = "₹ " + totalPrice + " (Loop credit ₹ " + loopCredit + ", Paytm ₹ " + paytmCash + ")";
                            try {
                                                            /* compare time selected with current time....*/

                                Log.d("Time selected", spnTimePicker.getSelectedItem().toString());
                                Date time = sdf.parse(spnTimePicker.getSelectedItem().toString().replace("\"", ""));
                                Date date = new Date();
                                String formattedDateTime = dateFormat.format(date);
                                String selectedTime=dateFormat.format(time);

                                Date dtSlected=dateFormat.parse(selectedTime);
                                Date dtCurrent=dateFormat.parse(formattedDateTime);

                                long diffMs = dtSlected.getTime()- dtCurrent.getTime() ;
                                long diffSec = diffMs / 1000;
                                long min = diffSec / 60;
                                long sec = diffSec % 60;

                                System.out.println("The difference is " + min + " minutes and " + sec + " seconds.");

                                if(min>0) {

                                    Log.d("Current Time--", dtCurrent + "");
                                    String strTime = dateFormat24.format(time);


                                    if (loginID == null) {
                                        Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_BOOKING_SOURCE_id, startPointID);
                                        Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_BOOKING_DESTINATION_id, endPointID);
                                        Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_BOOKING_ROUTE_ID, routeId);
                                        Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_BOOKING_Run_ID, runID);
                                        Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_BOOKING_TIME, strTime);
                                        Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_BOOKING_PRICE, "0");
                                        Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_BOOKING_LOOP_CREDIT, price+"");
                                        startActivity(new Intent(BookingNew.this, SignUpScreen.class));
                                    } else {

                                        objBookingBE.setUserID(loginID);
                                        objBookingBE.setRouteID(routeId);
                                        objBookingBE.setStartPoint(startPointID);
                                        objBookingBE.setEndPoint(endPointID);
                                        objBookingBE.setRunID(runID);
                                        objBookingBE.setPrice("0");
                                        objBookingBE.setLoopCredit(price+"");

                                        objBookingBE.setTime(strTime);

                                         /* call google analytics*/

                                        try {
                                            Application.tracker().setScreenName("Booking Screen");
                                            Application.tracker().send(new HitBuilders.EventBuilder()
                                                    .setLabel("Logged-in")
                                                    .setCategory("Booking Screen")
                                                    .setAction("Book Button")
                                                    .setValue(Integer.valueOf(startPointID))
                                                    .setValue(Integer.valueOf(endPointID))
                                                    .setValue(Integer.valueOf(strTime))
                                                    .build());
                                            // AffleInAppTracker.inAppTrackerViewName(getApplicationContext(), "Landing Screen", "App First Screen", "APP Open", null);

                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                        new InsertBooking().execute();
                                    }
                                }
                                else{
                                        Toast.makeText(getApplicationContext(),"Selected time should be greater than current time.",Toast.LENGTH_LONG).show();
                                    }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please select time of departure.", Toast.LENGTH_SHORT).show();
                        }
        }
        else{
                    if(loginID==null){
                        startActivity(new Intent(BookingNew.this, SignUpScreen.class));
                    }
                    else {
                        showDialogCredit(BookingNew.this);
                    }

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

                Constant.swapRoute=false;

                Log.v("POINT RUN", Constant.pointRun[0] + "");

                if(Constant.pointRun[0].trim().length()==0){

                    Log.v("Under if condition", Constant.pointRun[0].trim().length() + "");
                    if(Constant.pointDownRun[0].trim().length()==0){
                        tvError.setText("No trips for this route today.");
                        tvError.setVisibility(View.VISIBLE);
                        btnSwipe.setEnabled(false);
                        btnSwipe.setBackgroundResource(R.drawable.btn_gray_swipe);
                    }
                    else {
                        btnSwipe.setEnabled(false);
                        btnSwipe.setBackgroundResource(R.drawable.btn_gray_swipe);
                        swapButtonClicked();

                        /*tvError.setText("No vehicles going in this direction today. Tip - Use Swap button to see runs in reverse direction.");
                        tvError.setVisibility(View.VISIBLE);*/

                    }

                }
                else {
                    Log.v("Under else condition", Constant.pointRun[0].trim().length() + "");
                    String ss[] = Constant.pointRun[0].split(",");
                    Log.d("Length", ss.length + "");

                        Constant.pointRunArray = new String[ss.length];
                        for (int i = 0; i < ss.length; i++) {
                            Constant.pointRunArray[i] = ss[i];
                        }

                }


                String strTotal[]=Constant.pointTotalSeat[0].split(",");
                String strBooked[]=Constant.pointBookedSeat[0].split(",");

                Constant.pointAvailableSeat=new int[strTotal.length];
                for(int i=0;i<strTotal.length;i++){
                    Constant.pointAvailableSeat[i]=Integer.valueOf(strTotal[i])-Integer.valueOf(strBooked[i]);
                }

            }catch (NullPointerException e){
                showDialogResponse(BookingNew.this);
            }catch (Exception e){
                //NoResponseServer();
            }finally {
                mProgressDialog.dismiss();
            }

        }
    }

    /* calculate trip price between start point and end point  */

    /*

    * Formula for price calculation: (route.fixed_price)+(route.per_km_price * route_point_distance_price.distance).

    */

    private int calculatePrice(){
        Double price=0.0;
        Double distance=endPointDist-startPointDist;

        distance=Math.abs(distance);

        Log.d("Calculated Distance", distance + "");

        price=(Constant.routeFixedPrice)+(Constant.routePerKmPrice * distance);

        Log.d("Calculated Price", price + "");
        return ((int)Math.round(price));
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
        dropPos=counter;
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

        pickselected = true;


        for(int i=1;i<pointName.length;i++){
            listDropPoint.add(pointName[i]);
        }

     /*   startPointID = Constant.pointID[0];
        startPointDist = Constant.pointDistance[0];*/
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookingNew.this,R.layout.spinner_item,listDropPoint);
        spnDrop.setAdapter(adapter);
        //spnDrop.setSelection(pointName.length-1);
    }

    /* update time drop down on each pick point selection */

    private void updateTimeDropDown(int pos){
        List listPickTime=new ArrayList<>();

        //listPickTime.add("Select Time");

        //tvError.setVisibility(View.GONE);

        int cc=-1;

        listRun.clear();

        String timeArray[]=Constant.pointTime[pos].split(",");

       dtCurrent=getCurrentTime();

        Log.d("Current Time--",dtCurrent+"");

        for(int i=0;i<timeArray.length;i++){
             try {
                dtArray = dateFormat.parse(timeArray[i]);
                //String format=dateFormat.format(dd);
                Log.d(" Time Array --",dtArray+"");
                if (dtArray.compareTo(dtCurrent)>=0){

                    try {

                        if (Constant.pointAvailableSeat[i] > 0) {
                            listPickTime.add(new SimpleDateFormat("K:mm a").format(dtArray));
                            listRun.add(Constant.pointRunArray[i]);
                            cc++;
                        }
                    }
                    catch (Exception e){

                    }
                }
                else
                {
                    Log.d("COMPARE-->", "OUTSIDE  IF");
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

            if(cc==-1){
            //tvError.setVisibility(View.VISIBLE);
            //tvError.setText("No vehicles going in this direction today.");
               timeSelected=false;
        }
        else{
            //tvError.setVisibility(View.INVISIBLE);
                timeSelected=true;
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

        tvError.setVisibility(View.INVISIBLE);
        timeSelected=false;

        if(Constant.swapRoute){
            objBookingBL.parseSubJson(Constant.jsonUP);
            setPickPoint(Constant.pointName);  // Initialize Pick Point DropDown
            setDropPoint(Constant.pointName);  // Initialize Drop Point DropDown
            String ss[]=Constant.pointRun[0].split(",");

            String strTotal[]=Constant.pointTotalSeat[0].split(",");
            String strBooked[]=Constant.pointBookedSeat[0].split(",");

            if(Constant.pointRun[0].trim().length()!=0) {
                Constant.pointAvailableSeat = new int[strTotal.length];
                for (int i = 0; i < strTotal.length; i++) {
                    Constant.pointAvailableSeat[i] = Integer.valueOf(strTotal[i]) - Integer.valueOf(strBooked[i]);
                }
            }
            else
            {
                tvError.setVisibility(View.VISIBLE);
                tvError.setText("No vehicles going in this direction today. Tip - Use Swap button to see runs in reverse direction.");
            }

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

            String strTotal[]=Constant.pointTotalSeat[0].split(",");
            String strBooked[]=Constant.pointBookedSeat[0].split(",");
            if(Constant.pointDownRun[0].trim().length()!=0) {
                Constant.pointAvailableSeat = new int[strTotal.length];
                for (int i = 0; i < strTotal.length; i++) {
                    Constant.pointAvailableSeat[i] = Integer.valueOf(strTotal[i]) - Integer.valueOf(strBooked[i]);
                }
            }
            else
            {
                tvError.setVisibility(View.VISIBLE);
                tvError.setText("No vehicles going in this direction today. \n Tip - Use Swap button to see runs in reverse direction.");
            }
            Constant.swapRoute=true;
         }
        Toast.makeText(BookingNew.this,"Direction reversed.",Toast.LENGTH_SHORT).show();
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
                if(objBookingBL.status.equals(Constant.WS_RESULT_SUCCESS)){
                    Log.d("Booking id-->", s);
                    Toast.makeText(getApplicationContext(), "Ride Successfully Booked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BookingNew.this, TicketScreen.class).putExtra("BookingID", s).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
                else if(objBookingBL.status.equals("full"))
                {
                    Toast.makeText(getApplicationContext(), "Sorry your booking did not go through. Try again please?", Toast.LENGTH_SHORT).show();
                    new GetSelectedRoute().execute(routeId);
                    //finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Sorry your booking did not go through. Try again please?", Toast.LENGTH_SHORT).show();
                    new GetSelectedRoute().execute(routeId);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerAdapter.notifyDataSetChanged();
    }

   /* private void NoResponseServer()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Constant.ERR_NO_SERVER_RESPONSE)
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        new GetSelectedRoute().execute(routeId);
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }*/

  /*  private void notEnoughCredit()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Constant.ERR_NO_LOOP_CREDIT_MESSAGE)
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Buy loop credits", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(getApplicationContext(), AddLoopCredit.class));
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }*/


    void createCustomNew(int pick,int drop){

            int pos=100;
            LinearLayout llMain=new LinearLayout(this);
            llMain.setId(getResources().getInteger(R.integer.id));
            llMain.setOrientation(LinearLayout.VERTICAL);

            for(int i=0;i<Constant.pointID.length;i++) {

                LinearLayout LL = new LinearLayout(this);
                LL.setOrientation(LinearLayout.HORIZONTAL);

                ViewGroup.LayoutParams LLParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LL.setLayoutParams(LLParams);

                LinearLayout llFirst=new LinearLayout(this);
                llFirst.setOrientation(LinearLayout.VERTICAL);

                ViewGroup.LayoutParams LLParamsFirst = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                llFirst.setLayoutParams(LLParamsFirst);

                LinearLayout llSecond=new LinearLayout(this);
                llSecond.setOrientation(LinearLayout.VERTICAL);

                ViewGroup.LayoutParams LLParamsSecond = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                llSecond.setLayoutParams(LLParamsSecond);


                ImageView ladder = new ImageView(this);

                if(i==pick){
                    ladder.setImageResource(R.drawable.ic_source);
                }
                else if(i==drop){
                    ladder.setImageResource(R.drawable.ic_destination);
                }
                else {
                    ladder.setImageResource(R.drawable.ic_booking_bottom_circle);
                }
                TextView title=new TextView(this);
                title.setText(Constant.pointName[i]);
                title.setPadding(10, 0, 0, 0);
                title.setTextColor(getResources().getColor(R.color.BlueTextColor));
                //title.setTextSize(getResources().getDimension(R.dimen.booking_text_size));
                llFirst.addView(ladder);
                llSecond.addView(title);

                if(i!=Constant.pointID.length-1) {
                    ImageView ivLine = new ImageView(this);

                    ivLine.setImageResource(R.drawable.ic_booking_lines);

                    ImageView ivLineInvisible = new ImageView(this);
                    ivLineInvisible.setImageResource(R.drawable.ic_booking_lines);
                    ivLineInvisible.setVisibility(View.INVISIBLE);


                    llFirst.addView(ivLine);
                    llSecond.addView(ivLineInvisible);
                }

                LL.addView(llFirst);
                LL.addView(llSecond);
                llMain.addView(LL);

            }


            rlDiagram.addView(llMain);

        }


    private void showDialogResponse(final Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText("D'oh!");
        tvMsg.setText("Sorry, something didn't quite work.");
        btnClosePopup.setText("Cancel");
        btnsave.setText("Try again?");


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           new GetSelectedRoute().execute(routeId);

                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

    private void showDialogCredit(final Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText("Not enough Loop Credits");
        tvMsg.setText("You don't have enough Loop Credits left to book this ride");
        btnClosePopup.setText("Cancel");
        btnsave.setText("Buy Loop Credits");


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           startActivity(new Intent(getApplicationContext(), AddLoopCredit.class));

                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

    private void showDialogConnection(final Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText("No Internet");
        tvMsg.setText("Looks like you have no or very slow data connectivity.");
        btnClosePopup.setText("Cancel");
        btnsave.setText("Try again?");


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           new GetSelectedRoute().execute(routeId);
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

}
