package com.sharpflux.supergodeliveryapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class OtpRegisterActivity extends AppCompatActivity {
    Bundle bundle;
    private EditText enterOTPCodeEt;
    private TextView verifyButton, resendOtpTv;
    String otp = "", enteredOtp = "", username = "", email = "", mob = "", password = "";
    AlertDialog.Builder builder;
    TextView tv_timer1;
    MyCountDownTimer1 myCountDownTimer1;
    MyCountDownTimer2 myCountDownTimer2;

    int o1, o2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        verifyButton = findViewById(R.id.verifyButton);
        resendOtpTv = findViewById(R.id.resendOtpTv);
        enterOTPCodeEt = findViewById(R.id.enterOTPCodeEt);
        tv_timer1 = findViewById(R.id.tv_timer1);

        myCountDownTimer1 = new MyCountDownTimer1(59000, 1000);
        myCountDownTimer1.start();


        builder = new AlertDialog.Builder(this);
        bundle = getIntent().getExtras();

        if (bundle != null) {
            otp = bundle.getString("otp");
            username = bundle.getString("CustomerFullName");
            email = bundle.getString("Email");
            mob = bundle.getString("MobileNo");
            password = bundle.getString("Password");
        }

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredOtp = enterOTPCodeEt.getText().toString();
                o1 = Integer.parseInt(otp);
                o2 = Integer.parseInt(enteredOtp);
                if (o1 == o2) {


                   /* OtpRegisterActivity.AsyncTaskRunner runner = new OtpRegisterActivity.AsyncTaskRunner();
                    String sleepTime = "1";
                    runner.execute(sleepTime);*/

                    OtpRegisterActivity.AsyncTaskRunner runner = new OtpRegisterActivity.AsyncTaskRunner();
                    String sleepTime = "1";
                    runner.execute(sleepTime);
                   // registerUser();


                } else {
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
                //RegenerateOTP(bundle.getString("MobileNo"));

                myCountDownTimer1.onFinish();
                RegenerateOTP(bundle.getString("MobileNo"));


            }
        });
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {


              /*  setDynamicFragmentToTabLayout();
                Thread.sleep(100);

                resp = "Slept for " + params[0] + " seconds";*/


                int time = Integer.parseInt(params[0]) * 1000;
                registerUser();
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
            //
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(OtpRegisterActivity.this,
                    "Loading...",
                    "Saving your Data");
        }

        @Override
        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);

        }

    }

    private void registerUser() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("Iserror")) {

                                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                               /* User user = new User(
                                        obj.getInt("CustomerId"),
                                        obj.getString("CustomerFullName"),
                                        obj.getString("Email"),
                                        obj.getString("MobileNo")

                                );
                               SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                               finish();*/


                                User user = new User(
                                        obj.getInt("CustomerId"),
                                        obj.getString("CustomerFullName"),
                                        obj.getString("EmailId"),
                                        obj.getString("MobileNo")

                                );
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                Intent in = new Intent(getApplicationContext(), ChooseActionActivity.class);
                                startActivity(in);

                            } else {
                                builder.setMessage("Invalid User")
                                        .setCancelable(false)

                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();

                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.setTitle("User already exists with same Mobile No");
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

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CustomerFullName", username);
                params.put("EmailId", email);
                params.put("MobileNo", mob);
                params.put("Password", password);
                //params.put("gender", gender);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
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
                                otp = obj.getString("OTP");

                                myCountDownTimer2 = new MyCountDownTimer2(59000, 1000);
                                myCountDownTimer2.start();

                                myCountDownTimer2.onFinish();


                            } else {
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

    public class MyCountDownTimer1 extends CountDownTimer {
        //public int counter=59;
        public MyCountDownTimer1(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {


            NumberFormat f = new DecimalFormat("00");
            long hour = (millisUntilFinished / 3600000) % 24;
            long min = (millisUntilFinished / 60000) % 60;
            long sec = (millisUntilFinished / 1000) % 60;

            tv_timer1.setText(f.format(min) + ":" + f.format(sec));
            resendOtpTv.setVisibility(View.GONE);
            // f.format(hour)

        }

        @Override
        public void onFinish() {
            tv_timer1.setText("");
            resendOtpTv.setVisibility(View.VISIBLE);
        }

    }

    public class MyCountDownTimer2 extends CountDownTimer {
        public int counter=59;
        public MyCountDownTimer2(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {


            NumberFormat f = new DecimalFormat("00");
            long hour = (millisUntilFinished / 3600000) % 24;
            long min = (millisUntilFinished / 60000) % 60;
            long sec = (millisUntilFinished / 1000) % 60;

            tv_timer1.setText(f.format(min) + ":" + f.format(sec));
            resendOtpTv.setVisibility(View.GONE);

        }

        @Override
        public void onFinish() {
            tv_timer1.setText("");

            resendOtpTv.setVisibility(View.VISIBLE);

        }

    }



}




