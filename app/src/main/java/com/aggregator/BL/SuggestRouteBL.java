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

    public String sendMessage(String userId,String msg)
    {

            String result = fetRecord(userId, msg);
           String finalValue  = validate(result);



        return finalValue;
    }

    private String fetRecord(String userId,String msg)
    {

        String url="user_id="+userId+"&route="+msg;


        String txtJson= RestFullWS.callWS(url, Constant.WEBSERVICE_HELP);

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
