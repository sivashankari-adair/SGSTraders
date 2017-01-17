package com.adairtechnology.sgstraders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Android-Team1 on 1/5/2017.
 */


public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView img1,img2,img3,img4,img5,img6;
    Toolbar toolbar;
    ImageView image;
    public static TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title1);
        image = (ImageView)findViewById(R.id.img);
        image.setImageDrawable(getResources().getDrawable(R.drawable.home));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tvTitle.setText("   Home");



        img1 = (ImageView) findViewById(R.id.image1);
        img2 = (ImageView) findViewById(R.id.image2);
        img3 = (ImageView) findViewById(R.id.image3);
        img4 = (ImageView) findViewById(R.id.image4);
        img5 = (ImageView) findViewById(R.id.image5);
        img6 = (ImageView) findViewById(R.id.image6);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);
    }
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.image1:
                Intent in=new Intent(HomeScreenActivity.this,GodownEntryActivity.class);
                startActivity(in);
                finish();
                break;

            case R.id.image2:
                Toast.makeText(HomeScreenActivity.this,"Under Construction",Toast.LENGTH_SHORT).show();
                break;

            case R.id.image3:
                Toast.makeText(HomeScreenActivity.this,"Under Construction",Toast.LENGTH_SHORT).show();
                break;

            case R.id.image4:
                Toast.makeText(HomeScreenActivity.this,"Under Construction",Toast.LENGTH_SHORT).show();
                break;

            case R.id.image5:
                Toast.makeText(HomeScreenActivity.this,"Under Construction",Toast.LENGTH_SHORT).show();
                break;

            case R.id.image6:
             //   Toast.makeText(HomeScreenActivity.this,"Under Construction",Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor edit_pref = getSharedPreferences("MYPREF", MODE_PRIVATE).edit();
                edit_pref.clear();
                edit_pref.commit();

                restartActivity();
                break;

        }


    }

    private void restartActivity() {
        finish();
        Intent in = new Intent(HomeScreenActivity.this,LoginActivity.class);
        startActivity(in);


    }
}