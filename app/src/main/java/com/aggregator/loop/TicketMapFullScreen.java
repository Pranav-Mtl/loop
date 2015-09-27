package com.aggregator.loop;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();

        objTicketScreenBE= (TicketScreenBE) getIntent().getSerializableExtra("TicketScreenBE");



/*

        start = new LatLng(28.7077,77.1259);
         end= new LatLng(28.7150,77.1154);
         waypoint = new LatLng(28.7010993, 77.1168303);
         waypoint1 = new LatLng(28.720719,77.107133);
         waypoint2 = new LatLng(28.7288259,77.1068059);
         gurgaoh = new LatLng(28.4700,77.0300);

        initializeMap();

        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.WALKING)
                .withListener(MapTestActivity.this)
                .waypoints(start, waypoint,waypoint1,waypoint2, end)
                .build();
        routing.execute();*/

        //createDashedLine(googleMap,start,gurgaoh,R.color.DarkGreen);

        PickText = objTicketScreenBE.getPickPointName();

        initializeMap(objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong());

        findDirections(objTicketScreenBE.getStartPointLat(), objTicketScreenBE.getStartPointLong(), objTicketScreenBE.getEndPointLat(), objTicketScreenBE.getEndPointLong(), GMapV2Direction.MODE_WALKING);

        showMarkerPick(objTicketScreenBE.getPickPointLat(), objTicketScreenBE.getPickPointLong());

        showMarkerDrop(objTicketScreenBE.getDropPointLat(), objTicketScreenBE.getDropPointLong());

    }
    private void initializeMap(Double lat,Double lon) {
        // TODO Auto-generated method stub


        LatLng latLng = new LatLng(lat, lon);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
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


    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
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

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_booking_pic_drop));
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

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_booking_pic_drop));
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
}
