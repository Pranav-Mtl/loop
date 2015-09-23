package com.aggregator.loop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.appsee.Appsee;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicketScreen extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    GoogleMap googleMap;
    Button btnMap,btnImage;
    LinearLayout llMap;
    ImageView imgTicket;
    LinearLayout btnShare,btnRateTrip;

    LinearLayout btnCross;

    TextView tvPick,tvDrop,tvTime,tvVehicle;

    String userRunID;

    private Toolbar toolbar;

    TicketScreenBL objTicketScreenBL;
    TicketScreenBE objTicketScreenBE;

    ProgressDialog mProgressDialog;

    static Double currentlongtitude;
    static Double currentlatitude;
    Location objLocation;
    Float distance;
    private LatLngBounds latlngBounds;
    private Polyline newPolyline;

    String PickText;
    String CurrentText;

    // Animation
    Animation animBlink;

    RelativeLayout llHeader;

    private static LatLng Source,Destinatiom;

    ImageButton btnTicket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_screen);

        Appsee.start("de8395d3ae424245b695b4c9d6642f71");

        btnMap= (Button) findViewById(R.id.ticket_btn_map);
        btnImage= (Button) findViewById(R.id.ticket_btn_image);
        btnShare= (LinearLayout) findViewById(R.id.ticket_btn_share);
        btnRateTrip= (LinearLayout) findViewById(R.id.ticket_rate_trip);
        llMap= (LinearLayout) findViewById(R.id.ticket_map_ll);
        imgTicket= (ImageView) findViewById(R.id.ticket_image);
        btnCross= (LinearLayout) findViewById(R.id.ticket_cross);
        btnTicket= (ImageButton) findViewById(R.id.ticket_img);

        tvPick= (TextView) findViewById(R.id.ticket_pick);
        tvDrop= (TextView) findViewById(R.id.ticket_drop);
        tvVehicle= (TextView) findViewById(R.id.ticket_vehicle);
        tvTime= (TextView) findViewById(R.id.ticket_time);

        llHeader= (RelativeLayout) findViewById(R.id.ticket_header_ll);

        btnMap.setBackgroundResource(R.drawable.ic_map_btn_pressed);
        btnMap.setTextColor(getResources().getColor(R.color.WhiteColor));
        btnImage.setBackgroundResource(R.drawable.ic_map_btn);
        btnImage.setTextColor(getResources().getColor(R.color.OranceColor));

        mProgressDialog=new ProgressDialog(TicketScreen.this);

        objTicketScreenBL=new TicketScreenBL();
        objTicketScreenBE=new TicketScreenBE();

        btnMap.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnRateTrip.setOnClickListener(this);

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
               new GetTicketData().execute(userRunID);
           }
            else{
               AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(TicketScreen.this);

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
        catch (NullPointerException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }



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
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

        //LatLng latlng=new LatLng(Double.valueOf(CreateGameRecord.latitude[0]),Double.valueOf(CreateGameRecord.longitude[0]));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ticket_btn_map:
                Log.i("Clicked","MAP");
                imgTicket.setVisibility(View.GONE);
                llMap.setVisibility(View.VISIBLE);
                btnMap.setBackgroundResource(R.drawable.ic_map_btn_pressed);
                btnMap.setTextColor(getResources().getColor(R.color.WhiteColor));
                btnImage.setBackgroundResource(R.drawable.ic_map_btn);
                btnImage.setTextColor(getResources().getColor(R.color.OranceColor));

                break;
            case R.id.ticket_btn_image:
                Log.i("Clicked","Image");
                llMap.setVisibility(View.GONE);
                imgTicket.setVisibility(View.VISIBLE);
                btnMap.setBackgroundResource(R.drawable.ic_map_btn);
                btnMap.setTextColor(getResources().getColor(R.color.OranceColor));
                btnImage.setBackgroundResource(R.drawable.ic_map_btn_pressed);
                btnImage.setTextColor(getResources().getColor(R.color.WhiteColor));
                break;
            case R.id.ticket_btn_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Share your loop experience with your friends.";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Your Experience");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.ticket_rate_trip:
                startActivity(new Intent(TicketScreen.this,TripFeedback.class).putExtra("RunID",userRunID));
                break;
            case R.id.ticket_cross:
                startActivity(new Intent(TicketScreen.this,TripHistory.class));
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


                tvPick.setText(spanString);
                tvDrop.setText(spanString2);
                tvVehicle.setText(spanString3);
                tvTime.setText(spanString4);

                PickText = objTicketScreenBE.getPickPointName();

                initializeMap(objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong());

                btnTicket.startAnimation(animBlink);
            }catch (NullPointerException e){
                NoResponseServer();
            }catch (Exception e){
                NoResponseServer();
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
            buildAlertMessageNoGps();
        }
        else {

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                try {

                    getCurrentLocation();
                    Log.d("Current Lat", currentlatitude + "");
                    Log.d("Current Lon", currentlongtitude + "");
                    showMarker(currentlatitude, currentlongtitude);
                    distance = getDistance(currentlatitude, currentlatitude, objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong());
                    PickText = PickText + " \n Distance :" + distance + " km";
                    Log.d("Distance-->", distance + "");
                    Source = new LatLng(currentlatitude, currentlongtitude);
                    Destinatiom = new LatLng(objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong());
                    findDirections(currentlatitude, currentlongtitude, objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong(), GMapV2Direction.MODE_WALKING);


                    showMarkerPick(objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong());

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
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_man));
            // adding marker

            marker.title("Here You Are !!!");

            googleMap.addMarker(marker);




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

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pick));
            // adding marker

            marker.title(PickText);

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

    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

	   GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(TicketScreen.this);
		asyncTask.execute(map);
    }

    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED);

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

    private void NoResponseServer()
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
    }
}
