package com.sharpflux.supergodeliveryapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayPalActivity extends AppCompatActivity implements OnTaskCompleted {
    int userId;
    String userid;
    Date date1;
    ExpandableRelativeLayout expandableLayout1;
    TextView paytv,tvwallet,tvcod,tvcreditanddebitcard,distance;
    private static String DistanceAndDuration,Distance,Duration,TotalSecond;
    RadioButton cod,candd;
    ProgressDialog progressDialog;
    LinearLayout Payonline2;
    DatabaseHelper myDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);
        Payonline2=findViewById(R.id.Payonline2);
        paytv = findViewById(R.id.paytv);
       // tvwallet=findViewById(R.id.wallet);
        //distance=findViewById(R.id.distance);
        tvcod=findViewById(R.id.cashOndelivery);
        myDatabase = new DatabaseHelper(getBaseContext());

        User user = SharedPrefManager.getInstance(PayPalActivity.this).getUser();

        //customerName.setText("Hey "+user.getUsername()+"!");
        userId=user.getId();

        userid=String.valueOf(userId);

        Payonline2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iin = getIntent();
                Bundle b = iin.getExtras();

                Intent intent = new Intent(PayPalActivity.this,PaymentActivity.class);
                intent.putExtra("amount",b.getString("totalCharges"));

                startActivity(intent);
            }
        });

        tvcod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(PayPalActivity.this,OrderSuccessfullyPlaced.class);
                //startActivity(intent);
                progressDialog = new ProgressDialog(PayPalActivity.this);
                progressDialog.setMessage("Saving your data..."); // Setting Message
                //progressDialog.setTitle("ProgressDialog"); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(4000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }).start();
                saveOrderDetails();
            }

        });
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if(b!=null) {

            String fromLat=b.getString("FromLat");
            String fromLang=b.getString("FromLong");

            String ToLat=b.getString("ToLat");
            String ToLong=b.getString("ToLong");


            String url = getRequestUrl(fromLat + "," +fromLang,ToLat + "," + ToLong);
            new DistanceAndDuration(this).execute(url);
            TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
            taskRequestDirections.execute(url);
        }
    }
    public  void Notification() {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_FIREBASE_SEND_NOTIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                                    /*OrderDetails user = new OrderDetails(

                                            obj.getString("PickupFromLocationName"),
                                            obj.getString("DropLocationName"),
                                            obj.getString("CustomerFullName"),
                                            obj.getString("CustomerFullName"),
                                            obj.getString("CustomerFullName"),
                                            obj.getString("CustomerFullName"),

                                            obj.getString("CustomerFullName"),
                                            obj.getString("CustomerFullName"),
                                            obj.getString("CustomerFullName"),
                                            obj.getString("CustomerFullName"),
                                            obj.getString("CustomerFullName"),
                                            obj.getString("CustomerFullName"),
                                            obj.getString("CustomerFullName"),
                                            obj.getString("CustomerFullName")
                                    );*/


                                Intent intent = new Intent(PayPalActivity.this, OrderSuccessfullyPlaced.class);
                                startActivity(intent);




                                // startActivity(new Intent(getApplicationContext(), OrderSuccessfullyPlaced.class));
                            } else {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR",error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    public  void  saveOrderDetails() {

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

            final String pickupAddress = b.getString("PickupAddress");
            final String deliveryAddress = b.getString("DeliveryAddress");
            final String fromLat = b.getString("FromLat");
            final String fromLang = b.getString("FromLong");
            final String vehicleType = b.getString("Vehicle");
            final String product = b.getString("Product");
            final String pickupDate = b.getString("PickupDate");
            final String pickuptime = b.getString("PickupTime");
            final String cpName = b.getString("ContactPerson");
            final String mobile = b.getString("Mobile");
            final String alternatemobile = b.getString("AlternateMobile");
            final String ToLong =b.getString("ToLong");
            final String ToLat =b.getString("ToLat");
            final String totalCharges =b.getString("totalCharges");
            final String ImageUrl =b.getString("ImageUrl");
            String[] arrOfStr=null;
            if(DistanceAndDuration!="")
            {
                String str = DistanceAndDuration;
                arrOfStr = str.split(":");

            }
            if (arrOfStr != null || arrOfStr.length != 0) {
                Distance=arrOfStr[0];
                Duration=arrOfStr[1];
                TotalSecond=arrOfStr[2];
            }
        final String objDistance =Distance;
        final String objDuration =Duration;
        final String objTotalSecond=TotalSecond;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DETAILS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject obj = new JSONObject(response);


                                if (!obj.getBoolean("error")) {


                                    Notification();

                                    myDatabase.GetLastId();
                                    myDatabase.DeleteRecord(myDatabase.GetLastId());
                                            Intent intent = new Intent(PayPalActivity.this, OrderSuccessfullyPlaced.class);
                                            intent.putExtra("DeliveryId",obj.getString("DeliveryId"));
                                            startActivity(intent);




                                   // startActivity(new Intent(getApplicationContext(), OrderSuccessfullyPlaced.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR",error.getMessage());
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("DeliveryId","0");
                    params.put("CustomerId",userid);
                    params.put("vehicleType", vehicleType);
                    params.put("pickupAddress", pickupAddress);
                    params.put("deliveryAddress", deliveryAddress);
                    params.put("fromLat", fromLat);
                    params.put("fromLang", fromLang);
                    params.put("product", product);
                    params.put("pickupDate", pickupDate);
                    params.put("pickuptime", pickuptime);
                    params.put("cpName", cpName);
                    params.put("mobile", mobile);
                    params.put("alternatemobile", alternatemobile);
                    params.put("paymenttype", "1");
                    params.put("ToLong", ToLong);
                    params.put("ToLat", ToLat);
                    params.put("Distance", objDistance);
                    params.put("Duration", objDuration);
                    params.put("TotalSecond", objTotalSecond);
                    params.put("ImageUrl", ImageUrl);
                    return params;
                }
            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

        }


    @Override
    public void onTaskCompleted(String... values) {
        DistanceAndDuration=values[0].toString();
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private String getRequestUrl(String fromLatLong,String toLatLong) {
        //Value of origin
   /*  String str_org = "origin=" + origin.latitude +","+origin.longitude;
     //Value of destination
     String str_dest = "destination=" + dest.latitude+","+dest.longitude;*/


        // String str_org = "origin=" + origin.latitude +","+origin.longitude;
        //Value of destination
//     String str_dest = "destination=" + dest.latitude+","+dest.longitude;

        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=driving";

        String key="key=AIzaSyBDl1LtAS21s-0JkYMEC0JgMLKf5jyJqi80";
        //Build the full param
        //  String param = str_org +"&" + str_dest + "&" +sensor+"&" +mode+"&" +key;
        //Output format
        String output = "json";
        //Create url to request
        // String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;

        String[] latlong =  fromLatLong.split(",");

        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        LatLng location = new LatLng(latitude, longitude);


        String[] latlong2 =  toLatLong.split(",");
        latitude = Double.parseDouble(latlong2[0]);
        longitude = Double.parseDouble(latlong2[1]);
        LatLng location2 = new LatLng(latitude, longitude);
        String url="https://maps.googleapis.com/maps/api/directions/json?origin="+latlong[0]+","+latlong[1]+"&destination="+latlong2[0]+","+latlong2[1]+"&travelmode=driving&sensor=false&key=AIzaSyD3lPCpXWKTSMLC4wCL4rXmatN3f9M4lt4";

        return url;
    }
    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }
    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }
    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;



            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }



                polylineOptions.addAll(points);
                polylineOptions.width(8);
                polylineOptions.color(Color.BLACK);
                polylineOptions.geodesic(true);
            }

          /*  if (polylineOptions!=null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }*/

        }


    }
}

