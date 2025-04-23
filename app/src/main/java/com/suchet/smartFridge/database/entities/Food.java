package com.suchet.smartFridge.database.entities;

import java.time.LocalDate;

public class Food {
    private String name;
    private LocalDate expirationDate;
    private int quantity;

    public Food(String name, LocalDate expirationDate, int quantity) {
        this.name = name;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }


    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
