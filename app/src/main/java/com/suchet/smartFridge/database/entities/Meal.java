package com.suchet.smartFridge.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity(tableName = "meal_table")
public class Meal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private LocalDate date;

    private List<Food> foodList;

    private int userId;


    public Meal(String name, LocalDate date, List<Food> foodList, int userId) {
        this.name = name;
        this.date = date;
        this.foodList = foodList;
        this.userId = userId;
    }
    @Ignore
    public Meal(){
        this.name = "c";
        this.date = LocalDate.now();
        foodList = new ArrayList<>();
        this.userId =-1;
    }

    // Getters et setters
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}