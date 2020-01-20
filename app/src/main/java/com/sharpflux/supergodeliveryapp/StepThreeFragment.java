package com.sharpflux.supergodeliveryapp;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepThreeFragment extends Fragment implements View.OnClickListener, OnTaskCompleted {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private String totalCharges="";
    AlertDialog.Builder builder;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String DistanceAndDuration,ImageUrl;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView txtTotal, txtRideTime,txtKm,txtPaybleAmountKm,txtPaybleAmount,gstlabel,txtFixCharges,txtPerMinCharges,gstAmount;
    private OnStepThreeListener mListener;
    private TextView editTextdeliveryaddress,textviewPickup;
    DatabaseHelper myDatabase;

    public  StepThreeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param PickupAddress Parameter 1.
     * @param DeliveryAddress Parameter 2.
     * @return A new instance of fragment BePartnerStepOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StepThreeFragment newInstance(String PickupAddress, String DeliveryAddress, String FromLat, String FromLong, String Vehicle, String Product){
        StepThreeFragment fragment = new StepThreeFragment();
        Bundle args = new Bundle();
        args.putString("PickupAddress", PickupAddress);
        args.putString("DeliveryAddress", DeliveryAddress);
        args.putString("FromLat", FromLat);
        args.putString("FromLong", FromLong);
        args.putString("Vehicle", Vehicle);
        args.putString("Product", Product);
        fragment.setArguments(args);
        return fragment;
    }
    public static String PickupAddress;
    public static String DeliveryAddress;
    public static String FromLat;
    public static String FromLong;
    public static String Vehicle;
    public static String Product;
    public static String ToLat;
    public static String ToLong;

    public static String date,time,cpname,cnum,anum;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter("fragment-three"));

        return inflater.inflate(R.layout.fragment_step_three, container, false);

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("PickupAddress");
            PickupAddress=message;
            DeliveryAddress=intent.getStringExtra("DeliveryAddress");
            FromLat=intent.getStringExtra("FromLat");
            FromLong=intent.getStringExtra("FromLong");
            Vehicle=intent.getStringExtra("Vehicle");
            Product=intent.getStringExtra("Product");
            ToLat=intent.getStringExtra("ToLat");
            ToLong=intent.getStringExtra("ToLong");

            date=intent.getStringExtra("PickupDate");
            time=intent.getStringExtra("PickupTime");
            cpname=intent.getStringExtra("ContactPerson");
            cnum= intent.getStringExtra("Mobile");
            anum=intent.getStringExtra("AlternateMobile");
            ImageUrl=intent.getStringExtra("ImageUrl");

            AsyncTaskRunner runner = new AsyncTaskRunner();
            String sleepTime = "1";
            runner.execute(sleepTime);

        }
    };

    private Button backBT;
    private Button payBT;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backBT=view.findViewById(R.id.buttonBackThree);
        payBT=view.findViewById(R.id.buttonPay);
        payBT=view.findViewById(R.id.buttonPay);
        txtTotal = view.findViewById(R.id.txtTotal);
        txtRideTime= view.findViewById(R.id.txtRideTime);
        txtKm= view.findViewById(R.id.txtKm);
        txtPaybleAmountKm= view.findViewById(R.id.txtPaybleAmountKm);
        txtPaybleAmount= view.findViewById(R.id.txtPaybleAmount);
        gstlabel= view.findViewById(R.id.gstlabel);
        txtFixCharges= view.findViewById(R.id.txtFixCharges);
        txtPerMinCharges= view.findViewById(R.id.txtPerMinCharges);
        gstAmount= view.findViewById(R.id.gstAmount);
        myDatabase = new DatabaseHelper(getContext());
        payBT.setVisibility(View.GONE);
        builder = new AlertDialog.Builder(getContext());
    }

    public void DistanceDuration()
    {
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            if(myDatabase.GetLastId()!="" && myDatabase.GetLastId()!="0" ) {
                Cursor res = myDatabase.DeliveryGETById(myDatabase.GetLastId());
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    String url = getRequestUrl(res.getString(2) + "," +res.getString(3),res.getString(5)+ "," + res.getString(6));
                    new DistanceAndDuration(this).execute(url);
                }
            }
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
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
            if ((progressDialog != null) && !progressDialog.isShowing()) {
                progressDialog = ProgressDialog.show(getContext(),
                        "Loading...",
                        "Wait for result..");
            }
        }


        @Override
        protected void onPreExecute() {
            if ((progressDialog != null) && !progressDialog.isShowing()) {
                progressDialog = ProgressDialog.show(getContext(),
                        "Loading...",
                        "Wait for result..");
            }
        }


        @Override
        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        backBT.setOnClickListener(this);
        payBT.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        backBT.setOnClickListener(null);
        payBT.setOnClickListener(null);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonBackThree:
                if (mListener != null)
                    mListener.onBackPressed(this);
                break;

            case R.id.buttonPay:
              /*  Bundle b = getArguments();
                String s = b.getString("PickupAddress");
                Toast.makeText(getContext(), s , Toast.LENGTH_SHORT).show();*/

                Intent in = new Intent(getContext(), PayPalActivity.class);
                in.putExtra("PickupAddress", PickupAddress);
                in.putExtra("DeliveryAddress", DeliveryAddress);

                in.putExtra("FromLat", FromLat);
                in.putExtra("FromLong", FromLong);

                in.putExtra("ToLat", ToLat);
                in.putExtra("ToLong", ToLong);

                in.putExtra("Vehicle", Vehicle);
                in.putExtra("Product",Product);

                in.putExtra("PickupDate", date);
                in.putExtra("PickupTime",time);
                in.putExtra("ContactPerson", cpname);

                in.putExtra("Mobile",cnum);
                in.putExtra("AlternateMobile",anum);
                in.putExtra("totalCharges",totalCharges);
                in.putExtra("ImageUrl",ImageUrl);
                startActivity(in);

                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepThreeListener) {
            mListener = (OnStepThreeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        backBT=null;
        payBT=null;
    }


    private class RateCalculatorAsynchTask extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

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
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }


        @Override
        protected void onPreExecute() {
            if ((progressDialog != null) && !progressDialog.isShowing()) {
                progressDialog = ProgressDialog.show(getContext(),
                        "Loading...",
                        "Wait for result..");
            }
        }


        @Override
        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);

        }
    }
    @Override
    public void onTaskCompleted(String... values) {
        DistanceAndDuration=values[0].toString();
        txtTotal.setText(DistanceAndDuration);

        String[] arrOfStr=null;
        if(DistanceAndDuration!="")
        {
            String str = DistanceAndDuration;
            arrOfStr = str.split(":");
        }

        if (arrOfStr != null || arrOfStr.length != 0) {
            txtRideTime.setText("("+arrOfStr[1]+")");
            txtKm.setText("("+arrOfStr[0]+")");

            if(Vehicle!=null) {
                RateCalculatorAsynchTask runner = new RateCalculatorAsynchTask();
                runner.execute(Vehicle, arrOfStr[0], arrOfStr[2]);
            }
          //  GetPayableAmount(Vehicle,arrOfStr[0],arrOfStr[1]);
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
                            //converting response to json object
                            //JSONObject obj = new JSONObject(response);
                            JSONArray obj = new JSONArray(response);

                            if(obj.length()==0){
                                builder.setMessage("Invalid response from server please try again")
                                        .setCancelable(false)

                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();

                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.setTitle("Invalid response");
                                alert.show();
                                return;
                            }
                            //if no error in response
                            //Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < obj.length(); i++) {
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject(i);

                                if (!userJson.getBoolean("error")) {

                                    gstlabel.setText("GST @"+userJson.getString("GSTPercentage")+"%");
                                    txtFixCharges.setText(userJson.getString("FixCharges")+" Rs.");
                                    txtPerMinCharges.setText(userJson.getString("PerMinCharges")+" Rs.");
                                    txtTotal.setText(userJson.getString("TotalCharges")+" Rs.");
                                    txtPaybleAmountKm.setText(userJson.getString("KilometerCharges")+" Rs.");
                                    txtPaybleAmount.setText(userJson.getString("TotalCharges")+" Rs.");
                                    gstAmount.setText(userJson.getString("GstAmount")+" Rs.");
                                    totalCharges=userJson.getString("TotalCharges");
                                    payBT.setVisibility(View.VISIBLE);
                                }
                                else{
                                   // Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                                    builder.setMessage("Invalid response from server")
                                            .setCancelable(false)

                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //  Action for 'NO' Button
                                                    dialog.cancel();

                                                }
                                            });

                                    AlertDialog alert = builder.create();
                                    alert.setTitle(response.toString());
                                    alert.show();
                                    return;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    public interface OnStepThreeListener {
        void onBackPressed(Fragment fragment);
        void onNextPressed(Fragment fragment);
    }
    private String getRequestUrl(String fromLatLong,String toLatLong) {
        String url="";
        if(fromLatLong!=null && fromLatLong!="" && toLatLong!=null && toLatLong!="") {
            String sensor = "sensor=false";
            String mode = "mode=driving";
            String key = "key=AIzaSyBDl1LtAS21s-0JkYMEC0JgMLKf5jyJqi8";
            String output = "json";
            String[] latlong = fromLatLong.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            LatLng location = new LatLng(latitude, longitude);
            String[] latlong2 = toLatLong.split(",");
            latitude = Double.parseDouble(latlong2[0]);
            longitude = Double.parseDouble(latlong2[1]);
            LatLng location2 = new LatLng(latitude, longitude);
            url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latlong[0] + "," + latlong[1] + "&destination=" + latlong2[0] + "," + latlong2[1] + "&travelmode=driving&sensor=false&key=AIzaSyD3lPCpXWKTSMLC4wCL4rXmatN3f9M4lt4";
        }
        else {
            builder.setMessage("Invalid Address supplied")
                    .setCancelable(false)

                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {//  Action for 'NO' Button
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.setTitle("Invalid Address");
            alert.show();
        }
        return url;
    }
}
