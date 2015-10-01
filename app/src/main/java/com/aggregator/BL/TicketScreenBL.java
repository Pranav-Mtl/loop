package com.aggregator.BL;

import android.content.Context;

import com.aggregator.BE.TicketScreenBE;
import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 9/11/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class TicketScreenBL {

    Context mContext;
    TicketScreenBE objTicketScreenBE;

    public String getTicketDetail(String userRunId,Context context,TicketScreenBE ticketScreenBE){
        mContext=context;
        objTicketScreenBE=ticketScreenBE;
        String result=callWsUrl(userRunId);  // call webservice
        String status=validate(result);           // parse json
        return status;
    }


    private String callWsUrl(String userRunId){

        String URL="user_run_id="+userRunId;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_TICKET);

        return txtJson;
    }

    private String validate(String result){

        String status="";
        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());

            objTicketScreenBE.setPickPointName(jsonObject.get("start_point").toString());
            objTicketScreenBE.setPickPointLat(Double.valueOf(jsonObject.get("start_point_lat").toString()));
            objTicketScreenBE.setPickPointLong(Double.valueOf(jsonObject.get("start_point_long").toString()));
            objTicketScreenBE.setPickPointImage(jsonObject.get("point_image").toString());

            objTicketScreenBE.setDropPointName(jsonObject.get("end_point").toString());
            objTicketScreenBE.setDropPointLat(Double.valueOf(jsonObject.get("end_point_lat").toString()));
            objTicketScreenBE.setDropPointLong(Double.valueOf(jsonObject.get("end_point_long").toString()));

            objTicketScreenBE.setVehicleType(jsonObject.get("vehicle_make").toString());
            objTicketScreenBE.setVehicleRegistration(jsonObject.get("vehicle_registration").toString());

            objTicketScreenBE.setDepartureTime(jsonObject.get("trip_date").toString());
            objTicketScreenBE.setRunID(jsonObject.get("run_id").toString());
            objTicketScreenBE.setEndPointTime(jsonObject.get("end_depature_time").toString());
            objTicketScreenBE.setWayPoints(jsonObject.get("route_detail").toString());




        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return status;
    }
}
