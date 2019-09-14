package com.sharpflux.supergodeliveryapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerRegisterActivity extends AppCompatActivity {

    EditText editTextUsername, editTextEmail, editTextPassword, editTextMobile;
    // RadioGroup radioGroupGender;
    //ProgressBar progressBar;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        //  progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ChooseActionActivity.class));
            return;
        }
        editTextMobile = findViewById(R.id.editTextMobileNumber);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        //radioGroupGender = (RadioGroup) findViewById(R.id.radioGender);
        builder = new AlertDialog.Builder(this);

        findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server

                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = "1";
                runner.execute(sleepTime);

            }
        });

        findViewById(R.id.buttonloginAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on login
                //we will open the login screen
                finish();
                startActivity(new Intent(CustomerRegisterActivity.this, CustomerLoginActivity.class));
            }
        });

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;
        Handler h = new Handler();


        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
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


            h.postDelayed(new Runnable() {

                @Override
                public void run() {
                    progressDialog.dismiss();
                }


            }, 100);
            // finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {

            h.postDelayed(new Runnable() {

                @Override
                public void run() {
                    progressDialog = ProgressDialog.show(CustomerRegisterActivity.this,
                            "Loading...",
                            "Wait for result..");
                }


            }, 100);

        }


        @Override
        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);

        }

    }

    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String mob = editTextMobile.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();


        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mob)) {
            editTextMobile.setError("Please enter your mobile number");
            editTextMobile.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }

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
                                Log.d("Mess", response);
                                /*User user = new User(
                                        obj.getInt("CustomerId"),
                                        obj.getString("CustomerFullName"),
                                        obj.getString("Email"),
                                        obj.getString("MobileNo")

                                );
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);*/
                                //finish();
                                Intent in = new Intent(CustomerRegisterActivity.this, OtpRegisterActivity.class);
                                in.putExtra("otp", obj.getString("OTP"));
                                in.putExtra("CustomerId", obj.getString("CustomerId"));
                                in.putExtra("CustomerFullName", obj.getString("CustomerFullName"));
                                in.putExtra("Email", obj.getString("EmailId"));
                                in.putExtra("MobileNo", obj.getString("MobileNo"));
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


    private void getOtp() {
        //first getting the values
        final String mobNo = editTextMobile.getText().toString();

        if (!CommonUtils.isValidPhone(mobNo)) {
            editTextMobile.setError("Invalid Mobile number");
            return;
        }
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Intent in = new Intent(CustomerRegisterActivity.this,
                                        OtpRegisterActivity.class);
                                in.putExtra("otp", obj.getString("OTP"));
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
                                alert.setTitle(response);
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

                        builder.setMessage("Invalid User")
                                .setCancelable(false)

                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();

                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.setTitle(error.getMessage());
                        alert.show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("OTPMobileNo", mobNo);
                params.put("OTPType", "OTP");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}