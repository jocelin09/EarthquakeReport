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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jocelinthomas on 11/11/18.
 */

public class Utils {
    private static final String LOG_TAG = EarthquakeActivity.class.getSimpleName();

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }


    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
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
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
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
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
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


    /**
     * Return a list of {@link EarthQuake} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<EarthQuake> extractFeatureFromJson(String earthquakeJSON){
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        List<EarthQuake> earthQuakeList = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < earthquakeArray.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");

                EarthQuake earthquake = new EarthQuake(magnitude, location, time, url);

                earthQuakeList.add(earthquake);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return earthQuakeList;
    }
    /**
     * Query the USGS dataset and return a list of {@link EarthQuake} objects.
     */
    public static List<EarthQuake> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<EarthQuake> earthQuakesList = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return earthQuakesList;
    }
}
