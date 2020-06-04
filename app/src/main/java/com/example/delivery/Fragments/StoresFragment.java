package com.example.delivery.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.delivery.Adapter.StoreAdapter;
import com.example.delivery.Model.Store;
import com.example.delivery.R;
import com.example.delivery.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoresFragment extends Fragment {

    private RecyclerView rvStore;
    private View view;
    private FragmentActivity fragmentActivity;
    private StoreAdapter adapter;
    private static List<Store> storeList=new ArrayList<>();
    LinearLayout refersh;


    @Override
    public void onAttach(Context context) {
        fragmentActivity =(FragmentActivity)context;
        super.onAttach(context);
    }

    public StoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_stores, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refersh=view.findViewById(R.id.refresh);
        refersh.setVisibility(View.GONE);
        rvStore = view.findViewById(R.id.rvStores);
        rvStore.setHasFixedSize(true);
        rvStore.setLayoutManager(new GridLayoutManager(getContext(), 2));
        if (storeList.isEmpty())
            getStores();
        else {
            adapter = new StoreAdapter(storeList, getContext(), fragmentActivity);
            rvStore.setAdapter(adapter);
        }

        refersh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStores();
            }
        });
    }
    private void getStores() {
        refersh.setVisibility(View.GONE);

        final ProgressDialog loading=new ProgressDialog(getContext());
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();

        RetrofitClient.getApi().getStores().enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if(response.isSuccessful()){
                    storeList=new ArrayList<>(response.body());
                    adapter=new StoreAdapter(storeList,getContext(), fragmentActivity);
                    rvStore.setAdapter(adapter);
                    loading.dismiss();
                    refersh.setVisibility(View.GONE);
                }else{
                    loading.dismiss();
                    Toast.makeText(fragmentActivity, getString(R.string.failed_to_get_stores), Toast.LENGTH_SHORT).show();
                    refersh.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(fragmentActivity, getString(R.string.failed_to_get_stores), Toast.LENGTH_SHORT).show();
                refersh.setVisibility(View.VISIBLE);
            }
        });

    }

}
