package com.adairtechnology.sgstraders.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.adairtechnology.sgstraders.Models.Item;
import com.adairtechnology.sgstraders.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Android-Team1 on 1/4/2017.
 */

public class ItemAdapter extends ArrayAdapter<Item> {
    customButtonListener customListner;
    static ArrayList<String> value = new ArrayList<>();
  //  public static StringBuilder sb;
    StringBuilder sb;
    private String logininfo;

    public String[] text;
  //  public static HashMap<Integer,String> myList=new HashMap<Integer,String>();
   // private String[] mDataset;


    Context mContext;
    LayoutInflater inflater;
    List<Item> itemlist ;
    ArrayList<Item> arraylist;
    ArrayList test;
    Typeface type;
    final ArrayList<String> list = new ArrayList<String>();


    public ItemAdapter(Context context, List<Item> itemlist) {
        super(context,0);
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


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final Item item = getItem(position);

        View view = null;
        convertView = null;

        SharedPreferences prefs = getContext().getSharedPreferences("MYPREFF", MODE_PRIVATE);
        logininfo = prefs.getString("loginInfo", null);

        System.out.println("godown_entry_activity"+logininfo);
        if (logininfo != null) {
            try {
                JSONObject jsonObj = new JSONObject(logininfo);
                String item_option = jsonObj.getString("isel");
                String item_font = jsonObj.getString("af");

                System.out.println("godown_entry_activity"+ "" +item_option + "" +item_font);


                if(!item_option.equals("name") && item_font.equals("false")){
                   //--------------------//
                    System.out.println("godown_entry_activity"+ "print all item code with quantity");
                   //---------------------//

                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.godown_list_itemname_activity, parent, false);

                        viewHolder = new ViewHolder();
                        viewHolder.text = (TextView) convertView
                                .findViewById(R.id.list_label_name);
                        viewHolder.edit = (EditText) convertView
                                .findViewById(R.id.qty_editText);
                        viewHolder.text_id = (TextView) convertView
                                .findViewById(R.id.list_label_qty);
                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = (ViewHolder) convertView.getTag();
                    }

                    // Populate the data into the template view using the data object
                    viewHolder.text.setText(itemlist.get(position).getId());
                    viewHolder.edit.setText(itemlist.get(position).getQty());
                    viewHolder.text_id.setText(itemlist.get(position).getItemcode());


                    viewHolder.edit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            sb = new StringBuilder(s.length());
                            sb.append(s);


                            viewHolder.edit.setTextColor(Color.RED);
                            String ts =String.valueOf(viewHolder.text.getText()) +"_"+sb.toString();
                            value.add(ts);

                            System.out.println("array list size1"+value.size());
                            System.out.println("array list size1"+value);

                            SharedPreferences pref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("Value", String.valueOf(value));
                            editor.clear();
                            editor.commit();

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    value.clear();
                    System.out.println("array list size"+ value);

                    return convertView;


                }
                else if(item_option.equals("name") && item_font.equals("false")){

                    //========================//
                    System.out.println("godown_entry_activity"+ " , Item name equal to name ");
                    //========================//
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.godown_list_item_activity, parent, false);

                        viewHolder = new ViewHolder();
                        viewHolder.text = (TextView) convertView
                                .findViewById(R.id.list_label_name);
                        viewHolder.edit = (EditText) convertView
                                .findViewById(R.id.qty_editText);
                        viewHolder.text_id = (TextView) convertView
                                .findViewById(R.id.list_label_qty);
                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = (ViewHolder) convertView.getTag();
                    }
                    // Populate the data into the template view using the data object
                    viewHolder.text.setText(itemlist.get(position).getName());
                    viewHolder.edit.setText(itemlist.get(position).getQty());
                    viewHolder.text_id.setText(itemlist.get(position).getId());

                    viewHolder.edit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            sb = new StringBuilder(s.length());
                            sb.append(s);

                            list.add(sb.toString());
                            System.out.println("array list size2"+list);


                            viewHolder.edit.setTextColor(Color.RED);
                            String ts =String.valueOf(viewHolder.text_id.getText()) +"_"+sb.toString();
                            value.add(ts);
                            System.out.println("array list size2"+ value.size());
                            System.out.println("array list size2"+value);

                            SharedPreferences pref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("Value", String.valueOf(value));
                            editor.clear();
                            editor.commit();

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    value.clear();
                    System.out.println("array list size2"+value);


                    return convertView;

                }

                else if(item_option.equals("name") && item_font.equals("true")){

                    //========================//
                    System.out.println("godown_entry_activity"+ "Both true , Item name equal to name and font equal to true then display tamil font");
                    //========================//
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.godown_list_item_font, parent, false);

                        viewHolder = new ViewHolder();
                        viewHolder.text = (TextView) convertView
                                .findViewById(R.id.list_label_name);
                        viewHolder.edit = (EditText) convertView
                                .findViewById(R.id.qty_editText);
                        viewHolder.text_id = (TextView) convertView
                                .findViewById(R.id.list_label_qty);
                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = (ViewHolder) convertView.getTag();
                    }

                     viewHolder.text.setText(itemlist.get(position).getFont());
                     viewHolder.edit.setText(itemlist.get(position).getQty());
                     viewHolder.text_id.setText(itemlist.get(position).getId());


                    viewHolder.edit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            sb = new StringBuilder(s.length());
                            sb.append(s);
                            viewHolder.edit.setTextColor(Color.RED);
                            String ts =String.valueOf(viewHolder.text_id.getText()) +"_"+sb.toString();
                            value.add(ts);
                            System.out.println("array list size3"+ value.size());
                            System.out.println("array list size3"+value);

                            SharedPreferences pref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("Value", String.valueOf(value));
                            editor.clear();
                            editor.commit();

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    value.clear();
                    System.out.println("array list size3"+value);


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
        EditText edit;
        TextView text_id;
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
