package com.sharpflux.supergodeliveryapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CheckOutCart extends AppCompatActivity implements PaymentResultListener, OnTaskCompleted {

    DatabaseHelperMerchant myDatabase;
    DatabaseHelper dbHelper;
    ImageView mImage;
    TextView mTitle, tvDropAddress;
    TextView price, cart_product_quantity_tv, total_amount;
    Button btnAddCart, btnCall;
    RecyclerView recyclerView;
    private List<CheckOutItems> merchantList;
    android.support.v7.widget.Toolbar toolbar;
    Bundle bundle;
    LinearLayout linearLayout;
    TextView tvTotalCount, tvMerchantName;
    ProgressDialog mProgressDialog;
    private static String DistanceAndDuration, Distance, Duration, TotalSecond, FromLat, FromLong, MerchantAddress, MerchantId;
    ;
    int userId;
    Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        myDatabase = new DatabaseHelperMerchant(this);
        dbHelper = new DatabaseHelper(getBaseContext());

        recyclerView = findViewById(R.id.rvCheckOutItems);
        LinearLayoutManager mGridLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mGridLayoutManager);
        merchantList = new ArrayList<>();
        toolbar = (android.support.v7.widget.Toolbar) this.findViewById(R.id.toolbar);
        tvTotalCount = toolbar.findViewById(R.id.tvTotalCount);
        tvMerchantName = toolbar.findViewById(R.id.tvMerchantName);
        mImage = findViewById(R.id.imageviewMerchant);
        mTitle = findViewById(R.id.tvFirmname);
        price = findViewById(R.id.tvprice);
        total_amount = findViewById(R.id.total_amount);
        tvDropAddress = findViewById(R.id.tvDropAddress);
        btnCall = findViewById(R.id.btnCall);
        linearLayout = (LinearLayout) findViewById(R.id.droplocationView);
        bundle = getIntent().getExtras();
        JSONArray array;
        tvMerchantName.setText("Check Out");

        // builder.append("<?xml version=\"1.0\" ?>");
        // Initialize the progress dialog
        mProgressDialog = new ProgressDialog(CheckOutCart.this);
        mProgressDialog.setIndeterminate(false);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Progress dialog title
        mProgressDialog.setTitle("Placing Order");
        // Progress dialog message
        mProgressDialog.setMessage("Please wait, we are saving your data...");

        User user = SharedPrefManager.getInstance(CheckOutCart.this).getUser();


        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {

            FromLat = b.getString("FromLat");
            FromLong = b.getString("FromLong");
            MerchantId = b.getString("MerchantId");
            MerchantAddress=b.getString("MerchantAddress");
        }


        //customerName.setText("Hey "+user.getUsername()+"!");
        userId = user.getId();


        if (bundle != null) {
            if (bundle.getString("DeliveryAddress") != null) {
                linearLayout.setVisibility(View.VISIBLE);
                tvDropAddress.setText(bundle.getString("DeliveryAddress"));
                btnCall.setText("Pay Now");
            } else {
                linearLayout.setVisibility(View.GONE);
                btnCall.setText("Place Order");
            }
        } else {
            linearLayout.setVisibility(View.GONE);
            btnCall.setText("Place Order");
        }
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bundle != null) {
                    if (bundle.getString("DeliveryAddress") != null) {

                        String url = getRequestUrl( bundle.getString("FromLat") + "," +  bundle.getString("FromLong"),  bundle.getString("ToLat") + "," +  bundle.getString("ToLong"));
                        new DistanceAndDuration(CheckOutCart.this::onTaskCompleted).execute(url);
                        startPayment();


                    } else {
                        Intent fintent = new Intent(CheckOutCart.this, ChooseDeliveryAddressActivity.class);
                        fintent.putExtra("FromLat", FromLat);
                        fintent.putExtra("FromLong", FromLong);
                        fintent.putExtra("MerchantId", MerchantId);
                        fintent.putExtra("MerchantAddress", MerchantAddress);
                        startActivity(fintent);

                    }
                } else {
                    Intent fintent = new Intent(CheckOutCart.this, ChooseDeliveryAddressActivity.class);
                    startActivity(fintent);

                }
            }
        });

        Cursor res = myDatabase.GetCart();
        if (res.getCount() == 0) {

        }

        while (res.moveToNext()) {

            CheckOutItems sellOptions = new CheckOutItems
                    (res.getString(1),
                            res.getString(5),
                            res.getString(2),
                            Double.valueOf(res.getString(4)),
                            Integer.valueOf(res.getString(3))
                    );

            merchantList.add(sellOptions);
            CheckOutAdapter myAdapter = new CheckOutAdapter(CheckOutCart.this, merchantList, toolbar, total_amount);
            recyclerView.setAdapter(myAdapter);

        }

    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

  /*      Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if(b!=null) {


            String ToLat=b.getString("ToLat");
            String ToLong=b.getString("ToLong");


            String url = getRequestUrl(FromLat + "," +FromLong,ToLat + "," + ToLong);
            new DistanceAndDuration(this).execute(url);
            CheckOutCart.TaskRequestDirections taskRequestDirections = new CheckOutCart.TaskRequestDirections();
            taskRequestDirections.execute(url);
        }*/

        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        final Checkout co = new Checkout();
        try {
            String str = total_amount.getText().toString();
            String[] arrOfStr = str.split("₹", 2);
            JSONObject options = new JSONObject();
            options.put("name", "Super Go");
            options.put("description", "Order Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "http://www.supergo.in/assets/images/supergologo.png");
            options.put("currency", "INR");
            double Paise = 1;//Double.parseDouble(arrOfStr[1].toString());
            options.put("amount", Paise * 100);
            JSONObject preFill = new JSONObject();
            preFill.put("email", user.getEmail());
            preFill.put("contact", user.getMobile());
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute(razorpayPaymentID);
            //saveOrderDetails(razorpayPaymentID);
        } catch (Exception e) {
            Log.e("Eception", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, "Payment failed: " + s + " " + s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentError", e);
        }
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;


        @Override
        protected String doInBackground(String... params) {

            try { Thread.sleep(100); }
            catch (InterruptedException e) { e.printStackTrace(); }
            try {
                saveOrderDetails(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            // mProgressDialog.dismiss();

            // finalResult.setText(result);
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... text) {
            mProgressDialog.setProgress(92);

        }

    }

    public void saveOrderDetails(String razorpayPaymentID) {

        StringBuilder
                builder = new StringBuilder();
        ;

        User user = SharedPrefManager.getInstance(CheckOutCart.this).getUser();

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            FromLong = b.getString("FromLong");
            FromLat = b.getString("FromLat");
            MerchantId = b.getString("MerchantId");
            MerchantAddress=b.getString("MerchantAddress");
        }


        final String pickupAddress = MerchantAddress;
        final String deliveryAddress = tvDropAddress.getText().toString();
        final String fromLat = FromLat;
        final String fromLang = FromLong;
        final String vehicleType = "Merchant";
        final String product = "ORDER";
        final String pickupDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        final String pickuptime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        final String cpName = String.valueOf(user.getUsername());
        final String mobile = user.getMobile().toString();
        final String alternatemobile = user.getMobile().toString();
        final String ToLong = bundle.getString("ToLong");
        final String ToLat = bundle.getString("ToLat");
        final String totalCharges = calculateTotal().toString();
        final String ImageUrl = "0";
        String[] arrOfStr = null;



        Cursor res = myDatabase.GetCart();
        if (res.getCount() == 0) {

        }
        builder.append("<Parent>");
        while (res.moveToNext()) {
            builder.append("<Assign>");
            builder.append("<ItemId>" + res.getString(1) + "</ItemId>");
            builder.append("<Qunatity>" + res.getString(3) + "</Qunatity>");
            builder.append("<Price>" + res.getString(4) + "</Price>");
            builder.append("</Assign>");
        }
        builder.append("</Parent>");


        if (DistanceAndDuration != "" && DistanceAndDuration!=null) {
            String str = DistanceAndDuration;
            arrOfStr = str.split(":");
            if (arrOfStr != null || arrOfStr.length != 0) {
                Distance = arrOfStr[0];
                Duration = arrOfStr[1];
                TotalSecond = arrOfStr[2];
            }
        }
        else {
            Distance = "8.2 Km";
            Duration = "20 Min";
            TotalSecond = "9999";
        }

        final String objDistance = Distance;
        final String objDuration = Duration;
        final String objTotalSecond = TotalSecond;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            mProgressDialog.dismiss();
                            JSONObject obj = new JSONObject(response);


                            if (!obj.getBoolean("error")) {


                                // Notification();

                                //dbHelper.GetLastId();
                                // dbHelper.DeleteRecord(dbHelper.GetLastId());
                                Intent intent = new Intent(CheckOutCart.this, OrderSuccessfullyPlaced.class);
                                intent.putExtra("DeliveryId", obj.getString("DeliveryId"));
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
                        Log.e("ERROR", error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("DeliveryId", "0");
                params.put("CustomerId", String.valueOf(userId));
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
                params.put("TransactionId", razorpayPaymentID);
                params.put("DeliveryTypeId", "2");
                params.put("TotalCharges", totalCharges);
                params.put("MerchantId", MerchantId);
                params.put("ImageUrl", ImageUrl);
                params.put("OrderedXml", builder.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onTaskCompleted(String... values) {
        DistanceAndDuration = values[0].toString();
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
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            CheckOutCart.TaskParser taskParser = new CheckOutCart.TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

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

                    points.add(new LatLng(lat, lon));
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

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
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

    public Double total = 0.0;

    public Double calculateTotal() {
        int i = 0;
        total = 0.0;
        Cursor res = myDatabase.GetCart();
        if (res.getCount() == 0) {

        }

        while (res.moveToNext()) {

            total = total + (Integer.valueOf(res.getString(3)) * Double.valueOf(res.getString(4)));

        }
        return total;


    }

    private String getRequestUrl(String fromLatLong, String toLatLong) {
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

        String key = "key=AIzaSyBDl1LtAS21s-0JkYMEC0JgMLKf5jyJqi80";
        //Build the full param
        //  String param = str_org +"&" + str_dest + "&" +sensor+"&" +mode+"&" +key;
        //Output format
        String output = "json";
        //Create url to request
        // String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;

        String[] latlong = fromLatLong.split(",");

        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        LatLng location = new LatLng(latitude, longitude);


        String[] latlong2 = toLatLong.split(",");
        latitude = Double.parseDouble(latlong2[0]);
        longitude = Double.parseDouble(latlong2[1]);
        LatLng location2 = new LatLng(latitude, longitude);
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latlong[0] + "," + latlong[1] + "&destination=" + latlong2[0] + "," + latlong2[1] + "&travelmode=driving&sensor=false&key=AIzaSyD3lPCpXWKTSMLC4wCL4rXmatN3f9M4lt4";

        return url;
    }
}
