package com.sharpflux.supergodeliveryapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MerchantDescriptionAdapter extends RecyclerView.Adapter<MerchantDescriptionViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<Description> mlist;
    int minteger = 0;
    DatabaseHelperMerchant myDatabase;
    android.support.v7.widget.Toolbar  toolbar;
    TextView tvTotalCount;
    ImageView img_dot;

    public MerchantDescriptionAdapter(Context mContext, List<Description> merchantlist,android.support.v7.widget.Toolbar tool, ImageView img_dot) {
        this.toolbar=tool;
        this.mContext = mContext;
        this.mlist = merchantlist;
        this.img_dot = img_dot;
        tvTotalCount=tool.findViewById(R.id.tvTotalCount);
    }

    @Override
    public MerchantDescriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_row, parent, false);


        return new MerchantDescriptionViewHolder(mView);
    }
    public void CountItemsInCart() {
        Cursor res = myDatabase.GetCart();
        if (res.getCount() == 0) {

        }
        tvTotalCount.setText(res.getCount() + " Items");
    }
    @Override
    public void onBindViewHolder(final MerchantDescriptionViewHolder holder, final int position) {
        Picasso.get().load(mlist.get(position).getImage()).into(holder.mImage);
        holder.mTitle.setText(mlist.get(position).getName());
        myDatabase = new DatabaseHelperMerchant(mContext);
        double priced = mlist.get(position).getPrice();
        String priceS = String.valueOf(priced);
        holder.price.setText("₹" + priceS);

 /*       holder.cart_minus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                minteger = Integer.valueOf(holder.cart_product_quantity_tv.getText().toString()) - 1;
                if (minteger >= 0) {
                    if (myDatabase.GETExist(mlist.get(position).getId()) != "0")
                        myDatabase.OrderInsert(Integer.valueOf(mlist.get(position).getId()), mlist.get(position).getName(), String.valueOf(minteger), mlist.get(position).getPrice());
                    else
                        myDatabase.UpdateOrder(Integer.valueOf(mlist.get(position).getId()), mlist.get(position).getName(), String.valueOf(minteger), mlist.get(position).getPrice());

                    holder.cart_product_quantity_tv.setText(String.valueOf(minteger));

                }
            }
        });
        holder.cart_plus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minteger = Integer.valueOf(holder.cart_product_quantity_tv.getText().toString()) + 1;
                if (minteger >= 0) {
                    if (myDatabase.GETExist(mlist.get(position).getId()) != "0")
                        myDatabase.OrderInsert(Integer.valueOf(mlist.get(position).getId()), mlist.get(position).getName(), String.valueOf(minteger), mlist.get(position).getPrice());
                    else
                        myDatabase.UpdateOrder(Integer.valueOf(mlist.get(position).getId()), mlist.get(position).getName(), String.valueOf(minteger), mlist.get(position).getPrice());

                    holder.cart_product_quantity_tv.setText(String.valueOf(minteger));

                }
            }
        });*/

        holder.btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (myDatabase.CheckItemIsExists(mlist.get(position).getId()) ==false) {
                    myDatabase.OrderInsert(Integer.valueOf(mlist.get(position).getId()), mlist.get(position).getName(),"1", mlist.get(position).getPrice(),mlist.get(position).getImage());
                    img_dot.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext,"Inserted", Toast.LENGTH_SHORT).show();
                }
                else {
                    myDatabase.UpdateOrder(Integer.valueOf(mlist.get(position).getId()), mlist.get(position).getName(), "1", mlist.get(position).getPrice());
                   // Toast.makeText(mContext,"Updated", Toast.LENGTH_SHORT).show();
                }

                CountItemsInCart();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public void onClick(View v) {

    }


}

class MerchantDescriptionViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    TextView mTitle;
    TextView price, cart_product_quantity_tv;
    Button btnAddCart;
    ItemClickListener clickListener;
    private List<Description> mlist;
    ImageView cart_minus_img, cart_plus_img, img_deleteitem;


    MerchantDescriptionViewHolder(View itemView) {
        super(itemView);

        cart_minus_img = (ImageView) itemView.findViewById(R.id.cart_minus_img);
        cart_plus_img = (ImageView) itemView.findViewById(R.id.cart_plus_img);
        cart_product_quantity_tv = (TextView) itemView.findViewById(R.id.cart_product_quantity_tv);
        mImage = itemView.findViewById(R.id.imageviewMerchant);
        mTitle = itemView.findViewById(R.id.tvFirmname);
        price = itemView.findViewById(R.id.tvprice);
        btnAddCart = itemView.findViewById(R.id.btnAddCart);


    }

}