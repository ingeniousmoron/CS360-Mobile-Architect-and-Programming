package com.zybooks.cs360inventoryapp.models;

public class Item {
    long id;
    private String itemName;
    private String itemUnit;
    private int quantity;

    public Item(long id, String itemName, String itemUnit, int quantity) {
        this.itemName = itemName;
        this.itemUnit = itemUnit;
        this.quantity = quantity;
        this.id = id;
    }

    public Item() {
        this.itemName = "";
        this.itemUnit = "";
        this.quantity = 0;
        this.id = 0;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemUnit() {
        return itemUnit;
    }
}
