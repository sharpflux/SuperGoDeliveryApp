package com.sharpflux.supergodeliveryapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import java.util.List;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapterViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<CheckOutItems> mlist;
    int minteger = 0;
    DatabaseHelperMerchant myDatabase;
    android.support.v7.widget.Toolbar  toolbar;
    TextView tvTotalCount,total_amount,txt_delivery_charge,txt_subTotal;
    Double TotalAmount;
    String DeliveryCharges,GstAmount;
    public CheckOutAdapter(Context mContext, List<CheckOutItems> merchantlist,android.support.v7.widget.Toolbar tool,TextView total_amount,String DeliveryCharges,
                           String GstAmount,TextView txt_delivery_charge,TextView txt_subTotal) {
        this.toolbar=tool;
        this.mContext = mContext;
        this.mlist = merchantlist;
        tvTotalCount=tool.findViewById(R.id.tvTotalCount);
        this.total_amount=total_amount;
        this.txt_subTotal=txt_subTotal;
        this.DeliveryCharges=DeliveryCharges;
        this.GstAmount=GstAmount;
        this.txt_delivery_charge=txt_delivery_charge;
    }

    @NonNull
    @Override
    public CheckOutAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_checkout, viewGroup, false);
        return new CheckOutAdapterViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutAdapterViewHolder holder, int position) {
        Picasso.get().load(mlist.get(position).getImage()).into(holder.mImage);
        holder.mTitle.setText(mlist.get(position).getName());
        myDatabase = new DatabaseHelperMerchant(mContext);
        double priced = mlist.get(position).getPrice();
        String priceS = String.valueOf(priced);
        holder.price.setText("₹" + priceS);
        TotalAmount=0.0;
        txt_subTotal.setText("₹" +String.valueOf(  calculateTotal()));
        txt_delivery_charge.setText("₹" +String.valueOf(DeliveryCharges));
        holder.cart_product_quantity_tv.setText(String.valueOf(mlist.get(position).getQuantity()));
        holder.cart_minus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                minteger = Integer.valueOf(holder.cart_product_quantity_tv.getText().toString()) - 1;
                if (minteger >= 0) {
                    myDatabase.UpdateQty(Integer.valueOf(mlist.get(position).getId()),String.valueOf(minteger));
                    holder.cart_product_quantity_tv.setText(String.valueOf(minteger));
                }
                if (minteger == 0) {
                    myDatabase.DeleteRecord(mlist.get(position).getId());
                    removeItem(position);
                    Toast.makeText(mContext,"DELETED", Toast.LENGTH_SHORT).show();
                    txt_subTotal.setText("₹" +String.valueOf(  calculateTotal()));
                }
                txt_subTotal.setText("₹" +String.valueOf(  calculateTotal()));

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
                }
                if (minteger == 0) {
                    myDatabase.DeleteRecord(mlist.get(position).getId());
                    removeItem(position);
                    Toast.makeText(mContext,"DELETED", Toast.LENGTH_SHORT).show();
                    txt_subTotal.setText("₹" +String.valueOf(  calculateTotal()));
                    total_amount.setText("₹" +String.valueOf(  calculateTotal()));
                }
                //calculateTotal();
                txt_subTotal.setText("₹" +String.valueOf(  calculateTotal()));
               // txt_subTotal.setText("₹" +String.valueOf(  calculateTotal()));
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minteger = Integer.valueOf(holder.cart_product_quantity_tv.getText().toString()) + 1;
                if (minteger >= 0) {
                    myDatabase.DeleteRecord(mlist.get(position).getId());
                    removeItem(position);
                    Toast.makeText(mContext,"DELETED", Toast.LENGTH_SHORT).show();
                    txt_subTotal.setText("₹" +String.valueOf(  calculateTotal()));
                    //txt_subTotal.setText("₹" +String.valueOf(  calculateTotal()));

                }
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

        }
        txt_subTotal.setText(res.getCount()+" Items");
        while (res.moveToNext()) {
            total=total + ( Integer.valueOf(res.getString(3)) * Double.valueOf(res.getString(4)));
        }
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
    TextView price, cart_product_quantity_tv,total_amount;
    Button btnAddCart;
    ItemClickListener clickListener;
    private List<Description> mlist;
    ImageView cart_minus_img, cart_plus_img, img_deleteitem;


    CheckOutAdapterViewHolder(View itemView) {
        super(itemView);
        total_amount=itemView.findViewById(R.id.total_amount);
        cart_minus_img = (ImageView) itemView.findViewById(R.id.cart_minus_img);
        cart_plus_img = (ImageView) itemView.findViewById(R.id.cart_plus_img);
        cart_product_quantity_tv = (TextView) itemView.findViewById(R.id.cart_product_quantity_tv);
        mImage = itemView.findViewById(R.id.imageviewMerchant);
        mTitle = itemView.findViewById(R.id.tvFirmname);
        price = itemView.findViewById(R.id.tvprice);
        imgDelete=itemView.findViewById(R.id.imgDelete);
        //btnAddCart = itemView.findViewById(R.id.btnAddCart);


    }

}