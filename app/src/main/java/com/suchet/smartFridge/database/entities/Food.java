package com.suchet.smartFridge.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.suchet.smartFridge.database.SmartFridgeDatabase;

import java.time.LocalDate;
@Entity(tableName = SmartFridgeDatabase.FOOD_TABLE)
public class Food {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private double quantity;
    private LocalDate datePeremption;
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Food(String name) {
        this.name = name;
        quantity=0.0;
        datePeremption = LocalDate.now().plusDays(7);
    }
    public void add(double _quantity){
        quantity+=_quantity;
    }
    public boolean remove(double _quantity){
        if (_quantity<quantity){
            return false;
        }
        double temp =_quantity;
        return true;
    }

    public LocalDate getDatePeremption() {
        return datePeremption;
    }

    public void setDatePeremption(LocalDate datePeremption) {
        this.datePeremption = datePeremption;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
