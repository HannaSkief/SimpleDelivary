package com.example.delivery.Common;

import com.example.delivery.Model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private static List<OrderItem> cartItems =new ArrayList<>();


    public static boolean addItem(OrderItem item){
        if(!cartItems.contains(item)) {
            cartItems.add(item);
            return true;
        }
        return false;
    }

    public static void removeItem(OrderItem item){
        if(cartItems.contains(item)) {
            cartItems.remove(item);
        }
    }

    public static void clear(){
        cartItems.clear();
    }

    public static List<OrderItem> getCartItems(){
        return cartItems;
    }

    public static double getTotalPrice() {
        double totalPrice=0;
        for(OrderItem item:cartItems){
            totalPrice+=(item.getPrice()*item.getAmount());
        }
        return totalPrice;
    }

    public static void setCartOrderId(long id){
        for (OrderItem item:cartItems){
            item.setOrderId(id);
        }
    }

}
