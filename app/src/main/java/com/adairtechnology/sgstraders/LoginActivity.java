package com.adairtechnology.sgstraders;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adairtechnology.sgstraders.Adapters.ItemAdapterTest;
import com.adairtechnology.sgstraders.DB.Controller;
import com.adairtechnology.sgstraders.DB.ItemController;
import com.adairtechnology.sgstraders.Models.Item;
import com.adairtechnology.sgstraders.Util.EndPoints;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Android-Team1 on 1/4/2017.
 */

public class LoginActivity extends AppCompatActivity {
    Toolbar toolbar;
    public static TextView tvTitle;
    ImageView image;
    private EditText username, password;
    private Button login;
    private TextInputLayout userlayout, passwordlayout;
    private String uname, upass, parameters, server_response;
    ProgressDialog progress;
    TextView suc_entry;
    private String date;
    private int year;
    private int month;
    private int day;
    private String url;
    String status, userid, usercode, Entchkflag, af, isel, gcode, godown, isnof, isnot;
    ItemController controller = new ItemController(LoginActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title1);
        image = (ImageView) findViewById(R.id.img);
         suc_entry = (TextView) findViewById(R.id.suc_entry);

        image.setImageDrawable(getResources().getDrawable(R.drawable.home));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tvTitle.setText("   User Login");


        userlayout = (TextInputLayout) findViewById(R.id.emailloginLayout);
        passwordlayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        username = (EditText) findViewById(R.id.loginEmailET);
        password = (EditText) findViewById(R.id.loginPasswordET);
        login = (Button) findViewById(R.id.loginBtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = username.getText().toString();
                upass = password.getText().toString();
                Log.i("uname", uname + "" + upass);

                GetLoginDetails();
             /*    if (!Utils.isNetworkAvailable(LoginActivity.this)) {
                      Toast.makeText(LoginActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                                }
                                else {
                     GetLoginDetails();

                 }
*/
            }
        });
    }

    private void GetLoginDetails() {
        System.out.println( "hgfdhg " + SplashTest.ip_host_url);
        new getLogin().execute();
    }

    //Sending data to server(all login details)
    private class getLogin extends AsyncTask<Void, Void, Void> {
        //String parameters = EndPoints.update + value ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(LoginActivity.this, "Loading",
                    "Loading...", true);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //http://space7cloud.com/sgs_trader/sgs_datas.php?page=login&username=sankari&password=adair
            // parameters = MainActivity.final_url + "?page=login&username=" + uname + "&password="+ upass;
            //parameters = EndPoints.login_check + uname + "&password=" + upass;
            parameters = SplashTest.ip_host_url + EndPoints.login_check + uname + "&password=" + upass;
            String test = parameters;

            System.out.println("testtest1" + test);
            System.out.println("testtest2" + parameters);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(parameters);

            HttpResponse response = null;
            try {
                response = httpclient.execute(httpget);
                if (response.getStatusLine().getStatusCode() == 200) {
                    server_response = EntityUtils.toString(response.getEntity());
                    Log.i("Server response", server_response);
                    System.out.println("testtest3" + server_response);

                    addValuesToTables(server_response);
                } else {
                    Log.i("Server response", "Failed to get server response");
                    System.out.println("testtest4" + server_response);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            progress.dismiss();
            //   Toast.makeText(LoginActivity.this,server_response,Toast.LENGTH_SHORT).show();
            if (server_response != null) {
                try {
                    JSONObject jsonObjj = new JSONObject(server_response);
                    String status = jsonObjj.getString("status");

                    String check_status = status;
                    if (check_status.equals("true")) {
                        SharedPreferences.Editor edit_pref = getSharedPreferences("MYPREF", MODE_PRIVATE).edit();
                        edit_pref.putString("UserName", uname);
                        edit_pref.putString("Password", upass);
                        edit_pref.commit();
                        edit_pref.clear();

                        SharedPreferences.Editor prefs = getSharedPreferences("MYPREFF", MODE_PRIVATE).edit();
                        prefs.putString("loginInfo", server_response);
                        prefs.commit();
                        prefs.clear();

                        // Get current date by calender
                        final Calendar c = Calendar.getInstance();
                        year = c.get(Calendar.YEAR);
                        month = c.get(Calendar.MONTH);
                        day = c.get(Calendar.DAY_OF_MONTH);

                        date = String.valueOf((new StringBuilder().append(day)
                                .append("-").append(month + 1).append("-").append(year)
                                .append(" ")));

                     //   new GetItemListLogin().execute();
                        Intent in = new Intent(LoginActivity.this, HomeScreenActivity.class);
                        startActivity(in);
                        finish();

                    } else {
                        suc_entry.setText("Wrong User Name or Password");
                        suc_entry.setVisibility(View.VISIBLE);
                        suc_entry.postDelayed(new Runnable() {
                            public void run() {
                                suc_entry.setVisibility(View.GONE);
                            }
                        }, 3000);
                        username.setText("");
                        password.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private void addValuesToTables(String server_response) {

        List<Item> listForSearchConcepts = new ArrayList<Item>();

        try {
            JSONObject jsonObjj = new JSONObject(server_response);

            userid = jsonObjj.getString("userid");
            usercode = jsonObjj.getString("usercode");
            Entchkflag = jsonObjj.getString("Entchkflag");
            af = jsonObjj.getString("af");
            isel = jsonObjj.getString("isel");
            status = jsonObjj.getString("status");

            // Getting JSON Array node for godown
            JSONArray godown_name = jsonObjj.getJSONArray("Gcode");
            // looping through All Contacts
            for (int i = 0; i < godown_name.length(); i++) {
                JSONObject c = godown_name.getJSONObject(i);
                gcode = c.optString("Gcode");
                godown = c.optString("Godown");
                isnof = c.optString("isnof");

            }


            SQLiteDatabase db1 = controller.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("status", status);
            cv.put("userid", userid);
            cv.put("user", uname);
            cv.put("usercode", usercode);
            cv.put("entchkflag", "null");
            cv.put("af", af);
            cv.put("isel", isel);
            cv.put("password", upass);
            db1.insert("user_table_new", null, cv);
            System.out.println("test user table : "+cv.toString());
            db1.close();

            SQLiteDatabase db2 = controller.getWritableDatabase();
            ContentValues cv1 = new ContentValues();
            // cv1.put("workdetid", status); //PK
            cv1.put("workcode", usercode);
            cv1.put("Gcode", gcode);
            cv1.put("isnof", isnof);
            cv1.put("isnot", isnot);
            db2.insert("work_detail_new", null, cv1);
            db2.close();

            System.out.println("work detail table : "+cv1.toString());

            //Getting JSON ARRAY for Items
            JSONObject Godown_jsonobjj = jsonObjj.getJSONObject("Godown");
            JSONObject Godown_list_jsonobjj = Godown_jsonobjj.getJSONObject(gcode);
            JSONArray Godown_list_items_jsonobjj = Godown_list_jsonobjj.getJSONArray("items");
            for (int j = 0; j < Godown_list_items_jsonobjj.length(); j++) {
                JSONObject c = Godown_list_items_jsonobjj.getJSONObject(j);

                Item item = new Item();
                item.setId(c.optString("id"));
                item.setQty(c.optString("qty"));
                item.setName(c.optString("name"));
                item.setItemcode(c.optString("itemcode"));
                item.setFont(c.optString("font"));
                listForSearchConcepts.add(item);
                Log.i("myitemlist", listForSearchConcepts.toString());


                String itemcode = c.optString("itemcode");
                String itemname = c.optString("name");
                String itemfont = c.optString("font");
                String itemid = c.optString("id");
                String itemgcode = gcode;

                SQLiteDatabase db3 = controller.getWritableDatabase();
                ContentValues cv2 = new ContentValues();

                cv2.put("name", itemname);
                cv2.put("itemcode", itemcode);
                cv2.put("font", itemfont);
                cv2.put("itemid", itemid);
                cv2.put("Gcode", itemgcode);
                db3.insert("item_table_news", null, cv2);
                db3.close();

                System.out.println("item table : "+cv2.toString());


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}