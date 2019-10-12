package com.sharpflux.supergodeliveryapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MultipleMerchantAdapter extends RecyclerView.Adapter<MultipleMerchantViewHolder> {

    private Context mContext;
    private List<Merchants> multiplemerchantlist;


    public MultipleMerchantAdapter(Context mContext, List<Merchants> multiplemerchantlist) {
        this.mContext = mContext;
        this.multiplemerchantlist = multiplemerchantlist;
    }

    @NonNull
    @Override
    public MultipleMerchantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_merchant_item, viewGroup, false);
        return new MultipleMerchantViewHolder(mView);

    }

    @Override
    public void onBindViewHolder(@NonNull MultipleMerchantViewHolder holder, int position) {

        if(!multiplemerchantlist.get(position).getImageUrl().equals("0"))
         Picasso.get().load(multiplemerchantlist.get(position).getImageUrl()).into(holder.mImage);

        holder.mTitle.setText(multiplemerchantlist.get(position).getFirmName());
        holder.MerchantId = multiplemerchantlist.get(position).getMerchantId();
        holder.mdetails.setText(multiplemerchantlist.get(position).getFirmName());

        holder.mobilenum=multiplemerchantlist.get(position).getMobileNum();
        holder.FromLat=multiplemerchantlist.get(position).getFromLat();
        holder.FromLong=multiplemerchantlist.get(position).getFromLong();
        holder.MerchantAddress=multiplemerchantlist.get(position).getMerchantAddress();

    }


    @Override
    public int getItemCount() {
        return multiplemerchantlist.size();
    }


}


class MultipleMerchantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mImage;
    TextView mTitle;
    TextView mdetails;
    String MerchantId,mobilenum,FromLat,FromLong,MerchantAddress;;

    MultipleMerchantViewHolder(View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.imageviewMerchant);
        mTitle = itemView.findViewById(R.id.tvFirmname);
        mdetails = itemView.findViewById(R.id.tvDetails);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        Intent intent;
        intent = new Intent(context, MerchantDescriptionActivity.class);
        intent.putExtra("MerchantId", MerchantId.toString());
        intent.putExtra("MerchantName", mTitle.getText().toString());
        intent.putExtra("mobilenum", mobilenum.toString());
        intent.putExtra("FromLat", FromLat.toString());
        intent.putExtra("FromLong", FromLong.toString());
        intent.putExtra("MerchantAddress", MerchantAddress.toString());
        context.startActivity(intent);
    }


}