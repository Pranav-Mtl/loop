package com.aggregator.BL;


import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 9/8/2015.
 */
public class GettingProfileInformation {

    String userId,phone,pass,email;
    String finalValue;
    public String status;

    public String getData(String userId)
    {
        this.userId=userId;
        try {

            String result = fetRecord(userId);
            finalValue  = validate(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalValue;
    }

    private String fetRecord(String userId)
    {
        String url="user_id="+userId;
        String txtJson= RestFullWS.callWS(url, Constant.WEBSERVICE_EDIT_PROFILE);
        return txtJson;
    }


    private String validate(String strValue)
    {
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject1=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());//

            Constant.usrname=jsonObject1.get("name").toString();
            Constant.phoneNumber=jsonObject1.get("mobile").toString();
            Constant.emaiId=jsonObject1.get("email").toString();
            Constant.password=jsonObject1.get("password").toString();
        } catch (Exception e) {

        }
        return status;
    }






}