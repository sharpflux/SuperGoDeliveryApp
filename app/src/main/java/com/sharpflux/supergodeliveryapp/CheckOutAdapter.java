package com.sharpflux.supergodeliveryapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapterViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<CheckOutItems> mlist;
    int minteger = 0;
    DatabaseHelperMerchant myDatabase;
    android.support.v7.widget.Toolbar  toolbar;
    TextView tvTotalCount,total_amount,txt_delivery_charge,txt_subTotal,txtItemCount;
    Double TotalAmount,deliveryCharges;
    String DeliveryCharges="40",GstAmount;
    String FromLat,FromLong,ToLat,ToLong,MerchantId,tvDeliveryCharges;
    private static String DistanceAndDuration,totalChargesfinal;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    TextView tvTotalAmount,tvGrandTotal,txt_itemtotal;

    public CheckOutAdapter(Context mContext, List<CheckOutItems> merchantlist,android.support.v7.widget.Toolbar tool,TextView total_amount,
                           String DeliveryCharges,
                           String GstAmount,TextView txt_delivery_charge,TextView txt_subTotal,TextView txtItemCount,
                           String FromLat,String FromLong,String ToLat,String ToLong, String totalChargesfinal,String MerchantId) {
        this.toolbar=tool;
        this.mContext = mContext;
        this.mlist = merchantlist;
        tvTotalCount=tool.findViewById(R.id.tvTotalCount);
        this.total_amount=total_amount;
        this.txt_subTotal=txt_subTotal;
        this.DeliveryCharges=DeliveryCharges;
        this.FromLat=FromLat;
        this.FromLong=FromLong;
        this.ToLat=ToLat;
        this.ToLong=ToLong;
        this.GstAmount=GstAmount;
        this.txt_delivery_charge=txt_delivery_charge;
        this.txtItemCount=txtItemCount;
        this.totalChargesfinal=totalChargesfinal;
        this.deliveryCharges= Double.valueOf(totalChargesfinal);
        this.MerchantId=MerchantId;
    }

    public CheckOutAdapter(Context mContext, List<CheckOutItems> merchantlist,TextView tvTotalAmount,TextView tvGrandTotal,TextView txt_itemtotal,String tvDeliveryCharges) {
        this.mContext = mContext;
        this.mlist = merchantlist;
        this.tvTotalAmount=tvTotalAmount;
        this.tvGrandTotal=tvGrandTotal;
        this.txt_itemtotal=txt_itemtotal;
        this.tvDeliveryCharges=tvDeliveryCharges;
    }

    @NonNull
    @Override
    public CheckOutAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //row_checkout
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items, viewGroup, false);
        return new CheckOutAdapterViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutAdapterViewHolder holder, int position) {

        String DeliveryCharges="40";
        //Picasso.get().load(mlist.get(position).getImage()).into(holder.mImage);
        holder.mTitle.setText(mlist.get(position).getName());
        myDatabase = new DatabaseHelperMerchant(mContext);
        double priced = mlist.get(position).getPrice();
        String priceS = String.valueOf(priced);
        holder.price.setText(priceS);
        TotalAmount=0.0;

        //txt_subTotal.setText("₹"+String.valueOf(  df2.format(calculateTotal())));

        //txt_delivery_charge.setText("₹" +totalChargesfinal);
        //total_amount.setText("₹"+String.valueOf(df2.format(calculateTotal()+deliveryCharges)));
        holder.cart_product_quantity_tv.setText(String.valueOf(mlist.get(position).getQuantity()));
        holder.cart_minus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                minteger = Integer.valueOf(holder.cart_product_quantity_tv.getText().toString()) - 1;

                    if (minteger >= 0) {

                        myDatabase.UpdateQty(Integer.valueOf(mlist.get(position).getId()), String.valueOf(minteger));
                        holder.cart_product_quantity_tv.setText(String.valueOf(minteger));
                       // holder.price.setText(priceS);
                        calculateTotal();

                    }
                    if (minteger == 0) {
                        myDatabase.DeleteRecord(mlist.get(position).getId());
                        removeItem(position);
                        Toast.makeText(mContext, "DELETED", Toast.LENGTH_SHORT).show();
                        calculateTotal();

                        //txt_subTotal.setText("₹"+String.valueOf(  df2.format(calculateTotal())));
                        //total_amount.setText("₹"+String.valueOf(df2.format(calculateTotal()+deliveryCharges)));
                    }
                    calculateTotal();
                   // txt_subTotal.setText("₹"+String.valueOf(  df2.format(calculateTotal())));
                    //total_amount.setText("₹"+String.valueOf(df2.format(calculateTotal()+deliveryCharges)));
                }

        });
        holder.cart_plus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minteger = Integer.valueOf(holder.cart_product_quantity_tv.getText().toString()) + 1;
                if (minteger >= 0) {
                    CheckOutItems filter = mlist.get(position);
                    myDatabase.UpdateQty(Integer.valueOf(mlist.get(position).getId()),String.valueOf(minteger));
                    holder.cart_product_quantity_tv.setText(String.valueOf(minteger));
                    calculateTotal();
                }
                if (minteger == 0) {
                    myDatabase.DeleteRecord(mlist.get(position).getId());
                    removeItem(position);
                    calculateTotal();
                    Toast.makeText(mContext,"DELETED", Toast.LENGTH_SHORT).show();
                    //txt_subTotal.setText("₹"+String.valueOf(  df2.format(calculateTotal())));
                    //total_amount.setText("₹"+String.valueOf(df2.format(calculateTotal()+deliveryCharges)));
                }
                calculateTotal();
                //calculateTotal();
                //txt_subTotal.setText("₹"+String.valueOf(  df2.format(calculateTotal())));
                //total_amount.setText("₹"+String.valueOf(df2.format(calculateTotal()+deliveryCharges)));
            }
        });



        }
    public void removeItem(int position) {
        mlist.remove(position);
        notifyItemRemoved(position);
    }
    public Double total=0.0;
    public Double calculateTotal(){
        int i=0;
        total=0.0;



        Cursor res = myDatabase.GetCart();
        if (res.getCount() == 0) {
            Intent inte = new Intent(mContext,MerchantDescriptionActivity.class);
            inte.putExtra("FromLat", FromLat);
            inte.putExtra("FromLong", FromLong);
            inte.putExtra("MerchantId", MerchantId);
            inte.putExtra("MerchantAddress", "Add");
            //inte.putExtra("TotalCharges", TotalCharges);
           // inte.putExtra("GstAmount", GstAmount);
            mContext.startActivity(inte);
        }
       // txtItemCount.setText(res.getCount()+" Items");
        while (res.moveToNext()) {

            total=total + ((Integer.valueOf(res.getString(3)) * Double.valueOf(res.getString(4))));

        }

        this.tvTotalAmount.setText(res.getCount() + " Items | ₹"+total.toString() );
        this.tvGrandTotal.setText("₹"+total.toString());
        this.txt_itemtotal.setText("₹"+total.toString());
        return  total;


    }
    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public void onClick(View v) {

    }


}

class CheckOutAdapterViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage,imgDelete;
    TextView mTitle;
    TextView price, cart_product_quantity_tv,total_amount,txtItemCount;
    Button btnAddCart;
    ItemClickListener clickListener;
    private List<Description> mlist;
    TextView cart_minus_img, cart_plus_img, img_deleteitem;


    CheckOutAdapterViewHolder(View itemView) {
        super(itemView);
        total_amount=itemView.findViewById(R.id.total_amount);
       // txtItemCount=itemView.findViewById(R.id.txtItemCount);
        cart_minus_img = (TextView) itemView.findViewById(R.id.cart_minus_img);
        cart_plus_img = (TextView) itemView.findViewById(R.id.cart_plus_img);
        cart_product_quantity_tv = (TextView) itemView.findViewById(R.id.cart_product_quantity_tv);
        mImage = itemView.findViewById(R.id.imageviewMerchant);
        mTitle = itemView.findViewById(R.id.tvFirmname);
        price = itemView.findViewById(R.id.tvprice);
        imgDelete=itemView.findViewById(R.id.imgDelete);
        //btnAddCart = itemView.findViewById(R.id.btnAddCart);


    }

}