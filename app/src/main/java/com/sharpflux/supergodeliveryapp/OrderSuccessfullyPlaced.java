package com.sharpflux.supergodeliveryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderSuccessfullyPlaced extends AppCompatActivity {
    String DeliveryId;
    LinearLayout TrackOrder,back_linear;
    TextView tvOrderId,tvMerchantName;
    android.support.v7.widget.Toolbar toolbar;
    LinearLayout lr_back;
    ImageView img_back_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_successfully_placed);
        tvOrderId=findViewById(R.id.tvOrderId);
        TrackOrder= findViewById(R.id.TrackOrder);
        back_linear= findViewById(R.id.back_linear);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            DeliveryId= extras.getString("DeliveryId");
            tvOrderId.setText("Your Order ID : "+DeliveryId);
        }


        toolbar = (android.support.v7.widget.Toolbar) this.findViewById(R.id.toolbar);

        tvMerchantName = toolbar.findViewById(R.id.tvMerchantName);
        tvMerchantName.setText("ORDER PLACED");
        img_back_cart=toolbar.findViewById(R.id.img_back_cart);


        img_back_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ChooseActionActivity.class);
                startActivity(i);
                finish();
            }
        });
 /*       lr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccessfullyPlaced.this,ChooseActionActivity.class);
                startActivity(intent);
            }
        });*/

        DeliveryId="0";
        TrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = getIntent().getExtras();
                if(extras!=null){
                    DeliveryId= extras.getString("DeliveryId");
                }

                Intent intent = new Intent(OrderSuccessfullyPlaced.this,TrackDeliveryBoy.class);
                intent.putExtra("DeliveryId",DeliveryId);
                intent.putExtra("InsertTime","");
                intent.putExtra("Total","");
                startActivity(intent);
            }
        });
        back_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccessfullyPlaced.this,ChooseActionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(),ChooseActionActivity.class);
                startActivity(i);
                finish();
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
    }
}
