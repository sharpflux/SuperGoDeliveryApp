package com.sharpflux.supergodeliveryapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.sharpflux.supergodeliveryapp.database.dbAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
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
    ImageView mImage,img_back_cart;
    TextView mTitle, tvDropAddress,txt_address;
    TextView price, cart_product_quantity_tv, total_amount,txt_subTotal;
    Button btnAddCart;
    RecyclerView recyclerView;
    private List<CheckOutItems> merchantList;
    android.support.v7.widget.Toolbar toolbar;
    Bundle bundle;
    LinearLayout linearLayout;
    TextView tvTotalCount, tvMerchantName,txt_delivery_charge,txt_itemtotal,tvChangeAddress;
    ProgressDialog mProgressDialog;
    private static String DistanceAndDuration, Distance, Duration, TotalSecond, FromLat, FromLong,
            MerchantAddress, MerchantId,TotalCharges,GstAmount,ToLat,ToLong,MerchantName;
    ;
    int userId;
    Cursor cursor;
    dbAddress myAddress;
    ImageView img_editAddress;
    AlertDialog.Builder Alertbuilder;
    String MerchantTypeId;

    AlertDialog.Builder builder;
    String totalCharges="0.00";
    CheckOutAdapter myAdapter;
    LinearLayout lr_back,LinChangeAddress;
    TextView txt_itemCount,tvTotalAmount,tvGrandTotal,tvAddress,tvDeliveryCharges,tvPlaceOrder;
    RadioGroup rg_payment;
    RadioButton rb_online,rb_cod;
    Integer PaymentMethodid=0;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        myDatabase = new DatabaseHelperMerchant(this);
        dbHelper = new DatabaseHelper(getBaseContext());

        recyclerView = findViewById(R.id.rvCheckOutItems);
        LinearLayoutManager mGridLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mGridLayoutManager);
        merchantList = new ArrayList<>();
        toolbar = (android.support.v7.widget.Toolbar) this.findViewById(R.id.toolbar);

        tvMerchantName = toolbar.findViewById(R.id.tvMerchantName);
        tvMerchantName.setText("CART");
        tvPlaceOrder = findViewById(R.id.tvPlaceOrder);
        img_back_cart=toolbar.findViewById(R.id.img_back_cart);

        rg_payment=findViewById(R.id.rg_payment);
        rb_online=findViewById(R.id.rb_online);
        rb_cod=findViewById(R.id.rb_cod);

        /*tvTotalCount = toolbar.findViewById(R.id.tvTotalCount);

        total_amount = findViewById(R.id.total_amount);
        txt_address = findViewById(R.id.txt_address);
        txt_delivery_charge=findViewById(R.id.txt_delivery_charge);

        txt_subTotal=findViewById(R.id.txt_subTotal);
        txtItemCount=findViewById(R.id.txtItemCount);
        lr_back = findViewById(R.id.lr_back);*/

        Alertbuilder = new AlertDialog.Builder(this);

       /* mImage = findViewById(R.id.imageviewMerchant);
        mTitle = findViewById(R.id.tvFirmname);
        price = findViewById(R.id.tvprice);

        tvDropAddress = findViewById(R.id.tvDropAddress);
        tvPlaceOrder = findViewById(R.id.tvPlaceOrder);
        linearLayout = (LinearLayout) findViewById(R.id.droplocationView);*/
        bundle = getIntent().getExtras();
        JSONArray array;
        //tvMerchantName.setText("Check Out");

        // builder.append("<?xml version=\"1.0\" ?>");
        // Initialize the progress dialog
        mProgressDialog = new ProgressDialog(CheckOutCart.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        User user = SharedPrefManager.getInstance(CheckOutCart.this).getUser();
        //img_editAddress=findViewById(R.id.img_editAddress);
        txt_itemtotal = findViewById(R.id.txt_itemtotal);
        tvTotalAmount=findViewById(R.id.tvTotalAmount);
        tvGrandTotal=findViewById(R.id.tvGrandTotal);
        tvAddress=findViewById(R.id.tvAddress);
        tvChangeAddress=findViewById(R.id.tvChangeAddress);
        LinChangeAddress=findViewById(R.id.LinChangeAddress);
        tvDeliveryCharges=findViewById(R.id.tvDeliveryCharges);
        tvDeliveryCharges.setText("0");

        // Progress dialog title
        mProgressDialog.setTitle("Prepairing Cart");
        // Progress dialog message
        mProgressDialog.setMessage("Please wait, we are perpairing your cart...");

        mProgressDialog.show();

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            FromLat = b.getString("FromLat");
            FromLong = b.getString("FromLong");
            MerchantId = b.getString("MerchantId");
            MerchantTypeId = b.getString("MerchantTypeId");
            MerchantName = b.getString("MerchantName");
            MerchantAddress=b.getString("MerchantAddress");
            TotalCharges = b.getString("TotalCharges");
            GstAmount=b.getString("GstAmount");
        }

        LinChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle != null) {
                    Intent intent = new Intent(getApplicationContext(), ChooseDeliveryAddressActivity.class);
                    intent.putExtra("MerchantTypeId", MerchantTypeId.toString());
                    intent.putExtra("MerchantId", bundle.getString("MerchantId"));
                    intent.putExtra("MerchantName",bundle.getString("MerchantName"));
                    intent.putExtra("mobilenum", "");
                    intent.putExtra("FromLat", bundle.getString("FromLat"));
                    intent.putExtra("FromLong", bundle.getString("FromLong"));
                    intent.putExtra("MerchantAddress", bundle.getString("MerchantAddress"));
                    intent.putExtra("TotalCharges", bundle.getString("TotalCharges"));
                    intent.putExtra("GstAmount", bundle.getString("GstAmount"));
                    intent.putExtra("ImageUrl", bundle.getString("ImageUrl"));
                    intent.putExtra("Speciality", bundle.getString("Speciality"));
                    startActivity(intent);
                }
            }
        });
        tvChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle != null) {
                    Intent intent = new Intent(getApplicationContext(), ChooseDeliveryAddressActivity.class);
                    intent.putExtra("MerchantTypeId", MerchantTypeId.toString());
                    intent.putExtra("MerchantId", bundle.getString("MerchantId"));
                    intent.putExtra("MerchantName",bundle.getString("MerchantName"));
                    intent.putExtra("mobilenum", "");
                    intent.putExtra("FromLat", bundle.getString("FromLat"));
                    intent.putExtra("FromLong", bundle.getString("FromLong"));
                    intent.putExtra("MerchantAddress", bundle.getString("MerchantAddress"));
                    intent.putExtra("TotalCharges", bundle.getString("TotalCharges"));
                    intent.putExtra("GstAmount", bundle.getString("GstAmount"));
                    intent.putExtra("ImageUrl", bundle.getString("ImageUrl"));
                    intent.putExtra("Speciality", bundle.getString("Speciality"));
                    startActivity(intent);
                }
            }
        });

        rg_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                switch(checkedId)
                {
                    case R.id.rb_online:
                        PaymentMethodid =1;
                        Toast.makeText(getApplicationContext(),
                                "Online",
                                Toast.LENGTH_LONG).show();
                        break;
                    case R.id.rb_cod:
                        PaymentMethodid =4;
                        Toast.makeText(getApplicationContext(),
                                "COD",
                                Toast.LENGTH_LONG).show();
                        break;

                }





            }
        });


        img_back_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),MerchantDescriptionActivity.class);
                i.putExtra("MerchantId",b.getString("MerchantId"));
                i.putExtra("MerchantTypeId",b.getString("MerchantTypeId"));
                i.putExtra("MerchantName",b.getString("MerchantName"));
                i.putExtra("ImageUrl",b.getString("ImageUrl"));
                startActivity(i);
            }
        });

      /*  lr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),MerchantDescriptionActivity.class);
                i.putExtra("MerchantId",b.getString("MerchantId"));
                i.putExtra("MerchantTypeId",b.getString("MerchantTypeId"));
                i.putExtra("MerchantName",b.getString("MerchantName"));
                startActivity(i);
            }
        });*/

        //customerName.setText("Hey "+user.getUsername()+"!");
        userId = user.getId();
        myAddress = new dbAddress(getApplicationContext());
        Cursor res = myAddress.GetAddress();
        if(res.getCount()==0){
        }
        while (res.moveToNext()) {
            tvAddress.setText(res.getString(2));

            b.putString("Address",   res.getString(2));
            b.putString("Lat",  res.getString(9)  );
            b.putString("Long", res.getString(10) );
            b.putString("FromLat",  FromLat );
            b.putString("FromLong",FromLong );
            b.putString("MerchantId",MerchantId );
            b.putString("MerchantAddress",   MerchantAddress);
            b.putString("TotalCharges",TotalCharges );
            b.putString("GstAmount",   GstAmount);
            b.putString("ActivityType",   "Merchant" );
            ToLat=res.getString(9);
            ToLong=res.getString(10);
        }

        DistanceDuration();

     /*   if (bundle != null) {
         if(!txt_address.getText().equals("")) {
                //linearLayout.setVisibility(View.VISIBLE);
               // tvDropAddress.setText(bundle.getString("DeliveryAddress"));
                tvPlaceOrder.setText("Pay Now");
            } else {
               // linearLayout.setVisibility(View.GONE);
                tvPlaceOrder.setText("Place Order");
            }
        } else {
           // linearLayout.setVisibility(View.GONE);
            tvPlaceOrder.setText("Place Order");
        }*/

     /*  CheckOutCart.AsyncTaskRunner1 runner = new CheckOutCart.AsyncTaskRunner1();
        String sleepTime = "1";
        runner.execute(sleepTime);*/

     /*String url = getRequestUrl(FromLat+ "," +  FromLong, ToLat + "," + ToLong);
        new DistanceAndDuration(CheckOutCart.this::onTaskCompleted).execute(url);
*/

     /*    img_editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fintent = new Intent(CheckOutCart.this, ChooseDeliveryAddressActivity.class);
                fintent.putExtra("FromLat", FromLat);
                fintent.putExtra("FromLong", FromLong);
                fintent.putExtra("MerchantId", MerchantId);
                fintent.putExtra("MerchantAddress", MerchantAddress);
                fintent.putExtra("TotalCharges", TotalCharges);
                fintent.putExtra("GstAmount", GstAmount);
                startActivity(fintent);
            }
        });*/
      tvPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                if (bundle != null) {
                    if(!tvAddress.getText().equals(""))  {
                        if(ToLat==null &&ToLong==null) {
                            ToLat=bundle.getString("Lat");
                            ToLong=bundle.getString("Long");
                        }
                        // Progress dialog title
                        mProgressDialog.setTitle("Placing Order");
                        // Progress dialog message
                        mProgressDialog.setMessage("Please wait, we are saving your data...");
                        if(PaymentMethodid==1) {
                            startPayment();
                        }
                        else  if(PaymentMethodid==4) {
                            String url = getRequestUrl(FromLat + "," + FromLong, ToLat + "," + ToLong);
                            new DistanceAndDuration(CheckOutCart.this::onTaskCompleted).execute(url);
                            AsyncTaskRunner runner = new AsyncTaskRunner();
                            runner.execute("95235952");
                        }
                        else {
                            if (!rb_online.isChecked() || rb_cod.isChecked()) {
                                rb_online.setError("please select Payment Method");
                                rb_online.requestFocus();
                                rb_cod.setError("please select Payment Method");
                                rb_cod.requestFocus();
                                return;
                                //  Toast.makeText(getApplicationContext(),"please select Payment Method",Toast.LENGTH_SHORT).show();
                            }
                        }

                    } else {
                        Intent fintent = new Intent(CheckOutCart.this, ChooseDeliveryAddressActivity.class);
                        fintent.putExtra("FromLat", FromLat);
                        fintent.putExtra("FromLong", FromLong);
                        fintent.putExtra("MerchantId", MerchantId);
                        fintent.putExtra("MerchantAddress", MerchantAddress);
                        fintent.putExtra("TotalCharges", TotalCharges);
                        fintent.putExtra("GstAmount", GstAmount);
                        startActivity(fintent);

                    }
                } else {
                    Intent fintent = new Intent(CheckOutCart.this, ChooseDeliveryAddressActivity.class);
                    fintent.putExtra("FromLat", FromLat);
                    fintent.putExtra("FromLong", FromLong);
                    fintent.putExtra("MerchantId", MerchantId);
                    fintent.putExtra("MerchantAddress", MerchantAddress);
                    fintent.putExtra("TotalCharges", TotalCharges);
                    fintent.putExtra("GstAmount", GstAmount);
                    startActivity(fintent);
                }
            }
        });

    }

    public  void CartItemFetch(String FromLat, String FromLong, String ToLat, String ToLong,String totalCharges,TextView tvTotalAmount,Double DeliveryCharges)
    {
        ProgressDialog progressDialog;
        Cursor res = myDatabase.GetCart();
        if (res.getCount() == 0) {
        }
        total = 0.0;
        merchantList.clear();
        while (res.moveToNext()) {

            CheckOutItems sellOptions = new CheckOutItems
                    (
                            res.getString(1),
                            res.getString(5),
                            res.getString(2),
                            Double.valueOf(res.getString(4)),
                            Integer.valueOf(res.getString(3))
                    );

            total=total + ((Integer.valueOf(res.getString(3)) * Double.valueOf(res.getString(4))));
            merchantList.add(sellOptions);
          /*  myAdapter = new CheckOutAdapter(CheckOutCart.this, merchantList, toolbar, total_amount,TotalCharges,
                    GstAmount,txt_delivery_charge,txt_subTotal,txtItemCount,FromLat,FromLong,ToLat,ToLong,totalCharges,MerchantId);*/
            myAdapter = new CheckOutAdapter(CheckOutCart.this, merchantList,tvTotalAmount,tvGrandTotal,txt_itemtotal,tvDeliveryCharges.getText().toString());
            recyclerView.setAdapter(myAdapter);
        }
        tvGrandTotal.setText("₹"+ df2.format(total + DeliveryCharges));
        txt_itemtotal.setText("₹"+total.toString());
        tvTotalAmount.setText(res.getCount() + " Items | ₹"+df2.format(total + DeliveryCharges) );
    }
    @Override
    public void onBackPressed() {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setMessage("Do you want to leave this page ?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    if (bundle != null)
                    {

                    Intent intent = new Intent(getApplicationContext(), MerchantDescriptionActivity.class);
                    intent.putExtra("MerchantTypeId", MerchantTypeId.toString());
                    intent.putExtra("MerchantId", bundle.getString("MerchantId"));
                    intent.putExtra("MerchantName",bundle.getString("MerchantName"));
                    intent.putExtra("mobilenum", "");
                    intent.putExtra("FromLat", bundle.getString("FromLat"));
                    intent.putExtra("FromLong", bundle.getString("FromLong"));
                    intent.putExtra("MerchantAddress", bundle.getString("MerchantAddress"));
                    intent.putExtra("TotalCharges", bundle.getString("TotalCharges"));
                    intent.putExtra("GstAmount", bundle.getString("GstAmount"));
                    intent.putExtra("ImageUrl", bundle.getString("ImageUrl"));
                    intent.putExtra("Speciality", bundle.getString("Speciality"));
                    startActivity(intent);
                    finish();
                    }

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();


                }
            });
            builder.show();







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
            String str = tvGrandTotal.getText().toString();
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
        ProgressDialog progressDialog;
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
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {

            if ((progressDialog != null) && !progressDialog.isShowing()) {
                progressDialog = ProgressDialog.show(CheckOutCart.this,
                        "Loading...",
                        "Wait for result..");
            }
        }

        @Override
        protected void onProgressUpdate(String... text) {
           // mProgressDialog.setProgress(92);

        }

    }

    @Override
    public void onTaskCompleted(String... values) {
        DistanceAndDuration = values[0].toString();
        String[] arrOfStr=null;
        if(DistanceAndDuration!="")
        {
            String str = DistanceAndDuration;
            arrOfStr = str.split(":");
        }

        if (arrOfStr != null || arrOfStr.length != 0) {
            CheckOutCart.RateCalculatorAsynchTask runner = new CheckOutCart.RateCalculatorAsynchTask();
            runner.execute("Merchant", arrOfStr[0], arrOfStr[2]);
        }
        else{
            Alertbuilder.setMessage("Error while getting distance and duration from MAP")
                    .setCancelable(false)

                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();

                        }
                    });

            AlertDialog alert = Alertbuilder.create();
            alert.setTitle("MAP Error");
            alert.show();
        }
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

    public void DistanceDuration()
    {
        String url = getRequestUrl(FromLat+ "," +  FromLong, ToLat + "," + ToLong);
        new DistanceAndDuration(CheckOutCart.this::onTaskCompleted).execute(url);
    }

    private class AsyncTaskRunner1 extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
           // publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {

                DistanceDuration();

            }

            catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }


        @Override
        protected void onPreExecute() {

                progressDialog = ProgressDialog.show(CheckOutCart.this,
                        "Loading...",
                        "Wait for result..");

        }


        @Override
        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);

        }

    }

    private class RateCalculatorAsynchTask extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog3;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                GetPayableAmount(params[0],params[1],params[2]);
            }

            catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            if ((progressDialog3 != null) && progressDialog3.isShowing()) {
                progressDialog3.dismiss();
            }
        }


        @Override
        protected void onPreExecute() {
            if ((progressDialog3 != null) && progressDialog3.isShowing()) {
                progressDialog3 = ProgressDialog.show(CheckOutCart.this,
                        "Loading...",
                        "Wait for result..");
            }

        }


        @Override
        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);

        }

    }

    private void GetPayableAmount(final String vehicleType, final String Distance,final  String Duration) {

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_RATECALCULATOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressBar.setVisibility(View.GONE);
                        try {

                            mProgressDialog.dismiss();
                            //converting response to json object
                            //JSONObject obj = new JSONObject(response);
                            JSONArray obj = new JSONArray(response);

                            if(obj.length()==0){
                                Alertbuilder.setMessage("Rate is not defined please contact to admin")
                                        .setCancelable(false)

                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();

                                            }
                                        });

                                AlertDialog alert = Alertbuilder.create();
                                alert.setTitle("Invalid Rate");
                                alert.show();
                                return;
                            }
                            //if no error in response
                            //Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < obj.length(); i++) {
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject(i);

                                if (!userJson.getBoolean("error")) {

                                    totalCharges=userJson.getString("TotalCharges");
                                    tvDeliveryCharges.setText("₹"+totalCharges);

                                    CartItemFetch(FromLat,FromLong,ToLat,ToLong,totalCharges,tvTotalAmount,Double.valueOf(totalCharges));

                                }
                                else{
                                    // Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                                    Alertbuilder.setMessage("Invalid response from server")
                                            .setCancelable(false)

                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //  Action for 'NO' Button
                                                    dialog.cancel();

                                                }
                                            });

                                    AlertDialog alert = Alertbuilder.create();
                                    alert.setTitle(response.toString());
                                    alert.show();
                                    return;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Alertbuilder.setMessage("There is some error please try again")
                                    .setCancelable(false)

                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Action for 'NO' Button
                                            dialog.cancel();

                                        }
                                    });

                            AlertDialog alert = Alertbuilder.create();
                            alert.setTitle(response.toString());
                            alert.show();
                            return;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("vehicleType", vehicleType);
                params.put("Distance", Distance);
                params.put("Duration", Duration);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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
        final String deliveryAddress = tvAddress.getText().toString();
        final String fromLat = FromLat;
        final String fromLang = FromLong;
        final String vehicleType = "Merchant";
        final String product = "ORDER";
        final String pickupDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        final String pickuptime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        final String cpName = String.valueOf(user.getUsername());
        final String mobile = user.getMobile().toString();
        final String alternatemobile = user.getMobile().toString();

        if(ToLat==null &&ToLong==null) {
            ToLat=bundle.getString("Lat");
            ToLong=bundle.getString("Long");
        }


        final String ToLong = ToLat;
        final String ToLat = ToLong;
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

                                Alertbuilder.setMessage("Already order placed")
                                        .setCancelable(false)

                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();

                                            }
                                        });

                                AlertDialog alert = Alertbuilder.create();
                                alert.setTitle("Already order placed");
                                alert.show();


                                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                        Alertbuilder.setMessage("Already order placed")
                                .setCancelable(false)

                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();

                                    }
                                });

                        AlertDialog alert = Alertbuilder.create();
                        alert.setTitle("Already order placed");
                        alert.show();

                        // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("paymenttype", String.valueOf(PaymentMethodid));
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
}
