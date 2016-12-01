package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    public static final String LOG_TAG = Utils.class.getSimpleName();


    //Since this is the only “public”  method that the EarthquakeAsyncTask needs to interact with, make all other helper methods “private”.
    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try{
            jsonResponse = makeHttpRequest(url); //can throw an exception. see makeHttpRequest signature.
            // if it throws exception, then I have to catch it here in fetchEarthquakeData();
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);
        return earthquakes;
    }

    private static URL createUrl(String stringUrl) { //if i write throws MalformedURLException here then i can remove try,catch block below but the
        // calling function(i.e. from the place this function is being called..see above) should have a try catch block around the createUrl(); call because after
        // writing throws MalformedURLException after the method signature i can defer the error handling inside but I have to handle it at the place
        // where it is being called.

        URL url = null;
        try {
            url = new URL(stringUrl); //if i write just this line without try catch then i get a red line stating that it can throw an exception. can check its android documentation to find out the type of exception it can throw
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000); // milliseconds
            urlConnection.setConnectTimeout(15000); // milliseconds
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);//can throw IOException. will catch the exception in catch{...} here if it occurs
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {// therefore exception will be handled at the calling place(i.e. where call is made(); see above for call)
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    //InputStream is the ancestor class of all possible streams of bytes, it is not useful by itself. If you want to read binary data use InputStream.
    //InputStreamReader converts byte streams to character streams. It reads bytes and decodes them into characters using a specified charset. The charset that it uses may be specified by name or may be given explicitly, or the platform's default charset may be accepted.
    //Buffering means that data is aggregated in an array called a "buffer". BufferedReader "buffers" characters so as to provide efficient reading of characters and lines.

    private static List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {
        /*try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        List earthquakesList = new ArrayList<>();

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        try {
            //converts JSON response which is in string to JSONObject
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON); //this call can throw system exception exactly like new URL(); in createUrl() function above. these are reported from the compiler,
            // so i won't defer its handling by writing 'throws Exception' after the method signature. Better handle this type of exception here in the same code block like in createUrl() I have handled

            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            for(int i = 0; i < earthquakeArray.length(); i++ ){
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                double magnitudeValue = properties.getDouble("mag");
                DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
                String magnitude = magnitudeFormat.format(magnitudeValue);

                String location = properties.getString("place");
                String[] parts;
                String offset, priLoc;

                if(location.contains("of")) {
                    parts = location.split("of");
                    offset = parts[0] + "of";
                    priLoc = parts[1];
                }
                else {
                    offset = "Near the";
                    priLoc = location;
                }

                String time                    = properties.getString("time");
                long timeInMilliSec            = Long.parseLong(time);
                Date dateObject                = new Date(timeInMilliSec);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
                SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");

                String dateToDisplay = dateFormatter.format(dateObject);
                String timeToDisplay = timeFormatter.format(dateObject);

                String url = properties.getString("url");

                Earthquake earthquake = new Earthquake(magnitude,offset,priLoc,dateToDisplay,timeToDisplay,url);
                earthquakesList.add(earthquake);


            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return earthquakesList;
    }

}


