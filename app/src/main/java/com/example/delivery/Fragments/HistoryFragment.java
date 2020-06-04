package com.example.delivery.Fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.delivery.Adapter.HistoryAdapter;
import com.example.delivery.Model.Order;
import com.example.delivery.R;
import com.example.delivery.retrofit.RetrofitClient;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    View view;
    RecyclerView rvHistory;
    SharedPreferences preferences;
    long userId;



    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_history, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rvHistory=view.findViewById(R.id.rvHistory);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvHistory.setLayoutManager(linearLayoutManager);
        preferences= PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId=preferences.getLong("userId",0);

        getHistoryRecord();
    }

    private void getHistoryRecord() {
        final ProgressDialog loading = new ProgressDialog(getContext());
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();

        RetrofitClient.getApi().getOrders(userId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response.isSuccessful()){
                    rvHistory.setAdapter(new HistoryAdapter(response.body(),getContext()));
                    loading.dismiss();
                }else{
                    loading.dismiss();
                    Toast.makeText(getContext(), getString(R.string.failed_to_get_history_record), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getContext(), getString(R.string.failed_to_get_history_record), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
