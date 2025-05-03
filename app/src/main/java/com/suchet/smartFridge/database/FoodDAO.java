package com.suchet.smartFridge.database;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.suchet.smartFridge.database.entities.Food;

import java.util.List;

@Dao
public interface FoodDAO {
    @Insert
    void insert(Food food);

    @Update
    void update(Food food);

    @Query("DELETE FROM "+ SmartFridgeDatabase.FOOD_TABLE)
    void deleteAll();

    @Delete
    void delete(Food food);

    @Query("SELECT * FROM " +  SmartFridgeDatabase.FOOD_TABLE)
    List<Food> getAllFoods();

    @Query("SELECT * FROM " + SmartFridgeDatabase.FOOD_TABLE + " WHERE userId = :userId")
    List<Food> getFoodByUser(int userId);

    @Query("SELECT * FROM " + SmartFridgeDatabase.FOOD_TABLE + " WHERE name = :name AND userId = :userId LIMIT 1")
    Food getFoodByName(String name, int userId);

}
