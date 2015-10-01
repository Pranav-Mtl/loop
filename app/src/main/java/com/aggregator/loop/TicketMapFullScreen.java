package com.aggregator.loop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.aggregator.BE.TicketScreenBE;
import com.aggregator.gps.GMapV2Direction;
import com.aggregator.gps.GPSTracker;
import com.aggregator.gps.GetDirections;
import com.aggregator.gps.GetDirectionsAsyncTask;
import com.directions.route.Route;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicketMapFullScreen extends AppCompatActivity implements RoutingListener{

    GoogleMap googleMap;
    LatLng start,waypoint,waypoint1,end,waypoint2,gurgaoh;
    TicketScreenBE objTicketScreenBE;

    static Double currentlongtitude;
    static Double currentlatitude;
    Location objLocation;
    Float distance;
    private LatLngBounds latlngBounds;
    private Polyline newPolyline;
    String PickText;
    private static LatLng Source,Destinatiom;
    Button btnTrack;

    String wayPoints="";

    double sourceLat,sourceLong,destinationLat,destinationLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);

        btnTrack= (Button) findViewById(R.id.vehicle_tracking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();

        objTicketScreenBE= (TicketScreenBE) getIntent().getSerializableExtra("TicketScreenBE");

        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),TicketTrackBus.class).putExtra("TicketScreenBE",objTicketScreenBE));
            }
        });




        setRoute(objTicketScreenBE.getWayPoints());

        PickText = objTicketScreenBE.getPickPointName();

        initializeMap(objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong());

        findDirections(sourceLat, sourceLong, destinationLat, destinationLong,wayPoints, GMapV2Direction.MODE_WALKING);

        showMarkerPick(objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong());

        showMarkerDrop(objTicketScreenBE.getDropPointLat(), objTicketScreenBE.getDropPointLong());

    }
    private void initializeMap(Double lat,Double lon) {       // TODO Auto-generated method stub

        LatLng latLng = new LatLng(lat, lon);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        //LatLng latlng=new LatLng(Double.valueOf(CreateGameRecord.latitude[0]),Double.valueOf(CreateGameRecord.longitude[0]));

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRoutingFailure() {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(PolylineOptions polylineOptions, Route route) {

        PolylineOptions polyoptions = new PolylineOptions();
        polyoptions.color(Color.BLUE);
        polyoptions.width(5);
        polyoptions.addAll(polylineOptions.getPoints());
        googleMap.addPolyline(polyoptions);

        MarkerOptions options = new MarkerOptions();
        options.position(start);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pick));
        googleMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(end);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_man));
        googleMap.addMarker(options);
    }

    @Override
    public void onRoutingCancelled() {

    }


    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong,String wayPoint, String mode)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.WAY_POINTS, wayPoint);
        map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

        GetDirections asyncTask = new GetDirections(TicketMapFullScreen.this);
        asyncTask.execute(map);
    }

    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.BLUE);

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

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pick));
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

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.drop));
            // adding marker


            googleMap.addMarker(marker);
        }catch(Exception e){}
    }


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
                        showMarker(currentlatitude, currentlongtitude);
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
}
