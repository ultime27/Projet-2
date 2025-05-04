package com.suchet.smartFridge.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.suchet.smartFridge.database.entities.Recipe;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Recipe recipe);

    @Query("DELETE FROM recipe_table")
    void deleteAll();

    @Query("DELETE FROM recipe_table WHERE name = :name")
    void deletebyName(String name);

    @Query("SELECT * FROM recipe_table")
    LiveData<List<Recipe>> getAllRecipesLive();

    @Query("SELECT * FROM recipe_table WHERE name LIKE '%' || :name || '%'")
    LiveData<List<Recipe>> searchByNameLive(String name);
    @Query("SELECT * FROM recipe_table WHERE name = :name")
    Recipe searchByName(String name);


}

