package com.aggregator.BL;

import android.content.Context;

import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 8/20/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */

public class SignInBL {
    Context mContext;

    public String validateSignInDetails(String Email,String password,String type,Context context,String gcmID){
        mContext=context;
        String result=callWsUrl(Email,password,gcmID);  // call webservice
        String status=validate(result);           // parse json
        return status;
    }

    private String callWsUrl(String Email,String password,String gcmID){

        String URL="mobile="+Email+"&password="+password+"&gcm_id="+gcmID;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_LOGIN);
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
}
