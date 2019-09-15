package com.sharpflux.supergodeliveryapp;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
    Button btnCall;
    String merchantId = "",mobilenum="";
    Bundle bundle;
    RecyclerView recyclerView;
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_description);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        SliderDots = (LinearLayout) findViewById(R.id.SliderDots);
        btnCall = findViewById(R.id.btnCall);
        recyclerView = findViewById(R.id.rvMenuList);

        sliderImg = new ArrayList<ImageModel>();

        LinearLayoutManager mGridLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mGridLayoutManager);
        merchantList = new ArrayList<>();

        toolbar= (android.support.v7.widget.Toolbar)this.findViewById(R.id.toolbar);


        bundle = getIntent().getExtras();

        if (bundle != null) {
            merchantId = bundle.getString("MerchantId");
            mobilenum = bundle.getString("mobilenum");
        }

        //call recycler data

        setDynamicFragmentToTabLayout();


        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fintent = new Intent(MerchantDescriptionActivity.this,CheckOutCart.class);
                startActivity(fintent);

            }
        });


        final float startSize = 00; // Size in pixels
        final float endSize = 20;
        final int animationDuration = 2000; // Animation duration in ms

        ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
        animator.setDuration(animationDuration);


        animator.start();

        getImages();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == 101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber();
            }
        }
    }

    public void callPhoneNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MerchantDescriptionActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobilenum));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobilenum));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



    private void setDynamicFragmentToTabLayout() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_MERCHANTDESCRIPTION+merchantId,
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
                                MerchantDescriptionAdapter myAdapter = new MerchantDescriptionAdapter(MerchantDescriptionActivity.this, merchantList,toolbar);
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

    private void getImages() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_DESCIMAGES+merchantId,
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

                            viewPager.setAdapter(viewPagerAdapter);

                            dotscount = viewPagerAdapter.getCount();

                            dots = new ImageView[dotscount];

                            for (int i = 0; i < dotscount; i++) {

                                dots[i] = new ImageView(MerchantDescriptionActivity.this);

                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                params.setMargins(8, 0, 8, 0);

                                SliderDots.addView(dots[i], params);

                            }

                            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

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


}
