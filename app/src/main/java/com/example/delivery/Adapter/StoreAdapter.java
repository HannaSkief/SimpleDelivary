package com.example.delivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.delivery.Common.Common;
import com.example.delivery.Fragments.StoreItemsFragment;
import com.example.delivery.Model.Store;
import com.example.delivery.R;
import com.example.delivery.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private List<Store> storeList;
    private Context context;
    private FragmentActivity fragmentActivity;

    public StoreAdapter(List<Store> storeList, Context context,FragmentActivity fragmentActivity) {
        this.storeList = storeList;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgStore;
        private TextView tvStoreName,tvType;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgStore=itemView.findViewById(R.id.imgStore);
            tvStoreName=itemView.findViewById(R.id.tvStoreName);
            tvType=itemView.findViewById(R.id.tvType);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openStoreItemFragment((Store)itemView.getTag());
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.stores_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemView.setTag(storeList.get(position));
        holder.tvStoreName.setText(storeList.get(position).getName());
        holder.tvType.setText(storeList.get(position).getType());
        Picasso.get().load(RetrofitClient.store_image_path+storeList.get(position).getImg()).fit().centerCrop().into(holder.imgStore);

    }

    @Override
    public int getItemCount() {
        return storeList==null?0:storeList.size();
    }

    private void openStoreItemFragment(Store store){

        Common.selected_store=store;
        FragmentManager manager= fragmentActivity.getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame,new StoreItemsFragment())
                .addToBackStack("store")
                .commit();
    }
}
