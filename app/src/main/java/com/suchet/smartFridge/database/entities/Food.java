package com.suchet.smartFridge.database.entities;

import java.time.LocalDate;

public class Food {
    private String name;
    private double quantity;
    private LocalDate datePeremption;

    public Food(String name) {
        this.name = name;
        quantity=0.0;

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
