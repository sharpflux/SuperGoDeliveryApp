package com.sharpflux.supergodeliveryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtpRegisterActivity extends AppCompatActivity {
    Bundle bundle;
    private EditText enterOTPCodeEt;
    private TextView verifyButton,resendOtpTv;
    String otp = "", enteredOtp = "";
    AlertDialog.Builder builder;

    int o1,o2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        verifyButton = findViewById(R.id.verifyButton);
        resendOtpTv= findViewById(R.id.resendOtpTv);
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

                        User user = new User(
                             Integer.valueOf( bundle.getString("CustomerId")),
                                bundle.getString("CustomerFullName"),
                                bundle.getString("Email"),
                                bundle.getString("MobileNo")

                        );
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

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

        resendOtpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegenerateOTP( bundle.getString("MobileNo"));
            }
        });
        }


    private void RegenerateOTP(String ToMobile) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                otp=obj.getString("OTP");
                            }
                            else{
                                builder.setMessage(obj.getString("message"))
                                        .setCancelable(false)

                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();

                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.setTitle("Error");
                                alert.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        builder.setMessage(error.getMessage())
                                .setCancelable(false)

                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();

                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.setTitle("Error");
                        alert.show();
                        // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("OTPMobileNo", ToMobile);
                params.put("OTPType", "OTP");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    }




