package com.aggregator.BL;

import android.content.Context;

import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 9/1/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class OTPBL {

    Context mContext;

    public String updateStatus(String Email,Context context){
        mContext=context;
        String result=callWsUrl(Email);  // call webservice
        String status=validate(result);           // parse json
        return status;
    }


    private String callWsUrl(String Email){

        String URL="mobile="+Email;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_UPDATE_STATUS);

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

            if(status.equals(Constant.WS_RESULT_SUCCESS))
            {
                String id=jsonObject.get("user_id").toString();
                Util.setSharedPrefrenceValue(mContext, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_User_id, id);
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return status;
    }

    public String resendOTP(String mobile,String otp){
        String result=callWsOTP(mobile,otp);
        String status=validateOTP(result);
        return status;
    }

    private String callWsOTP(String mobile,String otp){

        String URL="phone="+mobile+"&otp="+otp;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_RESEND_OTP);

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
        }catch (Exception e){

        }

        return status;
    }
}
