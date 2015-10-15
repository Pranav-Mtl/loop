package com.aggregator.BL;

import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 9/10/2015.
 */



public class SendCommentBL {


    String msg,userId,pass,email;
    String finalValue;
    public String status;
    public String result;
    String rating;

    public String sendComment(String userId,String rating,String msg,String comment,String runID)
    {
        try {
            String result = fetRecord(userId,rating,msg,comment,runID);
            finalValue  = validate(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalValue;
    }

    private String fetRecord(String userId,String rating,String msg,String comment,String runID)
    {

        String url="user_id="+userId+"&rating="+rating+"&comment_text="+comment+"&user_run_id="+runID+"&issue="+msg;

        String text= RestFullWS.callWS(url, Constant.WEBSERVICE_COMMENT);
        return text;
    }
    private String validate(String strValue)
    {


        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject1=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            result=jsonObject1.get("result").toString();




        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }






}