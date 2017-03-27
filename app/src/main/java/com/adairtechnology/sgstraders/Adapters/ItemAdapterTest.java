
package com.adairtechnology.sgstraders.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adairtechnology.sgstraders.DB.Controller;
import com.adairtechnology.sgstraders.DB.ItemController;
import com.adairtechnology.sgstraders.GodownEntryActivity;
import com.adairtechnology.sgstraders.GodownWithOUtInternetActivity;
import com.adairtechnology.sgstraders.Models.Item;
import com.adairtechnology.sgstraders.R;
import com.adairtechnology.sgstraders.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Android-Team1 on 1/17/2017.
 */

public class ItemAdapterTest extends ArrayAdapter<Item> {
    customButtonListener customListner;
    public static ArrayList<String> value_idd= new ArrayList<>();
    public static ArrayList<String> value_qtyd= new ArrayList<>();
    int count;
    ArrayList<String> value_qty_test= new ArrayList<>();
    ArrayList<String> value = new ArrayList<>();
    String value_changed_item_count;
    ArrayList<String> item_qty_for_all = new ArrayList<>();
    ArrayList value_count = new ArrayList<>();
    //  public static StringBuilder sb;
    StringBuilder sb;
    private String logininfo;
    public String[] text;
    Context mContext;
    LayoutInflater inflater;
    List<Item> itemlist;
    ArrayList<Item> arraylist;
    ArrayList test;
    Typeface type;
    ArrayList<String> list_caption_count = new ArrayList<String>();
    Controller controller = new Controller(getContext());
    ArrayList<String> value_id= new ArrayList<>();
    ArrayList<String> value_qty= new ArrayList<>();
    ArrayList<String> value_item= new ArrayList<>();


    public ItemAdapterTest(Context context, List<Item> itemlist) {
        super(context, 0);
        mContext = context;
        this.itemlist = itemlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Item>();
        this.arraylist.addAll(itemlist);

    }

    @Override
    public int getCount() {

        return itemlist.size();
    }

    @Override
    public Item getItem(int position) {
        return itemlist.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public interface customButtonListener {
        public void onButtonClickListner(int position, String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    private Context context;
    private ArrayList<String> data = new ArrayList<String>();
   // public static ViewHolder viewHolder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final Item item = getItem(position);

        /* View view = null;
        convertView = null;*/

        SharedPreferences prefs = getContext().getSharedPreferences("MYPREFF", MODE_PRIVATE);
        logininfo = prefs.getString("loginInfo", null);
        System.out.println("godown_entry_activity" + logininfo);

        if (logininfo != null) {
            try {
                JSONObject jsonObj = new JSONObject(logininfo);
                String item_option = jsonObj.getString("isel");
                String item_font = jsonObj.getString("af");

                System.out.println("godown_entry_activity" + "" + item_option + "" + item_font);


                if (!item_option.equals("name") && item_font.equals("false")) {
                    //--------------------//
                    System.out.println("godown_entry_activity" + "print all item code with quantity");
                    //---------------------//

                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.godown_list_itemname_activity, parent, false);

                        viewHolder = new ViewHolder();
                        viewHolder.text = (TextView) convertView
                                .findViewById(R.id.list_label_name);
                        viewHolder.caption = (EditText) convertView
                                .findViewById(R.id.qty_editText);
                        viewHolder.text_id = (TextView) convertView
                                .findViewById(R.id.list_label_qty);


                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = (ViewHolder) convertView.getTag();
                    }

                    // Populate the data into the template view using the data object
                    viewHolder.text.setText(itemlist.get(position).id);
                    viewHolder.caption.setText(itemlist.get(position).qty);
                    viewHolder.text_id.setText(itemlist.get(position).itemcode);
                    viewHolder.caption.setTag(position);



                    viewHolder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                final int position = (Integer) v.getTag();
                                final EditText Caption = (EditText) v;
                                itemlist.get(position).qty = Caption.getText().toString();
                                System.out.println(arraylist.get(position).qty);
                                viewHolder.caption.setTextColor(Color.RED);

                                if(  itemlist.get(position).qty.equals("")){
                                    System.out.println("Dont take that empty space");
                                }
                                else {
                                    String ts = String.valueOf((itemlist.get(position).id) + "_" + itemlist.get(position).qty);
                                    value.add(ts);
                                    System.out.println(value);
                                    System.out.println(value.size());

                                    String pas_val = (itemlist.get(position).id);
                                    value_id.add(pas_val);
                                    ItemController.value_id(String.valueOf(value_id));
                                    value_idd.add(pas_val);

                                    String pas_qty = itemlist.get(position).qty;
                                    value_qty.add(pas_qty);
                                    ItemController.value_qty(String.valueOf(value_qty));
                                    value_qtyd.add(pas_qty);


                                    if (!Utils.isNetworkAvailable(mContext)) {

                                        String tws = String.valueOf(value.size());
                                       // GodownWithOUtInternetActivity.selected_item.setText(tws+"");
                                    }
                                    else {
                                        String t = value.toString();
                                        GodownEntryActivity.testsearch(t);

                                      //  String tws = String.valueOf(value.size());
                                       // GodownEntryActivity.selected_item.setText(tws+"");
                                    }

                                }

                                SharedPreferences pref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("Value", String.valueOf(value));
                                editor.putString("QtyValue", String.valueOf(value_qty));
                                editor.putString("IdValue", String.valueOf(value_id));
                                editor.clear();
                                editor.commit();


                            }


                        }

                    });
                    item_qty_for_all.add(itemlist.get(position).qty);
                    System.out.println("Value Check for clear" + item_qty_for_all);

                    String test = itemlist.get(position).qty;
                    System.out.println("all values in edit text :" +test);

                    // viewHolder.caption.setTextColor(Color.RED);
                    value_idd.clear();
                    value_qtyd.clear();


                    return convertView;


                } else if (item_option.equals("name") && item_font.equals("false")) {

                    //========================//
                    System.out.println("godown_entry_activity" + " , Item name equal to name ");
                    //========================//
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.godown_list_item_activity, parent, false);

                        viewHolder = new ViewHolder();
                        viewHolder.text = (TextView) convertView
                                .findViewById(R.id.list_label_name);
                        viewHolder.caption = (EditText) convertView
                                .findViewById(R.id.qty_editText);
                        viewHolder.text_id = (TextView) convertView
                                .findViewById(R.id.list_label_qty);



                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = (ViewHolder) convertView.getTag();
                    }
                    // Populate the data into the template view using the data object

                    viewHolder.text.setText(itemlist.get(position).name);
                    viewHolder.caption.setText(itemlist.get(position).qty);
                    viewHolder.text_id.setText(itemlist.get(position).id);
                    viewHolder.caption.setTag(position);


                    viewHolder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                final int position = (Integer) v.getTag();
                                final EditText Caption = (EditText) v;
                                itemlist.get(position).qty = Caption.getText().toString();
                                System.out.println(arraylist.get(position).qty);
                                viewHolder.caption.setTextColor(Color.RED);



                                if(  itemlist.get(position).qty.equals("")){
                                    System.out.println("test");
                                }
                                else {
                                    String ts = String.valueOf((itemlist.get(position).id) + "_" + itemlist.get(position).qty);
                                    value.add(ts);
                                    System.out.println(value);
                                    System.out.println(value.size());

                                    String pas_val = (itemlist.get(position).id);
                                    value_id.add(pas_val);
                                    ItemController.value_id(String.valueOf(value_id));
                                    value_idd.add(pas_val);

                                    String pas_qty = itemlist.get(position).qty;
                                    value_qty.add(pas_qty);
                                    ItemController.value_qty(String.valueOf(value_qty));
                                    value_qtyd.add(pas_qty);

                                    if (!Utils.isNetworkAvailable(mContext)) {

                                        String tws = String.valueOf(value.size());
                                       // GodownWithOUtInternetActivity.selected_item.setText(tws+"");
                                    }
                                    else {
                                        String t = value.toString();
                                        GodownEntryActivity.testsearch(t);

                                     //   String tws = String.valueOf(value.size());
                                     //   GodownEntryActivity.selected_item.setText(tws+"");
                                    }


                                }


                                SharedPreferences pref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("Value", String.valueOf(value));
                                editor.putString("QtyValue", String.valueOf(value_qty));
                                editor.putString("IdValue", String.valueOf(value_id));
                                editor.clear();
                                editor.commit();


                            }




                        }

                    });
                    value_idd.clear();
                    value_qtyd.clear();

                    item_qty_for_all.add(itemlist.get(position).qty);
                    System.out.println("Value Check for clear" + item_qty_for_all);

                    String test = itemlist.get(position).qty;
                    System.out.println("all values in edit text :" +test);


                    return convertView;

                } else if (item_option.equals("name") && item_font.equals("true")) {

                    //========================//
                    System.out.println("godown_entry_activity" + "Both true , Item name equal to name and font equal to true then display tamil font");
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.godown_list_item_font, parent, false);

                        viewHolder = new ViewHolder();
                        viewHolder.text = (TextView) convertView
                                .findViewById(R.id.list_label_name);
                        viewHolder.caption = (EditText) convertView
                                .findViewById(R.id.qty_editText);



                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = (ViewHolder) convertView.getTag();
                    }


                    viewHolder.text.setText(itemlist.get(position).font);
                    viewHolder.caption.setText(itemlist.get(position).qty);
                    viewHolder.text_id.setText(itemlist.get(position).id);
                    viewHolder.caption.setTag(position);


                    viewHolder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                final int position = (Integer) v.getTag();
                                final EditText Caption = (EditText) v;

                                    itemlist.get(position).qty = Caption.getText().toString();
                                    System.out.println(arraylist.get(position).qty);
                                    viewHolder.caption.setTextColor(Color.RED);


                                if(  itemlist.get(position).qty.equals("")){
                                   System.out.println("test");
                                }
                                else {
                                String ts = String.valueOf((itemlist.get(position).id) + "_" + itemlist.get(position).qty);
                                value.add(ts);
                                System.out.println(value);
                                System.out.println(value.size());

                                    String tws = String.valueOf(value_count.size());
                                    //GodownEntryActivity.selected_item.setText(tws+"");

                                    String pas_val = (itemlist.get(position).id);
                                    value_id.add(pas_val);
                                    ItemController.value_id(String.valueOf(value_id));
                                    value_idd.add(pas_val);

                                    String pas_qty = itemlist.get(position).qty;
                                    value_qty.add(pas_qty);
                                    ItemController.value_qty(String.valueOf(value_qty));
                                    value_qtyd.add(pas_qty);

                                    if (!Utils.isNetworkAvailable(mContext)) {

                                        String twss = String.valueOf(value.size());
                                    //    GodownWithOUtInternetActivity.selected_item.setText(twss+"");
                                    }
                                    else {
                                        String t = value.toString();
                                        GodownEntryActivity.testsearch(t);

                                     //   String twss = String.valueOf(value.size());
                                      //  GodownEntryActivity.selected_item.setText(twss+"");
                                    }


                                }


                                SharedPreferences pref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("Value", String.valueOf(value));
                                editor.putString("QtyValue", String.valueOf(value_qty));
                                editor.putString("IdValue", String.valueOf(value_id));
                                editor.clear();
                                editor.commit();


                            }

                        }


                    });
                    item_qty_for_all.add(itemlist.get(position).qty);
                    System.out.println("Value Check for clear" + item_qty_for_all);

                    String test = itemlist.get(position).qty;
                    System.out.println("all values in edit text :" +test);

                    value_idd.clear();
                    value_qtyd.clear();



                    return convertView;


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return convertView;
    }

    public class ViewHolder {
        TextView text;
        EditText caption;
        public TextView text_id;
        Button clear;
    }


    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        itemlist.clear();
        if (charText.length() == 0) {
            itemlist.addAll(arraylist);
            System.out.print("response search nothing"+itemlist);

        } else {
            for (Item postDetail : arraylist) {
                System.out.print("response search nothing"+arraylist);
                if (charText.length() != 0 && postDetail.getName().toLowerCase(Locale.getDefault()).contains(charText)
                        || postDetail.getFont().toLowerCase(Locale.getDefault()).contains(charText)
                        || postDetail.getItemcode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    itemlist.add(postDetail);
                }
            }
        }
        notifyDataSetChanged();
    }

}
