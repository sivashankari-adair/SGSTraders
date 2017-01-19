package com.adairtechnology.sgstraders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.adairtechnology.sgstraders.Util.EndPoints;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Android-Team1 on 1/4/2017.
 */

public class LoginActivity extends AppCompatActivity {
    Toolbar toolbar;
    public static TextView tvTitle;
    ImageView image;
    private EditText username,password;
    private Button login;
    private TextInputLayout userlayout,passwordlayout;
    private String uname,upass,parameters,server_response;
    ProgressDialog progress;
    TextView  suc_entry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title1);
        image = (ImageView)findViewById(R.id.img);
        suc_entry = (TextView)findViewById(R.id.suc_entry);

        image.setImageDrawable(getResources().getDrawable(R.drawable.home));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tvTitle.setText("   User Login");


        userlayout =(TextInputLayout)findViewById(R.id.emailloginLayout);
        passwordlayout = (TextInputLayout)findViewById(R.id.passwordLayout);
        username = (EditText)findViewById(R.id.loginEmailET);
        password =(EditText)findViewById(R.id.loginPasswordET);
        login = (Button)findViewById(R.id.loginBtn);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = username.getText().toString();
                upass = password.getText().toString();
                Log.i("uname", uname + "" +upass);

                GetLoginDetails();





            }
        });
    }

    private void GetLoginDetails() {

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
            parameters = EndPoints.login_check + uname + "&password="+ upass;
            String test = parameters;

            System.out.println("testtest"+test);
            System.out.println("testtest"+parameters);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(parameters);

            HttpResponse response = null;
            try {
                response = httpclient.execute(httpget);
                if (response.getStatusLine().getStatusCode() == 200) {
                    server_response = EntityUtils.toString(response.getEntity());
                    Log.i("Server response", server_response);
                    System.out.println("testtest"+server_response);


                } else {
                    Log.i("Server response", "Failed to get server response");
                    System.out.println("testtest"+server_response);
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
                    if(check_status.equals("true"))
                    {

                        SharedPreferences.Editor edit_pref = getSharedPreferences("MYPREF", MODE_PRIVATE).edit();
                        edit_pref.putString("UserName",uname);
                        edit_pref.putString("Password",upass);
                        edit_pref.commit();
                        edit_pref.clear();


                        SharedPreferences.Editor prefs = getSharedPreferences("MYPREFF", MODE_PRIVATE).edit();
                        prefs.putString("loginInfo", server_response);
                        prefs.commit();
                        prefs.clear();

                        Intent in = new Intent(LoginActivity.this,HomeScreenActivity.class);
                        startActivity(in);
                        finish();
                    }
                    else
                    {
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
                }}



        }
    }
}
