package com.adairtechnology.sgstraders;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.adairtechnology.sgstraders.Adapters.ItemAdapter;
import com.adairtechnology.sgstraders.Adapters.ItemAdapterTest;
import com.adairtechnology.sgstraders.Models.Godown;
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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.adairtechnology.sgstraders.Util.TamilFont.initialize;

/**
 * Created by Android-Team1 on 1/4/2017.
 */


public class GodownEntryActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editsearch;
    public static TextView tvTitle;
    ImageView image;
    private ListView list;
  //  ItemAdapter adapter;
    ItemAdapterTest adapter;
    String correctDecoded;
    String url;
    ArrayList<String> dataItems = new ArrayList<String>();
    private Button submit;
    Item item;
    private ImageView cal;
    private TextView date;
    private int year;
    private int month;
    private int day;
    SharedPreferences pref;
    String text,input_for_date,value,server_response,update_date,parameters,
            logininfo,godown_id,godown_name_title,af,usercode,str,siz;
    static final int DATE_PICKER_ID = 1111;
    private TextView selected_item,not_selected_item,proceed;
    private TextView suc_entry;
    ProgressDialog progress;
    ArrayList<Godown> godnn;
    ArrayList<String> godownname;
    private Spinner godown_name;
    private Typeface type;
    private static String selected_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.godown_activity);

        editsearch = (EditText) findViewById(R.id.search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title1);
        image = (ImageView)findViewById(R.id.img);
        proceed = (TextView)findViewById(R.id.proceed);
        image.setImageDrawable(getResources().getDrawable(R.drawable.arrow_left));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tvTitle.setText("  Godown Entry");
        selected_item = (TextView)findViewById(R.id.selected_count);
        not_selected_item = (TextView)findViewById(R.id.not_selected_count);
        suc_entry = (TextView)findViewById(R.id.suc_entry);
        list = (ListView) findViewById(R.id.poll_list_listView);
        submit = (Button) findViewById(R.id.submit);
        date = (TextView) findViewById(R.id.dod);
        cal = (ImageView) findViewById(R.id.img_cal);
        godown_name = (Spinner) findViewById(R.id.spinner_godown_name);

        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        date.setText(new StringBuilder().append(day)
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //spinner load for godown name
        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(GodownEntryActivity.this,"No Connection Available.",Toast.LENGTH_SHORT).show();
        }
        else {
            GetSpinnerItem();
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(),"No Connection Available.",Toast.LENGTH_SHORT).show();
                }
                else {
                    new GetItemList().execute();

                }
            }
        });

        // Button listener to show date picker dialog
        cal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);

            }

        });

        //update api call
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                value = pref.getString("Value", "");
                System.out.println("Value Submit for update: "+ ""+value);

                str = value;
                str = str.replaceAll("\\[", "").replaceAll("\\]","");
                System.out.println("Value Submit: "+ ""+str);
                List<String> alist = Arrays.asList(str.split(","));
                siz = String.valueOf(alist.size());
                selected_item.setText(siz);


                if (!Utils.isNetworkAvailable(GodownEntryActivity.this)) {
                    Toast.makeText(GodownEntryActivity.this,"No Connection Available.",Toast.LENGTH_SHORT).show();
                }
                else {
                    new GetUpdateQuantity().execute();
                }
            }
        });
    }

    private void GetSpinnerItem() {

        new GetGodownListSpinner().execute();


    }
    private void back() {
        Intent in = new Intent(GodownEntryActivity.this,HomeScreenActivity.class);
        startActivity(in);
        finish();
    }

    // Download JSON file AsyncTask(godown name)
    private class GetGodownListSpinner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        @Override
        protected Void doInBackground(Void... params) {
            // Locate the WorldPopulation Class
            godnn = new ArrayList<Godown>();
            // Create an array to populate the spinner
            godownname = new ArrayList<String>();
            // JSON file URL address
            if (logininfo != null) {
                try {
                    JSONObject jsonObj = new JSONObject(logininfo);

                    // Getting JSON Array node
                    JSONArray godown_name = jsonObj.getJSONArray("Gcode");

                    // looping through All Contacts
                    for (int i = 0; i < godown_name.length(); i++) {
                        JSONObject c = godown_name.getJSONObject(i);


                        Godown g_name = new Godown();
                        g_name.setGcode(c.optString("Gcode"));
                        g_name.setGodown(c.optString("Godown"));
                        godnn.add(g_name);

                        // Populate spinner with country names
                        godownname.add(c.optString("Godown"));

                    }
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            // Spinner adapter
            godown_name
                    .setAdapter(new ArrayAdapter<String>(GodownEntryActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            godownname));

            // Spinner on item click listener
            godown_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0,
                                           View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub
                    // Locate the textviews in activity_main.xml
                    godown_id = godnn.get(position).getGcode();
                    godown_name_title = godnn.get(position).getGodown();
                    // Set the text followed by the position
                    //Toast.makeText(GodownEntryActivity.this, godnn.get(position).getGcode(), Toast.LENGTH_SHORT).show();
                    tvTitle.setText("   "+godown_name_title);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }

    // Download JSON file AsyncTask(list view items)
    private class GetItemList extends AsyncTask<Void, Void, Void> {
        List<Item> listForSearchConcepts = new ArrayList<Item>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = ProgressDialog.show(GodownEntryActivity.this, "Collecting Godown items",
                    "Collecting Data...", true);
            progress.show();
            input_for_date = date.getText().toString();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

          // http://space7cloud.com/sgs_trader/sgs_datas.php?user_godown_id=g001&date=//for testing
            url = EndPoints.tdy_wise_update + godown_id + "&date=" + input_for_date;

            // Making a request to url and getting response
            String jsonStrr = sh.makeServiceCall(url);
            Log.e("Test url2", url);
            //Log.e("Response from url:", jsonStrr);

            if (jsonStrr != null) {
                try {
                    JSONObject jsonObjj = new JSONObject(jsonStrr);

                    // Getting JSON Array node
                    JSONArray itemss = jsonObjj.getJSONArray("items");

                    // looping through All Contacts
                    for (int i = 0; i < itemss.length(); i++) {

                        JSONObject c = itemss.getJSONObject(i);

                        Item item = new Item();
                        item.setId(c.optString("id"));
                        item.setQty(c.optString("qty"));
                        item.setName(c.optString("name"));
                        item.setItemcode(c.optString("itemcode"));
                        item.setFont(c.optString("font"));
                        listForSearchConcepts.add(item);
                        Log.i("myitemlist",listForSearchConcepts.toString());


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            progress.dismiss();
            adapter = new ItemAdapterTest(GodownEntryActivity.this, (ArrayList<Item>) listForSearchConcepts);
            list.setAdapter(adapter);
            list.setScrollingCacheEnabled(true);

            //get serach api
            search();

            String count = String.valueOf(list.getAdapter().getCount());
            not_selected_item.setText(count);

        }


    }

    //Sending data to server(all new item qty)
    private class GetUpdateQuantity extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           progress = ProgressDialog.show(GodownEntryActivity.this, "Saving Item Quantity",
                    "Updating...", true);
            progress.show();
       }

        @Override
        protected Void doInBackground(Void... params) {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("space7cloud.com")
                    .appendPath("sgs_trader")
                    .appendPath("sgs_datas.php")
                    .appendQueryParameter("save_godown_id", godown_id)
                    .appendQueryParameter("date",selected_date)
                    .appendQueryParameter("value",value)
                    .appendQueryParameter("usercode",usercode);

            String myUrl = builder.build().toString();
            System.out.println("Value Submit Url"+myUrl);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(myUrl);
            HttpResponse response = null;
            try {
                response = httpclient.execute(httpget);
                if (response.getStatusLine().getStatusCode() == 200) {
                    server_response = EntityUtils.toString(response.getEntity());
                    Log.i("Server response", server_response);
                    Log.i("Server response", myUrl);
                    System.out.println("Value server resonse"+server_response);

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

            progress.dismiss();

            Toast.makeText(GodownEntryActivity.this,server_response,Toast.LENGTH_SHORT).show();
            String s = "updated";
            if(s.equals(server_response)){
                suc_entry.setText("New Quantity Added");
                suc_entry.setVisibility(View.VISIBLE);
                suc_entry.postDelayed(new Runnable() {
                    public void run() {
                        suc_entry.setVisibility(View.GONE);
                    }
                }, 3000);


            }else{
                suc_entry.setText("Old Quantity Updated");
                suc_entry.setVisibility(View.VISIBLE);
                suc_entry.postDelayed(new Runnable() {
                    public void run() {
                        suc_entry.setVisibility(View.GONE);
                    }
                }, 3000);

            }

        }
    }

    //Search code
    private void search() {
        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                if(!af.equals("true"))
                {
                    text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                    System.out.println("Test text"+text);
                     new GetSearchItem().execute();
                     // adapter.filter(text);
                }
                else
                {
                    Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Bamini.ttf");
                    editsearch = (EditText) findViewById(R.id.search);
                    editsearch.setTypeface(myTypeface);
                    text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                    System.out.println("Test text"+text.toLowerCase(Locale.getDefault()));
                    // String test = initialize(String.valueOf(editsearch));
                    // adapter.filter(text);
                    new GetSearchItem().execute();

                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

    }

    //get search item list
    private class GetSearchItem extends AsyncTask<Void, Void, Void> {
        List<Item> listForSearchConcepts = new ArrayList<Item>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            input_for_date = date.getText().toString();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

           // http://space7cloud.com/sgs_trader/sgs_datas.php?page=search&godown_id=g001&search_date=17-1-2017&Search_value=//for testing
            url = EndPoints.search_test + godown_id + "&search_date=" + input_for_date + "&Search_value=" + Uri.encode(text); ;
            url = url.replaceAll(" ", "%20"); //for removing space
            try {
                URL sourceUrl = new URL(url);
                Log.e("Test url", sourceUrl+"");
                //
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            // Making a request to url and getting response
             String jsonStrr = sh.makeServiceCall(url);
             Log.e("Test url2", url);
            //Log.e("Response from url:", jsonStrr);

            if (jsonStrr != null) {
                try {
                    JSONObject jsonObjj = new JSONObject(jsonStrr);

                    // Getting JSON Array node
                    JSONArray itemss = jsonObjj.getJSONArray("items");

                    // looping through All Contacts
                    for (int i = 0; i < itemss.length(); i++) {

                        JSONObject c = itemss.getJSONObject(i);

                        Item item = new Item();
                        item.setId(c.optString("id"));
                        item.setQty(c.optString("qty"));
                        item.setName(c.optString("name"));
                        item.setItemcode(c.optString("itemcode"));
                        item.setFont(c.optString("font"));
                        listForSearchConcepts.add(item);
                        Log.i("myitemlist",listForSearchConcepts.toString());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

           // progress.dismiss();
            adapter = new ItemAdapterTest(GodownEntryActivity.this, (ArrayList<Item>) listForSearchConcepts);
            list.setAdapter(adapter);
            list.setScrollingCacheEnabled(true);
        }


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
            date.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append(" "));

            selected_date = date.getText().toString();

            selected_item.setText("");
            System.out.println("Value Date"+selected_date);
            Log.e("Test url", selected_date+""  );

        }
    };


}

