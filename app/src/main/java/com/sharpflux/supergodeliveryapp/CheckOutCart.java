package com.sharpflux.supergodeliveryapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CheckOutCart  extends AppCompatActivity  {

    DatabaseHelperMerchant myDatabase;
    ImageView mImage;
    TextView mTitle;
    TextView price, cart_product_quantity_tv;
    Button btnAddCart;
    RecyclerView recyclerView;
    private List<CheckOutItems> merchantList;
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        myDatabase = new DatabaseHelperMerchant(this);

        recyclerView = findViewById(R.id.rvCheckOutItems);
        LinearLayoutManager mGridLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mGridLayoutManager);
        merchantList = new ArrayList<>();
        toolbar= (android.support.v7.widget.Toolbar)this.findViewById(R.id.toolbar);
        mImage =  findViewById(R.id.imageviewMerchant);
        mTitle =  findViewById(R.id.tvFirmname);
        price =  findViewById(R.id.tvprice);
        Cursor res = myDatabase.GetCart();
        if (res.getCount() == 0) {

        }

        while (res.moveToNext()) {

            CheckOutItems sellOptions = new CheckOutItems
                            (res.getString(1),
                                    res.getString(5),
                                    res.getString(2),
                                  Double.valueOf(res.getString(4)),
                                 Integer.valueOf( res.getString(3))
                            );

            merchantList.add(sellOptions);
            CheckOutAdapter myAdapter = new CheckOutAdapter(CheckOutCart.this, merchantList,toolbar);
            recyclerView.setAdapter(myAdapter);


        }

    }


}
