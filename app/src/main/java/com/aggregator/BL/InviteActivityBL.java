package com.aggregator.BL;

/**
 * Created by Pranav Mittal on 9/16/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */

import android.content.Context;

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

    public String getPromocode(String userId,String promocode,String promovalue)
    {
        this.userId=userId;
        this.promocode=promocode;
        this.promovalue=promovalue;
        System.out.println("inside the webservices getting record--------->"+kicks);


        try {
            String result = fetRecord(userId,promocode,promovalue);
            finalValue  = validate(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalValue;
    }

    private String fetRecord(String userId,String promocode,String promovalue)
    {

        String url="user_id="+userId+"&referral_code="+promocode+"&referral_value="+promovalue;



        String txtJson= RestFullWS.callWS(url, Constant.WEBSERVICE_SHARE_EARN);

        return txtJson;
    }

    public String validate(String strValue)
    {
        System.out.println("ththththththpppppppppp------>"+strValue);

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());//
            status=jsonObject.get("result").toString();

           /* String imgurl=jsonObject.get("image_address").toString();
            String proFileName=jsonObject.get("Fullname").toString();
            Configuration.setSharedPrefrenceValue(context, Constant.PREFS_NAME, Constant.profilepic, imgurl);
            Configuration.setSharedPrefrenceValue(context, Constant.PREFS_NAME, Constant.profileName, proFileName);*/
            System.out.println("insde the kicks updationpppppppppppppp---->"+status);

        } catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
        return status;
    }


}