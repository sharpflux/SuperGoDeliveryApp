package com.sharpflux.supergodeliveryapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import afu.org.checkerframework.checker.oigj.qual.I;


public class OrderListMainAdapter extends RecyclerView.Adapter<OrderListMainAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<OrderDetails> deliveryList;
    String DriverStatusId, InsertionTime;
    Button buttonGpsTrack, btn_cancel;
    OrderFragment frag = null;


    public OrderListMainAdapter(Context mCtx, List<OrderDetails> deliveryList,OrderFragment frag) {
        this.mCtx = mCtx;
        this.deliveryList = deliveryList;
        this.DriverStatusId = DriverStatusId;
        this.InsertionTime = InsertionTime;
        this.frag = frag;
        this.notifyDataSetChanged();

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_my_order_list, null);

        return new ProductViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        OrderDetails product = deliveryList.get(position);
        holder.pickup_location.setText(product.getPickupAddress());
        holder.tvTotalCharges.setText("₹" + product.getTotalCharges());
        holder.tvDistance.setText(product.getDistance());
        holder.tvDuration.setText(product.getDuration());
        holder.tvStatus.setText(product.getDeliveryStatus());
        holder.tvOrderId.setText("Order Id : " + product.getDeliveryId());
        InsertionTime = product.getInsertionTime();
        DriverStatusId = product.getDeliveryStatusId();

        if(DriverStatusId.equals("6")||DriverStatusId.equals("7")||DriverStatusId.equals("9")){
            holder.lr_btn.setVisibility(View.INVISIBLE);

        }
        else{ holder.buttonGpsTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String str = product.getDeliveryId();

                //  String array1[] = str.split(":");
                Intent in = new Intent(v.getContext(), TrackDeliveryBoy.class);
                // in.putExtra("DeliveryId", array1[1].toString().trim());
                in.putExtra("DeliveryId", product.getDeliveryId());
                in.putExtra("InsertionTime", InsertionTime);
                in.putExtra("Total", product.getTotalCharges());
                v.getContext().startActivity(in);
            }
        });

            holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    frag.DECLINEORDER(product.getDeliveryId(),"9");
                    // frag.DECLINEORDER(String.valueOf(product.getDeliveryId()));
                }

            });}





    }


    @Override
    public int getItemCount() {
        return deliveryList.size();
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView pickup_location, drop_location, tvTotalCharges, tvDistance, tvDuration, tvStatus, tvOrderId;
        Button buttonGpsTrack, btn_cancel;
        String InsertionDate, InsertionTime, DriverStatusId;
        LinearLayout lr_btn;

        public ProductViewHolder(View itemView) {
            super(itemView);


            pickup_location = itemView.findViewById(R.id.pickup_location);
            tvTotalCharges = itemView.findViewById(R.id.tvTotalCharges);
            buttonGpsTrack = itemView.findViewById(R.id.buttonGpsTrack);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
            lr_btn = itemView.findViewById(R.id.lr_btn);

           /* if(DriverStatusId.equals("4")){
                buttonGpsTrack.setVisibility(View.INVISIBLE);
                btn_cancel.setVisibility(View.INVISIBLE);
            }*/


        }

        /*public void DECLINEORDER(String OrderId) {
            Context context= null;
            final String deliveryidobj;
            final String customerIdobj;
            String  deliveryid = OrderId;

           User user = SharedPrefManager.getInstance(context.getApplicationContext()).getUser();
           String  customerId = String.valueOf(user.getId());
            deliveryidobj = String.valueOf(deliveryid);
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
                    params.put("vehicleType", "7");//DECLINE
                    return params;
                }
            };

            VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
        }*/

    }

}