package com.sharpflux.supergodeliveryapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    Button customerLogin,deliveryperson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        customerLogin=findViewById(R.id.buttoncustomer);
        //deliveryperson=findViewById(R.id.buttonDelivery);

        customerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in1 = new Intent(WelcomeActivity.this, CustomerLoginActivity.class);
                startActivity(in1);
                finish();
            }
        });

        /*deliveryperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 =new Intent(WelcomeActivity.this,DeliveryPersonLoginActivity.class);
                startActivity(in2);
                finish();
            }
        });*/

    }


}
