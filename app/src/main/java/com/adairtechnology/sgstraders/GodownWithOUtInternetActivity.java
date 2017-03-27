package com.adairtechnology.sgstraders;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

import com.adairtechnology.sgstraders.Adapters.ItemAdapterTest;
import com.adairtechnology.sgstraders.DB.Controller;
import com.adairtechnology.sgstraders.DB.ItemController;
import com.adairtechnology.sgstraders.Models.Godown;
import com.adairtechnology.sgstraders.Models.Item;
import com.adairtechnology.sgstraders.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Android-Team1 on 3/24/2017.
 */

public class GodownWithOUtInternetActivity extends AppCompatActivity{
    Toolbar toolbar;
    EditText editsearch;
    public static TextView tvTitle;
    ImageView image;
    TextView selected_item_new ;
    private ListView list;
    ItemAdapterTest adapter;
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
    public static String text;
    int cnt;
    String input_for_date,value,server_response,update_date,parameters,
            logininfo,godown_name_title,af,str,siz,count_val,count_before_update,val_qty,val_id,send_id,send_qty;
    public static String usercode;
    public static String godown_id;
    static final int DATE_PICKER_ID = 1111;
    private TextView proceed,clear,not_selected_item;
    //public static TextView selected_item ;
    TextView selected_item ;
    private TextView suc_entry;
    ProgressDialog progress;
    ArrayList<Godown> godnn;
    ArrayList<String> godownname;
    private Spinner godown_name;
    private Typeface type;
    private static String selected_date;
    public static String final_date;
    public static String currentDateandTime;
    ArrayList<Item> godownItemList = new ArrayList<Item>();
    ItemController databaseHelper = new ItemController(GodownWithOUtInternetActivity.this);
    Button save_later;
    String ip;
    private ArrayList<Item> data;
    public static ArrayList<String> iID=new ArrayList<>();
    public static ArrayList<String> iQTY=new ArrayList<>();
    public static List<String> alistT,alistTt;
    ArrayList<String> val_te = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.godown_entry_activity);
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

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        currentDateandTime = sdf.format(new Date());
        System.out.println("test time : " +currentDateandTime);

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

        GetSpinnerItem();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetItemList().execute();
            }
        });
        cal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);
                //val_test.clear();
            }

        });
        search();

      //update api call
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                value = pref.getString("Value", "");
                val_id = pref.getString("IdValue", "");
                val_qty = pref.getString("QtyValue", "");
                System.out.println("Value Submit for update: "+ ""+value);

                send_id = val_id;
                send_id = send_id.replaceAll("\\[", "").replaceAll("\\]","");
                String[] array = send_id.split(",");
                ArrayList<String> dfd = new ArrayList<>(Arrays.asList(array));
                iID = dfd;
                cnt = iID.size();
                System.out.println("new value: "+ ""+iID);

                send_qty = val_qty;
                send_qty = send_qty.replaceAll("\\[", "").replaceAll("\\]","");
                String[] arrayY = send_qty.split(",");
                ArrayList<String> ggs= new ArrayList<>(Arrays.asList(arrayY));
                iQTY = ggs;
                System.out.println("new value: "+ ""+iQTY);

                str = value;
                str = str.replaceAll("\\[", "").replaceAll("\\]","");
                System.out.println("Value Submit: "+ ""+str);
                List<String> alist = Arrays.asList(str.split(","));
                siz = String.valueOf(alist.size());

                databaseHelper.SubmitAllValues();
                Toast.makeText(GodownWithOUtInternetActivity.this,"Locally updated",Toast.LENGTH_SHORT).show();



            }
        });

    }
    //getting godown name
    private void GetSpinnerItem() {

        new GetGodownListSpinner().execute();

    }

    //move back
    private void back() {
        Intent in = new Intent(GodownWithOUtInternetActivity.this,HomeScreenActivity.class);
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
            godnn = new ArrayList<Godown>();
            godownname = new ArrayList<String>();
            if (logininfo != null) {
                try {
                    JSONObject jsonObj = new JSONObject(logininfo);
                    JSONArray godown_name = jsonObj.getJSONArray("Gcode");

                    // looping through All items
                    for (int i = 0; i < godown_name.length(); i++) {
                        JSONObject c = godown_name.getJSONObject(i);
                        Godown g_name = new Godown();
                        g_name.setGcode(c.optString("Gcode"));
                        g_name.setGodown(c.optString("Godown"));
                        godnn.add(g_name);

                        // Populate spinner with godown names
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
                    .setAdapter(new ArrayAdapter<String>(GodownWithOUtInternetActivity.this,
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
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(GodownWithOUtInternetActivity.this, "Collecting Godown items",
                    "Collecting Data...", true);
            progress.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            godownItemList = databaseHelper.getAllGodownItems();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            progress.dismiss();
            adapter = new ItemAdapterTest(GodownWithOUtInternetActivity.this, godownItemList);
            list.setAdapter(adapter);
            list.setScrollingCacheEnabled(true);

            String count = String.valueOf(list.getAdapter().getCount());
            not_selected_item.setText(count);

            selected_item.setText("0");
            System.out.println("Value for qty item"+count_before_update);

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
                    .append(""));

            selected_date = date.getText().toString();
            final_date = selected_date;

            selected_item.setText("");
            System.out.println("Value Date"+selected_date);
            Log.e("Test url", selected_date+""  );

        }
    };

    //Search code
    private void search() {
        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
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
                if (!af.equals("true")) {
                    text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                    System.out.println("Test text" + text);
                    new GetSearch().execute();
                } else {
                    //Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Bamini.ttf");
                    editsearch = (EditText) findViewById(R.id.search);
                    //editsearch.setTypeface(myTypeface);
                    text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                    System.out.println("Test text" + text.toLowerCase(Locale.getDefault()));
                    new GetSearch().execute();

                }
            }
        });

    }

    //Sending data to server(all new item qty)
    private class GetSearch extends AsyncTask<Void, Void, Void>  {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            godownItemList = databaseHelper.getSearchItem();
            System.out.println("double value"+ godownItemList);
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            adapter = new ItemAdapterTest(GodownWithOUtInternetActivity.this, godownItemList);
            list.setAdapter(adapter);
            list.setScrollingCacheEnabled(true);
            test();

        }
    }

    public void test() {

        databaseHelper.getItemId();

    }
}
