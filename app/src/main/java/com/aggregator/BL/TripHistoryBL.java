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
    public String getAllTrip(String userID,String page)
    {
        String result=callWS(userID,page);
        String status=validate(result);
        return status;
    }

    private String callWS(String userID,String page)
    {
        String URL="user_id="+userID+"&pg_no="+page;
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
                   Constant.tripPrice = new Double[jsonArrayObject.size()];
                   Constant.tripLoopCredit = new Double[jsonArrayObject.size()];
                   Constant.tripStatus = new String[jsonArrayObject.size()];
                   Constant.tripRunId = new String[jsonArrayObject.size()];
                   Constant.tripTotalAmount = new Double[jsonArrayObject.size()];
                   Constant.tripFeedbackStatus = new String[jsonArrayObject.size()];

                   Constant.tripSize=0;


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
                       Constant.tripPrice[i] = Double.valueOf(jsonObject.get("wallet_used_amount").toString());
                       Constant.tripLoopCredit[i] = Double.valueOf(jsonObject.get("credits_used_amount").toString());
                       Constant.tripStatus[i] = jsonObject.get("status").toString();
                       Constant.tripRunId[i] = jsonObject.get("user_run_id").toString();
                       Constant.tripTotalAmount[i]=Double.valueOf(jsonObject.get("credits_used_amount").toString())+Double.valueOf(jsonObject.get("wallet_used_amount").toString());
                       Constant.tripFeedbackStatus[i]=jsonObject.get("user_route_feedback").toString();
                       Constant.tripSize++;
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

    /*---------------------------------------------------------------------------------------------------------------*/

                /*  load more data on scroll  */


    public String getMoreAllTrip(String userID,String page)
    {
        String result=callMoreWS(userID,page);
        String status=validateMore(result);
        return status;
    }

    private String callMoreWS(String userID,String page)
    {
        String URL="user_id="+userID+"&pg_no="+page;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_TRIP);
        return txtJson;
    }

    private String validateMore(String result)
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
                    Constant.tripRouteID =createNewArray(Constant.tripRouteID,jsonArrayObject.size());
                    Constant.tripStartID =createNewArray(Constant.tripStartID,jsonArrayObject.size());
                    Constant.tripStartName =createNewArray(Constant.tripStartName,jsonArrayObject.size());
                    Constant.tripEndID =createNewArray(Constant.tripEndID,jsonArrayObject.size());
                    Constant.tripEndName =createNewArray(Constant.tripEndName,jsonArrayObject.size());

                    Constant.tripDate =createNewArray(Constant.tripDate,jsonArrayObject.size());
                    Constant.tripFav = createNewArray(Constant.tripFav,jsonArrayObject.size());
                    Constant.tripTime =createNewArray(Constant.tripTime,jsonArrayObject.size());

                    Constant.tripStatus =createNewArray(Constant.tripStatus,jsonArrayObject.size());
                    Constant.tripRunId = createNewArray(Constant.tripRunId,jsonArrayObject.size());

                    Constant.tripTotalAmount =createNewArrayDouble(Constant.tripTotalAmount, jsonArrayObject.size());
                    Constant.tripPrice = createNewArrayDouble(Constant.tripPrice,jsonArrayObject.size());
                    Constant.tripLoopCredit =createNewArrayDouble(Constant.tripLoopCredit,jsonArrayObject.size());

                    Constant.tripFeedbackStatus = createNewArray(Constant.tripFeedbackStatus, jsonArrayObject.size());


                    for (int i = 0; i < jsonArrayObject.size(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                        Constant.tripRouteID[Constant.tripSize] = jsonObject.get("route_id").toString();
                        Constant.tripStartID[Constant.tripSize] = jsonObject.get("start_point").toString();
                        Constant.tripStartName[Constant.tripSize] = jsonObject.get("start_point_name").toString();
                        Constant.tripEndID[Constant.tripSize] = jsonObject.get("end_point").toString();
                        Constant.tripEndName[Constant.tripSize] = jsonObject.get("end_point_name").toString();
                        Constant.tripDate[Constant.tripSize] = jsonObject.get("booking_date").toString();
                        Constant.tripFav[Constant.tripSize] = jsonObject.get("route_status").toString();
                        Constant.tripTime[Constant.tripSize] = jsonObject.get("departure_time").toString();
                        Constant.tripPrice[Constant.tripSize] = Double.valueOf(jsonObject.get("wallet_used_amount").toString());
                        Constant.tripLoopCredit[Constant.tripSize] = Double.valueOf(jsonObject.get("credits_used_amount").toString());
                        Constant.tripStatus[Constant.tripSize] = jsonObject.get("status").toString();
                        Constant.tripRunId[Constant.tripSize] = jsonObject.get("user_run_id").toString();
                        Constant.tripTotalAmount[Constant.tripSize]=Double.valueOf(jsonObject.get("credits_used_amount").toString())+Double.valueOf(jsonObject.get("wallet_used_amount").toString());
                        Constant.tripFeedbackStatus[Constant.tripSize]=jsonObject.get("user_route_feedback").toString();
                        Constant.tripSize++;

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

    public String[] createNewArray(String[] oldArray,int size){
        String[] newArray = new String[oldArray.length + size];
        for(int i = 0; i < oldArray.length; i++) {
            newArray[i] = oldArray[i];
        }

        return newArray;
    }

    public Double[] createNewArrayDouble(Double[] oldArray,int size){
        Double[] newArray = new Double[oldArray.length + size];
        for(int i = 0; i < oldArray.length; i++) {
            newArray[i] = oldArray[i];
        }

        return newArray;
    }
}
