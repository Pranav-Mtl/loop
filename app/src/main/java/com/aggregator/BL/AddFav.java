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
public class AddFav {

    public String addfav(String userID,String routeID,String startPoint,String endPoint){
        String result=callWS(userID, routeID, startPoint, endPoint);
        String status=validate(result);
        return status;
    }

    private String callWS(String userID,String routeID,String startPoint,String endPoint)
    {

        String URL="userid="+userID+"&routeid="+routeID+"&startpoint="+startPoint+"&endpoint="+endPoint;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_FAV);
        return txtJson;

    }

    private String validate(String result){
        String text="";
        JSONParser jsonP = new JSONParser();
        try {

            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            text=jsonObject.get("result").toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return text;
    }
}
