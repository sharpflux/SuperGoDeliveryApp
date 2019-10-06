package com.sharpflux.supergodeliveryapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class OrderListMainAdapter extends RecyclerView.Adapter<OrderListMainAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<OrderDetails> deliveryList;




    public OrderListMainAdapter(Context mCtx, List<OrderDetails> deliveryList) {
        this.mCtx = mCtx;
        this.deliveryList = deliveryList;
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
        holder.tvTotalCharges.setText("₹"+product.getTotalCharges());
        holder.tvDistance.setText(product.getDistance());
        holder.tvDuration.setText(product.getDuration());
        holder.tvStatus.setText(product.getDeliveryStatus());
        holder.tvOrderId.setText("Order Id : "+product.getDeliveryId());
        holder.InsertionDate=product.getInsertionDate();
        holder.InsertionTime=product.getInsertionTime();
    }


    @Override
    public int getItemCount() {
        return deliveryList.size();
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView pickup_location,drop_location,tvTotalCharges,tvDistance,tvDuration,tvStatus,tvOrderId;
        Button buttonGpsTrack;
        String InsertionDate,InsertionTime;
        public ProductViewHolder(View itemView) {
            super(itemView);


            pickup_location = itemView.findViewById(R.id.pickup_location);
            tvTotalCharges=itemView.findViewById(R.id.tvTotalCharges);
            buttonGpsTrack=itemView.findViewById(R.id.buttonGpsTrack);
            tvDistance=itemView.findViewById(R.id.tvDistance);
            tvDuration=itemView.findViewById(R.id.tvDuration);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvOrderId=itemView.findViewById(R.id.tvOrderId);
            buttonGpsTrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str=tvOrderId.getText().toString();

                    String array1[]= str.split(":");
                    Intent in = new Intent(v.getContext(),TrackDeliveryBoy.class);
                    in.putExtra("DeliveryId",array1[1].toString().trim());
                    in.putExtra("InsertTime",InsertionTime);
                    in.putExtra("Total",tvTotalCharges.getText());
                    v.getContext().startActivity(in);
                }
            });



        }



    }



}