package com.aggregator.BL;

/**
 * Created by Pranav Mittal on 9/16/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */

import android.content.Context;

import com.aggregator.BE.InviteActivityBE;
import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 9/16/2015.
 */
public class InviteActivityBL {

    String finalValue;
    String userId;
    Context context;
    String kicks;
    public String status="";
    public  String status1="";
    String promocode;
    String promovalue;
    InviteActivityBE objInviteActivityBE;

    public String getPromocode(String userId,InviteActivityBE inviteActivityBE)
    {
        this.userId=userId;
        objInviteActivityBE=inviteActivityBE;


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



        String txtJson= RestFullWS.callWS(url, Constant.WEBSERVICE_SHARE_EARN);

        return txtJson;
    }

    public String validate(String strValue)
    {


        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());//
            status=jsonObject.get("result").toString();

            if(status.equals(Constant.WS_RESULT_SUCCESS)){
                objInviteActivityBE.setReferralCode(jsonObject.get("referal_code").toString());
                objInviteActivityBE.setReferralValue(jsonObject.get("referral_value").toString());
            }


        } catch (Exception e) {

            e.printStackTrace();
        }
        return status;
    }


}