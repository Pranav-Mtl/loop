package com.aggregator.BL;


import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 9/10/2015.
 */
public class TripFeedbackBL {


    String userId,phone,pass,email;
    String finalValue;
    public String status;

    public String sendId(String userId,String id)
    {
       this.userId=userId;
        try {
            String result = fetRecord(userId,id);
            finalValue  = validate(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalValue;
    }

    private String fetRecord(String userId,String id)
    {


        String url="user_run_id="+userId+"&user_id="+id;

        String text= RestFullWS.callWS(url, Constant.WEBSERVICE_FEEDBACK);
        return text;
    }


    public String validate(String strValue)
    {

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject1=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());//

            Constant.date=jsonObject1.get("trip_date").toString();
            Constant.pickPoint=jsonObject1.get("start_point").toString();
            Constant.dropPoint=jsonObject1.get("end_point").toString();
            Constant.rate=jsonObject1.get("price").toString();
            status=jsonObject1.get("feedback_check").toString();
            if(status.equalsIgnoreCase("y")){
                Constant.feedback_comment=jsonObject1.get("feedback_comment").toString();
                Constant.feedback_rating=(Float.valueOf(jsonObject1.get("feedback_rating").toString()));
            }
            System.out.println("getting record---->"+Constant.usrname+"  "+Constant.dropPoint);
        } catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
        return status;
    }






}