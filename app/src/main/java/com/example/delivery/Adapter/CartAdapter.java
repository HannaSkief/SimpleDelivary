package com.example.delivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.delivery.Common.Cart;
import com.example.delivery.Model.OrderItem;
import com.example.delivery.R;
import com.example.delivery.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<OrderItem> orderItemList;
    Context context;
    OnOrderItemChange activity;
    boolean hideImages;

    public CartAdapter(Context context, OnOrderItemChange activity) {
        this.context = context;
        this.activity = activity;
        orderItemList= Cart.getCartItems();
        hideImages=false;
    }

    public CartAdapter(List<OrderItem> orderItemList, Context context, OnOrderItemChange activity) {
        this.orderItemList = orderItemList;
        this.context = context;
        this.activity = activity;
        hideImages=true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img,imgInc,imgDec,imgDelete;
        TextView tvName,tvAmount,tvTotalPrice;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            imgInc=itemView.findViewById(R.id.imgInc);
            imgDec=itemView.findViewById(R.id.imgDec);
            imgDelete=itemView.findViewById(R.id.imgDelete);
            tvName=itemView.findViewById(R.id.tvName);
            tvAmount=itemView.findViewById(R.id.tvAmount);
            tvTotalPrice=itemView.findViewById(R.id.tvTotalPrice);

            imgInc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(activity!=null)
                        activity.onIncClicked((OrderItem)itemView.getTag());
                }
            });
            imgDec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(activity!=null)
                        activity.onDecClicked((OrderItem)itemView.getTag());
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(activity!=null)
                        activity.onDeleteClicked((OrderItem)itemView.getTag());
                }
            });
        }
    }

    public interface OnOrderItemChange {
        void onIncClicked(OrderItem item);
        void onDecClicked(OrderItem item);
        void onDeleteClicked(OrderItem item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.order_item_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(orderItemList.get(position));
        holder.tvName.setText(orderItemList.get(position).getName());
        holder.tvAmount.setText(String.valueOf(orderItemList.get(position).getAmount()));
        holder.tvTotalPrice.setText(String.valueOf(orderItemList.get(position).getPrice()*orderItemList.get(position).getAmount()));

        Picasso.get().load(RetrofitClient.item_image_path+orderItemList.get(position).getImg())
                .fit().centerCrop().into(holder.img);

        if(hideImages){
            holder.imgDec.setVisibility(View.GONE);
            holder.imgInc.setVisibility(View.GONE);
            holder.imgDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderItemList==null?0:orderItemList.size();
    }

}
