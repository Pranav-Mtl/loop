package com.aggregator.BL;


import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 9/9/2015.
 */

public class SendEmail {

    public String getData(String userEmail)
    {

            String result = fetRecord(userEmail);
            String status  = validate(result);

        return status;
    }

    private String fetRecord(String userEmail)
    {
        String url="mobile="+userEmail;
        String txtJson= RestFullWS.callWS(url, Constant.WEBSERVICE_RESET_PASSWORD);
        return txtJson;
    }

    private String validate(String strValue)
    {
        String status="";

        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject1=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=jsonObject1.get("result").toString();

        } catch (Exception e) {

            e.printStackTrace();
        }
        return status;
    }






}