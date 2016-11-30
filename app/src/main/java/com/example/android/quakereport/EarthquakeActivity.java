package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    private static final String SAMPLE_JSON_RESPONSE_3 = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    //private static final String SAMPLE_JSON_RESPONSE_4 = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";
    private EarthquakeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

        // Create a fake list of earthquake locations.
        //   ArrayList<Earthquake> earthquakes = new ArrayList<>();
        /*
        earthquakes.add(new Earthquake("7.2", "San Francisco", "Feb 2 2016"));
        earthquakes.add(new Earthquake("7.2", "London", "Feb 2 2016"));
        earthquakes.add(new Earthquake("7.2", "Tokyo", "Feb 2 2016"));
        earthquakes.add(new Earthquake("7.2", "Mexico City", "Feb 2 2016"));
        earthquakes.add(new Earthquake("7.2", "Moscow", "Feb 2 2016"));
        earthquakes.add(new Earthquake("7.2", "Rio de Janeiro", "Feb 2 2016"));
        earthquakes.add(new Earthquake("7.2", "Paris", "Feb 2 2016")); */

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an EMPTY LIST of earthquakes as input -> M.IMP
        //new ArrayList<Earthquake> because earlier we used to get the list by doing --> ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes(); (see above). now this line is used inside doInBackground of AsyncTask
        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(SAMPLE_JSON_RESPONSE_3);
        //task.execute(SAMPLE_JSON_RESPONSE_4);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }


    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        protected List<Earthquake> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Earthquake> result = Utils.fetchEarthquakeData(urls[0]);
            return result;
        }
        
        protected void onPostExecute(List<Earthquake> result) {
            if (result == null) {
                return;
            }
            // Clear the adapter of previous earthquake data
            adapter.clear();
            adapter.addAll(result);  //IMP. instead of calling updateUI method(which is generally inside AsyncTask and it refers to the views ID and
            // then setText; we use adapter.add(result) after adapter.clear()...this will clear the empty adapter and update it with the new ones.
        }
    }
}
