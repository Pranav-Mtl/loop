package com.aggregator.BL;

import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 9/16/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */

public class PromoCodeBL {

    public String sendCode(String userID,String code){
        String result=callWsOTP(userID,code);
        String status=validateOTP(result);
        return status;
    }

    private String callWsOTP(String userID,String code){

        String URL="user_id="+userID+"&promo_code="+code;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_PROMO_CODE);
        return txtJson;
    }

    private String validateOTP(String result){

        String status="";
        JSONParser jsonP=new JSONParser();
        try {

            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            status = jsonObject.get("result").toString();

            if(status.equals(Constant.WS_RESULT_SUCCESS)){
                Constant.amount=jsonObject.get("user_credits").toString();
                Constant.LoopCredit=Constant.LoopCreditText+Constant.amount;
                Constant.LoopCreditUsed=jsonObject.get("promo_value").toString();
            }
        }catch (Exception e){

        }

        return status;
    }
}
