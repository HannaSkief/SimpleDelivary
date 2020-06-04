package com.example.delivery.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delivery.Adapter.CartAdapter;
import com.example.delivery.Common.Cart;
import com.example.delivery.Model.Order;
import com.example.delivery.Model.OrderItem;
import com.example.delivery.R;
import com.example.delivery.retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements CartAdapter.OnOrderItemChange {

    View view;
    TextView tvTotalCost;
    RecyclerView rvCart;
    CartAdapter adapter;
    Button btnOrder;
    SharedPreferences preferences;
    long userId;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnOrder = view.findViewById(R.id.btnOrder);
        tvTotalCost = view.findViewById(R.id.tvTotalCost);
        rvCart = view.findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(getContext(), this);
        rvCart.setAdapter(adapter);
        tvTotalCost.setText(String.valueOf(Cart.getTotalPrice()));
        preferences= PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId=preferences.getLong("userId",0);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Cart.getCartItems().isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.the_cart_is_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
                dialog.setTitle(getString(R.string.are_you_sure));
                dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        order();
                    }
                });
                dialog.setNegativeButton(getString(R.string.cancel),null);
                dialog.show();
            }
        });
    }

    private void order() {

        final ProgressDialog loading = new ProgressDialog(getContext());
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(Cart.getTotalPrice());
        RetrofitClient.getApi().order(order).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    Cart.setCartOrderId(response.body().getId());
                    RetrofitClient.getApi().orderItem(new Gson().toJson(Cart.getCartItems()), response.body().getId()).enqueue(new Callback<List<OrderItem>>() {
                        @Override
                        public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                            if (response.isSuccessful()) {
                                loading.dismiss();
                                Cart.clear();
                                adapter.notifyDataSetChanged();
                                tvTotalCost.setText("0");
                                Toast.makeText(getContext(), getString(R.string.done), Toast.LENGTH_SHORT).show();
                            } else {
                                loading.dismiss();
                                Toast.makeText(getContext(), getString(R.string.failed_to_make_an_order), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                            loading.dismiss();
                            Toast.makeText(getContext(), getString(R.string.failed_to_make_an_order), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    loading.dismiss();
                    Toast.makeText(getContext(), getString(R.string.failed_to_make_an_order), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getContext(), getString(R.string.failed_to_make_an_order), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onIncClicked(OrderItem item) {
        item.setAmount(item.getAmount() + 1);
        adapter.notifyDataSetChanged();
        tvTotalCost.setText(String.valueOf(Cart.getTotalPrice()));
    }

    @Override
    public void onDecClicked(OrderItem item) {
        if (item.getAmount() != 1) {
            item.setAmount(item.getAmount() - 1);
            adapter.notifyDataSetChanged();
            tvTotalCost.setText(String.valueOf(Cart.getTotalPrice()));
        }
    }

    @Override
    public void onDeleteClicked(OrderItem item) {
        Cart.removeItem(item);
        adapter.notifyDataSetChanged();
        tvTotalCost.setText(String.valueOf(Cart.getTotalPrice()));
    }
}
