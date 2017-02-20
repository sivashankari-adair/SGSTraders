package com.adairtechnology.sgstraders;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.adairtechnology.sgstraders.DB.Controller;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Android-Team1 on 2/4/2017.
 */

public class PlacesList extends ActionBarActivity {
    Controller controller = new Controller(this);
    ListView ls;
    TextView infotext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placeslist);

        ls = (ListView) findViewById(R.id.placeslist);
        infotext = (TextView) findViewById(R.id.txtresulttext);

        try {
            List<HashMap<String, String>> data = controller.getAllPlace();
            if (data.size() != 0) {
                // Srno, RMCode, Fileno, Loc, FileDesc, TAGNos
             /*   SimpleAdapter adapter = new SimpleAdapter(
                        PlacesList.this, data, R.layout.rows,
                        new String[]{"id", "place", "country"}, new int[]{
                        R.id.txtplaceid, R.id.txtplacename,
                        R.id.txtcountry});
*/
                SimpleAdapter adapter = new SimpleAdapter(
                        PlacesList.this, data, R.layout.rows,
                        new String[]{"id", "name", "quantity"}, new int[]{
                        R.id.txtplaceid, R.id.txtplacename,
                        R.id.txtcountry});

                ls.setAdapter(adapter);
                String length = String.valueOf(data.size());
                infotext.setText(length + " items");
            } else {
                infotext.setText("No data in database");
            }

        } catch (Exception ex) {
            infotext.setText(ex.getMessage().toString());
        }
    }


}
