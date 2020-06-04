package com.example.delivery.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delivery.Adapter.ItemAdapter;
import com.example.delivery.Adapter.StoreAdapter;
import com.example.delivery.Common.Common;
import com.example.delivery.Model.Item;
import com.example.delivery.R;
import com.example.delivery.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreItemsFragment extends Fragment {

    private View view;
    private RecyclerView rvItems;
    private TextView tvStoreName;
    ImageView imgBackground;
    public StoreItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_store_items, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvStoreName=view.findViewById(R.id.tvStoreName);
        imgBackground=view.findViewById(R.id.imgBackground);

        rvItems =view.findViewById(R.id.rvItems);
        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));

        tvStoreName.setText(Common.selected_store.getName());

        Picasso.get().load(RetrofitClient.store_image_path+Common.selected_store.getImg())
                .fit()
                .centerCrop()
                .into(imgBackground);

        getItems();

    }

    private void getItems(){
        final ProgressDialog loading=new ProgressDialog(getContext());
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
        RetrofitClient.getApi().getItems(Common.selected_store.getId()).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(response.isSuccessful()){
                    rvItems.setAdapter(new ItemAdapter(response.body(),getContext()));
                    loading.dismiss();
                }else{
                    loading.dismiss();
                    Toast.makeText(getContext(), getString(R.string.failed_to_get_items), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getContext(), getString(R.string.failed_to_get_items), Toast.LENGTH_SHORT).show();

            }
        });


    }
}
