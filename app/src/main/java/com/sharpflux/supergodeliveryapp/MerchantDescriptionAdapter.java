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
import android.widget.LinearLayout;
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
    TextView tvTotalCount,pri_Txt;
    ImageView img_dot;
    LinearLayout btnCheckOut,linearCheckOut;

    public MerchantDescriptionAdapter(Context mContext, List<Description> merchantlist,android.support.v7.widget.Toolbar tool,
                                      ImageView img_dot, LinearLayout btnCheckOut,TextView pri_Txt,LinearLayout linearCheckOut) {
        this.toolbar=tool;
        this.mContext = mContext;
        this.mlist = merchantlist;
        this.img_dot = img_dot;
        this.btnCheckOut= btnCheckOut;
        tvTotalCount=tool.findViewById(R.id.tvTotalCount);
        this.pri_Txt=pri_Txt;
        this.linearCheckOut=linearCheckOut;

    }

    @Override
    public MerchantDescriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_row, parent, false);


        return new MerchantDescriptionViewHolder(mView);
    }
    public Double total=0.0;
    public void CountItemsInCart(TextView pri_Txt) {

        total=0.0;
        Cursor res = myDatabase.GetCart();
        if (res.getCount() == 0) {

        }
        else {
            while (res.moveToNext()) {
                total=total + ((Integer.valueOf(res.getString(3)) * Double.valueOf(res.getString(4))));
            }
            tvTotalCount.setText(res.getCount() + " Items");
            pri_Txt.setText(res.getCount() + " Items | ₹"+total.toString());
            btnCheckOut.setVisibility(View.VISIBLE);
            linearCheckOut.setVisibility(View.VISIBLE);
        }


       // btnCheckOut.setVisibility(View.VISIBLE);
    }
    @Override
    public void onBindViewHolder(final MerchantDescriptionViewHolder holder, final int position) {
        Picasso.get().load(mlist.get(position).getImage()).resize(200,200).centerCrop().into(holder.mImage);
        holder.mTitle.setText(mlist.get(position).getName());
        myDatabase = new DatabaseHelperMerchant(mContext);
        double priced = mlist.get(position).getPrice();
        String priceS = String.valueOf(priced);
        holder.price.setText("₹" + priceS );
        holder.txt_measure.setText(" /"+mlist.get(position).getMeasurementName());



        btnCheckOut.setVisibility(View.GONE);
        linearCheckOut.setVisibility(View.GONE);
        holder.cart_product_quantity_tv.setText("1");

        Cursor res = myDatabase.GetCart();

        while (res.moveToNext()) {

            if( mlist.get(position).getId().equals( res.getString(1)))
            {
                holder.btnAddCart.setVisibility(View.GONE);
                holder.add_cart_linear.setVisibility(View.VISIBLE);
                holder.cart_product_quantity_tv.setText( res.getString(3));
                CountItemsInCart(pri_Txt);
            }

        }


/*        builder.append("<Assign>");
        builder.append("<ItemId>" + res.getString(1) + "</ItemId>");
        builder.append("<Qunatity>" + res.getString(3) + "</Qunatity>");
        builder.append("<Price>" + res.getString(4) + "</Price>");
        builder.append("</Assign>");*/

        holder.cart_minus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                minteger = Integer.valueOf(holder.cart_product_quantity_tv.getText().toString()) - 1;

                if (minteger >= 0) {

                    myDatabase.UpdateQty(Integer.valueOf(mlist.get(position).getId()), String.valueOf(minteger));
                    holder.cart_product_quantity_tv.setText(String.valueOf(minteger));
                    CountItemsInCart(pri_Txt);
                }
                if (minteger == 0) {
                    myDatabase.DeleteRecord(mlist.get(position).getId());
                    CountItemsInCart(pri_Txt);
                    holder.btnAddCart.setVisibility(view.VISIBLE);
                    holder.add_cart_linear.setVisibility(View.GONE);

                }

                //CountItemsInCart(pri_Txt);
            }

        });
        holder.cart_plus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minteger = Integer.valueOf(holder.cart_product_quantity_tv.getText().toString()) + 1;
                if (minteger >= 0) {
                    myDatabase.UpdateQty(Integer.valueOf(mlist.get(position).getId()),String.valueOf(minteger));
                    holder.cart_product_quantity_tv.setText(String.valueOf(minteger));
                    CountItemsInCart(pri_Txt);
                }
                if (minteger == 0) {
                    myDatabase.DeleteRecord(mlist.get(position).getId());
                    CountItemsInCart(pri_Txt);
                    holder.btnAddCart.setVisibility(view.VISIBLE);
                    holder.add_cart_linear.setVisibility(View.GONE);
                }

               // CountItemsInCart(pri_Txt);
            }
        });
        holder.btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    boolean flag=true;
                if (myDatabase.CheckItemIsExists(mlist.get(position).getId()) ==false) {
                    myDatabase.OrderInsert(Integer.valueOf(mlist.get(position).getId()), mlist.get(position).getName(),"1", mlist.get(position).getPrice(),mlist.get(position).getImage());
                    //img_dot.setVisibility(View.VISIBLE);
                    holder.btnAddCart.setVisibility(view.GONE);
                    holder.add_cart_linear.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext,"Inserted", Toast.LENGTH_SHORT).show();
                    CountItemsInCart(pri_Txt);
                }
                else {
                    myDatabase.UpdateOrder(Integer.valueOf(mlist.get(position).getId()), mlist.get(position).getName(), "1", mlist.get(position).getPrice());
                    holder.btnAddCart.setVisibility(view.GONE);
                    holder.add_cart_linear.setVisibility(View.VISIBLE);
                    CountItemsInCart(pri_Txt);
                   // Toast.makeText(mContext,"Updated", Toast.LENGTH_SHORT).show();
                }
               // CountItemsInCart(pri_Txt);
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
    TextView price, cart_product_quantity_tv,pri_Txt,txt_measure;
    TextView btnAddCart;
    ItemClickListener clickListener;
    private List<Description> mlist;
    TextView cart_minus_img, cart_plus_img, img_deleteitem;
        LinearLayout add_cart_linear;

    MerchantDescriptionViewHolder(View itemView) {
        super(itemView);

        cart_minus_img = (TextView) itemView.findViewById(R.id.cart_minus_img);
        cart_plus_img = (TextView) itemView.findViewById(R.id.cart_plus_img);
        cart_product_quantity_tv = (TextView) itemView.findViewById(R.id.cart_product_quantity_tv);
        mImage = itemView.findViewById(R.id.imageviewMerchant);
        mTitle = itemView.findViewById(R.id.tvFirmname);
        pri_Txt=itemView.findViewById(R.id.pri_Txt);
        price = itemView.findViewById(R.id.tvprice);
        txt_measure = itemView.findViewById(R.id.txt_measure);
        btnAddCart = itemView.findViewById(R.id.btnAddCart);
        add_cart_linear=itemView.findViewById(R.id.add_cart_linear);

    }

}