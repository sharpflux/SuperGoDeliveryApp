package com.sharpflux.supergodeliveryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sharpflux.supergodeliveryapp.utils.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MerchantListActivity extends AppCompatActivity {
    private List<MerchantsType> merchantList;
    private RecyclerView mRecyclerView;
    MyMerchantAdapter myAdapter;






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_list);

        setTitle("Our Merchants!");

        mRecyclerView =findViewById(R.id.rvMain);


        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(mGridLayoutManager);

        merchantList = new ArrayList<>();

        setDynamicFragmentToTabLayout();
    }
    private void setDynamicFragmentToTabLayout() {

        CustomProgressDialog.showSimpleProgressDialog(this, "Loading...", "Fetching data", false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_RECYCLER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONArray obj = new JSONArray(response);

                            for (int i = 0; i < obj.length(); i++) {

                                JSONObject userJson = obj.getJSONObject(i);

                                MerchantsType sellOptions=
                                        new  MerchantsType
                                                ( userJson.getString("MerchantTypeId"),
                                                        "http://admin.supergo.in/" +  userJson.getString("ImgUrl"),
                                                        userJson.getString("TypeName"));

                                merchantList.add(sellOptions);


                                myAdapter = new MyMerchantAdapter(MerchantListActivity.this, merchantList);
                                mRecyclerView.setAdapter(myAdapter);
                                CustomProgressDialog.removeSimpleProgressDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        VolleySingleton.getInstance(MerchantListActivity.this).addToRequestQueue(stringRequest);


    }

}
