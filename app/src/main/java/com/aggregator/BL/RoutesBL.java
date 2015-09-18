package com.aggregator.BL;

import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 8/25/2015.
 */
public class RoutesBL {

    String jsonAllRoutes,jsonFav,jsonRecent,jsonPersonalInfo;

    /*  Fetch All Routes when user comes first time  */
    public String getAllRoutes()
    {
        String result=callWS();
        String status=validate(result);
        return status;
    }

    private String callWS()
    {
        String URL="";
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_ROUTES);
        return txtJson;
    }

    private String validate(String result)
    {
        String status="";
        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            Constant.routeId=new String[jsonArrayObject.size()];
            Constant.routeName=new String[jsonArrayObject.size()];
            Constant.routeExpand=new String[jsonArrayObject.size()];

            String text = "<font color=#f55d2b>"+" ● "+"</font>";

            for(int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.routeId[i]=jsonObject.get("route_id").toString();
                Constant.routeName[i]=jsonObject.get("route_name").toString();
               // String txt=Html.fromHtml(text).toString();
                Constant.routeExpand[i]=jsonObject.get("sub_route").toString().replace("[","").replace("]","").replaceAll("\"","").replaceAll(",",text);
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return "";
    }

     /*  Fetch All Routes, Favourite route, Recent route and Personal info after login/signup  */

    public String getAllRoutesLogin(String userId)
    {
        String result=callWSLogin(userId);
        String status=validateLogin(result);
        return status;
    }

    private String callWSLogin(String userId)
    {
        String URL="user_id="+userId;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_ROUTES_LOGIN);
        return txtJson;
    }

    private String validateLogin(String result)
    {
        String status="";
        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

            jsonAllRoutes=jsonObject.get("all_routes").toString();
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

            jsonPersonalInfo=jsonObject.get("user_data").toString();
            parseAllRoutes(jsonAllRoutes);

            parsePersonalInfo(jsonPersonalInfo);


        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        catch (Exception e) {
            e.getLocalizedMessage();
        }

        return "";
    }

    private void parseAllRoutes(String result){
        System.out.println("All Routes JSON ----->" + result);
        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            Constant.routeId=new String[jsonArrayObject.size()];
            Constant.routeName=new String[jsonArrayObject.size()];
            Constant.routeExpand=new String[jsonArrayObject.size()];

            String text = "<font color=#f55d2b>"+" ● "+"</font>";

            for(int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.routeId[i]=jsonObject.get("route_id").toString();
                Constant.routeName[i]=jsonObject.get("route_name").toString();
                Constant.routeExpand[i]=jsonObject.get("sub_route").toString().replace("[","").replace("]","").replaceAll("\"","").replaceAll(",",text);
            }

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.getLocalizedMessage();
        }

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

            for(int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.recentRouteID[i]=jsonObject.get("route_id").toString();
                Constant.recentRouteStartID[i]=jsonObject.get("start_point").toString();
                Constant.recentRouteEndID[i]=jsonObject.get("end_point").toString();
                Constant.recentRouteStartName[i]=jsonObject.get("start_name").toString();
                Constant.recentRouteEndName[i]=jsonObject.get("end_name").toString();
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

            for(int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.favRouteID[i]=jsonObject.get("route_id").toString();
                Constant.favRouteStartID[i]=jsonObject.get("start_point").toString();
                Constant.favRouteEndID[i]=jsonObject.get("end_point").toString();
                Constant.favRouteStartName[i]=jsonObject.get("start_name").toString();
                Constant.favRouteEndName[i]=jsonObject.get("end_name").toString();
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }
    private void parsePersonalInfo(String result){
        System.out.println("Personal Info JSON ----->"+result);
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            Constant.NAME=jsonObject.get("user_fullname").toString();

                    Constant.amount=jsonObject.get("user_credits").toString();
                    Constant.LoopCredit=Constant.LoopCreditText+Constant.amount;
                    String pautmString=jsonObject.get("user_paytm_id").toString();
                    Constant.PayTMWalet=Constant.PayTMWaletText+pautmString;


        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

}
