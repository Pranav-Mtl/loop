package com.aggregator.BE;

/**
 * Created by Pranav Mittal on 8/26/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class BookingBE {

    private String userID;
    private String routeID;
    private String runID;
    private String startPoint;
    private String endPoint;
    private String price;
    private String time;
    private String loopCredit;

    public String getLoopCredit() {
        return loopCredit;
    }

    public void setLoopCredit(String loopCredit) {
        this.loopCredit = loopCredit;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getRunID() {
        return runID;
    }

    public void setRunID(String runID) {
        this.runID = runID;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }




}
