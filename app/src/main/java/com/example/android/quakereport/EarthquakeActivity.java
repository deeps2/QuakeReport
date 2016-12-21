package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final String SAMPLE_JSON_RESPONSE_3 = "http://earthquake.usgs.gov/fdsnws/event/1/query";//?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    private EarthquakeAdapter adapter;

    private TextView mEmptyStateTextView;

    //Constant value for the earthquake loader ID. We can choose any integer.
    // This really only comes into play if you're using multiple loaders.
    private static final int EARTHQUAKE_LOADER_ID = 1;


    // we need to override the three methods specified in the LoaderCallbacks interface. We need onCreateLoader(), for when the LoaderManager has
    // determined that the loader with our specified ID isn't running, so we should create a new one.
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );


        Uri baseUri = Uri.parse(SAMPLE_JSON_RESPONSE_3);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());




       // return new EarthquakeLoader(this, SAMPLE_JSON_RESPONSE_3);
    }


    //We need onLoadFinished(), where we'll do exactly what we did in onPostExecute(), and use the
    // earthquake data to update our UI - by updating the dataset in the adapter.
    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        // Clear the adapter of previous earthquake data
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter.addAll(earthquakes);
        }

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);


        // Set empty state text to display "No earthquakes found."
        //It’s okay if this text is set every time the loader finishes because it’s not too expensive of an operation.
        mEmptyStateTextView.setText(R.string.no_earthquakes);
    }


    // we need onLoaderReset(), we're we're being informed that the data from our loader is no longer valid.
    // This isn't actually a case that's going to come up with our simple loader, but the correct thing to do
    // is to remove all the earthquake data from our UI by clearing out the adapter’s data set.
    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);



        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            /*
            to retrieve an earthquake, we need to get the loader manager and tell the loader manager to
            initialize the loader with the specified ID, the second argument allows us to pass a bundle
            of additional information, which we'll skip. The third argument is what object should receive
            the LoaderCallbacks (and therefore, the data when the load is complete!) - which will be this
            activity. This code goes inside the onCreate() method of the EarthquakeActivity, so that the
            loader can be initialized as soon as the app opens.
            */

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else {
            // Otherwise, display error. First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an EMPTY LIST of earthquakes as input -> M.IMP
        //"new" ArrayList<Earthquake> because earlier we used to get the list by
            // doing --> ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();
            // now this line is used inside doInBackground of AsyncTask.
            // Inside QueryUtils.extractEarthquakes() you can see at the top: ArrayList<Earthquake> earthquakes = new ArrayList<>();
            // So from there this 'new'ly created ArrayList was being returned

        //even before that we used: EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);
            // where ArrayList<String> earthquakes = "new" ArrayList<>(); and earthquakes.add();{... like that many hardcoded data}
        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>()); //1st constructor

        //2nd method//adapter = new EarthquakeAdapter(this); //2nd constructor
        //3rd method//adapter = new EarthquakeAdapter(this,-1);//or 0,1 even 100 also. Why???. 3rd constructor



        // Set the adapter on the {@link ListView so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
        earthquakeListView.setEmptyView(mEmptyStateTextView);



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, Settings.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
