package com.sharpflux.supergodeliveryapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CheckOutCart extends AppCompatActivity  implements PaymentResultListener {

    DatabaseHelperMerchant myDatabase;
    ImageView mImage;
    TextView mTitle, tvDropAddress;
    TextView price, cart_product_quantity_tv, total_amount;
    Button btnAddCart, btnCall;
    RecyclerView recyclerView;
    private List<CheckOutItems> merchantList;
    android.support.v7.widget.Toolbar toolbar;
    Bundle bundle;
    LinearLayout linearLayout;
    TextView tvTotalCount,tvMerchantName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        myDatabase = new DatabaseHelperMerchant(this);


        recyclerView = findViewById(R.id.rvCheckOutItems);
        LinearLayoutManager mGridLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mGridLayoutManager);
        merchantList = new ArrayList<>();
        toolbar = (android.support.v7.widget.Toolbar) this.findViewById(R.id.toolbar);
        tvTotalCount = toolbar.findViewById(R.id.tvTotalCount);
        tvMerchantName=toolbar.findViewById(R.id.tvMerchantName);
        mImage = findViewById(R.id.imageviewMerchant);
        mTitle = findViewById(R.id.tvFirmname);
        price = findViewById(R.id.tvprice);
        total_amount = findViewById(R.id.total_amount);
        tvDropAddress = findViewById(R.id.tvDropAddress);
        btnCall = findViewById(R.id.btnCall);
        linearLayout = (LinearLayout) findViewById(R.id.droplocationView);
        bundle = getIntent().getExtras();

        tvMerchantName.setText("Check Out");

        if (bundle != null) {
            if (bundle.getString("DeliveryAddress") != null) {
                linearLayout.setVisibility(View.VISIBLE);
                tvDropAddress.setText(bundle.getString("DeliveryAddress"));
                btnCall.setText("Pay Now");
            }
        } else {
            linearLayout.setVisibility(View.GONE);
            btnCall.setText("Place Order");
        }
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bundle != null) {
                    startPayment();
                } else {
                    Intent fintent = new Intent(CheckOutCart.this, ChooseDeliveryAddressActivity.class);
                    startActivity(fintent);

                }
            }
        });

        Cursor res = myDatabase.GetCart();
        if (res.getCount() == 0) {

        }

        while (res.moveToNext()) {

            CheckOutItems sellOptions = new CheckOutItems
                    (res.getString(1),
                            res.getString(5),
                            res.getString(2),
                            Double.valueOf(res.getString(4)),
                            Integer.valueOf(res.getString(3))
                    );

            merchantList.add(sellOptions);
            CheckOutAdapter myAdapter = new CheckOutAdapter(CheckOutCart.this, merchantList, toolbar, total_amount);
            recyclerView.setAdapter(myAdapter);


        }

    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        final Checkout co = new Checkout();
        try {
            String str = total_amount.getText().toString();
            String[] arrOfStr = str.split("₹", 2);
            JSONObject options = new JSONObject();
            options.put("name", "Super Go");
            options.put("description", "Order Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "http://www.supergo.in/assets/images/supergologo.png");
            options.put("currency", "INR");
            double Paise = Double.parseDouble(arrOfStr[1].toString());
            options.put("amount", Paise * 100);
            JSONObject preFill = new JSONObject();
            preFill.put("email", user.getEmail());
            preFill.put("contact", user.getMobile());
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Eception", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, "Payment failed: " + s + " " + s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentError", e);
        }
    }



}
