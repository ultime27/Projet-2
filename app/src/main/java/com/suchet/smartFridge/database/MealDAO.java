package com.suchet.smartFridge.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.suchet.smartFridge.database.entities.Meal;

import java.util.List;

@Dao
public interface MealDAO {
    @Query("SELECT * FROM meal_table WHERE userId = :userId ORDER BY date ASC")
    LiveData<List<Meal>> getMealsByUserId(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Meal meal);
}
