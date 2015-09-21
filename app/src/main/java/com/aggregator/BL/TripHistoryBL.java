package com.aggregator.BL;

import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 9/14/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class TripHistoryBL {

    /*  Fetch All Trip History  */
    public String getAllTrip(String userID)
    {
        String result=callWS(userID);
        String status=validate(result);
        return status;
    }

    private String callWS(String userID)
    {
        String URL="user_id="+userID;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_TRIP);
        return txtJson;
    }

    private String validate(String result)
    {
        String status = "";
       try {

           if (result.equals("[]")) {
               status = Constant.WS_RESULT_FAILURE;
           } else {
               status = Constant.WS_RESULT_SUCCESS;
               JSONParser jsonP = new JSONParser();

               try {
                   Object obj = jsonP.parse(result);
                   JSONArray jsonArrayObject = (JSONArray) obj;
                   Constant.tripRouteID = new String[jsonArrayObject.size()];
                   Constant.tripStartID = new String[jsonArrayObject.size()];
                   Constant.tripStartName = new String[jsonArrayObject.size()];
                   Constant.tripEndID = new String[jsonArrayObject.size()];
                   Constant.tripEndName = new String[jsonArrayObject.size()];

                   Constant.tripDate = new String[jsonArrayObject.size()];
                   Constant.tripFav = new String[jsonArrayObject.size()];

                   Constant.tripTime = new String[jsonArrayObject.size()];
                   Constant.tripPrice = new String[jsonArrayObject.size()];
                   Constant.tripStatus = new String[jsonArrayObject.size()];
                   Constant.tripRunId = new String[jsonArrayObject.size()];


                   for (int i = 0; i < jsonArrayObject.size(); i++) {
                       JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                       Constant.tripRouteID[i] = jsonObject.get("route_id").toString();
                       Constant.tripStartID[i] = jsonObject.get("start_point").toString();
                       Constant.tripStartName[i] = jsonObject.get("start_point_name").toString();
                       Constant.tripEndID[i] = jsonObject.get("end_point").toString();
                       Constant.tripEndName[i] = jsonObject.get("end_point_name").toString();
                       Constant.tripDate[i] = jsonObject.get("booking_date").toString();
                       Constant.tripFav[i] = jsonObject.get("route_status").toString();
                       Constant.tripTime[i] = jsonObject.get("departure_time").toString();
                       Constant.tripPrice[i] = jsonObject.get("price").toString();
                       Constant.tripStatus[i] = jsonObject.get("status").toString();
                       Constant.tripRunId[i] = jsonObject.get("user_run_id").toString();

                   }

               } catch (Exception e) {
                   e.getLocalizedMessage();
               }
           }
       }
       catch (NullPointerException e){
           e.printStackTrace();
       }

        return status;
    }

}
