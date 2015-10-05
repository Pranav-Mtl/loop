package com.aggregator.BL;

import com.aggregator.BE.RefreshTicketBE;
import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 9/29/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class RefreshTicketBL {

    RefreshTicketBE objRefreshTicketBE;

    public String GetBusLoc(String runID,RefreshTicketBE refreshTicketBE){
        objRefreshTicketBE=refreshTicketBE;
        String result=callWsUrl(runID);
        String status=validate(result);
        return status;
    }

    private String callWsUrl(String runID){

        String URL="run_id="+runID;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_TRACK_BUS);
        return txtJson;
    }

    private String validate(String result){

        String status="";
        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            objRefreshTicketBE.setBusLat(Double.valueOf(jsonObject.get("latitude").toString()));
            objRefreshTicketBE.setBusLong(Double.valueOf(jsonObject.get("longitude").toString()));

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return status;
    }

}
