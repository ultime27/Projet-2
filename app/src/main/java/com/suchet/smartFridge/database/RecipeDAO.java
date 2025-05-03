package com.suchet.smartFridge.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.suchet.smartFridge.database.entities.Recipe;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Insert
    void insert(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("SELECT * FROM " + RecipeDatabase.RECIPE_TABLE + " ORDER BY name")
    LiveData<List<Recipe>> getAllRecipesLive();
    //todo: cree les query

    @Query("SELECT * FROM " + RecipeDatabase.RECIPE_TABLE + " WHERE name = :name")
    LiveData<List<Recipe>> searchByNameLive(String name);
    //todo: cree les query

    @Query("SELECT * FROM " + RecipeDatabase.RECIPE_TABLE + " WHERE name = :name")
    Recipe searchByName(String name);
    //todo: cree les query

    @Query("SELECT * FROM " + RecipeDatabase.RECIPE_TABLE + " WHERE recipeId = :recipeId")
    LiveData<Recipe> getRecipeByRecipeId(long recipeId);

    @Query("DELETE FROM " + RecipeDatabase.RECIPE_TABLE + " WHERE recipeId = :recipeId")
    void deleteRecipeByRecipeId(long recipeId);


}