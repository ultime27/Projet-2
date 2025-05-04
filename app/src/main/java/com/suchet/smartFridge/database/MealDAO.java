package com.suchet.smartFridge.database;


import androidx.room.Dao;

import androidx.room.Delete;
import androidx.room.Insert;

import androidx.room.Query;
import androidx.room.Update;

import com.suchet.smartFridge.database.entities.Meal;
import com.suchet.smartFridge.database.entities.User;

import java.util.List;

@Dao
public interface MealDAO {
    @Insert
    void insert(Meal meal);

    @Update
    void update(Meal meal);
    @Query("SELECT * FROM " + SmartFridgeDatabase.MEAL_TABLE + " WHERE userId = :userId ORDER BY date")
    List<Meal> getMealsByUser(int userId);

    @Delete
    void delete(Meal meal);

    @Query("SELECT * FROM " + SmartFridgeDatabase.MEAL_TABLE + " WHERE name == :mealName LIMIT 1")
    Meal getMealByName(String mealName);

    @Query("DELETE FROM meal_table WHERE name = :name")
    void deletebyName(String name);
}

