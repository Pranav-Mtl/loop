package com.aggregator.BL;

import android.content.ContentValues;

import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;
import com.aggregator.db.DBOperation;
import com.aggregator.db.TableBE;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 8/25/2015.
 */

public class RoutesBL {

    String jsonAllRoutes,jsonFav,jsonRecent,jsonPersonalInfo;
    DBOperation dbOperation;

    TableBE tableBE=new TableBE();

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

    public String getAllRoutesLogin(String userId,DBOperation objDbOperation)
    {
        dbOperation=objDbOperation;
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

            dbOperation.open();
            dbOperation.delete(tableBE.getTable_All_Route());
            dbOperation.close();

            String text = "<font color=#f55d2b>"+" ● "+"</font>";

            for(int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.routeId[i]=jsonObject.get("route_id").toString();
                Constant.routeName[i]=jsonObject.get("route_name").toString();
                Constant.routeExpand[i]=jsonObject.get("sub_route").toString().replace("[","").replace("]","").replaceAll("\"", "").replaceAll(",", text);

                TableBE curChatObj = tableAllRoute(Constant.routeId[i],Constant.routeName[i],Constant.routeExpand[i]);
                addToDB(curChatObj);
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
            Constant.recentRouteFavStatus=new String[jsonArrayObject.size()];
            Constant.recentRouteStatus=new String[jsonArrayObject.size()];

            dbOperation.open();
            dbOperation.delete(tableBE.getTable_Recent_Route());
            dbOperation.close();

            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                /*  int endId=(int)jsonObject.get("end_point");
                int startID=(int)jsonObject.get("start_point");

                if(Constant.favJson)
                {
                    for(int j=0;j<Constant.favRouteID.length;j++){
                        if((Constant.favRouteStartID[j] && Constant.favRouteEndID[j])==(startID && endId)){

                        }
                    }

                }*/

                Constant.recentRouteID[i]=jsonObject.get("route_id").toString();
                Constant.recentRouteStartID[i]=jsonObject.get("start_point").toString();
                Constant.recentRouteEndID[i]=jsonObject.get("end_point").toString();
                Constant.recentRouteStartName[i]=jsonObject.get("start_name").toString();
                Constant.recentRouteEndName[i]=jsonObject.get("end_name").toString();
                Constant.recentRouteFavStatus[i]=jsonObject.get("fav_route").toString();
                Constant.recentRouteStatus[i]=jsonObject.get("status").toString();

                TableBE tableBE=tableRecentRoute(Constant.recentRouteID[i],Constant.recentRouteStartID[i],Constant.recentRouteEndID[i],Constant.recentRouteStartName[i],Constant.recentRouteEndName[i], Constant.recentRouteFavStatus[i], Constant.recentRouteStatus[i]);
                addToDBRecent(tableBE);
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
            Constant.favRouteStatus=new String[jsonArrayObject.size()];

            dbOperation.open();
            dbOperation.delete(tableBE.getTable_Fav_Route());
            dbOperation.close();

            for(int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.favRouteID[i]=jsonObject.get("route_id").toString();
                Constant.favRouteStartID[i]=jsonObject.get("start_point").toString();
                Constant.favRouteEndID[i]=jsonObject.get("end_point").toString();
                Constant.favRouteStartName[i]=jsonObject.get("start_name").toString();
                Constant.favRouteEndName[i]=jsonObject.get("end_name").toString();
                Constant.favRouteFavStatus[i]="Yes";
                Constant.favRouteStatus[i]=jsonObject.get("status").toString();

                TableBE tableBE=tableFavRoute(Constant.favRouteID[i], Constant.favRouteStartID[i], Constant.favRouteEndID[i], Constant.favRouteStartName[i], Constant.favRouteEndName[i], Constant.favRouteFavStatus[i], Constant.favRouteStatus[i]);
                addToDBFav(tableBE);
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

            dbOperation.open();
            dbOperation.delete(tableBE.getTable_Personal_Info());
            dbOperation.close();

                    Constant.currentLoopCredit=(int)Math.round(Double.valueOf(jsonObject.get("user_credits").toString()));
                    Constant.amount=jsonObject.get("user_credits").toString();
                    Constant.LoopCredit=Constant.LoopCreditText+Constant.currentLoopCredit;
                    String pautmString=jsonObject.get("user_paytm_id").toString();
                    Constant.PayTMWalet=Constant.PayTMWaletText+pautmString;

            TableBE tableBE=tablePersonal(Constant.NAME,Constant.amount);
            addToDBPerson(tableBE);


        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }


    /*
    * DB Tables
    * */

    TableBE tablePersonal(String name, String price) {

        /*Log.i(TAG, "inserting : " + personName + ", " + chatMessage + ", "
                + toOrFrom + " , " + chattingToDeviceID);*/
        TableBE curChatObj = new TableBE();
        curChatObj.setNAME(name);
        curChatObj.setCurrentLoopCredit(price);


        return curChatObj;

    }

    void addToDBPerson(TableBE curChatObj) {

        TableBE people = new TableBE();
        ContentValues values = new ContentValues();
        values.put(people.getNAME(), curChatObj.getNAME());
        values.put(people.getCurrentLoopCredit(),curChatObj.getCurrentLoopCredit());




        dbOperation.open();
        long id=0;

        id = dbOperation.insertTableData(people.getTable_Personal_Info(), values);

        dbOperation.close();
        if (id != -1) {
            System.out.print("Successfully Inserted");
        }

        //populateChatMessages("ADD to DB");
    }

    TableBE tableAllRoute(String routeID, String routeName, String subRoute) {

        /*Log.i(TAG, "inserting : " + personName + ", " + chatMessage + ", "
                + toOrFrom + " , " + chattingToDeviceID);*/
        TableBE curChatObj = new TableBE();
        curChatObj.setRouteId(routeID);
        curChatObj.setRouteName(routeName);
        curChatObj.setRouteSubPoint(subRoute);

        return curChatObj;

    }

    void addToDB(TableBE curChatObj) {

        TableBE people = new TableBE();
        ContentValues values = new ContentValues();
        values.put(people.getRouteId(), curChatObj.getRouteId());
        values.put(people.getRouteName(),curChatObj.getRouteName());
        values.put(people.getRouteSubPoint(),curChatObj.getRouteSubPoint());



        dbOperation.open();
        long id=0;

            id = dbOperation.insertTableData(people.getTable_All_Route(), values);

        dbOperation.close();
        if (id != -1) {
            System.out.print("Successfully Inserted");
        }

        //populateChatMessages("ADD to DB");
    }


    TableBE tableFavRoute(String favRouteID, String favRouteStartID, String favRouteEndID,String favRouteStartName, String favRouteEndName, String favRouteFavStatus,String favRouteStatus) {

        /*Log.i(TAG, "inserting : " + personName + ", " + chatMessage + ", "
                + toOrFrom + " , " + chattingToDeviceID);*/
        TableBE curChatObj = new TableBE();
        curChatObj.setFavRouteID(favRouteID);
        curChatObj.setFavRouteStartID(favRouteStartID);
        curChatObj.setFavRouteEndID(favRouteEndID);
        curChatObj.setFavRouteStartName(favRouteStartName);
        curChatObj.setFavRouteEndName(favRouteEndName);
        curChatObj.setFavRouteFavStatus(favRouteFavStatus);
        curChatObj.setFavRouteStatus(favRouteStatus);

        return curChatObj;

    }

    void addToDBFav(TableBE curChatObj) {

        TableBE people = new TableBE();
        ContentValues values = new ContentValues();
        values.put(people.getFavRouteID(), curChatObj.getFavRouteID());
        values.put(people.getFavRouteStartID(),curChatObj.getFavRouteStartID());
        values.put(people.getFavRouteEndID(),curChatObj.getFavRouteEndID());
        values.put(people.getFavRouteStartName(), curChatObj.getFavRouteStartName());
        values.put(people.getFavRouteEndName(),curChatObj.getFavRouteEndName());
        values.put(people.getFavRouteFavStatus(),curChatObj.getFavRouteFavStatus());
        values.put(people.getFavRouteStatus(),curChatObj.getFavRouteStatus());



        dbOperation.open();
        long id=0;

        id = dbOperation.insertTableData(people.getTable_Fav_Route(), values);

        dbOperation.close();
        if (id != -1) {
            System.out.print("Successfully Inserted");
        }


    }



    TableBE tableRecentRoute(String recentRouteID, String recentRouteStartID, String recentRouteEndID,String recentRouteStartName, String recentRouteEndName, String recentRouteFavStatus,String recentRouteStatus) {

        /*Log.i(TAG, "inserting : " + personName + ", " + chatMessage + ", "
                + toOrFrom + " , " + chattingToDeviceID);*/
        TableBE curChatObj = new TableBE();
        curChatObj.setRecentRouteID(recentRouteID);
        curChatObj.setRecentRouteStartID(recentRouteStartID);
        curChatObj.setRecentRouteEndID(recentRouteEndID);
        curChatObj.setRecentRouteStartName(recentRouteStartName);
        curChatObj.setRecentRouteEndName(recentRouteEndName);
        curChatObj.setRecentRouteFavStatus(recentRouteFavStatus);
        curChatObj.setRecentRouteStatus(recentRouteStatus);

        return curChatObj;

    }

    void addToDBRecent(TableBE curChatObj) {

        TableBE people = new TableBE();
        ContentValues values = new ContentValues();
        values.put(people.getRecentRouteID(), curChatObj.getRecentRouteID());
        values.put(people.getRecentRouteStartID(),curChatObj.getRecentRouteStartID());
        values.put(people.getRecentRouteEndID(),curChatObj.getRecentRouteEndID());
        values.put(people.getRecentRouteStartName(), curChatObj.getRecentRouteStartName());
        values.put(people.getRecentRouteEndName(),curChatObj.getRecentRouteEndName());
        values.put(people.getRecentRouteFavStatus(),curChatObj.getRecentRouteFavStatus());
        values.put(people.getRecentRouteStatus(),curChatObj.getRecentRouteStatus());



        dbOperation.open();
        long id=0;

        id = dbOperation.insertTableData(people.getTable_Recent_Route(), values);

        dbOperation.close();
        if (id != -1) {
            System.out.print("Successfully Inserted");
        }


    }

}
