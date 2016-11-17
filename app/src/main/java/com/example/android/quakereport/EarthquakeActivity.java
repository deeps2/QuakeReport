package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

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

        // Create a new {@link ArrayAdapter} of earthquakes
        final EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);



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
}
