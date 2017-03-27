package com.adairtechnology.sgstraders.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Android-Team1 on 3/24/2017.
 */

public class IpController extends SQLiteOpenHelper {
    private static final String tablename = "ipaddrr";  // table name
    private static final String ip = "ip_addd";  // column name
    private static final String idd = "ID_numm";  // auto generated ID column
    private static final String databasename = "ipinfoo"; // Dtabasename
    private static final int versioncode = 1; //versioncode of the database
    public static String selectQuery;
    private static final String CREATE_IP_TABLE  = "CREATE TABLE " + tablename + "(" + idd + " INTEGER PRIMARY KEY, " + ip + " INTEGER)";


    public IpController(Context context) {
        super(context, databasename, null, versioncode);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
      database.execSQL(CREATE_IP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old,
                          int current_version) {
        database.execSQL("DROP TABLE IF EXISTS " + CREATE_IP_TABLE);
    }

    public String getlastvalue() {
        String selectQuery1 = "SELECT * FROM "+ tablename + " order by ID_numm desc limit 1" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery1, null);
        String str = "";
        if(cursor.moveToFirst())
            str  =  cursor.getString( cursor.getColumnIndex(ip) );
        System.out.println("dj : " + str);
        cursor.close();
        //db.close();
        return str;
    }

    public String getAllIp() {
        String selectQuery1 = "SELECT * FROM "+ tablename  ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery1, null);
        String str = "";
        if(cursor.moveToFirst())
            str  =  cursor.getString( cursor.getColumnIndex(ip) );
        System.out.println("dj : " + str);
        cursor.close();
        db.close();
        return str;
    }


}