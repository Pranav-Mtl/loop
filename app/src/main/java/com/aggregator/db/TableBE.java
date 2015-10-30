package com.aggregator.db;

import com.aggregator.Constant.Constant;

/**
 * Created by Pranav Mittal on 10/30/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class TableBE {

    private String Table_All_Route= Constant.Table_ALl_Route;
    private String Table_Fav_Route=Constant.Table_Fav_Route;
    private String Table_Recent_Route=Constant.Table_Recent_Route;
    private String Table_Personal_Info=Constant.Table_Personal_Info;

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
}
