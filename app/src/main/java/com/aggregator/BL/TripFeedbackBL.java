package com.aggregator.BL;


import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.apache.http.HttpEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by appslure on 9/10/2015.
 */
public class TripFeedbackBL {


    String userId,phone,pass,email;
    String finalValue;
    public String status;

    public String sendId(String userId)
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


        String url="user_run_id="+userId;

        String text= RestFullWS.callWS(url, Constant.WEBSERVICE_FEEDBACK);
        return text;
    }



    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n>0) {
            byte[] b = new byte[4096];
            n =  in.read(b);
            if (n>0) out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public String validate(String strValue)
    {
        System.out.println("ththththththpppppppppp------>"+strValue);

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject1=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());//
            //Constant.usrname=jsonObject1.get("name").toString();
            //Constant.phoneNumber=jsonObject1.get("mobile").toString();
            Constant.date=jsonObject1.get("trip_date").toString();
            Constant.pickPoint=jsonObject1.get("start_point").toString();
            Constant.dropPoint=jsonObject1.get("end_point").toString();
            Constant.rate=jsonObject1.get("price").toString();
            System.out.println("getting record---->"+Constant.usrname+"  "+Constant.dropPoint);
        } catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
        return status;
    }






}