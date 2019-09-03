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

public class MyMerchantAdapter extends RecyclerView.Adapter<MerchantViewHolder> {

    private Context mContext;
    private List<MerchantsType> merchantlist;

    public MyMerchantAdapter(Context mContext, List<MerchantsType> merchantlist) {

        this.mContext = mContext;
        this.merchantlist = merchantlist;
    }

    @Override
    public MerchantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_merchant, parent, false);
        return new MerchantViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MerchantViewHolder holder, final int position) {
        Picasso.get().load(merchantlist.get(position).getImageUrl()).into(holder.mImage);
        holder.mTitle.setText(merchantlist.get(position).getTypeName());
        holder.MerchantId= merchantlist.get(position).getMerchantId();
    }

    @Override
    public int getItemCount() {
        return merchantlist.size();
    }
}

class MerchantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mImage;
    TextView mTitle;
    String MerchantId;

    MerchantViewHolder(View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);


        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Context context=v.getContext();
        Intent intent;
        intent =  new Intent(context,MultipleMerchantActivity.class );
        intent.putExtra("MerchantTypeId",MerchantId.toString());
        context.startActivity(intent);
    }


}