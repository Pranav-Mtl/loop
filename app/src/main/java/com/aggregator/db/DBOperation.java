package com.aggregator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aggregator.Constant.Constant;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Created by Pranav Mittal on 10/30/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class DBOperation implements Serializable {

    String DB_TABLE;
    int DB_VERSION = 1;
    static String[] DATABASE_CREATE;
    private Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    public static String TAG = "GCM DB";

    public DBOperation(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public void createAndInitializeTables() {
        try {
            TableBE mytable = new TableBE();
            String[] tableCreateArray = { mytable.getDatabaseCreateAllRoute(),mytable.getDatabaseCreateFavRoute(),mytable.getDatabaseCreateRecentRoute(),mytable.getDatabaseCreatePersonalInfo()};
            DBOperation operation = new DBOperation(context, tableCreateArray);
            operation.open();
            operation.close();
            Log.i(TAG, "DB Created");
        } catch (Exception e) {
            Log.d(TAG, "Error creating table " + e.getMessage());
        }
    }

    public DBOperation(Context ctx, String[] query) {
        this.context = ctx;
        DATABASE_CREATE = query;
        DBHelper = new DatabaseHelper(context);
    }

    public Cursor getTableRow(String tablename, String[] dbFields,
                              String condition, String order, String limit) throws SQLException {
        DB_TABLE = tablename;
        Cursor mCursor = db.query(false, DB_TABLE, dbFields, condition, null,
                null, null, order, limit);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }




    public long insertTableData(String tablename, ContentValues values)
            throws SQLException {
        DB_TABLE = tablename;
        ContentValues contentValues = new ContentValues();
        Set<Map.Entry<String, Object>> s = values.valueSet();
        String new_val = "";
        for (Map.Entry<String, Object> entry : s) {
            new_val = values.getAsString(entry.getKey());
            contentValues.put(entry.getKey(), new_val);
        }
        return db.insert(DB_TABLE, null, contentValues);
    }
    public DBOperation open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void delete(String tablename) throws SQLException
    {
        System.out.println("TABLE DELETE"+tablename);
        db.execSQL("delete from " + tablename);
    }

    public void close() {
        DBHelper.close();
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, "inloop.db", null, DB_VERSION);
        }


        public void onCreate(SQLiteDatabase db) {
            try {
                for (String s : DATABASE_CREATE) {
                    db.execSQL(s);
                }
            } catch (Exception e) {
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }

    /* get data from all route table */

    public Cursor getDataFromTableRoute() {

        DBOperation operationObj = new DBOperation(context);
        operationObj.open();
        TableBE peopleTable = new TableBE();
        String condition = "";

        String[] dbFields = {peopleTable.getRouteId(), peopleTable.getRouteName(),
                peopleTable.getRouteSubPoint()};
        Cursor cursor = operationObj.getTableRow(Constant.Table_ALl_Route,
                dbFields, condition,null,
                null);
        operationObj.close();
        return cursor;
    }

    /* get data from fav route table */

    /* TableBE curChatObj = new TableBE();
        curChatObj.setFavRouteID(favRouteID);
        curChatObj.setFavRouteStartID(favRouteStartID);
        curChatObj.setFavRouteEndID(favRouteEndID);
        curChatObj.setFavRouteStartName(favRouteStartName);
        curChatObj.setFavRouteEndName(favRouteEndName);
        curChatObj.setFavRouteFavStatus(favRouteFavStatus);
        curChatObj.setFavRouteStatus(favRouteStatus);*/

    public Cursor getDataFromTableFavRoute() {

        DBOperation operationObj = new DBOperation(context);
        operationObj.open();
        TableBE peopleTable = new TableBE();
        String condition = "";

        String[] dbFields = {peopleTable.getFavRouteID(), peopleTable.getFavRouteStartID(),
                peopleTable.getFavRouteEndID(),peopleTable.getFavRouteStartName(), peopleTable.getFavRouteEndName(),peopleTable.getFavRouteFavStatus(), peopleTable.getFavRouteStatus()};
        Cursor cursor = operationObj.getTableRow(Constant.Table_Fav_Route,
                dbFields, condition,null,
                null);
        operationObj.close();
        return cursor;
    }

    /* get data from recent route table */

    public Cursor getDataFromTableRecentRoute() {

        DBOperation operationObj = new DBOperation(context);
        operationObj.open();
        TableBE peopleTable = new TableBE();
        String condition = "";

        String[] dbFields = {peopleTable.getRecentRouteID(), peopleTable.getRecentRouteStartID(),
                peopleTable.getRecentRouteEndID(),peopleTable.getRecentRouteStartName(), peopleTable.getRecentRouteEndName(),peopleTable.getRecentRouteFavStatus(), peopleTable.getRecentRouteStatus()};
        Cursor cursor = operationObj.getTableRow(Constant.Table_Recent_Route,
                dbFields, condition,null,
                null);
        operationObj.close();
        return cursor;
    }

    /* get data from personal info table */

    public Cursor getDataFromTablePersonalInfo() {

        DBOperation operationObj = new DBOperation(context);
        operationObj.open();
        TableBE peopleTable = new TableBE();
        String condition = "";

        String[] dbFields = {peopleTable.getNAME(), peopleTable.getCurrentLoopCredit()};
        Cursor cursor = operationObj.getTableRow(Constant.Table_Personal_Info,
                dbFields, condition,null,
                null);
        operationObj.close();
        return cursor;
    }

}
