package com.adairtechnology.sgstraders;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView image;
    public static TextView tvTitle;
    int i,ip_val,response_code;
    public static int status_code = 200;
    HttpURLConnection con;
    public static String customURL;
    public static String final_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Button btn = (Button) findViewById(R.id.btn_test);
        String customURL1 = "http://192.168.1.";
      /*  customURL = customURL1 + 7 + "/sgs_traders/sgs_datas.php";
        MyTask task = new MyTask();
        task.execute(customURL);*/



        for(i=5;i<=8;i++){

            ip_val = i;

            customURL = customURL1 + ip_val + "/sgs_traders/sgs_datas.php";
            System.out.println("cust" + customURL);

            MyTask task = new MyTask();
            task.execute(customURL);

            System.out.print("value of x : " + ip_val );

        }



    }

    private class MyTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                HttpURLConnection.setFollowRedirects(false);
                con = (HttpURLConnection) new URL(params[0]).openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                response_code = con.getResponseCode();
                System.out.println("Response Code" + response_code);
                System.out.println("Response Code" + params[0]);
                final_url = params[0];
                System.out.println("Response Code check " + final_url);

                if (response_code == status_code) {
                    System.out.println("response_code Code" + status_code);
                    finish();

                    Intent in =new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(in);
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
            if (bResponse == true) {
                Toast.makeText(MainActivity.this, "File exists!", Toast.LENGTH_SHORT).show();
                System.out.println("File Found");
                Log.i("Found file", "");
            } else {
                System.out.println("File Not");
                Log.i("No file", "");
                Toast.makeText(MainActivity.this, "File  Does Not exist!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
