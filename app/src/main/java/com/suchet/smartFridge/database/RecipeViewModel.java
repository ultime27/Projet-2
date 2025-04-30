package com.suchet.smartFridge.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.suchet.smartFridge.database.entities.Recipe;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private final RecipeDAO recipeDAO;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        RecipeDatabase db = RecipeDatabase.getDatabase(application);
        recipeDAO = db.recipeDAO();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return recipeDAO.getAllRecipesLive(); // méthode qu'on ajoute juste après
    }
}
