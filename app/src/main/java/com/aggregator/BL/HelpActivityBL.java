package com.aggregator.BL;

import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 9/9/2015.
 */
public class HelpActivityBL {
    String msg,userId,pass,email;
    String finalValue;
     String status;
     String result;

    public String sendMessage(String userId,String msg)
    {
        this.msg=msg;
        this.userId=userId;

        try {
            String result = fetRecord(userId, msg);
            finalValue  = validate(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalValue;
    }

    private String fetRecord(String userId,String msg)
    {

        String url="user_id="+userId+"&comment="+msg;


        String txtJson= RestFullWS.callWS(url, Constant.WEBSERVICE_HELP);

        return txtJson;
    }



    public String validate(String strValue)
    {
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject1=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());//
            result=jsonObject1.get("result").toString();



        } catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
        return result;
    }






}