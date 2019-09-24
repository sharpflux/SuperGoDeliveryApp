package com.sharpflux.supergodeliveryapp;


import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.sharpflux.supergodeliveryapp.utils.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    /*Button send,recieve,sell_button2;
    TextView txtUserGreet;
    private BroadcastReceiver mRegistrationBroadcastReceiver;*/
    ShimmerFrameLayout shimmerFrameLayout;
    private ArrayList<ImageModel> sliderImg;
    ViewPager viewPager;
    LinearLayout SliderDots;
    private int dotscount;
    private ImageView[] dots;

    private List<MerchantsType> merchantList;
    private RecyclerView mRecyclerView;
    MyMerchantAdapter myAdapter;
    TextView txtUserGreet;

    public HomeFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(NavigationDrawerConstants.TAG_HOME);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sliderImg = new ArrayList<ImageModel>();
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        SliderDots = (LinearLayout) view.findViewById(R.id.SliderDots);
        shimmerFrameLayout = view.findViewById(R.id.parentShimmerLayout);
        txtUserGreet = view.findViewById(R.id.txtUserGreet);
        //getting the current user
        User user = SharedPrefManager.getInstance(getContext()).getUser();

        txtUserGreet.setText("Hi " + user.getUsername() + "!" + "What would you like to order today?");


        mRecyclerView = view.findViewById(R.id.rvlistFacilities);


        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 4);

        mRecyclerView.setLayoutManager(mGridLayoutManager);

        merchantList = new ArrayList<>();

        shimmerFrameLayout.startShimmerAnimation();

        setDynamicFragmentToTabLayout();

        getImages();

       // shimmerFrameLayout.stopShimmerAnimation();
    }

    private void getImages() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_DESCIMAGES + 15,
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


                            ViewpagerAdapterForDescription viewPagerAdapter =
                                    new ViewpagerAdapterForDescription(getContext(), sliderImg);

                            viewPager.setAdapter(viewPagerAdapter);



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

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }

    private void setDynamicFragmentToTabLayout() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_RECYCLER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONArray obj = new JSONArray(response);

                            for (int i = 0; i < obj.length(); i++) {

                                JSONObject userJson = obj.getJSONObject(i);

                                MerchantsType sellOptions =
                                        new MerchantsType
                                                (userJson.getString("MerchantTypeId"),
                                                        "http://admin.supergo.in/" + userJson.getString("ImgUrl"),
                                                        userJson.getString("TypeName"));

                                merchantList.add(sellOptions);


                                myAdapter = new MyMerchantAdapter
                                        (getContext(), merchantList);
                                mRecyclerView.setAdapter(myAdapter);

                                shimmerFrameLayout.stopShimmerAnimation();
                                shimmerFrameLayout.setVisibility(View.GONE);


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

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }


}
