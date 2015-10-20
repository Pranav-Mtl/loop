package com.aggregator.loop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aggregator.BE.TicketScreenBE;
import com.aggregator.BL.TicketScreenBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.aggregator.gps.GMapV2Direction;
import com.aggregator.gps.GPSTracker;
import com.aggregator.gps.GetDirectionsAsyncTask;
import com.aggregator.gps.GetETA;
import com.appsee.Appsee;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TicketScreen extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    GoogleMap googleMap;
    Button btnMap,btnImage;
    RelativeLayout llMap;
    ImageView imgTicket;
    ImageButton btnShare,btnRateTrip,btnOpenOption;

    ImageButton btnCross;

    Button btnGetDirection,btnTrack;

    TextView tvPick,tvDrop,tvTime,tvVehicle,tvVehicleNumber,tvETA;

    String userRunID;

    private Toolbar toolbar;

    boolean optionFlag=false;

    TicketScreenBL objTicketScreenBL;
    TicketScreenBE objTicketScreenBE;

    ProgressDialog mProgressDialog;

    static Double currentlongtitude;
    static Double currentlatitude;
    Location objLocation;
    Float distance;
    private LatLngBounds latlngBounds;
    private Polyline newPolyline;

    Marker markers;

    SimpleDateFormat dateFormatCurrent = new SimpleDateFormat("K:mm:ss");


    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    Calendar cal;

    String PickText;
    String CurrentText;

    // Animation
    Animation animBlink;

    RelativeLayout llHeader;

    private static LatLng Source,Destinatiom;

    ImageButton btnTicket;
    LinearLayout llBlink;

    String wayPoints="";

    double sourceLat,sourceLong,destinationLat,destinationLong;

    boolean flag=true;
    boolean flagImage=false;

    DateFormat dateFormatChange = new SimpleDateFormat("kk:mm:ss", Locale.ENGLISH);

    int xx,yy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_screen);

        Appsee.start("de8395d3ae424245b695b4c9d6642f71");

        Display display = getWindowManager().getDefaultDisplay();

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



        //btnMap= (Button) findViewById(R.id.ticket_btn_map);
        btnImage= (Button) findViewById(R.id.ticket_btn_image);
        btnShare= (ImageButton) findViewById(R.id.ticket_btn_share);
        btnRateTrip= (ImageButton) findViewById(R.id.ticket_rate_trip);
        btnOpenOption= (ImageButton) findViewById(R.id.ticket_options);
        llMap= (RelativeLayout) findViewById(R.id.ticket_map_ll);
        imgTicket= (ImageView) findViewById(R.id.ticket_image);
        btnCross= (ImageButton) findViewById(R.id.ticket_cross);
        btnTicket= (ImageButton) findViewById(R.id.ticket_img);
        llBlink= (LinearLayout) findViewById(R.id.ll_blink);
        btnTrack= (Button) findViewById(R.id.ticket_btn_eta);

        tvPick= (TextView) findViewById(R.id.ticket_pick);
        tvDrop= (TextView) findViewById(R.id.ticket_drop);
        tvVehicle= (TextView) findViewById(R.id.ticket_vehicle);
        tvTime= (TextView) findViewById(R.id.ticket_time);
        tvVehicleNumber= (TextView) findViewById(R.id.ticket_vehicle_number);
        tvETA= (TextView) findViewById(R.id.ticket_eta);
        btnGetDirection= (Button) findViewById(R.id.ticket_btn_direction);

        llHeader= (RelativeLayout) findViewById(R.id.ticket_header_ll);

        //btnMap.setBackgroundResource(R.drawable.ic_map_btn_pressed);
        //btnMap.setTextColor(getResources().getColor(R.color.WhiteColor));


        mProgressDialog=new ProgressDialog(TicketScreen.this);

        objTicketScreenBL=new TicketScreenBL();
        objTicketScreenBE=new TicketScreenBE();

      //  btnMap.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnRateTrip.setOnClickListener(this);
        btnGetDirection.setOnClickListener(this);
        btnTrack.setOnClickListener(this);
        btnOpenOption.setOnClickListener(this);

        btnCross.setOnClickListener(this);

        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //System.out.println("At home Screen2");
        setSupportActionBar(toolbar);

        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();

        userRunID=getIntent().getExtras().get("BookingID").toString();

        try
        {
           if(Util.isInternetConnection(TicketScreen.this)) {

               final Handler handler = new Handler();
               Timer timer = new Timer();
               TimerTask doAsynchronousTask = new TimerTask() {
                   @Override
                   public void run() {
                       handler.post(new Runnable() {
                           public void run() {
                               try {
                                   //yout method here

                                   new GetTicketData().execute(userRunID);
                                   //  tvLatLong.setText(lat);
                                   //Toast.makeText(getApplicationContext(),currentlatitude+"/"+currentlongtitude+"",Toast.LENGTH_SHORT).show();

                               } catch (Exception e) {

                                   e.printStackTrace();
                               }
                           }
                       });
                   }
               };
               timer.schedule(doAsynchronousTask, 0, 180000); //execute in every 10 ms

           }
            else{
                    showDialogConnection(this);
           }




        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }


       /* googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                // TODO Auto-generated method stub
               startActivity(new Intent(TicketScreen.this,TicketMapFullScreen.class).putExtra("TicketScreenBE",objTicketScreenBE));
            }
        });*/

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

    private void initializeMap(Double lat,Double lon) {
        // TODO Auto-generated method stub


        LatLng latLng = new LatLng(lat, lon);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));

        //LatLng latlng=new LatLng(Double.valueOf(CreateGameRecord.latitude[0]),Double.valueOf(CreateGameRecord.longitude[0]));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {/*
            case R.id.ticket_btn_map:
                Log.i("Clicked","MAP");
                imgTicket.setVisibility(View.GONE);
                llMap.setVisibility(View.VISIBLE);
                btnMap.setBackgroundResource(R.drawable.ic_map_btn_pressed);
                btnMap.setTextColor(getResources().getColor(R.color.WhiteColor));
                btnImage.setBackgroundResource(R.drawable.ic_map_btn);
                btnImage.setTextColor(getResources().getColor(R.color.OranceColor));

                break;*/
            case R.id.ticket_btn_image:
                if(!flagImage) {
                    llMap.setVisibility(View.GONE);
                    imgTicket.setVisibility(View.VISIBLE);
                    flagImage=true;
                    btnImage.setText("Map");
                }
                else {
                    llMap.setVisibility(View.VISIBLE);
                    imgTicket.setVisibility(View.GONE);
                    flagImage=false;
                    btnImage.setText("Pickup point photo");
                }
                break;
            case R.id.ticket_btn_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("image/png");
                Uri uri = Uri.parse("android.resource://com.aggregator.loop/"+R.drawable.share);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                //sharingIntent.putExtra(Intent.EXTRA_TEXT, msgStart);
                String shareBody ="Hey! have you tried Loop? Smarter and cheaper option for daily commute. Click: http://tinyurl.com/pzxgpky to get the Loop app. Ask me for a referral code.";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.ticket_rate_trip:

                startActivity(new Intent(TicketScreen.this, TripFeedback.class).putExtra("RunID", userRunID));
              /* try {
                   Date date = new Date();
                   String formattedDateTime = dateFormat.format(date);
                   String selectedTime = dateFormat.format(objTicketScreenBE.getDepartureTime());

                   Date dtSlected = dateFormat.parse(selectedTime);
                   Date dtCurrent = dateFormat.parse(formattedDateTime);

                   long diffMs = dtSlected.getTime() - dtCurrent.getTime();
                   long diffSec = diffMs / 1000;
                   long min = diffSec / 60;
                   long sec = diffSec % 60;

                   Log.d("Ticket start Trip --> ",min+"");

                   if(min<0){
                       Toast.makeText(getApplicationContext(),getResources().getString(R.string.ticket_no_feedback_message),Toast.LENGTH_LONG).show();
                   }
                   else {
                       startActivity(new Intent(TicketScreen.this, TripFeedback.class).putExtra("RunID", userRunID));
                   }


               }
               catch (Exception e){
                   e.printStackTrace();
               }*/
                break;
            case R.id.ticket_cross:
                startActivity(new Intent(TicketScreen.this,TripHistory.class));
                break;
            case R.id.ticket_btn_direction:
                double destinationLatitude = objTicketScreenBE.getPickPointLat();
                double destinationLongitude = objTicketScreenBE.getPickPointLong();

                String url = "http://maps.google.com/maps?f=d&daddr="+ destinationLatitude+","+destinationLongitude+"&dirflg=d&layer=t";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
                break;
            case R.id.ticket_btn_eta:
                startActivity(new Intent(TicketScreen.this,TicketTrackBus.class).putExtra("TicketScreenBE",objTicketScreenBE));
                break;
            case R.id.ticket_options:
                if(!optionFlag){
                    btnRateTrip.setVisibility(View.VISIBLE);
                    btnShare.setVisibility(View.VISIBLE);
                    optionFlag=true;
                }
                else {
                    btnRateTrip.setVisibility(View.GONE);
                    btnShare.setVisibility(View.GONE);
                    optionFlag=false;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    private class GetTicketData extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String result=objTicketScreenBL.getTicketDetail(params[0],getApplicationContext(),objTicketScreenBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                SpannableString spanString = new SpannableString("Pickup :" + objTicketScreenBE.getPickPointName());

                spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, 0);

                SpannableString spanString2 = new SpannableString("Drop :" + objTicketScreenBE.getDropPointName());

                spanString2.setSpan(new StyleSpan(Typeface.BOLD), 0, 4, 0);

                SpannableString spanString3 = new SpannableString("In :" + objTicketScreenBE.getVehicleType() + "-" + objTicketScreenBE.getVehicleRegistration());

                spanString3.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);

                SpannableString spanString4 = new SpannableString("@ :" + objTicketScreenBE.getDepartureTime());

                spanString4.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);

                Date dtArray = dateFormat.parse(objTicketScreenBE.getDepartureTime());
                //String format=dateFormat.format(dd);





                tvPick.setText(objTicketScreenBE.getPickPointName());
                tvDrop.setText(objTicketScreenBE.getDropPointName());
                tvVehicle.setText(objTicketScreenBE.getVehicleType());
                tvTime.setText(new SimpleDateFormat("K:mm a").format(dtArray));
                tvVehicleNumber.setText(objTicketScreenBE.getVehicleRegistration());

                cal = Calendar.getInstance();
                String time=dateFormat.format(cal.getTime());

                Log.d("Current Time",time);
                Date d1 =dateFormat.parse(time);
                Date d2 = dateFormat.parse(objTicketScreenBE.getDepartureTime());
                long diffMs = d2.getTime()- d1.getTime() ;
                long diffSec = diffMs / 1000;
                long min = diffSec / 60;
                long sec = diffSec % 60;

                Log.d("Date Current Time", d1 + "");
                Log.d("Date Departure Time", d2 + "");

                Log.d("Current Time", time);
                Log.d("Departure Time", objTicketScreenBE.getDepartureTime());
                Log.d("Time difference", min + "");

                System.out.println("The difference is " + min + " minutes and " + sec + " seconds.");

                if(min<10 && min>-5){
                    btnTrack.setVisibility(View.INVISIBLE);

                    if(!(objTicketScreenBE.getBusLat()==null || objTicketScreenBE.getBusLong()==null)) {
                        showMarkerBus(objTicketScreenBE.getBusLat(), objTicketScreenBE.getBusLong());
                        findETA(objTicketScreenBE.getBusLat(), objTicketScreenBE.getBusLong(), objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong(), "", "Driving");
                    }
                }
                else {
                    btnTrack.setVisibility(View.INVISIBLE);
                }

                if(min<0){
                    btnRateTrip.setEnabled(true);
                }
                else
                {
                    btnRateTrip.setEnabled(false);
                }

                setRoute(objTicketScreenBE.getWayPoints());

                PickText = objTicketScreenBE.getPickPointName();

                initializeMap(objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong());

                findDirections(sourceLat, sourceLong, destinationLat, destinationLong, wayPoints, GMapV2Direction.MODE_WALKING);

                showMarkerPick(objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong());

                showMarkerDrop(objTicketScreenBE.getDropPointLat(), objTicketScreenBE.getDropPointLong());

                Date departure=dateFormat.parse(objTicketScreenBE.getEndPointTime());

                Log.d("Departure Time",departure+"");

                long diffMss = departure.getTime()- d1.getTime() ;
                long diffSecc = diffMss / 1000;
                long minn = diffSecc / 60;
                long secc = diffSecc % 60;

                if(minn<0 && minn<-10){
                    llBlink.setVisibility(View.INVISIBLE);
                }
                else {
                    llBlink.startAnimation(animBlink);
                }



             /*   Picasso.with(getApplicationContext())
                        .load(Constant.categoryImageURL[position])
                        .placeholder(R.drawable.ticket_pick)
                        .error(R.drawable.ticket_pick)
                        .into(imgTicket);*/

            }catch (NullPointerException e){
                showDialogResponse(TicketScreen.this);
            }catch (Exception e){
              //  NoResponseServer();
            }
            finally {
                mProgressDialog.dismiss();
            }


        }
    }

    /* Get current lat long of user by GPS*/

    private void getCurrentLocation() {
        // TODO Auto-generated method stub
        objLocation = GPSTracker.getInstance(
                getApplicationContext()).getLocation();
        currentlatitude=objLocation.getLatitude();
        currentlongtitude=objLocation.getLongitude();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /* CHECK GPS IS ENABLED OR NOT */
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

        }
        else {

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                try {

                    getCurrentLocation();
                    Log.d("Current Lat", currentlatitude + "");
                    Log.d("Current Lon", currentlongtitude + "");

                    if(flag) {
                        showMarker(currentlatitude, currentlongtitude);
                        flag=false;
                    }
                    else {
                        markers.remove();
                        showMarker(currentlatitude, currentlongtitude);
                    }
                    distance = getDistance(currentlatitude, currentlatitude, objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong());
                    PickText = PickText + " \n Distance :" + distance + " km";
                    Log.d("Distance-->", distance + "");
                    Source = new LatLng(currentlatitude, currentlongtitude);
                    Destinatiom = new LatLng(objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong());


                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                catch (Exception e){
                        e.printStackTrace();
                }



                }


            },4000);

        }


    }

    public void showMarker(Double Latitude, Double Longitude) {

        double latitude = Latitude;
        double longitude = Longitude;


        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title(
                Latitude + "\n" + Longitude);
        try{
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ticket_map_man));
            // adding marker

            marker.title("Here You Are !!!");

            markers=googleMap.addMarker(marker);




        }catch(Exception e){}

    }

    public void showMarkerPick(Double Latitude, Double Longitude) {

        double latitude = Latitude;
        double longitude = Longitude;

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title(
                Latitude + "\n" + Longitude);
        try{

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ticket_map_pick));
            // adding marker



            googleMap.addMarker(marker);
        }catch(Exception e){}

    }

    public void showMarkerBus(Double Latitude, Double Longitude) {

        double latitude = Latitude;
        double longitude = Longitude;

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title(
                Latitude + "\n" + Longitude);
        try{

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ticket_bus));
            // adding marker



            googleMap.addMarker(marker);
        }catch(Exception e){}

    }

    public void showMarkerDrop(Double Latitude, Double Longitude) {

        double latitude = Latitude;
        double longitude = Longitude;

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title(
                Latitude + "\n" + Longitude);
        try{

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ticket_map_drop));
            // adding marker
            googleMap.addMarker(marker);

        }catch(Exception e){}
    }

    private void buildAlertMessageNoGps()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enable location service to see your location on map.")
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });


        final AlertDialog alert = builder.create();
        alert.show();
    }

    private float getDistance(double lat1, double lon1, double lat2, double lon2)
    {
        android.location.Location homeLocation = new android.location.Location("");
        homeLocation .setLatitude(lat1);
        homeLocation .setLongitude(lon1);

        android.location.Location targetLocation = new android.location.Location("");
        targetLocation .setLatitude(lat2);
        targetLocation .setLongitude(lon2);

        float distanceInMeters =  targetLocation.distanceTo(homeLocation);
        return distanceInMeters/(1000) ;

    }

    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong,String waypoint, String mode)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.WAY_POINTS,waypoint);
        map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

        GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(TicketScreen.this);
		asyncTask.execute(map);
    }

    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
        PolylineOptions rectLine = new PolylineOptions().width(7).color(getResources().getColor(R.color.FavBG));

        for(int i = 0 ; i < directionPoints.size() ; i++)
        {
            rectLine.add(directionPoints.get(i));
        }
        if (newPolyline != null)
        {
            newPolyline.remove();
        }
        newPolyline = googleMap.addPolyline(rectLine);

        latlngBounds = createLatLngBoundsObject(Source, Destinatiom);

    }

    private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation)
    {
        if (firstLocation != null && secondLocation != null)
        {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(firstLocation).include(secondLocation);

            return builder.build();
        }
        return null;
    }


    @Override
    public void onBackPressed() {

    }

  /*  private void NoResponseServer()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Constant.ERR_NO_SERVER_RESPONSE)
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        new GetTicketData().execute(userRunID);
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }*/

    /* Way points code */

   /* private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for(int i=2;i<markerPoints.size();i++){
            LatLng point  = (LatLng) markerPoints.get(i);
            if(i==2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }*/

    private void setRoute(String result)
    {
        String status="";
        JSONParser jsonP=new JSONParser();
        try {

            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            for(int i=0;i<jsonArrayObject.size();i++){


                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                if(i==0)
                {
                    sourceLat=Double.valueOf(jsonObject.get("lat").toString());
                    sourceLong=Double.valueOf(jsonObject.get("long").toString());
                }
                else if(i==jsonArrayObject.size()-1){
                    destinationLat=Double.valueOf(jsonObject.get("lat").toString());
                    destinationLong=Double.valueOf(jsonObject.get("long").toString());
                }
                else {
                    wayPoints +=jsonObject.get("lat").toString() + "," + jsonObject.get("long").toString() + "|";
                }
            }

            Log.d("Way points",wayPoints);

        }catch (Exception e){

        }
    }

    public void findETA(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong,String wayPoint, String mode)
    {
        Log.d("Under ETA","");
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.WAY_POINTS, wayPoint);
        map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

        GetETA asyncTask = new GetETA(TicketScreen.this);
        try {
            asyncTask.execute(map);


        }catch (Exception e){

        }

    }

    public void setETA(){
       tvETA.setText(Constant.ETA);

    }
    private void showDialogResponse(Context context){
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

                                           new GetTicketData().execute(userRunID);
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

                                           new GetTicketData().execute(userRunID);
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }
}
