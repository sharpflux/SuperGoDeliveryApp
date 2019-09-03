package com.sharpflux.supergodeliveryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipleMerchantActivity extends AppCompatActivity {
    private List<Merchants> merchantList;
    String merchantId="";
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_merchant);

        mRecyclerView =findViewById(R.id.rvlist);
        LinearLayoutManager mGridLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        merchantList = new ArrayList<>();
        setTitle("Our Merchants!");


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            merchantId=bundle.getString("MerchantTypeId");
        }

        setDynamicFragmentToTabLayout();

    }


    private void setDynamicFragmentToTabLayout() {

        //CustomProgressDialog.showSimpleProgressDialog(this, "Loading...","Fetching data",false);

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_AllMERCHANT+merchantId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressBar.setVisibility(View.GONE);
                        try {

                            // CustomProgressDialog.removeSimpleProgressDialog();
                            //converting response to json object
                            //JSONObject obj = new JSONObject(response);
                            JSONArray obj = new JSONArray(response);
                            //if no error in response
                            //Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < obj.length(); i++) {
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject(i);


                                Merchants sellOptions=
                                        new  Merchants
                                                ( userJson.getString("MerchantId"),
                                                        "http://admin.supergo.in/" +userJson.getString("ImageUrl"),
                                                        userJson.getString("FirmName"),userJson.getString("FirmName"),
                                                        userJson.getString("MobileNo"));

                                merchantList.add(sellOptions);

                                MultipleMerchantAdapter myAdapter = new MultipleMerchantAdapter(MultipleMerchantActivity.this, merchantList);
                                mRecyclerView.setAdapter(myAdapter);;
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
}

