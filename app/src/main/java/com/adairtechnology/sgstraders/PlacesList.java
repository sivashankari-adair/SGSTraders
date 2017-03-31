package com.adairtechnology.sgstraders;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.adairtechnology.sgstraders.DB.Controller;
import com.adairtechnology.sgstraders.DB.IpController;
import com.adairtechnology.sgstraders.DB.ItemController;
import com.adairtechnology.sgstraders.DB.NewAdapter;
import com.adairtechnology.sgstraders.Models.Item;
import com.adairtechnology.sgstraders.Util.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Android-Team1 on 2/4/2017.
 */

public class PlacesList extends ActionBarActivity {
    ListView ls;
    String ip_from_db;
    String ip_from_db_new;
    TextView infotext;
    Button save_online,get_fromdb;
    Toolbar toolbar;
    ImageView image;
    public static TextView tvTitle;
    ItemController controller = new ItemController(this);
    TextView caltext;
    ImageView calimg;
    private TextView date;
    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111;
    public static String selected_date;
    ArrayList<Item> ItemList;
    NewAdapter adapter;
    String server_response;
    TextView textcheck;
    String logininfo,usercode,af;
    String gcode,godown,isnof;
    IpController contr = new IpController(PlacesList.this);
    public static int status_code = 200;
    HttpURLConnection con;
    public  int response_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placeslist);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title1);
        image = (ImageView)findViewById(R.id.img);
        image.setImageDrawable(getResources().getDrawable(R.drawable.arrow_left));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tvTitle.setText("   Back");

        ls = (ListView) findViewById(R.id.placeslist);
        save_online = (Button) findViewById(R.id.btn_saveonline);
        calimg = (ImageView)findViewById(R.id.img_cal_new);
        caltext =(TextView)findViewById(R.id.dod_new);
        get_fromdb=(Button)findViewById(R.id.getvalue_fromdb);
        textcheck = (TextView)findViewById(R.id.textcheck);

        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        caltext.setText(new StringBuilder().append(day)
                .append("-").append(month + 1).append("-").append(year)
                .append(" "));

        SharedPreferences prefs = getSharedPreferences("MYPREFF", MODE_PRIVATE);
        logininfo = prefs.getString("loginInfo", null);
        try {
            JSONObject jsonObj = new JSONObject(logininfo);
            usercode = jsonObj.optString("usercode");
            af = jsonObj.optString("af");

            System.out.println("testusercode" + usercode);
            System.out.println("testaf" + af);

            // Getting JSON Array node for godown
            JSONArray godown_name = jsonObj.getJSONArray("Gcode");
            // looping through All Contacts
            for (int i = 0; i < godown_name.length(); i++) {
                JSONObject cc = godown_name.getJSONObject(i);
                gcode = cc.optString("Gcode");
                godown = cc.optString("Godown");
                isnof = cc.optString("isnof");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Button listener to show date picker dialog
        calimg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);


            }

        });
            get_fromdb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemList = new ArrayList<Item>();
                    ItemList = controller.savedata();
                    System.out.println("test item list :" +ItemList);

                    adapter = new NewAdapter(PlacesList.this, ItemList);
                    ls.setAdapter(adapter);


                }
            });



        save_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOnline();

            }

        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    private void back() {
        Intent in = new Intent(PlacesList.this,HomeScreenActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay)
        {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            // Show selected date
            caltext.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append(""));

            selected_date = caltext.getText().toString();
            System.out.println("Value Date in placelist"+selected_date);


        }
    };

    private void saveOnline() {
        if (!Utils.isNetworkAvailable(PlacesList.this)) {
            Toast.makeText(PlacesList.this, "No Connection Available.", Toast.LENGTH_SHORT).show();
        }
        else {

            ip_from_db = contr.getlastvalue();
            String ip = "http://192.168.1." + ip_from_db + "/sgs_traders/sgs_datas.php?";
            System.out.println("test " + ip);
            if (!ip_from_db.equals("")) {
                System.out.println("dp value is not empty");
                new IpIsValidOrNot().execute(ip);

            } else {
                System.out.println("dp value is empty");
                FindIp();
            }
           // new GetUpdateQuantity().execute();
        }
    }


    class IpIsValidOrNot extends AsyncTask<String, Void, Boolean> {
        int response_code;
        String new_value;
        HttpURLConnection con;
        Context mContext;
        IpController controller = new IpController(mContext);
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                HttpURLConnection.setFollowRedirects(false);

                new_value = params[0];
                System.out.println("customURLTest" + new_value);

                con = (HttpURLConnection) new URL(new_value).openConnection();
                con.setRequestMethod("HEAD");
                response_code = con.getResponseCode();
                System.out.println("Response Code" + response_code);


                if (response_code == status_code) {
                    System.out.println("response_code Code" + status_code);

                } else {
                    System.out.println("Wrong");

                }
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            boolean bResponse = result;

            if (bResponse==true)
            {
                Toast.makeText(PlacesList.this, "File exists!", Toast.LENGTH_SHORT).show();

                new GetUpdateQuantity().execute();
            }
            else
            {
                Toast.makeText(PlacesList.this, "File does not exist!", Toast.LENGTH_SHORT).show();
                FindIp();
            }
        }
    }

    private void FindIp() {
        label1:
        for (int i=1;i<=255;i++ ) {

            String customURL = "http://192.168.1." + i + "/sgs_traders/sgs_datas.php";
            System.out.println("customURL" + customURL);

            try {
                con = (HttpURLConnection) new URL(customURL).openConnection();
                con.setRequestMethod("HEAD");
                response_code = con.getResponseCode();
                System.out.println("test : " +response_code);
                if (status_code == response_code ) {
                    System.out.println("if condition working" + response_code);
                    System.out.println("ip value" + i);

                    SQLiteDatabase db = contr.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("ip_addd", i);
                    db.insert("ipaddrR", null, cv);
                    db.close();
                    System.out.println("ip value in cv " + cv.toString());
                    System.out.println("ip value last " + contr.getAllIp());
                    AssignIp();
                    break label1;
                }
                else{
                    System.out.println("else condition working");
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void AssignIp() {

        ip_from_db_new = contr.getlastvalue();
        System.out.println("value from database : " +ip_from_db_new);
        new GetUpdateQuantityNew().execute();

    }

    //Sending data to server(all new item qty)
    private class GetUpdateQuantity extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            String ip_forupdate = "192.168.1."+ip_from_db;
            System.out.println("fhjf : "+ip_forupdate);
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority(ip_forupdate)//space7cloud.com//2//92.168.1.100
                    .appendPath("sgs_traders")//sgs_traders
                    .appendPath("sgs_datas.php")
                    .appendQueryParameter("save_godown_id", gcode)
                    .appendQueryParameter("date", selected_date)
                    .appendQueryParameter("value", String.valueOf(NewAdapter.value_list))
                    .appendQueryParameter("usercode", usercode);

            System.out.println("Value Submit gcode " + gcode);
            System.out.println("Value Submit selected_date " + selected_date);
            System.out.println("Value Submit usercode " + usercode);
            System.out.println("Value Submit ip_for_update " + SplashTest.ip_for_update);

            String myUrl = builder.build().toString();
            System.out.println("Value Submit Url" + myUrl);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(myUrl);
            HttpResponse response = null;
            try {
                response = httpclient.execute(httpget);
                if (response.getStatusLine().getStatusCode() == 200) {
                    server_response = EntityUtils.toString(response.getEntity());
                    Log.i("Server response", server_response);
                    Log.i("Server response", myUrl);
                    System.out.println("Value server resonse" + server_response);

                } else {
                    Log.i("Server response", "Failed to get server response");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            //Toast.makeText(PlacesList.this, server_response, Toast.LENGTH_SHORT).show();
            String jsonStrr = server_response;
            if (jsonStrr != null) {
                try {
                    JSONObject jsonObjj = new JSONObject(server_response);
                    String status = jsonObjj.getString("status");
                    Toast.makeText(PlacesList.this, status, Toast.LENGTH_SHORT).show();
                    controller.del_based_ondate();
                    Toast.makeText(PlacesList.this, "Local Value Deleted", Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Sending data to server(all new item qty)
    private class GetUpdateQuantityNew extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            String ip_forupdate = "192.168.1."+ip_from_db_new;
            System.out.println("fhjf : "+ip_forupdate);
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority(ip_forupdate)//space7cloud.com//2//92.168.1.100
                    .appendPath("sgs_traders")//sgs_traders
                    .appendPath("sgs_datas.php")
                    .appendQueryParameter("save_godown_id", gcode)
                    .appendQueryParameter("date", selected_date)
                    .appendQueryParameter("value", String.valueOf(NewAdapter.value_list))
                    .appendQueryParameter("usercode", usercode);

            System.out.println("Value Submit gcode " + gcode);
            System.out.println("Value Submit selected_date " + selected_date);
            System.out.println("Value Submit usercode " + usercode);
            System.out.println("Value Submit ip_for_update " + SplashTest.ip_for_update);

            String myUrl = builder.build().toString();
            System.out.println("Value Submit Url" + myUrl);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(myUrl);
            HttpResponse response = null;
            try {
                response = httpclient.execute(httpget);
                if (response.getStatusLine().getStatusCode() == 200) {
                    server_response = EntityUtils.toString(response.getEntity());
                    Log.i("Server response", server_response);
                    Log.i("Server response", myUrl);
                    System.out.println("Value server resonse" + server_response);

                } else {
                    Log.i("Server response", "Failed to get server response");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            //Toast.makeText(PlacesList.this, server_response, Toast.LENGTH_SHORT).show();
            String jsonStrr = server_response;
            if (jsonStrr != null) {
                try {
                    JSONObject jsonObjj = new JSONObject(server_response);
                    String status = jsonObjj.getString("status");
                    Toast.makeText(PlacesList.this, status, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

