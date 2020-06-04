package com.example.delivery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.delivery.Common.Common;
import com.example.delivery.ItemActivity;
import com.example.delivery.Model.Item;
import com.example.delivery.R;
import com.example.delivery.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    List<Item> itemList;
    Context context;

    public ItemAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName,tvPrice;
        RatingBar ratingBar;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            tvName=itemView.findViewById(R.id.tvName);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            ratingBar=itemView.findViewById(R.id.rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.selected_item=(Item)itemView.getTag();
                    context.startActivity(new Intent(context, ItemActivity.class));
                }
            });
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.item_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(itemList.get(position));
        holder.tvName.setText(itemList.get(position).getName());
        holder.ratingBar.setRating(Float.parseFloat(itemList.get(position).getRate()));
        holder.tvPrice.setText(itemList.get(position).getPrice());
        Picasso.get().load(RetrofitClient.item_image_path+itemList.get(position).getImg())
                .fit()
                .centerCrop()
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return itemList==null?0:itemList.size();
    }

}
