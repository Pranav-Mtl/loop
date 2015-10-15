package com.aggregator.BL;

import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Created by Pranav Mittal on 10/5/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class SuggestRouteBL {

    public String sendMessage(String pick,String drop,String userID)
    {

            String result = fetRecord(pick, drop,userID);
           String finalValue  = validate(result);



        return finalValue;
    }

    private String fetRecord(String pick,String drop,String userID)
    {

        String url="user_id="+userID+"&start_point="+pick+"&end_point="+drop;


        String txtJson= RestFullWS.callWS(url, Constant.WEBSERVICE_SUGGESTION);

        return txtJson;
    }



    private String validate(String strValue)
    {
        String result="";
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject1=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());//
            result=jsonObject1.get("result").toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
