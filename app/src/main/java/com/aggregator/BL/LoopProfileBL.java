package com.aggregator.BL;

import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 09/09/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class LoopProfileBL {

    String name,phone,pass,email;
    String finalValue;
    public String status;

    public String getProfileData(String name,String email,String mobile,String pass)
    {
        this.name=name;
        this.phone=mobile;
        this.pass=pass;
        this.email=email;


        try {
            String result = fetRecord(name,email,phone,pass);
            finalValue  = validate(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalValue;
    }

    private String fetRecord(String name,String email,String phone,String pass)
    {


        String url="name="+name+"&email="+email+"&mob_no="+phone+"&pass="+pass;

        String text= RestFullWS.callWS(url, Constant.WEBSERVICE_PROFILE);


        return text;
    }




    public String validate(String strValue)
    {

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());//
            status=jsonObject.get("result").toString();




        } catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
        return status;
    }



}
