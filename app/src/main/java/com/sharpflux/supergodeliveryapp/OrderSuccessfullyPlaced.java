package com.sharpflux.supergodeliveryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderSuccessfullyPlaced extends AppCompatActivity {
    String DeliveryId;
    Button btntack;
    TextView tvOrderId;
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


        DeliveryId="0";
        btntack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = getIntent().getExtras();
                if(extras!=null){
                    DeliveryId= extras.getString("DeliveryId");
                }


                Intent intent = new Intent(OrderSuccessfullyPlaced.this,ChooseActionActivity.class);
                intent.putExtra("DeliveryId",DeliveryId);
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
                //if user pressed "yes", then he is allowed to exit from application
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
