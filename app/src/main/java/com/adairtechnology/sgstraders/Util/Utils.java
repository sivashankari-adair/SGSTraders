package com.adairtechnology.sgstraders.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.adairtechnology.sgstraders.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {

    static String app_name = "SGS Traders";

    public static boolean validateEmailAddressFormat(String emailAddress) {

        //android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();

        String expression = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static boolean validateMobileNumberFormat(String mobileNumber) {
        //  From:http://stackoverflow.com/questions/5958665/validation-for-a-cell-number-in-android
        //matches numbers only
        String regexStr1 = "^[0-9]*$";

        //matches 10-digit numbers only
        String regexStr2 = "^[0-9]{10}$";

        //matches numbers and dashes, any order really.
        String regexStr3 = "^[0-9\\-]*$";

        //matches 9999999999, 1-999-999-9999 and 999-999-9999
        String regexStr4 = "^(1\\-)?[0-9]{3}\\-?[0-9]{3}\\-?[0-9]{4}$";

        CharSequence inputStr = mobileNumber;
        Pattern pattern = Pattern.compile(regexStr2, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static boolean validateNameFormat(String nameStr) {
        //From: http://stackoverflow.com/questions/15805555/java-regex-to-validate-full-name-allow-only-spaces-and-letters
        //Allows Alphabets, Dots, Spaces
        // This will also ensure DOT never comes at the start of the name.
        String expression = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
        CharSequence inputStr = nameStr;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static String getFormattedDateFromTimestamp(long timestampInMilliSeconds) {
        Date date = new Date();
        date.setTime(timestampInMilliSeconds);
        // String formattedDate=new SimpleDateFormat("MMM d yyyy, hh:mm a").format(date);
        String formattedDate = new SimpleDateFormat("MMM d yyyy").format(date);
        return formattedDate;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void replaceFragmentwithoutBack(AppCompatActivity c, Fragment fragment) {
        FragmentManager fragmentManager = c.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    public static void replaceFragment(AppCompatActivity c, Fragment fragment) {
        FragmentManager fragmentManager = c.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void replaceNewFragment(FragmentActivity c, Fragment fragment) {
        FragmentManager fragmentManager = c.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void addFragment(AppCompatActivity c, Fragment fragment) {
        FragmentManager fragmentManager = c.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void addNewFragment(FragmentActivity c, Fragment fragment) {
        FragmentManager fragmentManager = c.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void addNewWithDefaultFragment(FragmentActivity c, Fragment fragment) {
        FragmentManager fragmentManager = c.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void setSharedPrefs(Context c, String key, String value) {
        if (c != null) {
            SharedPreferences.Editor editor = c.getSharedPreferences(app_name,
                    Context.MODE_PRIVATE).edit();
            editor.putString(key, value);
            editor.commit();
        }

    }

    public static void setSharedPrefs(Context c, String key, int value) {
        if (c != null) {
            SharedPreferences.Editor editor = c.getSharedPreferences(app_name,
                    Context.MODE_PRIVATE).edit();
            editor.putInt(key, value);
            editor.commit();
        }

    }

    public static String getSharedPrefs(Context c, String key,
                                        String default_value) {
        if (c != null) {
            SharedPreferences prefs = c.getSharedPreferences(app_name,
                    Context.MODE_PRIVATE);
            return prefs.getString(key, default_value);
        } else {
            return default_value;
        }
    }

    public static int getSharedPrefs(Context c, String key, int default_value) {
        if (c != null) {
            SharedPreferences prefs = c.getSharedPreferences(app_name,
                    Context.MODE_PRIVATE);
            return prefs.getInt(key, default_value);
        } else {
            return default_value;
        }

    }

}
