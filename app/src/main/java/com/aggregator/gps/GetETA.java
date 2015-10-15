package com.aggregator.gps;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.aggregator.Constant.Constant;
import com.aggregator.loop.TicketScreen;
import com.aggregator.loop.TicketTrackBus;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Pranav Mittal on 9/29/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */

public class GetETA extends AsyncTask<Map<String, String>, Object, String> {
    public static final String USER_CURRENT_LAT = "user_current_lat";
    public static final String USER_CURRENT_LONG = "user_current_long";
    public static final String DESTINATION_LAT = "destination_lat";
    public static final String DESTINATION_LONG = "destination_long";
    public static final String DIRECTIONS_MODE = "directions_mode";
    public static final String WAY_POINTS = "way_points";
    private TicketScreen activity;

    private Exception exception;
    private ProgressDialog progressDialog;

    public GetETA(TicketScreen activity) {
        super();
        this.activity = activity;
    }

    public void onPreExecute() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Calculating directions");
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    @Override
    public void onPostExecute(String result) {

        progressDialog.dismiss();
        if (exception == null) {
            activity.setETA();
        } else {
            processException();
        }
    }

    @Override
    protected String doInBackground(Map<String, String>... params) {
        Map<String, String> paramMap = params[0];
        try {
            LatLng fromPosition = new LatLng(Double.valueOf(paramMap.get(USER_CURRENT_LAT)), Double.valueOf(paramMap.get(USER_CURRENT_LONG)));
            LatLng toPosition = new LatLng(Double.valueOf(paramMap.get(DESTINATION_LAT)), Double.valueOf(paramMap.get(DESTINATION_LONG)));
            GMapV2Direction md = new GMapV2Direction();

            Document doc = md.getDocument(fromPosition, toPosition, paramMap.get(WAY_POINTS), paramMap.get(DIRECTIONS_MODE));
            //ArrayList<LatLng> directionPoints = md.getDirection(doc);
           // md.getDurationValue(doc);
            Constant.ETA=md.getDurationText(doc);
            Constant.Duration=md.getDistanceText(doc);
            //md.getDistanceValue(doc);
            return "";
        } catch (Exception e) {
            exception = e;
            return null;
        }
    }

    private void processException() {
        Toast.makeText(activity, "Error retriving data", 3000).show();
    }
}