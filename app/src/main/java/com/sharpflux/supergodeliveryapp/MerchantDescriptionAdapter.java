package com.sharpflux.supergodeliveryapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MerchantDescriptionAdapter extends RecyclerView.Adapter<MerchantDescriptionViewHolder> {

    private Context mContext;
    private List<Description> mlist;

    public MerchantDescriptionAdapter(Context mContext, List<Description> merchantlist) {

        this.mContext = mContext;
        this.mlist = merchantlist;
    }

    @Override
    public MerchantDescriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_descr, parent, false);
        return new MerchantDescriptionViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MerchantDescriptionViewHolder holder, final int position) {
        Picasso.get().load(mlist.get(position).getImage()).into(holder.mImage);
        holder.mTitle.setText(mlist.get(position).getName());

        double priced =mlist.get(position).getPrice();
        String priceS=String.valueOf(priced);
        holder.price.setText("₹"+priceS);


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}

class MerchantDescriptionViewHolder extends RecyclerView.ViewHolder{

    ImageView mImage;
    TextView mTitle;
    TextView price;

    MerchantDescriptionViewHolder(View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.imageviewMerchant);
        mTitle = itemView.findViewById(R.id.tvFirmname);
        price = itemView.findViewById(R.id.tvprice);
        // mdetails = itemView.findViewById(R.id.tvDetails);


    }


}