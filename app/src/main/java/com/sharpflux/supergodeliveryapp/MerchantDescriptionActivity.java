package com.sharpflux.supergodeliveryapp;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MerchantDescriptionActivity extends AppCompatActivity {
    private List<Description> merchantList;
    ViewPager viewPager;
    LinearLayout SliderDots;
    private int dotscount;
    private ArrayList<ImageModel> sliderImg;
    private ImageView[] dots;
    Button btnCheckOut;
    String merchantId = "", mobilenum = "",MerchantTypeId,MerchantName;
    Bundle bundle;
    RecyclerView recyclerView;
    DatabaseHelperMerchant myDatabase;
    TextView tvTotalCount,tvMerchantName;
    android.support.v7.widget.Toolbar toolbar;
    AlertDialog.Builder builder;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_description);

        //viewPager = (ViewPager) findViewById(R.id.viewPager);
        SliderDots = (LinearLayout) findViewById(R.id.SliderDots);
        btnCheckOut = findViewById(R.id.btnCheckOut);
        recyclerView = findViewById(R.id.rvMenuList);

        sliderImg = new ArrayList<ImageModel>();

        LinearLayoutManager mGridLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mGridLayoutManager);
        merchantList = new ArrayList<>();
        myDatabase = new DatabaseHelperMerchant(this);
        toolbar = (android.support.v7.widget.Toolbar) this.findViewById(R.id.toolbar);
        tvTotalCount = toolbar.findViewById(R.id.tvTotalCount);

        tvMerchantName=toolbar.findViewById(R.id.tvMerchantName);
        img_back=toolbar.findViewById(R.id.img_back);
         builder = new AlertDialog.Builder(this);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle = getIntent().getExtras();

                if (bundle != null) {
                    MerchantTypeId=bundle.getString("MerchantTypeId");
                }
                Intent i = new Intent(getApplicationContext(),MultipleMerchantActivity.class);
                i.putExtra("MerchantTypeId",MerchantTypeId);

                startActivity(i);
            }
        });
        bundle = getIntent().getExtras();

        if (bundle != null) {
            MerchantTypeId=bundle.getString("MerchantTypeId");
            merchantId = bundle.getString("MerchantId");
            mobilenum = bundle.getString("mobilenum");
            tvMerchantName.setText(bundle.getString("MerchantName"));

        }
        CountItemsInCart();
        //call recycler data


        MerchantDescriptionActivity.AsyncTaskRunner runner = new MerchantDescriptionActivity.AsyncTaskRunner();
        String sleepTime = "1";
        runner.execute(sleepTime);


        //setDynamicFragmentToTabLayout();


        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bundle != null) {
                    merchantId = bundle.getString("MerchantId");
                    mobilenum = bundle.getString("mobilenum");
                    tvMerchantName.setText(bundle.getString("MerchantName"));

                    Intent fintent = new Intent(MerchantDescriptionActivity.this, CheckOutCart.class);
                    fintent.putExtra("MerchantId",bundle.getString("MerchantId"));
                    fintent.putExtra("MerchantName",bundle.getString("MerchantName"));
                    fintent.putExtra("FromLat",bundle.getString("FromLat"));
                    fintent.putExtra("FromLong",bundle.getString("FromLong"));
                    fintent.putExtra("MerchantAddress",bundle.getString("MerchantAddress"));
                    fintent.putExtra("TotalCharges",bundle.getString("TotalCharges"));
                    fintent.putExtra("GstAmount",bundle.getString("GstAmount"));
                    startActivity(fintent);

                }



            }
        });


        final float startSize = 00; // Size in pixels
        final float endSize = 20;
        final int animationDuration = 2000; // Animation duration in ms

        ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
        animator.setDuration(animationDuration);


        animator.start();

        getImages();

        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));


                final float startSize = 00; // Size in pixels
                final float endSize = 20;
                final int animationDuration = 500; // Animation duration in ms

                ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
                animator.setDuration(animationDuration);

                animator.start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

    }

    public void CountItemsInCart() {
        Cursor res = myDatabase.GetCart();
        if (res.getCount() == 0) {

        }
        tvTotalCount.setText(res.getCount() + " Items");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber();
            }
        }
    }

    public void callPhoneNumber() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MerchantDescriptionActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobilenum));
                startActivity(callIntent);

            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobilenum));
                startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void setDynamicFragmentToTabLayout() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_MERCHANTDESCRIPTION + merchantId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray obj = new JSONArray(response);

                            for (int i = 0; i < obj.length(); i++) {

                                JSONObject userJson = obj.getJSONObject(i);
                                Description sellOptions =
                                        new Description
                                                (userJson.getString("ItemId"),
                                                        "http://admin.supergo.in/" + userJson.getString("ImgUrl"),
                                                        userJson.getString("ItemName"),
                                                        userJson.getDouble("Price"));

                                merchantList.add(sellOptions);
                                MerchantDescriptionAdapter myAdapter = new MerchantDescriptionAdapter(MerchantDescriptionActivity.this, merchantList, toolbar);
                                recyclerView.setAdapter(myAdapter);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(MerchantDescriptionActivity.this).addToRequestQueue(stringRequest);


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
                setDynamicFragmentToTabLayout();
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
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MerchantDescriptionActivity.this,
                    "Loading...",
                    "");
        }

        @Override
        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);

        }

    }


    private void getImages() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_DESCIMAGES + merchantId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray obj = new JSONArray(response);

                            for (int i = 0; i < obj.length(); i++) {

                                JSONObject userJson = obj.getJSONObject(i);
                                ImageModel sellOptions =
                                        new ImageModel("http://admin.supergo.in/" + userJson.getString("ImgUrl"));

                                sliderImg.add(sellOptions);


                            }


                            ViewpagerAdapterForDescription viewPagerAdapter = new ViewpagerAdapterForDescription(MerchantDescriptionActivity.this, sliderImg);

                          /*  viewPager.setAdapter(viewPagerAdapter);

                            dotscount = viewPagerAdapter.getCount();

                            dots = new ImageView[dotscount];

                            for (int i = 0; i < dotscount; i++) {

                                dots[i] = new ImageView(MerchantDescriptionActivity.this);

                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                params.setMargins(8, 0, 8, 0);

                                SliderDots.addView(dots[i], params);

                            }

                            dots[0].setImageDrawable(ContextCompat.getDrawable
                                    (getApplicationContext(), R.drawable.active_dot));*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(MerchantDescriptionActivity.this).addToRequestQueue(stringRequest);


    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();


        builder.setCancelable(true);
        builder.setTitle("Do you want to clear your cart");
        builder.setMessage("If you leave this page your cart is empty");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDatabase.DeleteRecord(myDatabase.GetLastId());
                Intent intent = new Intent(getApplicationContext(),MultipleMerchantActivity.class);
                startActivity(intent);
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
    }*/


    @Override
    public void onBackPressed() {
        if (bundle != null) {
            merchantId = bundle.getString("MerchantId");

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to clear your cart ?");
        builder.setMessage("If you leave this page your cart is Empty");
        builder.setPositiveButton("Cleart cart", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                myDatabase.DeleteRecordAll();
                Intent intent = new Intent(getApplicationContext(),MultipleMerchantActivity.class);
                intent.putExtra("MerchantTypeId", MerchantTypeId.toString());
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();


            }
        });
        builder.show();
    }

}
