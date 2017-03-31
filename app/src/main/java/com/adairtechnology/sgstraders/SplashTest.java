package com.adairtechnology.sgstraders;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.adairtechnology.sgstraders.DB.IpController;
import com.adairtechnology.sgstraders.Util.EndPoints;
import com.adairtechnology.sgstraders.Util.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Android-Team1 on 1/4/2017.
 */

public class SplashTest extends AppCompatActivity {
    IpController controller = new IpController(SplashTest.this);
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    public  int response_code;
    int ip_original_local_val;
    public String new_value,ip_from_db;
    HttpURLConnection con;
    Context mContext;
    public static int status_code = 200;
    public static String ip_host_url,ip_for_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        /*String test = controller.getAllIp();
        System.out.println(test);*/



        if (!Utils.isNetworkAvailable(SplashTest.this)) {
            Toast.makeText(SplashTest.this,"No Connection Available.",Toast.LENGTH_SHORT).show();

            SharedPreferences prefs = getSharedPreferences("MYPREF", MODE_PRIVATE);
            String restoredTextEmail = prefs.getString("UserName", null);
            String restoredTextPassword = prefs.getString("Password", null);

            if (restoredTextEmail != null && restoredTextPassword != null) {
                Intent mainIntent = new Intent(SplashTest.this, HomeScreenActivity.class);
                SplashTest.this.startActivity(mainIntent);
                SplashTest.this.finish();
            } else {
                Intent mainIntent = new Intent(SplashTest.this, LoginActivity.class);
                SplashTest.this.startActivity(mainIntent);
                SplashTest.this.finish();
            }
        }
        else {
            Toast.makeText(SplashTest.this,"Connection Available.",Toast.LENGTH_SHORT).show();
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                    IpController controller = new IpController(SplashTest.this);
                    ip_from_db = controller.getlastvalue();
                    String ip = "http://192.168.1." + ip_from_db + "/sgs_traders/sgs_datas.php?";
                    System.out.println("test " + ip);
                    if (!ip_from_db.equals("")) {
                        System.out.println("dp value is not empty");
                         new IpIsValidOrNot().execute(ip);

                    } else {
                        System.out.println("dp value is empty");
                        FindIp();
                    }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
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

                    SQLiteDatabase db = controller.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("ip_addd", i);
                    db.insert("ipaddrR", null, cv);
                    db.close();
                    System.out.println("ip value in cv " + cv.toString());
                    System.out.println("ip value last " + controller.getAllIp());
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

        String ip_from_db = controller.getlastvalue();
        System.out.println("value from database : " +ip_from_db);
        ip_for_update="192.168.1."+ip_from_db;
        String ip = "http://192.168.1." + ip_from_db + "/sgs_traders/sgs_datas.php?";
        System.out.println("test " +ip);

        ip_host_url = ip;
        System.out.println("test end point :::: " +
                ip_host_url);

        GoToMainPage();



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
                Toast.makeText(SplashTest.this, "File exists!", Toast.LENGTH_SHORT).show();
                ip_host_url = new_value;
                System.out.println("test end point :: " +
                        ip_host_url);
                ip_for_update = "192.168.1."+ip_from_db;
               GoToMainPage();
            }
            else
            {
                Toast.makeText(SplashTest.this, "File does not exist!", Toast.LENGTH_SHORT).show();
                FindIp();
            }
        }
    }


    public void GoToMainPage() {

        SharedPreferences prefs = getSharedPreferences("MYPREF", MODE_PRIVATE);
        String restoredTextEmail = prefs.getString("UserName", null);
        String restoredTextPassword = prefs.getString("Password", null);

        if (restoredTextEmail != null && restoredTextPassword != null) {
            Intent mainIntent = new Intent(SplashTest.this, HomeScreenActivity.class);
            SplashTest.this.startActivity(mainIntent);
            SplashTest.this.finish();
        } else {
            Intent mainIntent = new Intent(SplashTest.this, LoginActivity.class);
            SplashTest.this.startActivity(mainIntent);
            SplashTest.this.finish();
        }
    }


}
