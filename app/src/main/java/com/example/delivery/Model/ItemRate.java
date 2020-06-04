package com.example.delivery.Model;

public class ItemRate {
    private long itemId;
    private float rate;

    public ItemRate() {
    }

    public ItemRate(long itemId, float rate) {
        this.itemId = itemId;
        this.rate = rate;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
