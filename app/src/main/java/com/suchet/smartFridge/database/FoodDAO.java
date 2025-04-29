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

    @Query("DELETE FROM "+ StockDatabase.STOCK_TABLE)
    void deleteAll();

    @Delete
    void delete(Food food);

    @Query("SELECT * FROM " + StockDatabase.STOCK_TABLE)
    List<Food> getAllFoods();
}
