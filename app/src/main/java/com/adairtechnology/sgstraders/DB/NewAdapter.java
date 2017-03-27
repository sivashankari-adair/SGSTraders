package com.adairtechnology.sgstraders.DB;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adairtechnology.sgstraders.Models.Item;
import com.adairtechnology.sgstraders.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-Team1 on 2/24/2017.
 */

public class NewAdapter extends ArrayAdapter<Item> {
    customButtonListener customListner;
    public static ArrayList<String> value_list = new ArrayList<>();
    public static ArrayList<String> value_id= new ArrayList<>();
    public static ArrayList<String> value_qty= new ArrayList<>();
    public static ArrayList<String> value_item= new ArrayList<>();
    public String[] text;
    Context mContext;
    LayoutInflater inflater;
    List<Item> itemlist;
    ArrayList<Item> arraylist;
    ArrayList test;
    Typeface type;
    final ArrayList<String> list = new ArrayList<String>();
    Controller controller = new Controller(getContext());
    public static String pas_val;
    public static String pas_qty;
    public static String pas_item;

    public NewAdapter(Context context, List<Item> itemlist) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.rows, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView
                    .findViewById(R.id.txtplacename);
            viewHolder.quantity = (TextView) convertView
                    .findViewById(R.id.txtplaceid);
            viewHolder.detailid = (TextView) convertView
                    .findViewById(R.id.txtcountry);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.text.setText(itemlist.get(position).id);
        viewHolder.quantity.setText(itemlist.get(position).qty);
        viewHolder.detailid.setText(itemlist.get(position).itemcode);

        String ts = String.valueOf((itemlist.get(position).id) + "_" + itemlist.get(position).qty);
        value_list.add(ts);
        System.out.println(value_list);
        System.out.println(value_list.size());

        return convertView;

    }
    public class ViewHolder {
        TextView text;
        TextView quantity;
        TextView detailid;

    }
}

