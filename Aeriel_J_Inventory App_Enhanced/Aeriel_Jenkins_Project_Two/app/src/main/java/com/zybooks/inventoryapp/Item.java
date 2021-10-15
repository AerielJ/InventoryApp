package com.zybooks.inventoryapp;

public class Item {
    private String mName;
    private int mQuantity;

    public Item(String name, int quantity) {
        mName = name;
        mQuantity = quantity;
    }

    public String getName() {
        return mName;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {mQuantity = quantity;}

}
