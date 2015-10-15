package com.aggregator.BL;

import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 10/15/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class ApplyReferralBL {

    public String applyReferralCode(String code){

        String result=callWS(code);
        String status=validate(result);
        return status;
    }

    private String callWS(String code){
        String URL="code="+code;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_CHECK_REFERRAL);

        return txtJson;
    }

    private String validate(String result){
        String status="";
        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=jsonObject.get("result").toString();

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return status;
    }
}
