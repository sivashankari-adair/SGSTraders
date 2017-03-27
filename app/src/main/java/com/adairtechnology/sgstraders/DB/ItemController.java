package com.adairtechnology.sgstraders.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.adairtechnology.sgstraders.Adapters.ItemAdapterTest;
import com.adairtechnology.sgstraders.GodownEntryActivity;
import com.adairtechnology.sgstraders.GodownWithOUtInternetActivity;
import com.adairtechnology.sgstraders.Models.Item;
import com.adairtechnology.sgstraders.PlacesList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Android-Team1 on 3/24/2017.
 */

public class ItemController extends SQLiteOpenHelper {

    String itemname,itemcode,itemfont,itemid,join_qry,item_detail_id,strRrR;
    String join_query_result = "";
    String id = null, qty = null;
    ArrayList<String> id_val;
    ArrayList<String> val_qty;
    ArrayList<String> invoiceid_id;
    ArrayList<String> invoicedetailid_id;

    // auto generated ID column
    private static final String databasename = "iteminfo"; // Dtabasename
    private static final int versioncode = 1; //versioncode of the database

    // Table Names
    private static final String TABLE_USER = "user_table_new";
    private static final String TABLE_WORK_DETAIL = "work_detail_new";
    private static final String TABLE_INVOICE_DETAIL = "invdetail_new";
    private static final String TABLE_INVOICE = "invoice_new";
    private static final String TABLE_ITEM = "item_table_news";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_WORKCODE = "workcode";
    private static final String KEY_GCODE = "Gcode";
    private static final String KEY_INVOICEID = "invid";
    private static final String KEY_INVOICEID_DETAIL = "invddid";
    private static final String KEY_INO = "ino";
    private static final String KEY_ITYPE = "itype";
    private static final String KEY_QTY = "qty"; //SEE THIS VALUE
    //private static final String KEY_QTYY = "qtyY";

    // TABLE_USER Table - column nmaes
    private static final String KEY_STATUS = "status";
    private static final String KEY_USERID = "userid";
    private static final String KEY_USER = "user";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USERCODE = "usercode";
    private static final String KEY_ENTCHKFLAG = "entchkflag";
    private static final String KEY_AF = "af";
    private static final String KEY_ISEL = "isel";

    // TABLE_WORK_DETAIL Table - column nmaes
    private static final String KEY_WORKDETAIL_ID = "workdetid";
    private static final String KEY_ISNOF = "isnof";
    private static final String KEY_ISNOT = "isnot";

    // TABLE_ITEM Table - column nmaes
    private static final String KEY_ITEMID = "itemid";
    private static final String KEY_ITEM = "name";
    private static final String KEY_CODE = "code";
    private static final String KEY_ITEMCODE = "itemcode"; //SEE THIS VALUE THIS IS COMMON BOTH TWO TABLES
    private static final String KEY_FONT = "font";
    private static final String KEY_ISNO = "isno";

    // TABLE_INVOICE_DETAIL Table - column nmaes
    private static final String KEY_INVDID = "invdid";
    private static final String KEY_ITEM_CODE = "itemcode";

    // TABLE_INVOICE Table - column nmaes
    private static final String KEY_IDATE = "idate";
    private static final String KEY_ITIME = "iTime";


    // Table Create Statements
    // user table create statement
    private static final String CREATE_TABLE_USER_NEW = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_STATUS + " TEXT, " + KEY_USERID + " INTEGER PRIMARY KEY, " + KEY_USER
            + " TEXT, " + KEY_PASSWORD + " TEXT, " + KEY_USERCODE
            + " TEXT, " + KEY_ENTCHKFLAG + " TEXT, " + KEY_AF + " TEXT, " + KEY_ISEL + " TEXT)";

    //work detail table create statement
    private static final String CREATE_TABLE_WORK_DETAIL_NEW = "CREATE TABLE "
            + TABLE_WORK_DETAIL + "(" + KEY_WORKDETAIL_ID + " INTEGER PRIMARY KEY, " + KEY_WORKCODE
            + " TEXT, " + KEY_GCODE + " TEXT, " + KEY_ISNOF + " INTEGER, " + KEY_ISNOT + " INTEGER)";

    //item table create statement
    private static final String CREATE_TABLE_ITEM_NEW = "CREATE TABLE "
            + TABLE_ITEM + "(" + KEY_ITEMID + " INTEGER PRIMARY KEY, " + KEY_ITEM + " TEXT, "
            + KEY_ITEMCODE + " TEXT, " + KEY_FONT + " TEXT, " + KEY_GCODE + " TEXT, "
            + KEY_ISNO + " INTEGER)";

    //invoice detail table create statement
    private static final String CREATE_TABLE_INVOICE_DETAIL_NEW = "CREATE TABLE "
            + TABLE_INVOICE_DETAIL + "(" + KEY_INVDID + " INTEGER PRIMARY KEY, " + KEY_INVOICEID_DETAIL + " INTEGER, "
            + KEY_ITYPE + " TEXT, " + KEY_INO + " INTEGER, " + KEY_ITEM_CODE + " TEXT, " + KEY_QTY + " INTEGER)";

    //invoice table create statement
    private static final String CREATE_TABLE_INVOICE_NEW = "CREATE TABLE "
            + TABLE_INVOICE + "(" + KEY_INVOICEID + " INTEGER PRIMARY KEY, " + KEY_INO + " INTEGER, " + KEY_ITYPE + " TEXT, "
            + KEY_IDATE + " TEXT, " + KEY_WORKCODE + " TEXT, " + KEY_GCODE + " TEXT, " + KEY_ITIME + " DATETIME DEFAULT CURRENT_TIMESTAMP)";


    public ItemController(Context context) {
        super(context, databasename, null, versioncode);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER_NEW);
        db.execSQL(CREATE_TABLE_WORK_DETAIL_NEW);
        db.execSQL(CREATE_TABLE_ITEM_NEW);
        db.execSQL(CREATE_TABLE_INVOICE_DETAIL_NEW);
        db.execSQL(CREATE_TABLE_INVOICE_NEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int version_old,
                          int current_version) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORK_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE);
    }

    //godown items
    public ArrayList<Item> getAllGodownItems() {
        String selectQuery = "SELECT * FROM " + TABLE_ITEM;
        System.out.println("The GowdownQury : " +selectQuery);
        ArrayList<Item> items = new ArrayList<Item>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        if (c != null) {
            while (c.moveToNext()) {

                String itemID = c.getString(c.getColumnIndex(KEY_ITEMID));
                String all_values = getQty(itemID);
                System.out.println("test_all" + " " + all_values);
                itemname = c.getString(c.getColumnIndex(KEY_ITEM));
                itemcode = c.getString(c.getColumnIndex(KEY_ITEMCODE));
                itemfont = c.getString(c.getColumnIndex(KEY_FONT));
                itemid = c.getString(c.getColumnIndex(KEY_ITEMID));


                Item emp = new Item();
                emp.setName(itemname);
                emp.setItemcode(itemcode);
                emp.setFont(itemfont);
                emp.setId(itemid);
                emp.setQty(all_values);

                Log.v("DBHelper: ", "Name: " + itemname);
                Log.v("DBHelper: ", "Code: " + itemcode);
                Log.v("DBHelper: ", "Font: " + itemfont);
                Log.v("DBHelper: ", "ItemId : " + itemid);
                Log.v("DBHelper: ", "ItemQuty : " + all_values);


                items.add(emp);
            }

        }
        c.close();
        return items;

    }
    public String getQty(String i) {
        String test = i;
        String Date = GodownWithOUtInternetActivity.final_date;
        System.out.println("The Date is :"+Date);
        String qury = "SELECT * FROM " + TABLE_ITEM + " JOIN "
                + TABLE_INVOICE + " on " + TABLE_ITEM + "." + KEY_ITEMID + " = " + TABLE_INVOICE + "." + KEY_INO
                + " JOIN " + TABLE_INVOICE_DETAIL + " ON " + TABLE_INVOICE + "." + KEY_INVOICEID
                + " = " + TABLE_INVOICE_DETAIL + "." + KEY_INVOICEID_DETAIL + " WHERE " +
                TABLE_INVOICE + "." + KEY_IDATE + "='" + Date + "' and " + TABLE_ITEM + "." +
                KEY_ITEMID + "='" + i + "' ORDER by " + TABLE_ITEM + "." + KEY_ITEMID;
        System.out.println("get id : " +qury);
        String selectQuery1 = qury;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery1, null);
        String str = "";
        if (cursor.moveToFirst())
            str = cursor.getString(cursor.getColumnIndex(KEY_QTY));
        System.out.println("get id 1: " +str);
        cursor.close();
        return str;
    }

    public ArrayList<Item> getSearchItem() {
        String text,Date,Gcode,Ucode;
        String itemid_for_search,name_for_search,itemcode_for_search,font_for_search;
        String selectQuery2;
        ArrayList<Item> employ = new ArrayList<Item>();
        text = GodownWithOUtInternetActivity.text;
        Date = GodownWithOUtInternetActivity.final_date;
        System.out.println("test date for search :"+Date);
        Gcode = GodownWithOUtInternetActivity.godown_id;
        Ucode = GodownWithOUtInternetActivity.usercode;
        String selectQuery = "SELECT * FROM " +
                TABLE_ITEM + " JOIN " + TABLE_WORK_DETAIL + " on " + TABLE_ITEM + "." + KEY_GCODE + " = " + TABLE_WORK_DETAIL + "." + KEY_GCODE + " WHERE " +
                TABLE_WORK_DETAIL + "." + KEY_GCODE + "='"+Gcode+"' AND " +
                TABLE_ITEM + "." + KEY_ITEM + " like '" +text +"%'" + " or " +
                TABLE_ITEM + "." + KEY_ITEM_CODE + " like '" + text + "%'" + " or "
                + TABLE_ITEM + "." + KEY_FONT + " like '"+text + "%'";
        System.out.println("select search query : "+selectQuery);
        SQLiteDatabase db1 = getWritableDatabase();
        Cursor cc = db1.rawQuery(selectQuery, null);
        System.out.println("select search query result : "+cc.toString());
        if (cc != null) {
            while (cc.moveToNext()) {
                itemid_for_search = cc.getString(cc.getColumnIndex(KEY_ITEMID));
                name_for_search = cc.getString(cc.getColumnIndex(KEY_ITEM));
                itemcode_for_search = cc.getString(cc.getColumnIndex(KEY_ITEM_CODE));
                font_for_search = cc.getString(cc.getColumnIndex(KEY_FONT));

                System.out.println(" before search " + itemid_for_search);
                System.out.println(" before search " + name_for_search);
                System.out.println(" before search " + itemcode_for_search);
                System.out.println(" before search " + font_for_search);

                selectQuery2 = "SELECT "+ TABLE_ITEM +"."+KEY_ITEMID +" as itemid,"+TABLE_ITEM+"."+KEY_ITEM+ " as name," + TABLE_ITEM+"."+KEY_FONT+ " as font,"
                        + TABLE_ITEM+"."+KEY_ITEM_CODE+ " as itemcode," + TABLE_INVOICE_DETAIL+"."+KEY_QTY+ " as qty," +TABLE_INVOICE+"."+KEY_INVOICEID+ " as invid FROM " +
                        TABLE_ITEM +" JOIN " + TABLE_INVOICE + " on " + TABLE_ITEM+"."+KEY_ITEMID +" = " + TABLE_INVOICE+"."+KEY_INO + " JOIN " +
                        TABLE_INVOICE_DETAIL +" ON " +TABLE_INVOICE+"."+KEY_INVOICEID+" = " +TABLE_INVOICE_DETAIL+"."+KEY_INVOICEID_DETAIL + " WHERE "+
                        TABLE_ITEM+"."+KEY_GCODE+"='"+Gcode+"' AND "+ TABLE_INVOICE+"."+KEY_IDATE+"='"+Date+"' AND " +TABLE_ITEM+"."+KEY_ITEMID+"='"+itemid_for_search+"' ORDER by "+
                        TABLE_ITEM+"."+KEY_ITEMID;

                System.out.println("search query two"+selectQuery2);
                SQLiteDatabase dbtest = getWritableDatabase();
                Cursor c = dbtest.rawQuery(selectQuery2, null);
                System.out.println("select search query two : "+c);
                int cnt = c.getCount();
                if(cnt != 0){
                    System.out.println("search result Found");

                    while (c.moveToNext()) {
                        String name1 = c.getString(c.getColumnIndex(KEY_ITEM));
                        String itemid1 = c.getString(c.getColumnIndex(KEY_ITEMID));
                        String itemcode1 = c.getString(c.getColumnIndex(KEY_ITEM_CODE));
                        String font1 = c.getString(c.getColumnIndex(KEY_FONT));
                        String qty1 = c.getString(c.getColumnIndex(KEY_QTY));

                        Item emp = new Item();
                        emp.setId(itemid1);
                        emp.setName(name1);
                        emp.setItemcode(itemcode1);
                        emp.setFont(font1);
                        emp.setQty(qty1);

                        employ.add(emp);

                        System.out.println(" search " + qty1);
                        System.out.println(" search " + name1);
                        System.out.println(" search " + itemid1);
                        System.out.println(" search " + itemcode1);


                    }
                }
                else{
                    System.out.println(" empty search ");

                    Item emp = new Item();
                    emp.setId(itemid_for_search);
                    emp.setName(name_for_search);
                    emp.setItemcode(itemcode_for_search);
                    emp.setFont(font_for_search);
                    employ.add(emp);

                    System.out.println(" before1 search " + itemid_for_search);
                    System.out.println(" before1 search " + name_for_search);
                    System.out.println(" before1 search " + itemcode_for_search);
                    System.out.println(" before1 search " + font_for_search);

                }
                c.close();
            }
        }
        cc.close();

        return employ;


    }

    public void SubmitAllValues() {

        String Date,time,Gcode,Ucode;

        Date = GodownWithOUtInternetActivity.final_date;
        time = GodownWithOUtInternetActivity.currentDateandTime;
        Gcode = GodownWithOUtInternetActivity.godown_id;
        Ucode = GodownWithOUtInternetActivity.usercode;
        System.out.println("update date : " +Date);

        ArrayList<String> id_val = GodownWithOUtInternetActivity.iID;
        System.out.println("test_check id : " + id_val);

        ArrayList<String> val_qty = GodownWithOUtInternetActivity.iQTY;
        System.out.println("test_check  qty : " + val_qty);



        if(id_val.size() == 0 && val_qty.size() == 0){
            System.out.println("value is ================== empty");
        }else{
            for (int i = 0; i < id_val.size(); i++) {
                //System.out.println("i value " + i + ": " + id_val.get(i));
                id = id_val.get(i);
                qty = val_qty.get(i);
                System.out.println("id : " + id + ", " + "qty : " + qty);

                join_qry = "SELECT * FROM " + TABLE_ITEM + " JOIN " + TABLE_INVOICE + " on "
                        + TABLE_ITEM + "." + KEY_ITEMID + " = " + TABLE_INVOICE + "." + KEY_INO + " JOIN " + TABLE_INVOICE_DETAIL +
                        " ON " + TABLE_INVOICE + "." + KEY_INVOICEID + " = " + TABLE_INVOICE_DETAIL + "." + KEY_INVOICEID_DETAIL + " WHERE " +
                        TABLE_INVOICE + "." + KEY_IDATE + "='" + Date + "' and " + TABLE_ITEM + "." + KEY_ITEMID + "=" + id
                        + " ORDER by " + TABLE_ITEM + "." + KEY_ITEMID;
                System.out.println("local update: " + join_qry);

                SQLiteDatabase db = this.getWritableDatabase();
                Cursor cursor = db.rawQuery(join_qry, null);

                if (cursor.moveToFirst())
                    item_detail_id = cursor.getString(cursor.getColumnIndex(KEY_INVOICEID_DETAIL)); //DOUBT
                cursor.close();

                insertQuantityDataValues();

            }
        }
    }

    public void getItemId() {

        String Date,time,Gcode,Ucode;

        Date = GodownWithOUtInternetActivity.final_date;
        time = GodownWithOUtInternetActivity.currentDateandTime;
        Gcode = GodownWithOUtInternetActivity.godown_id;
        Ucode = GodownWithOUtInternetActivity.usercode;
        System.out.println("update date : " +Date);

        ArrayList<String> id_val_from_adapter = ItemAdapterTest.value_idd;
        System.out.println("test_check id : " + id_val_from_adapter);

        ArrayList<String> val_qty_from_adapter = ItemAdapterTest.value_qtyd;
        System.out.println("test_check  qty : " + val_qty_from_adapter);

        if(id_val_from_adapter.size() == 0 && val_qty_from_adapter.size() == 0){
            System.out.println("value is ================== empty");
        }else{
            for (int i = 0; i < id_val_from_adapter.size(); i++) {
                //System.out.println("i value " + i + ": " + id_val.get(i));
        id = id_val_from_adapter.get(i);
        qty = val_qty_from_adapter.get(i);
        System.out.println("id : " + id + ", " + "qty : " + qty);

        join_qry = "SELECT * FROM " + TABLE_ITEM + " JOIN " + TABLE_INVOICE + " on "
                + TABLE_ITEM + "." + KEY_ITEMID + " = " + TABLE_INVOICE + "." + KEY_INO + " JOIN " + TABLE_INVOICE_DETAIL +
                " ON " + TABLE_INVOICE + "." + KEY_INVOICEID + " = " + TABLE_INVOICE_DETAIL + "." + KEY_INVOICEID_DETAIL + " WHERE " +
                TABLE_INVOICE + "." + KEY_IDATE + "='" + Date + "' and " + TABLE_ITEM + "." + KEY_ITEMID + "=" + id
                + " ORDER by " + TABLE_ITEM + "." + KEY_ITEMID;
        System.out.println("local update: " + join_qry);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(join_qry, null);

        if (cursor.moveToFirst())
            item_detail_id = cursor.getString(cursor.getColumnIndex(KEY_INVOICEID_DETAIL)); //DOUBT
        cursor.close();

        insertQuantityDataValues();

    }
}
    }
    //
    private void insertQuantityDataValues() {
        String Gcode,Ucode,Date,time;

        Date = GodownWithOUtInternetActivity.final_date;
        time = GodownWithOUtInternetActivity.currentDateandTime;
        Gcode = GodownWithOUtInternetActivity.godown_id;
        Ucode = GodownWithOUtInternetActivity.usercode;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        String test = join_qry, invoice_detail_id = null;
        System.out.println("test test :" +test);
        cursor = db.rawQuery(test, null);
        int cnt = cursor.getCount();
        try {
            if (cursor.moveToNext())
                join_query_result = cursor.getString(cursor.getColumnIndex(KEY_QTY));

            if (cnt != 1) //quantity value is EMPTY is Insert
            {

                //1ST
                String CREATE_TABLE_FOR_GETTING_ITEMID = "SELECT * FROM " + TABLE_ITEM + " WHERE " + KEY_ITEMID + "=" + id;

                Cursor cursor1 = db.rawQuery(CREATE_TABLE_FOR_GETTING_ITEMID, null);
                String strR = "";
                if (cursor1.moveToFirst())
                    strR = cursor1.getString(cursor1.getColumnIndex(KEY_ITEMCODE)); //DOUBT
                cursor1.close();

                //2ND
                String CREATE_INSERT_FOR_INVOICE_TABLE = "INSERT INTO " + TABLE_INVOICE + " (" + KEY_INO + ", "
                        + KEY_IDATE + ", " + KEY_WORKCODE + ", " + KEY_GCODE + ", " + KEY_ITIME + ") VALUES ('" + id + "','" + Date + "','"+ Ucode +"','"+Gcode+"','" + time + "')";

                Cursor cursor2 = db.rawQuery(CREATE_INSERT_FOR_INVOICE_TABLE, null);
                String strRr = "";

                if (cursor2.moveToFirst())
                    strRr = cursor2.getString(0); //DOUBT
                cursor2.close();

                //3RD
                String CREATE_SELECTION_QUERY = "SELECT * FROM " + TABLE_INVOICE + " ORDER by " + KEY_INVOICEID + " DESC LIMIT 1";

                Cursor cursor3 = db.rawQuery(CREATE_SELECTION_QUERY, null);
                strRrR = "";
                if (cursor3.moveToFirst())
                    strRrR = cursor3.getString(cursor3.getColumnIndex(KEY_INVOICEID)); //DOUBT
                cursor3.close();

                //4TH
                String CREATE_INSERT_FOR_INVOICE_DETAIL_TABLE = "INSERT INTO " + TABLE_INVOICE_DETAIL + " ("
                        + KEY_INVOICEID_DETAIL + ", " + KEY_INO + ", " + KEY_ITEM_CODE + ", " + KEY_QTY + ") VALUES ('" + strRrR + "','" + id + "','" +
                        strR + "','" + qty + "')";

                Cursor cursor4 = db.rawQuery(CREATE_INSERT_FOR_INVOICE_DETAIL_TABLE, null);
                String strRrR1 = "";
                if (cursor4.moveToFirst())
                    strRrR1 = cursor4.getString(cursor4.getColumnIndex(KEY_QTY)); //DOUBT
                cursor4.close();

            } else { //quantity value is NOT empty so UPDATE ....
                System.out.println("value is not empty");

                String update_query = "UPDATE " + TABLE_INVOICE_DETAIL + " SET " + KEY_QTY + "=" + qty + " WHERE " + KEY_INVOICEID_DETAIL
                        + "='" + item_detail_id + "' AND " + KEY_INO + "=" + id;
                Cursor cursor4 = db.rawQuery(update_query, null);
                String strRrR1 = "";
                if (cursor4.moveToFirst())
                    strRrR1 = cursor4.getString(cursor4.getColumnIndex(KEY_QTY)); //DOUBT
                cursor4.close();
            }


        } finally {
            cursor.close();
        }
    }

    public static String value_id(String value){
        System.out.println("test value:: "+ ""+value);
        return value;
    }



    public static String value_qty(String value){
        System.out.println("test value:: "+ ""+value);
        return value;
    }
//db.execSQL("delete from "+ TABLE_NAME);
    public void delete(){
        SQLiteDatabase db = this.getWritableDatabase();
        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORK_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE);*/

        db.execSQL("delete from " + TABLE_USER);
        db.execSQL("delete from " + TABLE_WORK_DETAIL);
        db.execSQL("delete from " + TABLE_ITEM);
        db.execSQL("delete from " + TABLE_INVOICE_DETAIL);
        db.execSQL("delete from " + TABLE_INVOICE);
    }

    public void Localdelete(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + TABLE_INVOICE_DETAIL);
        db.execSQL("delete from " + TABLE_INVOICE);
    }

    //
    public ArrayList<Item> savedata() {
        String id,date,qtyy,code;
        String Date = PlacesList.selected_date;
        System.out.println("dfdjh : " +Date);
        String qty = "SELECT * FROM " + TABLE_ITEM + " JOIN " + TABLE_INVOICE + " on "
                + TABLE_ITEM + "." + KEY_ITEMID + " = " + TABLE_INVOICE + "." + KEY_INO + " JOIN " + TABLE_INVOICE_DETAIL +
                " ON " + TABLE_INVOICE + "." + KEY_INVOICEID + " = " + TABLE_INVOICE_DETAIL + "." + KEY_INVOICEID_DETAIL + " WHERE " +
                TABLE_INVOICE + "." + KEY_IDATE + "='" + Date + "' ORDER by " + TABLE_ITEM + "." + KEY_ITEMID;
        System.out.println("query : " +qty);

      /*  SELECT * FROM item_table_news JOIN invoice_new on item_table_news.itemid = invoice_new.ino JOIN invdetail_new ON invoice_new.invid = invdetail_new.invddid WHERE
        invoice_new.idate='23-3-2017 ' ORDER by item_table_news.itemid
*/

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Item> items = new ArrayList<Item>();
        Cursor cursor = db.rawQuery(qty, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                id = cursor.getString(cursor.getColumnIndex(KEY_ITEMID)); //DOUBT
                date = cursor.getString(cursor.getColumnIndex(KEY_IDATE));
                qtyy = cursor.getString(cursor.getColumnIndex(KEY_QTY));
                code = cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE));

                Item emp = new Item();
                emp.setItemcode(code);
                emp.setId(id);
                emp.setQty(qtyy);
                items.add(emp);

                System.out.println("query id : " +id);
                System.out.println("query date : " +date);
                System.out.println("query qtyy: " +qtyy);
                System.out.println("query code: " +code);
            }
        }
        cursor.close();
        return items;
    }

    public void del_based_ondate(){
       // SELECT invdetail.invdid as invdid,invoice.invid as invid FROM invoice JOIN
        // invdetail ON invoice.invid = invdetail.invid WHERE invoice.idate='25-3-2017' ORDER by invoice.invid
        String Date = PlacesList.selected_date;
        String del_query = "SELECT " + TABLE_INVOICE_DETAIL +"." +KEY_INVDID +" as " + KEY_INVDID+","+
                TABLE_INVOICE+"."+KEY_INVOICEID + " as " + KEY_INVOICEID +" FROM " + TABLE_INVOICE + " JOIN "+
                TABLE_INVOICE_DETAIL + " ON " + TABLE_INVOICE+"."+KEY_INVOICEID+" = "+TABLE_INVOICE_DETAIL+"."+KEY_INVDID+" WHERE "+
                TABLE_INVOICE+"."+KEY_IDATE+"='"+Date+"' ORDER by " +TABLE_INVOICE+"."+KEY_INVOICEID;
        System.out.println("test del_query : " +del_query);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(del_query, null);
        int count = cursor.getCount();
        System.out.println("test del_query count : " + count);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id_invoice_detail = cursor.getString(cursor.getColumnIndex(KEY_INVDID)); //DOUBT
                String id_invoice = cursor.getString(cursor.getColumnIndex(KEY_INVOICEID));

                System.out.println("query id_invoice_detail : " +id_invoice_detail);
                System.out.println("query id_invoice : " +id_invoice);

                SQLiteDatabase dbde = this.getWritableDatabase();
                String del_invoice = "DELETE FROM " +TABLE_INVOICE +" WHERE "+KEY_INVOICEID+"='"+id_invoice+"'";
                System.out.println("test del invoice  : " +del_invoice);
                dbde.execSQL(del_invoice);

                SQLiteDatabase dbdsde = this.getWritableDatabase();
                String del_invoice_detail = "DELETE FROM " +TABLE_INVOICE_DETAIL +" WHERE "+KEY_INVDID+"='"+id_invoice_detail+"'";
                System.out.println("test del invoice detail  : " +del_invoice_detail);
                dbdsde.execSQL(del_invoice_detail);



              //DELETE FROM `invoice` WHERE `invid`='".$row_invoice_lists['invid']."'"
            }
        }
        cursor.close();
    }

}