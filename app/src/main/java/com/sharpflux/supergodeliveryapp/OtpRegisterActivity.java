package com.sharpflux.supergodeliveryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class OtpRegisterActivity extends AppCompatActivity {
    Bundle bundle;
    private EditText enterOTPCodeEt;
    private TextView verifyButton;
    String otp = "", enteredOtp = "";
    AlertDialog.Builder builder;

    int o1,o2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        verifyButton = findViewById(R.id.verifyButton);

        enterOTPCodeEt = findViewById(R.id.enterOTPCodeEt);
        builder = new AlertDialog.Builder(this);
        bundle = getIntent().getExtras();

        if (bundle != null) {
            otp = bundle.getString("otp");

        }

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enteredOtp = enterOTPCodeEt.getText().toString();
                    o1=Integer.parseInt(otp);
                    o2=Integer.parseInt(enteredOtp);
                    if (o1==o2) {
                        Intent in = new Intent(OtpRegisterActivity.this, ChooseActionActivity.class);
                        startActivity(in);
                    }
                    else
                    {
                        builder.setMessage("Enter valid otp")
                                .setCancelable(false)

                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();

                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.setTitle("Enter valid otp");
                        alert.show();
                    }
                }
            });
        }
    }


