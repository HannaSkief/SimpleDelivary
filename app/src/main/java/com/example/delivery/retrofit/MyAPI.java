package com.example.delivery.retrofit;

import com.example.delivery.Model.Item;
import com.example.delivery.Model.ItemRate;
import com.example.delivery.Model.Order;
import com.example.delivery.Model.OrderItem;
import com.example.delivery.Model.Store;
import com.example.delivery.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyAPI {

    @POST("login")
    Call<User> login(@Body User user);

    @POST("register")
    Call<User> register(@Body User user);

    @GET("stores")
    Call<List<Store>> getStores();

    @GET("items/{storeId}")
    Call<List<Item>> getItems(@Path("storeId")long id);

    @POST("orders")
    Call<Order> order(@Body Order order);

    @POST("orderItems")
    @FormUrlEncoded
    Call<List<OrderItem>> orderItem(@Field("order_item")String itemList,
                                    @Field("orderId")long orderId);

    @GET("orders/{userId}")
    Call<List<Order>> getOrders(@Path("userId") long id);

    @GET("orderItems/{orderId}")
    Call<List<OrderItem>> getOrderItems(@Path("orderId") long orderId);

    @POST("itemRates")
    Call<ItemRate> rateItem(@Body ItemRate rate);


}
