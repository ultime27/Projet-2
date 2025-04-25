package com.suchet.smartFridge.database.entities;

import java.time.LocalDate;

public class FoodBox {
    private LocalDate datePeremption;
    private double quantity;


    public FoodBox(double _quantity){
        quantity =_quantity;
        datePeremption = LocalDate.now().plusDays(10);

    }

    public double remove(double _quantity){
        double temp =_quantity;
        if(temp > quantity){
            temp -= quantity;
            quantity=0;
            return temp;
        }
        if (temp>0){
            quantity-=temp;

        }
        return 0;
    }

    public LocalDate getDatePeremption() {
        return datePeremption;
    }

    public void setDatePeremption(LocalDate datePeremption) {
        this.datePeremption = datePeremption;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
