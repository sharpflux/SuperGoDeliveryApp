package com.sharpflux.supergodeliveryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderSuccessfullyPlaced extends AppCompatActivity {
    String DeliveryId;
    Button btntack;
    TextView tvOrderId,tvMerchantName;
    android.support.v7.widget.Toolbar toolbar;
    LinearLayout lr_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_successfully_placed);
        tvOrderId=findViewById(R.id.tvOrderId);
        btntack= findViewById(R.id.btntack);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            DeliveryId= extras.getString("DeliveryId");
            tvOrderId.setText("Your Order ID : "+DeliveryId);
        }
        /*toolbar = (android.support.v7.widget.Toolbar) this.findViewById(R.id.toolbar);
        tvMerchantName=toolbar.findViewById(R.id.tvMerchantName);
        lr_back=toolbar.findViewById(R.id.lr_back);
        tvMerchantName.setText("Order Placed");*/

 /*       lr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccessfullyPlaced.this,ChooseActionActivity.class);
                startActivity(intent);
            }
        });*/

        DeliveryId="0";
        btntack.setOnClickListener(new View.OnClickListener() {
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
