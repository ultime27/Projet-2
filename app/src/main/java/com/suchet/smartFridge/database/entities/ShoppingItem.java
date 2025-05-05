package com.suchet.smartFridge.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_items")
public class ShoppingItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private double quantity;
    private boolean checked;
    private int userId;

    public ShoppingItem(String name) {
        this.name = name;
        this.checked = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public boolean isChecked() {
        return checked;
    }

    public int getUserId() {
        return userId;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
