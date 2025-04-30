package com.suchet.smartFridge.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity(tableName = "meal_table")
public class Meal {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    @NonNull
    private LocalDate date;

    private List<Food> foodList;

    public Meal(String name, @NonNull LocalDate date,List<Food> foods) {
        this.name = name;
        this.date = date;
        this.foodList = foods;
    }

    public Meal(){
        name = null;
        date = LocalDate.now();
        foodList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    public void setDate(@NonNull LocalDate date) {
        this.date = date;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }
}
