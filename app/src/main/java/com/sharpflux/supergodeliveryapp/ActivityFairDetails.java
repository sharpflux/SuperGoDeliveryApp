package com.sharpflux.supergodeliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ActivityFairDetails extends AppCompatActivity {

    TextView payonline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fair_details_three);

        payonline = findViewById(R.id.payonline);

        payonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent payIntent = new Intent(ActivityFairDetails.this,PayPalActivity.class);
                startActivity(payIntent);
            }
        });
    }
}
