package com.sharpflux.supergodeliveryapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipleMerchantActivity extends AppCompatActivity {
    private List<Merchants> merchantList;
    String merchantId = "";
    private RecyclerView mRecyclerView;
    MultipleMerchantAdapter myAdapter;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView txt_emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_merchant);
        txt_emptyView = findViewById(R.id.txt_emptyView);

        mRecyclerView = findViewById(R.id.rvlist);
        LinearLayoutManager mGridLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        merchantList = new ArrayList<>();
        setTitle("Our Merchants!");

        shimmerFrameLayout = findViewById(R.id.parentShimmerLayout);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            merchantId = bundle.getString("MerchantTypeId");
           // merchantId = bundle.getString("MerchantCode");
        }


        //shimmerFrameLayout.startShimmerAnimation();
       // setDynamicFragmentToTabLayout();

        MultipleMerchantActivity.AsyncTaskRunner runner = new MultipleMerchantActivity.AsyncTaskRunner();
        String sleepTime = "1";
        runner.execute(sleepTime);


    }


    private void setDynamicFragmentToTabLayout() {


        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_AllMERCHANT + merchantId+"&ToLat=18&ToLong=15&StartIndex=1&PageSize=100",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressBar.setVisibility(View.GONE);
                        try {


                            JSONArray obj = new JSONArray(response);
                            //if no error in response
                            //Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < obj.length(); i++) {
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject(i);

                                String Image;
                                if (userJson.getString("LogoImgUrl") == null)
                                    Image = "0";
                                else
                                    Image = "http://admin.supergo.in/" + userJson.getString("LogoImgUrl");
                                Merchants sellOptions =
                                        new Merchants
                                                (
                                                        userJson.getString("MerchantId"),
                                                        Image,
                                                        userJson.getString("FirmName"),
                                                        userJson.getString("FirmName"),
                                                        userJson.getString("MobileNo"),
                                                        userJson.getString("FromLat"),
                                                        userJson.getString("FromLong"),
                                                        userJson.getString("MerchantAddress"),
                                                        userJson.getString("TotalCharges"),
                                                        userJson.getString("GstAmount"),
                                                        userJson.getString("Kilmoter"),
                                                        merchantId
                                                        );

                                merchantList.add(sellOptions);

                                myAdapter = new MultipleMerchantAdapter(MultipleMerchantActivity.this, merchantList);
                                mRecyclerView.setAdapter(myAdapter);

                                if(myAdapter.getItemCount()==0);{
                                    txt_emptyView.setVisibility(View.VISIBLE);

                                }
                                txt_emptyView.setVisibility(View.GONE);



                               /* shimmerFrameLayout.stopShimmerAnimation();
                                shimmerFrameLayout.setVisibility(View.GONE);*/
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(SellerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //   params.put("ImageUrl", "");
//                params.put("CategoryName_EN", "");
                return params;
            }
        };

        VolleySingleton.getInstance(MultipleMerchantActivity.this).addToRequestQueue(stringRequest);

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {


              /*  setDynamicFragmentToTabLayout();
                Thread.sleep(100);

                resp = "Slept for " + params[0] + " seconds";*/


                int time = Integer.parseInt(params[0]) * 1000;
                setDynamicFragmentToTabLayout();
                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";


            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            // finalResult.setText(result);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MultipleMerchantActivity.this,
                    "Loading...",
                    "");
        }

        @Override
        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);

        }

    }

}

