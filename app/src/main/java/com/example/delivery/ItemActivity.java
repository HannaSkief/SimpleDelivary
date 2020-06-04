package com.example.delivery;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delivery.Adapter.CartAdapter;
import com.example.delivery.Common.Cart;
import com.example.delivery.Common.Common;
import com.example.delivery.Model.Item;
import com.example.delivery.Model.ItemRate;
import com.example.delivery.Model.OrderItem;
import com.example.delivery.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

public class ItemActivity extends AppCompatActivity {

    ImageView img,imgShowMyOrders,imgClose,imgRate;
    TextView tvName,tvPrice,tvDetails;
    Item item;
    Button btnAddToCard;
    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        item=Common.selected_item;
        initView();
    }

    private void initView() {
        img=findViewById(R.id.img);
        tvName=findViewById(R.id.tvName);
        tvPrice=findViewById(R.id.tvPrice);
        tvDetails=findViewById(R.id.tvDetails);

        imgClose=findViewById(R.id.imgClose);
        imgShowMyOrders=findViewById(R.id.imgShowMyOrders);
        btnAddToCard=findViewById(R.id.btnAddToCard);

        tvName.setText(item.getName());
        tvPrice.setText(item.getPrice());
        tvDetails.setText(item.getDetail());

        Picasso.get().load(RetrofitClient.item_image_path+item.getImg())
                .fit().centerCrop().into(img);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });

        imgShowMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCartItems();
            }
        });

        imgRate=findViewById(R.id.imgRate);
        imgRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateItem();
            }
        });

    }

    private void rateItem() {
        View view=LayoutInflater.from(this).inflate(R.layout.rating_dialog,null);
        final RatingBar ratingBar=view.findViewById(R.id.ratingBar);
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setView(view);
        dialog.setPositiveButton(getString(R.string.submit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ItemRate rate=new ItemRate(item.getId(),ratingBar.getRating());
                RetrofitClient.getApi().rateItem(rate).enqueue(new Callback<ItemRate>() {
                    @Override
                    public void onResponse(Call<ItemRate> call, Response<ItemRate> response) {
                        if(response.isSuccessful())
                             Toast.makeText(ItemActivity.this, getString(R.string.thanks), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ItemRate> call, Throwable t) {

                    }
                });
            }
        });
        dialog.setNegativeButton(getString(R.string.cancel),null);
        dialog.show();
    }

    private void showCartItems() {
        View view= LayoutInflater.from(this).inflate(R.layout.cart_list,null);
        final RecyclerView rvCard=view.findViewById(R.id.rvCart);
        final TextView tvTotalCost=view.findViewById(R.id.tvTotalCost);
        tvTotalCost.setText(String.valueOf(Cart.getTotalPrice()));
        rvCard.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter =new CartAdapter(this, new CartAdapter.OnOrderItemChange() {
            @Override
            public void onIncClicked(OrderItem item) {
                item.setAmount(item.getAmount()+1);
                cartAdapter.notifyDataSetChanged();
                tvTotalCost.setText(String.valueOf(Cart.getTotalPrice()));
            }

            @Override
            public void onDecClicked(OrderItem item) {
                if(item.getAmount()!=1){
                    item.setAmount(item.getAmount()-1);
                    cartAdapter.notifyDataSetChanged();
                    tvTotalCost.setText(String.valueOf(Cart.getTotalPrice()));
                }
            }

            @Override
            public void onDeleteClicked(OrderItem item) {
                Cart.removeItem(item);
                cartAdapter.notifyDataSetChanged();
                tvTotalCost.setText(String.valueOf(Cart.getTotalPrice()));
            }
        });
        rvCard.setAdapter(cartAdapter);

        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setView(view);
        dialog.setNegativeButton(getString(R.string.cancel),null);
        dialog.show();
    }

    private void addToCart() {
        OrderItem orderItem=new OrderItem();
        orderItem.setAmount(1);
        orderItem.setItemId(item.getId());
        orderItem.setName(item.getName());
        orderItem.setImg(item.getImg());

        orderItem.setPrice(Double.parseDouble(item.getPrice()));

        if(Cart.addItem(orderItem)){
            Toast.makeText(ItemActivity.this, getString(R.string.done), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ItemActivity.this, getString(R.string.item_already_exist), Toast.LENGTH_SHORT).show();
        }
    }

}
