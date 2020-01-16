package com.sharpflux.supergodeliveryapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

    int userId;

    //a list to store all the products
    List<OrderDetails> productList;

    //the recyclerview
    RecyclerView recyclerView;

    private ProgressBar spinner;
    OrderListMainAdapter adapter;
    OrderDetails list;
    SwipeRefreshLayout swipeRefreshLayout ;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(NavigationDrawerConstants.TAG_HOME);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_order, container, false);

        ////////////////
        //getting the recyclerview from xml
        recyclerView = view.findViewById(R.id.recylcerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);

        productList = new ArrayList<>();

        User user = SharedPrefManager.getInstance(getContext()).getUser();

        //customerName.setText("Hey "+user.getUsername()+"!");
        userId = user.getId();


        AsyncTaskRunner runner = new AsyncTaskRunner();
        String sleepTime = "1";
        runner.execute(sleepTime);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = "1";
                runner.execute(sleepTime);
                swipeRefreshLayout.setRefreshing(false);

            }
        });



        return view;
    }


    private void loadProducts() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_ORDERDETAILS + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

//                                DeliveryList list2 = new DeliveryList(  product.getString("mobile"));

                                list = new OrderDetails(
                                        product.getString("pickupAddress"),
                                        product.getString("TotalCharges"),
                                        product.getString("Distance"),
                                        product.getString("Duration"),
                                        product.getString("DeliveryStatus"),
                                        product.getString("DeliveryId"),
                                        product.getString("DeliveryStatusId"),
                                        product.getString("InsertionTime")
                                );
                                //adding the product to product list
                                productList.add(list);

                            }
                            //creating adapter object and setting it to recyclerview
                           adapter = new OrderListMainAdapter(getContext(), productList,OrderFragment.this);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


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


        //adding our stringrequest to queue
        Volley.newRequestQueue(getContext()).add(stringRequest);


    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0]) * 1000;
                loadProducts();
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
            progressDialog = ProgressDialog.show(getContext(),
                    "Loading...",
                    "Wait for result..");
        }


        @Override
        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);

        }
    }

    public void DECLINEORDER(String OrderId, String DeliveryStatusId) {

        final String deliveryidobj;
        final String customerIdobj;
        final String status2;
        String  deliveryid = OrderId;
        String  status = DeliveryStatusId;

        User user=   SharedPrefManager.getInstance(getContext()).getUser();
        String  customerId = String.valueOf(user.getId());
        deliveryidobj = String.valueOf(deliveryid);
        status2 = String.valueOf(status);

        customerIdobj = customerId;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progressBar.setVisibility(View.GONE);

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setCancelable(false);
                                builder.setMessage("Are you sure want to Cancel this Order");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //if user pressed "yes", then he is allowed to exit from application

                                        adapter.notifyDataSetChanged();
                                       /* AsyncTaskRunner runner = new AsyncTaskRunner();
                                        String sleepTime = "1";
                                        runner.execute(sleepTime);*/



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




                            } else {
                                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("DeliveryId", deliveryidobj);
                params.put("CustomerId", customerIdobj);
                params.put("vehicleType", status2);//DECLINE
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

}
