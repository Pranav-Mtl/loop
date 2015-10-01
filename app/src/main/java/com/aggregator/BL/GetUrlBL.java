package com.aggregator.BL;

import android.util.Log;

import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 9/30/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class GetUrlBL {

    public String getURL(String amount){

        String result=callWsUrl(amount);  // call webservice
        String status=validate(result);           // parse json
        return status;
    }


    private String callWsUrl(String amount){

        String URL="base_price="+amount;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_GET_URL);

        return txtJson;
    }

    private String validate(String result){

        boolean status;
        String url="";
        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=(boolean)jsonObject.get("success");

            Log.d("STATUS URL-->",status+"");

            if(status)
            {

                /*String link=jsonObject.get("link").toString();
                Log.d("link URL-->",link);*/

                jsonObject=(JSONObject)jsonP.parse(jsonObject.get("link").toString());
                url=jsonObject.get("url").toString();
                Log.d("WEB URL-->",url);

            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return url;
    }
}
