package com.sharpflux.supergodeliveryapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MerchantDescriptionAdapter extends RecyclerView.Adapter<MerchantDescriptionViewHolder>  implements View.OnClickListener{

    private Context mContext;
    private List<Description> mlist;
    int minteger = 0;
    public MerchantDescriptionAdapter(Context mContext, List<Description> merchantlist) {

        this.mContext = mContext;
        this.mlist = merchantlist;
    }

    @Override
    public MerchantDescriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_row, parent, false);
        return new MerchantDescriptionViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MerchantDescriptionViewHolder holder, final int position) {
        Picasso.get().load(mlist.get(position).getImage()).into(holder.mImage);
        holder.mTitle.setText(mlist.get(position).getName());

        double priced =mlist.get(position).getPrice();
        String priceS=String.valueOf(priced);
        holder.price.setText("₹"+priceS);

        holder.cart_minus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                minteger = Integer.valueOf(holder.cart_product_quantity_tv.getText().toString()) - 1;
                if(minteger >= 0) {
                    holder.cart_product_quantity_tv.setText(String.valueOf(minteger));
                }
            }
        });
        holder.cart_plus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minteger = Integer.valueOf(holder.cart_product_quantity_tv.getText().toString()) + 1;
                if(minteger >= 0) {
                    holder.cart_product_quantity_tv.setText(String.valueOf(minteger));
                }
            }
        });

      /*  holder.btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click Item",mlist.get(position).getName());
            }
        });*/
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
    TextView price,cart_product_quantity_tv;
    Button btnAddCart;
    ItemClickListener clickListener;
    private List<Description> mlist;
    ImageView cart_minus_img, cart_plus_img,img_deleteitem;
    MerchantDescriptionViewHolder(View itemView) {
        super(itemView);
        cart_minus_img=(ImageView) itemView.findViewById(R.id.cart_minus_img);
        cart_plus_img=(ImageView) itemView.findViewById(R.id.cart_plus_img);
        cart_product_quantity_tv=(TextView) itemView.findViewById(R.id.cart_product_quantity_tv);
        mImage = itemView.findViewById(R.id.imageviewMerchant);
        mTitle = itemView.findViewById(R.id.tvFirmname);
        price = itemView.findViewById(R.id.tvprice);
        btnAddCart= itemView.findViewById(R.id.btnAddCart);


    }

}