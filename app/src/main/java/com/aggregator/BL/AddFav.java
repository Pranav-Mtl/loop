package com.aggregator.BL;

import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 9/14/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class AddFav {

    String jsonFav,jsonRecent;

    public String addfav(String userID,String routeID,String startPoint,String endPoint){
        String result=callWS(userID, routeID, startPoint, endPoint);
        String status=validate(result);
        return status;
    }

    private String callWS(String userID,String routeID,String startPoint,String endPoint)
    {

        String URL="userid="+userID+"&routeid="+routeID+"&startpoint="+startPoint+"&endpoint="+endPoint;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_FAV);
        return txtJson;

    }

    private String validate(String result){
        String text="";
        JSONParser jsonP = new JSONParser();
        try {

            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            text=jsonObject.get("result").toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return text;
    }

    /* CALL ADD/REMOVE WALI FUNCTIONALITY FROM RPUTE SCREEN*/

    public String addfavGetData(String userID,String routeID,String startPoint,String endPoint){
        String result=callWSGetData(userID, routeID, startPoint, endPoint);
        String status=validateGetData(result);
        return status;
    }

    private String callWSGetData(String userID,String routeID,String startPoint,String endPoint)
    {

        String URL="user_id="+userID+"&routeid="+routeID+"&startpoint="+startPoint+"&endpoint="+endPoint;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_ADDFAV);
        return txtJson;

    }

    private String validateGetData(String result){
        String text="";
        JSONParser jsonP = new JSONParser();
        try {

            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            text=jsonObject.get("result").toString();
            jsonFav=jsonObject.get("fav_route").toString();
            jsonRecent = jsonObject.get("recent_route").toString();
            if(jsonFav.equals("[]")) {

                Constant.Tab2Name="Recent";
                Constant.favJson=false;
            }
            else
            {
                Constant.favJson=true;
                parseFavourites(jsonFav);
                Constant.Tab2Name="Favourites";
            }

            if(jsonRecent.equals("[]")){
                Constant.recentJson=false;
            }
            else
            {
                Constant.recentJson=true;
                parseRecent(jsonRecent);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return text;
    }

    private void parseRecent(String result){
        System.out.println("Recent Routes JSON ----->"+result);

        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            Constant.recentRouteID=new String[jsonArrayObject.size()];
            Constant.recentRouteStartID=new String[jsonArrayObject.size()];
            Constant.recentRouteEndID=new String[jsonArrayObject.size()];
            Constant.recentRouteStartName=new String[jsonArrayObject.size()];
            Constant.recentRouteEndName=new String[jsonArrayObject.size()];
            Constant.recentRouteFavStatus=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                Constant.recentRouteID[i]=jsonObject.get("route_id").toString();
                Constant.recentRouteStartID[i]=jsonObject.get("start_point").toString();
                Constant.recentRouteEndID[i]=jsonObject.get("end_point").toString();
                Constant.recentRouteStartName[i]=jsonObject.get("start_name").toString();
                Constant.recentRouteEndName[i]=jsonObject.get("end_name").toString();
                Constant.recentRouteFavStatus[i]="No";
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }
    private void parseFavourites(String result){
        System.out.println("Fav Routes JSON ----->"+result);
        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            Constant.favRouteID=new String[jsonArrayObject.size()];
            Constant.favRouteStartID=new String[jsonArrayObject.size()];
            Constant.favRouteEndID=new String[jsonArrayObject.size()];
            Constant.favRouteStartName=new String[jsonArrayObject.size()];
            Constant.favRouteEndName=new String[jsonArrayObject.size()];
            Constant.favRouteFavStatus=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                Constant.favRouteID[i]=jsonObject.get("route_id").toString();
                Constant.favRouteStartID[i]=jsonObject.get("start_point").toString();
                Constant.favRouteEndID[i]=jsonObject.get("end_point").toString();
                Constant.favRouteStartName[i]=jsonObject.get("start_name").toString();
                Constant.favRouteEndName[i]=jsonObject.get("end_name").toString();
                Constant.favRouteFavStatus[i]="Yes";
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }
}
