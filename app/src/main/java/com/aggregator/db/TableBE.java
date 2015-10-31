package com.aggregator.db;

import com.aggregator.Constant.Constant;

/**
 * Created by Pranav Mittal on 10/30/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class TableBE {

    /* All Table Name */

    private String Table_All_Route= Constant.Table_ALl_Route;
    private String Table_Fav_Route=Constant.Table_Fav_Route;
    private String Table_Recent_Route=Constant.Table_Recent_Route;
    private String Table_Personal_Info=Constant.Table_Personal_Info;

    /* Route Table Variables */
    private String ID="id";
    private String routeId="routeid";
    private String routeName="routeName";
    private String routeSubPoint="subPoint";


    /* Fav Table Variables */

    private String favRouteID="routeid";
    private String favRouteStartID="startid";
    private String favRouteEndID="endid";
    private String favRouteStartName="startname";
    private String favRouteEndName="endname";
    private String favRouteFavStatus="favstatus";
    private String favRouteStatus="routestatus";

    /* Recent Table Variables */

    private String recentRouteID="routeid";
    private String recentRouteStartID="startid";
    private String recentRouteEndID="endid";
    private String recentRouteStartName="startname";
    private String recentRouteEndName="endname";
    private String recentRouteFavStatus="favstatus";
    private String recentRouteStatus="routestatus";

    /* Personal Table Variables */

    private String NAME="name";
    private String currentLoopCredit="balance";

    /* Table name getters */

    public String getTable_All_Route() {
        return Table_All_Route;
    }

    public String getTable_Fav_Route() {
        return Table_Fav_Route;
    }

    public String getTable_Recent_Route() {
        return Table_Recent_Route;
    }

    public String getTable_Personal_Info() {
        return Table_Personal_Info;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteSubPoint() {
        return routeSubPoint;
    }

    public void setRouteSubPoint(String routeSubPoint) {
        this.routeSubPoint = routeSubPoint;
    }

    public String getFavRouteID() {
        return favRouteID;
    }

    public void setFavRouteID(String favRouteID) {
        this.favRouteID = favRouteID;
    }

    public String getFavRouteStartID() {
        return favRouteStartID;
    }

    public void setFavRouteStartID(String favRouteStartID) {
        this.favRouteStartID = favRouteStartID;
    }

    public String getFavRouteEndID() {
        return favRouteEndID;
    }

    public void setFavRouteEndID(String favRouteEndID) {
        this.favRouteEndID = favRouteEndID;
    }

    public String getFavRouteStartName() {
        return favRouteStartName;
    }

    public void setFavRouteStartName(String favRouteStartName) {
        this.favRouteStartName = favRouteStartName;
    }

    public String getFavRouteEndName() {
        return favRouteEndName;
    }

    public void setFavRouteEndName(String favRouteEndName) {
        this.favRouteEndName = favRouteEndName;
    }

    public String getFavRouteFavStatus() {
        return favRouteFavStatus;
    }

    public void setFavRouteFavStatus(String favRouteFavStatus) {
        this.favRouteFavStatus = favRouteFavStatus;
    }

    public String getFavRouteStatus() {
        return favRouteStatus;
    }

    public void setFavRouteStatus(String favRouteStatus) {
        this.favRouteStatus = favRouteStatus;
    }

    public String getRecentRouteID() {
        return recentRouteID;
    }

    public void setRecentRouteID(String recentRouteID) {
        this.recentRouteID = recentRouteID;
    }

    public String getRecentRouteStartID() {
        return recentRouteStartID;
    }

    public void setRecentRouteStartID(String recentRouteStartID) {
        this.recentRouteStartID = recentRouteStartID;
    }

    public String getRecentRouteEndID() {
        return recentRouteEndID;
    }

    public void setRecentRouteEndID(String recentRouteEndID) {
        this.recentRouteEndID = recentRouteEndID;
    }

    public String getRecentRouteStartName() {
        return recentRouteStartName;
    }

    public void setRecentRouteStartName(String recentRouteStartName) {
        this.recentRouteStartName = recentRouteStartName;
    }

    public String getRecentRouteEndName() {
        return recentRouteEndName;
    }

    public void setRecentRouteEndName(String recentRouteEndName) {
        this.recentRouteEndName = recentRouteEndName;
    }

    public String getRecentRouteFavStatus() {
        return recentRouteFavStatus;
    }

    public void setRecentRouteFavStatus(String recentRouteFavStatus) {
        this.recentRouteFavStatus = recentRouteFavStatus;
    }

    public String getRecentRouteStatus() {
        return recentRouteStatus;
    }

    public void setRecentRouteStatus(String recentRouteStatus) {
        this.recentRouteStatus = recentRouteStatus;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getCurrentLoopCredit() {
        return currentLoopCredit;
    }

    public void setCurrentLoopCredit(String currentLoopCredit) {
        this.currentLoopCredit = currentLoopCredit;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDatabaseCreateAllRoute() {
        final String DATABASE_CREATE = "create table IF NOT EXISTS "
                + Table_All_Route + " (" + ID+ " INTEGER PRIMARY KEY, "
                + routeId + " TEXT NOT NULL, "
                + routeName + " TEXT NOT NULL, "
                + routeSubPoint + " TEXT NOT NULL )";


        System.out.println(DATABASE_CREATE);
        return DATABASE_CREATE;
    }

    public String getDatabaseCreateFavRoute() {
        final String DATABASE_CREATE = "create table IF NOT EXISTS "
                + Table_Fav_Route + " (" + ID+ " INTEGER PRIMARY KEY, "
                + favRouteID + " TEXT NOT NULL, "
                + favRouteStartID + " TEXT NOT NULL, "
                + favRouteStartName + " TEXT NOT NULL,"
                + favRouteEndID + " TEXT NOT NULL,"
                + favRouteEndName + " TEXT NOT NULL ,"
                + favRouteFavStatus + " TEXT NOT NULL,"
                + favRouteStatus + " TEXT NOT NULL )";


        System.out.println(DATABASE_CREATE);
        return DATABASE_CREATE;
    }

    public String getDatabaseCreateRecentRoute() {
        final String DATABASE_CREATE = "create table IF NOT EXISTS "
                + Table_Recent_Route + " (" + ID+ " INTEGER PRIMARY KEY, "
                + recentRouteID + " TEXT NOT NULL, "
                + recentRouteStartID + " TEXT NOT NULL, "
                + recentRouteStartName + " TEXT NOT NULL,"
                + recentRouteEndID + " TEXT NOT NULL,"
                + recentRouteEndName + " TEXT NOT NULL ,"
                + recentRouteFavStatus + " TEXT NOT NULL,"
                + recentRouteStatus + " TEXT NOT NULL )";


        System.out.println(DATABASE_CREATE);
        return DATABASE_CREATE;
    }

    public String getDatabaseCreatePersonalInfo() {
        final String DATABASE_CREATE = "create table IF NOT EXISTS "
                + Table_Personal_Info + " (" + ID+ " INTEGER PRIMARY KEY, "
                + NAME + " TEXT NOT NULL, "
                + currentLoopCredit + " INTEGER NOT NULL )";
        System.out.println(DATABASE_CREATE);
        return DATABASE_CREATE;
    }
}
