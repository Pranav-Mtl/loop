package com.aggregator.loop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aggregator.Adapters.DrawerAdapter;
import com.aggregator.BE.BookingBE;
import com.aggregator.BL.BookingBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class BookingScreen extends AppCompatActivity implements View.OnTouchListener,
        View.OnDragListener,View.OnClickListener {

    TextView tvTimePicker,tvPrice;
    TextView tvPickDrag,tvDropDrag;
    LinearLayout llDropFirst,llDropSecond,llDropThird,llDropFourth,llDropFifth,llDropSource,llDropDestination;
    LinearLayout llDiag2,llDiag3,llDiag4,llDiag5,llDiag6;
    LinearLayout llImg2,llImg3,llImg4,llImg5,llImg6;
    LinearLayout llText2,llText3,llText4,llText5,llText6;
    LinearLayout llCircleFirst,llCircleSecond,llCircleThird,llCircleFourth,llCircleFifth,llCircleSource,llCircleDestination;
    TextView tvPick,tvDrop;
    Spinner spnTimePicker;

    TextView tvAddressFirst,tvAddressSecond,tvAddressThird,tvAddressFourth,tvAddressFifth,tvAddressSource,tvAddressDestination;

    Button btnDone;
    ImageButton btnSwipe;

    ImageView ivPickFirst,ivPickSecond,ivPickThird,ivPickFourth,ivPickFifth,ivPickSource;
    ImageView ivDropFirst,ivDropSecond,ivDropThird,ivDropFourth,ivDropFifth,ivDropDestination;

    BookingBL objBookingBL;
    BookingBE objBookingBE;

    String TITLES[] = {"Edit Profile","Trips", "Trip Feedback","Book a Ride", "Promos", "Notification", "Rate Us","Tutorial","Invite & Earn","Help", "Signout"};
    int ICONS[] = {R.drawable.ic_side_edit_profile,R.drawable.ic_side_trips, R.drawable.ic_side_feedback,R.drawable.ic_side_trips,R.drawable.ic_side_promo,R.drawable.ic_side_notification, R.drawable.ic_side_rate, R.drawable.ic_side_edit_profile,R.drawable.ic_side_invite_earn,R.drawable.ic_side_help, R.drawable.ic_side_sign_out};

    String routeId;

    ProgressDialog mProgressDialog;

    int sourcePrice,destinationPrice;

    int getRouteSize;

    int PickLocation,DropLocation;

    TextView tvErrorMsg;

    boolean flag=true;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;

    DrawerAdapter drawerAdapter;

    String NAME = "";
    String LoopCredit = "";
    String PayTMWalet ="";

    private Toolbar toolbar;

    String loginStatus;
    ImageButton btnBack;

    View _lastColored,_itemColoured;

    ArrayList arrTime=new ArrayList();


    private float dx; // postTranslate X distance
    private float dy; // postTranslate Y distance
    private float[] matrixValues = new float[9];
    float matrixX = 0; // X coordinate of matrix inside the ImageView
    float matrixY = 0; // Y coordinate of matrix inside the ImageView
    float width = 0; // width of drawable
    float height = 0; // height of drawable

    int HH,MM,SS;
    SimpleDateFormat sdf = new SimpleDateFormat("KK:mm");
    String currentTime;
    String compareTime;
    Date dtCurrent,dtCompare;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_screen);

        loginStatus= Util.getSharedPrefrenceValue(BookingScreen.this,Constant.SHARED_PREFERENCE_LOGIN_STATUS);

        spnTimePicker= (Spinner) findViewById(R.id.booking_timepicker);
        tvPrice= (TextView) findViewById(R.id.booking_price);
        btnBack= (ImageButton) findViewById(R.id.booking_back);

        llDropFirst= (LinearLayout) findViewById(R.id.ll_image_first);
        llDropSecond= (LinearLayout) findViewById(R.id.ll_image_second);
        llDropThird= (LinearLayout) findViewById(R.id.ll_image_third);
        llDropFourth= (LinearLayout) findViewById(R.id.ll_image_fourth);
        llDropFifth= (LinearLayout) findViewById(R.id.ll_image_fifth);
        llDropSource= (LinearLayout) findViewById(R.id.ll_image_source);
        llDropDestination= (LinearLayout) findViewById(R.id.ll_image_destination);

        llCircleFirst= (LinearLayout) findViewById(R.id.ll_circle_first);
        llCircleSecond= (LinearLayout) findViewById(R.id.ll_circle_second);
        llCircleThird= (LinearLayout) findViewById(R.id.ll_circle_third);
        llCircleFourth= (LinearLayout) findViewById(R.id.ll_circle_fourth);
        llCircleFifth= (LinearLayout) findViewById(R.id.ll_circle_fifth);
        llCircleSource= (LinearLayout) findViewById(R.id.ll_circle_source);
        llCircleDestination= (LinearLayout) findViewById(R.id.ll_circle_destination);

        tvPickDrag= (TextView) findViewById(R.id.tv_pick_drag);
        tvDropDrag= (TextView) findViewById(R.id.tv_drop_drag);

        tvPick= (TextView) findViewById(R.id.tv_pick);
        tvDrop= (TextView) findViewById(R.id.tv_drop);

        tvAddressFirst= (TextView) findViewById(R.id.tv_address_first);
        tvAddressSecond= (TextView) findViewById(R.id.tv_address_second);
        tvAddressThird= (TextView) findViewById(R.id.tv_address_third);
        tvAddressFourth= (TextView) findViewById(R.id.tv_address_fourth);
        tvAddressFifth= (TextView) findViewById(R.id.tv_address_fifth);
        tvAddressSource= (TextView) findViewById(R.id.tv_address_source);
        tvAddressDestination= (TextView) findViewById(R.id.tv_address_destination);

        btnDone= (Button) findViewById(R.id.booking_screen_btn);
        btnSwipe= (ImageButton) findViewById(R.id.booking_swap_button);

        ivPickSource= (ImageView) findViewById(R.id.ic_pick_source);
        ivPickFirst= (ImageView) findViewById(R.id.ic_pick_first);
        ivPickSecond= (ImageView) findViewById(R.id.ic_pick_second);
        ivPickThird= (ImageView) findViewById(R.id.ic_pick_third);
        ivPickFourth= (ImageView) findViewById(R.id.ic_pick_fourth);
        ivPickFifth= (ImageView) findViewById(R.id.ic_pick_fifth);

        ivDropFirst= (ImageView) findViewById(R.id.ic_drop_first);
        ivDropSecond= (ImageView) findViewById(R.id.ic_drop_second);
        ivDropThird= (ImageView) findViewById(R.id.ic_drop_third);
        ivDropFourth= (ImageView) findViewById(R.id.ic_drop_fourth);
        ivDropFifth= (ImageView) findViewById(R.id.ic_drop_fifth);
        ivDropDestination= (ImageView) findViewById(R.id.ic_drop_destination);

        llDiag2= (LinearLayout) findViewById(R.id.ll_first);
        llDiag3= (LinearLayout) findViewById(R.id.ll_second);
        llDiag4= (LinearLayout) findViewById(R.id.ll_third);
        llDiag5= (LinearLayout) findViewById(R.id.ll_fourth);
        llDiag6= (LinearLayout) findViewById(R.id.ll_fifth);

        llImg2= (LinearLayout) findViewById(R.id.ll_img_first);
        llImg3= (LinearLayout) findViewById(R.id.ll_img_second);
        llImg4= (LinearLayout) findViewById(R.id.ll_img_third);
        llImg5= (LinearLayout) findViewById(R.id.ll_img_fourth);
        llImg6= (LinearLayout) findViewById(R.id.ll_img_fifth);

        llText2= (LinearLayout) findViewById(R.id.ll_text_first);
        llText3= (LinearLayout) findViewById(R.id.ll_text_second);
        llText4= (LinearLayout) findViewById(R.id.ll_text_third);
        llText5= (LinearLayout) findViewById(R.id.ll_text_fourth);
        llText6= (LinearLayout) findViewById(R.id.ll_text_fifth);

        tvErrorMsg= (TextView) findViewById(R.id.booking_error);

        mProgressDialog=new ProgressDialog(BookingScreen.this);

        objBookingBL=new BookingBL();
        objBookingBE=new BookingBE();


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //System.out.println("At home Screen2");
        setSupportActionBar(toolbar);

        NAME="Full Name";
        LoopCredit="Loop Credit: Rs. 500";
        PayTMWalet="Paytm Wallet: Rs 200";

        drawerAdapter = new DrawerAdapter(TITLES, ICONS, NAME, LoopCredit,PayTMWalet, getApplicationContext());       // Creating the Adapter of com.example.balram.sampleactionbar.MyAdapter class(which we are going to see in a bit)

        mRecyclerView.setAdapter(drawerAdapter);                              // Setting the adapter to RecyclerView
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

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
         HH = cal.get(Calendar.HOUR);
         MM = cal.get(Calendar.MINUTE);
         SS = cal.get(Calendar.SECOND);
         currentTime=HH+":"+MM+":"+SS;
        try {
            dtCurrent = sdf.parse(currentTime);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        routeId=getIntent().getExtras().get("RouteId").toString();

        if(Util.isInternetConnection(BookingScreen.this)){
            new GetSelectedRoute().execute(routeId);
        }
        else{
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(BookingScreen.this);

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


        btnDone.setOnClickListener(this);
        btnSwipe.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        tvPickDrag.setOnTouchListener(this);
        tvDropDrag.setOnTouchListener(this);

        //ivPickSource.setOnTouchListener(this);
        //ivDropDestination.setOnTouchListener(this);



        llDropFirst.setOnDragListener(this);
        llDropSecond.setOnDragListener(this);
        llDropThird.setOnDragListener(this);
        llDropFourth.setOnDragListener(this);
        llDropFifth.setOnDragListener(this);
        llDropSource.setOnDragListener(this);
        llDropDestination.setOnDragListener(this);

      /*  llCircleFirst.setOnDragListener(this);
        llCircleSecond.setOnDragListener(this);
        llCircleThird.setOnDragListener(this);
        llCircleFourth.setOnDragListener(this);
        llCircleFifth.setOnDragListener(this);
        llCircleSource.setOnDragListener(this);
        llCircleDestination.setOnDragListener(this);*/


      /*  ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.spinner_item,"k");
        spnTimePicker.setAdapter(adapter);*/

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if(position!=0){
                            if(_itemColoured != null)
                            {
                                _itemColoured.setBackgroundColor(Color.parseColor("#1fc796"));
                                _itemColoured.invalidate();
                            }
                            _itemColoured = view;
                            view.setBackgroundColor(Color.parseColor("#66daae"));
                        }
                    }
                }));
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

    @Override
    public boolean onDrag(View layoutview, DragEvent dragevent) {
        int action = dragevent.getAction();
        View view = (View) dragevent.getLocalState();

        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                System.out.println("ACTION_DRAG_STARTED");
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                System.out.println("ACTION_DRAG_ENTERED");
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                System.out.println("ACTION_DRAG_EXITED");
                break;
            case DragEvent.ACTION_DROP:
                System.out.println("ACTION_DROP");
                ViewGroup owner = (ViewGroup) view.getParent();
                owner.removeView(view);
                LinearLayout container = (LinearLayout) layoutview;
                container.addView(view);
                view.setVisibility(View.VISIBLE);

                if(container.getId()==R.id.ll_image_source || container.getId()==R.id.ll_circle_source){
                    if(view.getId()==R.id.tv_pick_drag || view.getId()==R.id.ic_pick_source) {
                        tvPick.setText(tvAddressSource.getText().toString());

                        ivPickSource.setVisibility(View.VISIBLE);
                        ivPickFirst.setVisibility(View.GONE);
                        ivPickSecond.setVisibility(View.GONE);
                        ivPickThird.setVisibility(View.GONE);
                        ivPickFourth.setVisibility(View.GONE);
                        ivPickFifth.setVisibility(View.GONE);

                        //tvTimePicker.setText(Constant.boookingRouteTime[0]);
                        String splitTime[]=Constant.boookingRouteTime[0].split(",");
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookingScreen.this,R.layout.spinner_item,splitTime);
                        spnTimePicker.setAdapter(adapter);

                        sourcePrice=Integer.valueOf(Constant.boookingRoutePrice[0]);

                        PickLocation=0;

                    }
                    else {
                        tvDrop.setText(tvAddressSource.getText().toString());

                        ivDropFirst.setVisibility(View.GONE);
                        ivDropSecond.setVisibility(View.GONE);
                        ivDropThird.setVisibility(View.GONE);
                        ivDropFourth.setVisibility(View.GONE);
                        ivDropFifth.setVisibility(View.GONE);

                        destinationPrice=Integer.valueOf(Constant.boookingRoutePrice[0]);
                        DropLocation=0;
                    }
                   tvPrice.setText(destinationPrice-sourcePrice+"");
                    if(PickLocation>DropLocation)
                    {
                        tvErrorMsg.setVisibility(View.VISIBLE);
                        flag=false;
                    }
                    else
                    {
                        tvErrorMsg.setVisibility(View.GONE);
                        flag=true;
                    }

                }
                else if(container.getId()==R.id.ll_image_first || container.getId()==R.id.ll_circle_first){
                    if(view.getId()==R.id.tv_pick_drag || view.getId()==R.id.ic_pick_source) {
                        tvPick.setText(tvAddressFirst.getText().toString());

                        ivPickSource.setVisibility(View.GONE);
                        ivPickFirst.setVisibility(View.VISIBLE);
                        ivPickSecond.setVisibility(View.GONE);
                        ivPickThird.setVisibility(View.GONE);
                        ivPickFourth.setVisibility(View.GONE);
                        ivPickFifth.setVisibility(View.GONE);

                        //tvTimePicker.setText(Constant.boookingRouteTime[1]);
                        String splitTime[]=Constant.boookingRouteTime[1].split(",");
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookingScreen.this,R.layout.spinner_item,splitTime);
                        spnTimePicker.setAdapter(adapter);
                        sourcePrice=Integer.valueOf(Constant.boookingRoutePrice[1]);

                        PickLocation=1;


                    }
                    else {
                     //   tvDrop.setText(tvAddressFirst.getText().toString());


                        ivDropFirst.setVisibility(View.VISIBLE);
                        ivDropSecond.setVisibility(View.GONE);
                        ivDropThird.setVisibility(View.GONE);
                        ivDropFourth.setVisibility(View.GONE);
                        ivDropFifth.setVisibility(View.GONE);
                        ivDropDestination.setVisibility(View.GONE);

                        destinationPrice=Integer.valueOf(Constant.boookingRoutePrice[1]);

                        DropLocation=1;

                    }
                    tvPrice.setText(destinationPrice-sourcePrice+"");
                    if(PickLocation>=DropLocation)
                    {
                        tvErrorMsg.setVisibility(View.VISIBLE);
                        flag=false;
                    }
                    else
                    {
                        tvErrorMsg.setVisibility(View.GONE);
                        flag=true;
                    }
                }
                else if(container.getId()==R.id.ll_image_second || container.getId()==R.id.ll_circle_second){
                    if(view.getId()==R.id.tv_pick_drag || view.getId()==R.id.ic_pick_source) {
                        tvPick.setText(tvAddressSecond.getText().toString());

                        ivPickSource.setVisibility(View.GONE);
                        ivPickFirst.setVisibility(View.GONE);
                        ivPickSecond.setVisibility(View.VISIBLE);
                        ivPickThird.setVisibility(View.GONE);
                        ivPickFourth.setVisibility(View.GONE);
                        ivPickFifth.setVisibility(View.GONE);

                        //tvTimePicker.setText(Constant.boookingRouteTime[2]);

                        String splitTime[]=Constant.boookingRouteTime[2].split(",");
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookingScreen.this,R.layout.spinner_item,splitTime);
                        spnTimePicker.setAdapter(adapter);

                        sourcePrice=Integer.valueOf(Constant.boookingRoutePrice[2]);

                        PickLocation=2;


                    }
                    else {
                        tvDrop.setText(tvAddressSecond.getText().toString());


                        ivDropFirst.setVisibility(View.GONE);
                        ivDropSecond.setVisibility(View.VISIBLE);
                        ivDropThird.setVisibility(View.GONE);
                        ivDropFourth.setVisibility(View.GONE);
                        ivDropFifth.setVisibility(View.GONE);
                        ivDropDestination.setVisibility(View.GONE);

                        destinationPrice=Integer.valueOf(Constant.boookingRoutePrice[2]);

                        DropLocation=2;
                    }
                    tvPrice.setText(destinationPrice-sourcePrice+"");

                    if(PickLocation>=DropLocation)
                    {
                        tvErrorMsg.setVisibility(View.VISIBLE);
                        flag=false;
                    }
                    else
                    {
                        tvErrorMsg.setVisibility(View.GONE);
                        flag=true;
                    }
                }
                else if(container.getId()==R.id.ll_image_third || container.getId()==R.id.ll_circle_third){
                    if(view.getId()==R.id.tv_pick_drag || view.getId()==R.id.ic_pick_source) {
                        tvPick.setText(tvAddressThird.getText().toString());
                        ivPickSource.setVisibility(View.GONE);
                        ivPickFirst.setVisibility(View.GONE);
                        ivPickSecond.setVisibility(View.GONE);
                        ivPickThird.setVisibility(View.VISIBLE);
                        ivPickFourth.setVisibility(View.GONE);
                        ivPickFifth.setVisibility(View.GONE);

                        //tvTimePicker.setText(Constant.boookingRouteTime[3]);

                        String splitTime[]=Constant.boookingRouteTime[3].split(",");
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookingScreen.this,R.layout.spinner_item,splitTime);
                        spnTimePicker.setAdapter(adapter);

                        sourcePrice=Integer.valueOf(Constant.boookingRoutePrice[3]);

                        PickLocation=3;
                    }
                    else {
                        tvDrop.setText(tvAddressThird.getText().toString());


                        ivDropFirst.setVisibility(View.GONE);
                        ivDropSecond.setVisibility(View.GONE);
                        ivDropThird.setVisibility(View.VISIBLE);
                        ivDropFourth.setVisibility(View.GONE);
                        ivDropFifth.setVisibility(View.GONE);
                        ivDropDestination.setVisibility(View.GONE);

                        destinationPrice=Integer.valueOf(Constant.boookingRoutePrice[3]);

                        DropLocation=3;
                    }
                    tvPrice.setText(destinationPrice-sourcePrice+"");

                    if(PickLocation>=DropLocation)
                    {
                        tvErrorMsg.setVisibility(View.VISIBLE);
                        flag=false;
                    }
                    else
                    {
                        tvErrorMsg.setVisibility(View.GONE);
                        flag=true;
                    }

                }
                else if(container.getId()==R.id.ll_image_fourth || container.getId()==R.id.ll_circle_fourth){
                    if(view.getId()==R.id.tv_pick_drag || view.getId()==R.id.ic_pick_source) {
                        tvPick.setText(tvAddressFourth.getText().toString());
                        ivPickSource.setVisibility(View.GONE);
                        ivPickFirst.setVisibility(View.GONE);
                        ivPickSecond.setVisibility(View.GONE);
                        ivPickThird.setVisibility(View.VISIBLE);
                        ivPickFourth.setVisibility(View.GONE);
                        ivPickFifth.setVisibility(View.GONE);

                       // tvTimePicker.setText(Constant.boookingRouteTime[4]);

                        String splitTime[]=Constant.boookingRouteTime[4].split(",");
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookingScreen.this,R.layout.spinner_item,splitTime);
                        spnTimePicker.setAdapter(adapter);

                        sourcePrice=Integer.valueOf(Constant.boookingRoutePrice[4]);

                        PickLocation=4;
                    }
                    else {
                        tvDrop.setText(tvAddressFourth.getText().toString());


                        ivDropFirst.setVisibility(View.GONE);
                        ivDropSecond.setVisibility(View.GONE);
                        ivDropThird.setVisibility(View.GONE);
                        ivDropFourth.setVisibility(View.VISIBLE);
                        ivDropFifth.setVisibility(View.GONE);
                        ivDropDestination.setVisibility(View.GONE);

                        destinationPrice=Integer.valueOf(Constant.boookingRoutePrice[4]);

                        DropLocation=4;
                    }
                    tvPrice.setText(destinationPrice-sourcePrice+"");

                    if(PickLocation>=DropLocation)
                    {
                        tvErrorMsg.setVisibility(View.VISIBLE);
                        flag=false;
                    }
                    else
                    {
                        tvErrorMsg.setVisibility(View.GONE);
                        flag=true;
                    }

                }
                else if(container.getId()==R.id.ll_image_fifth || container.getId()==R.id.ll_circle_fifth){
                    if(view.getId()==R.id.tv_pick_drag || view.getId()==R.id.ic_pick_source) {
                        tvPick.setText(tvAddressFifth.getText().toString());
                        ivPickSource.setVisibility(View.GONE);
                        ivPickFirst.setVisibility(View.GONE);
                        ivPickSecond.setVisibility(View.GONE);
                        ivPickThird.setVisibility(View.GONE);
                        ivPickFourth.setVisibility(View.GONE);
                        ivPickFifth.setVisibility(View.VISIBLE);

                        //tvTimePicker.setText(Constant.boookingRouteTime[5]);

                        String splitTime[]=Constant.boookingRouteTime[5].split(",");
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookingScreen.this,R.layout.spinner_item,splitTime);
                        spnTimePicker.setAdapter(adapter);

                        sourcePrice=Integer.valueOf(Constant.boookingRoutePrice[5]);

                        PickLocation=5;
                    }
                    else {
                        tvDrop.setText(tvAddressFifth.getText().toString());
                        /*ivPickFirst.setVisibility(View.GONE);
                        ivPickSecond.setVisibility(View.GONE);
                        ivPickThird.setVisibility(View.GONE);
                        ivPickFourth.setVisibility(View.GONE);*/

                        ivDropFirst.setVisibility(View.GONE);
                        ivDropSecond.setVisibility(View.GONE);
                        ivDropThird.setVisibility(View.GONE);
                        ivDropFourth.setVisibility(View.GONE);
                        ivDropFifth.setVisibility(View.VISIBLE);
                        ivDropDestination.setVisibility(View.GONE);

                        destinationPrice=Integer.valueOf(Constant.boookingRoutePrice[5]);

                        DropLocation=5;
                    }
                    tvPrice.setText(destinationPrice-sourcePrice+"");
                    if(PickLocation>=DropLocation)
                    {
                        tvErrorMsg.setVisibility(View.VISIBLE);
                        flag=false;
                    }
                    else
                    {
                        tvErrorMsg.setVisibility(View.GONE);
                        flag=true;
                    }

                }
                else if(container.getId()==R.id.ll_image_destination || container.getId()==R.id.ll_circle_destination){
                    if(view.getId()==R.id.tv_pick_drag || view.getId()==R.id.ic_pick_source) {
                        tvPick.setText(tvAddressDestination.getText().toString());
                        ivPickSource.setVisibility(View.GONE);
                        ivPickFirst.setVisibility(View.GONE);
                        ivPickSecond.setVisibility(View.GONE);
                        ivPickThird.setVisibility(View.GONE);
                        ivPickFourth.setVisibility(View.GONE);
                        ivPickFifth.setVisibility(View.GONE);

                        sourcePrice=Integer.valueOf(Constant.boookingRoutePrice[getRouteSize-1]);

                        PickLocation=getRouteSize-1;
                    }
                    else {
                        tvDrop.setText(tvAddressDestination.getText().toString());
                        /*ivPickFirst.setVisibility(View.GONE);
                        ivPickSecond.setVisibility(View.GONE);
                        ivPickThird.setVisibility(View.GONE);
                        ivPickFourth.setVisibility(View.GONE);*/


                        ivDropFirst.setVisibility(View.GONE);
                        ivDropSecond.setVisibility(View.GONE);
                        ivDropThird.setVisibility(View.GONE);
                        ivDropFourth.setVisibility(View.GONE);
                        ivDropFifth.setVisibility(View.GONE);
                        ivDropDestination.setVisibility(View.VISIBLE);

                        destinationPrice=Integer.valueOf(Constant.boookingRoutePrice[getRouteSize-1]);
                        DropLocation=getRouteSize-1;
                    }
                    tvPrice.setText(destinationPrice-sourcePrice+"");

                    if(PickLocation>=DropLocation)
                    {
                        tvErrorMsg.setVisibility(View.VISIBLE);
                        flag=false;
                    }
                    else
                    {
                        tvErrorMsg.setVisibility(View.GONE);
                        flag=true;
                    }
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                // Log.d(LOGCAT, "Drag ended");
                if (dropEventNotHandled(dragevent)){
                    view.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(null, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);

            return true;
        } else {
            return false;
        }
    }


    private boolean dropEventNotHandled(DragEvent dragEvent) {
        return !dragEvent.getResult();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.booking_screen_btn:
                //startActivity(new Intent(BookingScreen.this,TicketScreen.class));
                if(flag)
                {
                    if(loginStatus==null){

                        final Calendar c = Calendar.getInstance();
                        int yy,mm,dd;
                        yy = c.get(Calendar.YEAR);
                        mm = c.get(Calendar.MONTH);
                        dd = c.get(Calendar.DAY_OF_MONTH);


                       /* Util.setSharedPrefrenceValue(BookingScreen.this, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_BOOKING_DATE, yy + "-" + mm + "-" + dd);
                        Util.setSharedPrefrenceValue(BookingScreen.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_ROUTE_ID,routeId);
                        Util.setSharedPrefrenceValue(BookingScreen.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_SOURCE,tvPick.getText().toString());
                        Util.setSharedPrefrenceValue(BookingScreen.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_DESTINATION,tvDrop.getText().toString());
                        Util.setSharedPrefrenceValue(BookingScreen.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_PRICE,tvPrice.getText().toString());
                        Util.setSharedPrefrenceValue(BookingScreen.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_BOOKING_TIME,spnTimePicker.getSelectedItem().toString());*/

                        startActivity(new Intent(BookingScreen.this,SignUpScreen.class));
                    }
                    else
                    {
                        //booking();
                    }

                }
                //booking();
                break;
            case R.id.booking_swap_button:
                swipeLocation();
                break;
            case R.id.booking_back:
                finish();
                break;

        }
    }

    void swipeLocation()
    {
        String locPick=tvPick.getText().toString();
        String locDrop=tvDrop.getText().toString();

        tvPick.setText("");
        tvDrop.setText("");

        tvPick.setText(locDrop);
        tvDrop.setText(locPick);

    }

    private class GetSelectedRoute extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }
        @Override
        protected String doInBackground(String... params) {
            String result=objBookingBL.getRoutesDetail(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                getRouteSize= Constant.boookingRouteName.length;

                tvAddressSource.setText(Constant.boookingRouteName[0]);
                tvAddressDestination.setText(Constant.boookingRouteName[getRouteSize-1]);

                tvPick.setText(Constant.boookingRouteName[0]);
                tvDrop.setText(Constant.boookingRouteName[getRouteSize-1]);

                //tvTimePicker.setText(Constant.boookingRouteTime[0]);


                 String splitTime[]=Constant.boookingRouteTime[0].split(",");

                for(int j=0;j<splitTime.length;j++)
                {
                    dtCompare=sdf.parse(splitTime[j]);
                    System.out.print("Current Time"+dtCurrent);
                    System.out.print("Compared Time"+dtCompare);
                    if (dtCompare.compareTo(dtCurrent)==0){
                        System.out.print("under");
                    }
                    else
                    {
                        System.out.print("outside");
                    }
                }

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookingScreen.this,R.layout.spinner_item,splitTime);
                spnTimePicker.setAdapter(adapter);

                tvPrice.setText(Integer.valueOf(Constant.boookingRoutePrice[getRouteSize-1])-Integer.valueOf(Constant.boookingRoutePrice[0])+"");

                sourcePrice=Integer.valueOf(Constant.boookingRoutePrice[0]);
                destinationPrice=Integer.valueOf(Constant.boookingRoutePrice[getRouteSize-1]);

                PickLocation=0;
                DropLocation=getRouteSize-1;

                if(getRouteSize>2)
                {
                    if(getRouteSize==3){
                        llDiag2.setVisibility(View.VISIBLE);
                        llImg2.setVisibility(View.VISIBLE);
                        llText2.setVisibility(View.VISIBLE);
                        tvAddressFirst.setText(Constant.boookingRouteName[1]);
                    }
                    else if(getRouteSize==4){
                        llDiag2.setVisibility(View.VISIBLE);
                        llDiag3.setVisibility(View.VISIBLE);

                        llImg2.setVisibility(View.VISIBLE);
                        llImg3.setVisibility(View.VISIBLE);

                        llText2.setVisibility(View.VISIBLE);
                        llText3.setVisibility(View.VISIBLE);

                        tvAddressFirst.setText(Constant.boookingRouteName[1]);
                        tvAddressSecond.setText(Constant.boookingRouteName[2]);
                    }
                    else if(getRouteSize==5){
                        llDiag2.setVisibility(View.VISIBLE);
                        llDiag3.setVisibility(View.VISIBLE);
                        llDiag4.setVisibility(View.VISIBLE);

                        llImg2.setVisibility(View.VISIBLE);
                        llImg3.setVisibility(View.VISIBLE);
                        llImg4.setVisibility(View.VISIBLE);


                        llText2.setVisibility(View.VISIBLE);
                        llText3.setVisibility(View.VISIBLE);
                        llText4.setVisibility(View.VISIBLE);

                        tvAddressFirst.setText(Constant.boookingRouteName[1]);
                        tvAddressSecond.setText(Constant.boookingRouteName[2]);
                        tvAddressThird.setText(Constant.boookingRouteName[3]);
                    }
                    else if(getRouteSize==6){
                        llDiag2.setVisibility(View.VISIBLE);
                        llDiag3.setVisibility(View.VISIBLE);
                        llDiag4.setVisibility(View.VISIBLE);
                        llDiag5.setVisibility(View.VISIBLE);

                        llImg2.setVisibility(View.VISIBLE);
                        llImg3.setVisibility(View.VISIBLE);
                        llImg4.setVisibility(View.VISIBLE);
                        llImg5.setVisibility(View.VISIBLE);

                        llText2.setVisibility(View.VISIBLE);
                        llText3.setVisibility(View.VISIBLE);
                        llText4.setVisibility(View.VISIBLE);
                        llText5.setVisibility(View.VISIBLE);

                        tvAddressFirst.setText(Constant.boookingRouteName[1]);
                        tvAddressSecond.setText(Constant.boookingRouteName[2]);
                        tvAddressThird.setText(Constant.boookingRouteName[3]);
                        tvAddressFourth.setText(Constant.boookingRouteName[4]);
                    }
                    else if(getRouteSize==7){
                        llDiag2.setVisibility(View.VISIBLE);
                        llDiag3.setVisibility(View.VISIBLE);
                        llDiag4.setVisibility(View.VISIBLE);
                        llDiag5.setVisibility(View.VISIBLE);
                        llDiag6.setVisibility(View.VISIBLE);

                        llImg2.setVisibility(View.VISIBLE);
                        llImg3.setVisibility(View.VISIBLE);
                        llImg4.setVisibility(View.VISIBLE);
                        llImg5.setVisibility(View.VISIBLE);
                        llImg6.setVisibility(View.VISIBLE);

                        llText2.setVisibility(View.VISIBLE);
                        llText3.setVisibility(View.VISIBLE);
                        llText4.setVisibility(View.VISIBLE);
                        llText5.setVisibility(View.VISIBLE);
                        llText6.setVisibility(View.VISIBLE);

                        tvAddressFirst.setText(Constant.boookingRouteName[1]);
                        tvAddressSecond.setText(Constant.boookingRouteName[2]);
                        tvAddressThird.setText(Constant.boookingRouteName[3]);
                        tvAddressFourth.setText(Constant.boookingRouteName[4]);
                        tvAddressFifth.setText(Constant.boookingRouteName[5]);
                    }

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

  /*  private void booking()
    {
        objBookingBE.setRouteID(routeId);
        objBookingBE.setRouteSource(tvPick.getText().toString());
        objBookingBE.setRouteDestination(tvDrop.getText().toString());
        objBookingBE.setRouteCost(tvPrice.getText().toString());
        objBookingBE.setRouteTime(spnTimePicker.getSelectedItem().toString());
        objBookingBE.setEmailId(loginStatus);

        final Calendar c = Calendar.getInstance();
        int yy,mm,dd;
        yy = c.get(Calendar.YEAR);
        mm = c.get(Calendar.MONTH);
        dd = c.get(Calendar.DAY_OF_MONTH);
        objBookingBE.setRoutedate(yy + "-" + mm + "-" + dd);

        if(Util.isInternetConnection(BookingScreen.this)){
            new InsertBooking().execute();
        }
        else{
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(BookingScreen.this);

// Setting Dialog Title
            alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);

// Setting Dialog Message
            alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);

// Setting Icon to Dialog


// Setting Positive "Yes" Btn
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
// Setting Negative "NO" Btn
            alertDialog2.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog

                            dialog.cancel();
                        }
                    });

// Showing Alert Dialog
            alertDialog2.show();

        }

    }*/

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
                if(s.equals(Constant.WS_RESULT_SUCCESS)){
                    startActivity(new Intent(BookingScreen.this,TicketScreen.class));
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
