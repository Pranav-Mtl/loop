package com.aggregator.BL;

import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 8/19/2015.
 */
public class SignUpBL {

    public String insertSignUpDetails(String fName,String Email,String password,String mobile,String payTm,String promoCode,String OTP,String userCredit,String userBalance,String gcmID){

        String result=callWsUrl(fName,Email,password,mobile,payTm,promoCode,OTP,userCredit,userBalance,gcmID);   // call webservice
        String status=validate(result);             // parse json
        return status;
    }


    private String callWsUrl(String fName,String Email,String password,String mobile,String payTm,String promoCode,String OTP,String userCredit,String userBalance,String gcmID){

        String URL="firstname="+fName+"&email="+Email+"&password="+password+"&phone_no="+mobile+"&paytm="+payTm+"&promocode="+promoCode+"&otp="+OTP+"&user_credit="+userCredit+"&user_balance="+userBalance+"&gcm_id="+gcmID;
        String txtJson=RestFullWS.callWS(URL, Constant.WEBSERVICE_SIGNUP);
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
