package com.example.delivery.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit instance;
    private static String url="delivary.flex-sol.co/api/";
    public static String store_image_path ="https://delivary.flex-sol.co/public/img/stores/";
    public static String item_image_path="https://delivary.flex-sol.co/public/img/items/";

    private static Retrofit getInstance(){

        if(instance==null){
            instance=new Retrofit.Builder()
                    .baseUrl("https://"+url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return instance;
    }

    public static MyAPI getApi(){
        return getInstance().create(MyAPI.class);
    }
}
