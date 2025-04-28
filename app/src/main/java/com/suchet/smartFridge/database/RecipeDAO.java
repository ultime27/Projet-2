package com.suchet.smartFridge.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.suchet.smartFridge.database.entities.Recipe;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Insert
    void insert(Recipe recipe);

    @Query("SELECT * FROM recipe_table")
    List<Recipe> getAllRecipes();

    @Query("DELETE FROM recipe_table")
    void deleteAll();
    //todo: cree les query
}

