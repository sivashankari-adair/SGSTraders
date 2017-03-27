package com.adairtechnology.sgstraders.DB;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.adairtechnology.sgstraders.Models.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Android-Team1 on 2/4/2017.
 */

public class Controller extends SQLiteOpenHelper {
    private static final String tablename = "items";  // table name
    private static final String name = "name";  // column name
    private static final String id = "ID";  // auto generated ID column
    private static final String itemcode = "itemcode";
    private static final String quantity = "quantity";// column name
    private static final String databasename = "iteminfo_new"; // Dtabasename
    private static final int versioncode = 1; //versioncode of the database


    public Controller(Context context) {
        super(context, databasename, null, versioncode);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE IF NOT EXISTS " + tablename + "(" + id + " integer primary key, "
                + name + " text, "
                + quantity + " text, "
                + itemcode + " text)";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old,
                          int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS " + tablename;
        database.execSQL(query);
        onCreate(database);
    }

    public void delete(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ tablename);
    }

    /* Method for fetching record from Database (For all items into cardview or grid view using pojo class)*/
    public ArrayList<Item> getAllEmployee() {
        String selectQuery = "SELECT  * FROM " + tablename;
        ArrayList<Item> employees = new ArrayList<Item>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        if (c != null) {
            while (c.moveToNext()) {
                String code1 = c.getString(c.getColumnIndex(id));
                String name1 = c.getString(c.getColumnIndex(name));
                String quantity1 = c.getString(c.getColumnIndex(quantity));
                String itemcode1 = c.getString(c.getColumnIndex(itemcode));

                Item emp = new Item();
                emp.setId(code1);
                emp.setName(name1);
                emp.setQty(quantity1);
                emp.setItemcode(itemcode1);

                Log.v("DBHelper: ", "Name: " + name1);
                Log.v("DBHelper: ", "Code: " + code1);
                Log.v("DBHelper: ", "Email: " + quantity1);
                Log.v("DBHelper: ", "Address: " + itemcode1);

                employees.add(emp);
            }
        }

        return employees;

    }


    //(get all items into listview(like single row)--listview)
    public ArrayList<HashMap<String, String>> getAllPlace() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + tablename;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("name", cursor.getString(1));
                map.put("quantity", cursor.getString(2));
                map.put("itemcode", cursor.getString(3));

                wordList.add(map);
            } while (cursor.moveToNext());
        }
        // return contact list
        return wordList;
    }
}
