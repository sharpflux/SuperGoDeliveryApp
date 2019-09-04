package com.sharpflux.supergodeliveryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText passwordEt, confirmPassEt;
    Button submitBtnTv;
    String UserId = "";
    AlertDialog.Builder builder;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        builder = new AlertDialog.Builder(this);

        passwordEt = findViewById(R.id.passwordEt);
        confirmPassEt = findViewById(R.id.confirmPassEt);
        submitBtnTv = findViewById(R.id.submitBtnTv);


        submitBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userReset();
               /* Intent i = new Intent(ResetPasswordActivity.this,CustomerLoginActivity.class);
                startActivity(i);*/
            }
        });

    }

    private void userReset() {
        //first getting the values
        //   final String username =  editTextmobile.getText().toString();

        Intent intent = getIntent();
        String UserId = intent.getStringExtra("UserId");


        final String newpass = passwordEt.getText().toString();
        final String confirmpass = confirmPassEt.getText().toString();



        if (TextUtils.isEmpty(newpass)) {
            passwordEt.setError("Please enter your new password");
            passwordEt.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirmpass)) {
            confirmPassEt.setError("Please enter your confirm password");
            confirmPassEt.requestFocus();
            return;
        }

        if (!newpass.equals(confirmpass)) {
            confirmPassEt.setError("Please enter correct password");
            confirmPassEt.requestFocus();
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_RESETPASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {

                                if (obj.getString("message").equals("Success")) {
                                    builder.setMessage("Password Sucessfully updated")
                                            .setCancelable(false)

                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //  Action for 'NO' Button
                                                    Intent i = new Intent(ResetPasswordActivity.this, CustomerLoginActivity.class);
                                                    startActivity(i);
                                                }
                                            });

                                    AlertDialog alert = builder.create();
                                    alert.setTitle("Success");
                                    alert.show();
                                }

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
                // params.put("UserId", username);
                params.put("UserId", UserId);
                params.put("Passwords", newpass);


                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
