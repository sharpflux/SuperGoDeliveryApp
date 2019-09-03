package com.sharpflux.supergodeliveryapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.common.collect.Maps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

public class DistanceAndDuration extends AsyncTask<String, String, String> {

    private OnTaskCompleted listener;

    public DistanceAndDuration(OnTaskCompleted listener){
        this.listener=listener;
    }
    @Override
    protected String doInBackground(String... url) {

        String data = "";

        try {
            data = downloadUrl(url[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        String distance2="";
        String TimeCal="";
        try {
            JSONObject jsonObject = new JSONObject(result);

            JSONArray routes = jsonObject.getJSONArray("routes");

            JSONObject routes1 = routes.getJSONObject(0);

            JSONArray legs = routes1.getJSONArray("legs");

            JSONObject legs1 = legs.getJSONObject(0);

            JSONObject distance = legs1.getJSONObject("distance");

            JSONObject duration = legs1.getJSONObject("duration");

            Log.d("Distance", distance.getString("text"));

            Log.d("Duration", duration.getString("text"));

            listener.onTaskCompleted(distance.getString("text")+":"+duration.getString("text")+":"+duration.getString("value"));


            //distanceText = distance.getString("text");

            //durationText = duration.getString("text");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}