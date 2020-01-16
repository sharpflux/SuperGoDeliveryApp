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
        holder.ImageUrl=multiplemerchantlist.get(position).getImageUrl();
        holder.mTitle.setText(multiplemerchantlist.get(position).getFirmName());
        holder.MerchantId = multiplemerchantlist.get(position).getMerchantId();
        holder.mdetails.setText(multiplemerchantlist.get(position).getSpeciality());
        holder.tvDistanceinTime.setText(multiplemerchantlist.get(position).getKilmoter());
        holder.mobilenum=multiplemerchantlist.get(position).getMobileNum();
        holder.FromLat=multiplemerchantlist.get(position).getFromLat();
        holder.FromLong=multiplemerchantlist.get(position).getFromLong();
        holder.MerchantAddress=multiplemerchantlist.get(position).getMerchantAddress();
        holder.TotalCharges=multiplemerchantlist.get(position).getTotalCharges();
        holder.GstAmount=multiplemerchantlist.get(position).getGstAmount();
        holder.MerchantTypeId=multiplemerchantlist.get(position).getMerchantTypeId();
        holder.tvTimeToReach.setText(multiplemerchantlist.get(position).getEstimateTime());
        holder.Speciality=multiplemerchantlist.get(position).getSpeciality();
    }


    @Override
    public int getItemCount() {
        return multiplemerchantlist.size();
    }


}


class MultipleMerchantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mImage;
    TextView mTitle;
    TextView mdetails,tvDistanceinTime,tvTimeToReach;
    String MerchantId,mobilenum,FromLat,FromLong,MerchantAddress,TotalCharges,GstAmount,MerchantTypeId,ImageUrl,Speciality;

    MultipleMerchantViewHolder(View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.imageviewMerchant);
        mTitle = itemView.findViewById(R.id.tvFirmname);
        mdetails = itemView.findViewById(R.id.tvDetails);
        tvDistanceinTime=itemView.findViewById(R.id.tvDistanceinTime);
        tvTimeToReach=itemView.findViewById(R.id.tvTimeToReach);
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        Intent intent;
        intent = new Intent(context, MerchantDescriptionActivity.class);
        intent.putExtra("MerchantTypeId", MerchantTypeId.toString());
        intent.putExtra("MerchantId", MerchantId.toString());
        intent.putExtra("MerchantName", mTitle.getText().toString());
        intent.putExtra("mobilenum", mobilenum.toString());
        intent.putExtra("FromLat", FromLat.toString());
        intent.putExtra("FromLong", FromLong.toString());
        intent.putExtra("MerchantAddress", MerchantAddress.toString());
        intent.putExtra("TotalCharges", TotalCharges.toString());
        intent.putExtra("GstAmount", GstAmount.toString());
        intent.putExtra("ImageUrl", ImageUrl.toString());
        intent.putExtra("Speciality", Speciality.toString());
        context.startActivity(intent);
    }


}