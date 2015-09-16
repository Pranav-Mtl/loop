package com.aggregator.BL;

import com.aggregator.BE.BookingBE;
import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 8/25/2015.
 */
public class BookingBL {

    BookingBE objBookingBE;

    // get selected route

    public String getRoutesDetail(String routeID)
    {
        String result=callWS(routeID);
        String status=validate(result);
        return status;
    }

    private String callWS(String routeId)
    {
        String URL="route_id="+routeId;
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_BOOKING_ROUTES);
        return txtJson;
    }

    private String validate(String result)
    {
        String status="";
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
                Constant.jsonUP=jsonObject.get("UP").toString();
                Constant.jsonDOWN=jsonObject.get("DOWN").toString();
                Constant.jsonRoutePrice=jsonObject.get("route_price").toString();

            parseSubJson(Constant.jsonUP);
            parseRoutePrice(Constant.jsonRoutePrice);

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return "";
    }

    private void parseRoutePrice(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

            Constant.routeFixedPrice=(Double.valueOf(jsonObject.get("fixed_price").toString()));
            Constant.routePerKmPrice=(Double.valueOf(jsonObject.get("per_km_price").toString()));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void parseSubJson(String result)
    {
        String status="";
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.pointID=new String[jsonArrayObject.size()];
            Constant.pointName=new String[jsonArrayObject.size()];
            Constant.pointDistance=new Double[jsonArrayObject.size()];
            Constant.pointTime=new String[jsonArrayObject.size()];
            Constant.pointOrder=new String[jsonArrayObject.size()];
            Constant.pointRun=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                Constant.pointID[i]=jsonObject.get("point_id").toString();
                Constant.pointName[i]=jsonObject.get("point_name").toString();
                Constant.pointDistance[i]=(Double.valueOf(jsonObject.get("distance").toString()));
                Constant.pointTime[i]=jsonObject.get("departure_time").toString().replace("[","").replace("]","").replaceAll("\"","");
                Constant.pointOrder[i]=jsonObject.get("route_point_order").toString();
                Constant.pointRun[i]=jsonObject.get("run").toString().replace("[", "").replace("]", "").replaceAll("\"","");
            }



        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }

    //insert booking

    public String insertBooking(BookingBE bookingBE)
    {
        objBookingBE=bookingBE;
        String result=callInsertWS();
        String status=validateInsert(result);

        return status;
    }

    //http://appslure.in/loop/booking_create.php?email=z@gmail.com&route_id=5&source=Pitampura&destination=Narela&cost=50&date=26-08-2015&time=10:40
    //http://appslure.in/loop/webservices/loop1/booking.php?user_id=1&route_id=1&run_id=2&start_point=22&end_point=22&price=100
    private String callInsertWS()
    {
        String URL="user_id="+objBookingBE.getUserID()+"&route_id="+objBookingBE.getRouteID()+"&start_point="+objBookingBE.getStartPoint()+"&end_point="+objBookingBE.getEndPoint()+"&run_id="+objBookingBE.getRunID()+"&price="+objBookingBE.getPrice()+"&trip_date="+objBookingBE.getTime();
        String txtJson= RestFullWS.callWS(URL, Constant.WEBSERVICE_BOOKING);
        return txtJson;
    }

    private String validateInsert(String result){

        String status="";
        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=jsonObject.get("result").toString();

            if(status.equals(Constant.WS_RESULT_SUCCESS)){
                status=jsonObject.get("user_run_id").toString();
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return status;
    }
}
