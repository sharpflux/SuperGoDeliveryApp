package com.sharpflux.supergodeliveryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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
import com.sharpflux.supergodeliveryapp.database.dbAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipleMerchantActivity extends AppCompatActivity {
    private List<Merchants> merchantList;
    String MerchantTypeId = "";
    private RecyclerView mRecyclerView;
    MultipleMerchantAdapter myAdapter;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView txt_emptyView,tvMerchantCount;
    boolean isLoading = false;
    dbAddress myAddress;
    DatabaseHelperMerchant myDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_merchants);
        txt_emptyView = findViewById(R.id.txt_emptyView);
        tvMerchantCount=findViewById(R.id.tvMerchantCount);
        mRecyclerView = findViewById(R.id.rvlist);
        LinearLayoutManager mGridLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        merchantList = new ArrayList<>();
        setTitle("Our Merchants!");


        myAddress = new dbAddress(getApplicationContext());

        myDatabase = new DatabaseHelperMerchant(this);
        shimmerFrameLayout = findViewById(R.id.parentShimmerLayout);

        myDatabase.DeleteRecordAll();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            MerchantTypeId = bundle.getString("MerchantTypeId");
           // merchantId = bundle.getString("MerchantCode");
        }


        MultipleMerchantActivity.AsyncTaskRunner runner = new MultipleMerchantActivity.AsyncTaskRunner();
        String sleepTime = "1";
        runner.execute(sleepTime);

        initAdapter();



        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                isLoading = true;
                initAdapter();
                int currentSize = myAdapter.getItemCount();
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = String.valueOf(page+1);
                runner.execute(sleepTime);

            }
        });

        //shimmerFrameLayout.startShimmerAnimation();
       // setDynamicFragmentToTabLayout();




    }


    private void setDynamicFragmentToTabLayout(String PageIndex) {

        Cursor res = myAddress.GetAddress();
        String ToLat="",ToLong="";

        if(res.getCount()==0){

        }

        while (res.moveToNext()) {

            ToLat=res.getString(9);
            ToLong=res.getString(10);
        }

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_AllMERCHANT + MerchantTypeId+"&ToLat="+ToLat+"&ToLong="+ToLong+"&StartIndex="+PageIndex+"&PageSize=10",
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

                                myAdapter.notifyItemInserted(merchantList.size() - 1);

                                tvMerchantCount.setText(userJson.getString("RCount")+" Merchants");
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
                                                        MerchantTypeId,
                                                        userJson.getString("Speciality"),
                                                        userJson.getString("EstimateTime")
                                                        );

                                merchantList.add(sellOptions);

                                if(myAdapter.getItemCount()==0);{
                                    txt_emptyView.setVisibility(View.VISIBLE);

                                }
                                txt_emptyView.setVisibility(View.GONE);



                               /* shimmerFrameLayout.stopShimmerAnimation();
                                shimmerFrameLayout.setVisibility(View.GONE);*/
                            }

                            myAdapter.notifyDataSetChanged();
                            isLoading = false;

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


    private void initAdapter() {
        myAdapter = new MultipleMerchantAdapter(MultipleMerchantActivity.this, merchantList);
        mRecyclerView.setAdapter(myAdapter);
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
                setDynamicFragmentToTabLayout(params[0]);
                Thread.sleep(1000);
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
            //progressDialog.dismiss();

            if ((progressDialog != null) && progressDialog.isShowing()) {
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();
    }
}

