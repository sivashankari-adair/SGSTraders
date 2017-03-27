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

import com.adairtechnology.sgstraders.Util.EndPoints;

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
        System.out.println("in main ACTIVITY : " + EndPoints.host_addr);
    }
}
