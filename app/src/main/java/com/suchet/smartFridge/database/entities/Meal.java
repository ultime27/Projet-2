package com.suchet.smartFridge.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

import com.suchet.smartFridge.database.SmartFridgeDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = SmartFridgeDatabase.MEAL_TABLE)
public class Meal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    @NonNull
    private LocalDate date;

    private int userId;

    private List<Food> foodList;

    public Meal(String name, @NonNull LocalDate date, List<Food> foods, int userId) {
        this.name = name;
        this.date = date;
        this.foodList = foods;
        this.userId = userId;
    }

    public Meal() {
        name = null;
        date = LocalDate.now();
        foodList = new ArrayList<>();
        userId = -1;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }
}