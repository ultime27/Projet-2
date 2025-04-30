package com.suchet.smartFridge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suchet.smartFridge.database.RecipeAdapteur;
import com.suchet.smartFridge.database.RecipeDatabase;
import com.suchet.smartFridge.database.RecipeViewModel;
import com.suchet.smartFridge.database.entities.Recipe;

import java.util.List;

public class SuggestionPageActivity extends AppCompatActivity {

    private RecipeAdapteur adapter;

    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_page);

        RecyclerView recyclerView = findViewById(R.id.recipies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapteur();
        recyclerView.setAdapter(adapter);

        // LiveData + ViewModel
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipeViewModel.getAllRecipes().observe(this, recipes -> {
            adapter.setRecipes(recipes);
        });
    }

    public static Intent suggestionPageActivityIntentFactory(Context context) {
        return new Intent(context, SuggestionPageActivity.class);
    }



    private void loadRecipes() {
        RecipeDatabase.getExecutor().execute(() -> {
            List<Recipe> filteredRecipes = RecipeDatabase
                    .getDatabase(getApplicationContext())
                    .recipeDAO()
                    .getAllRecipes();


            runOnUiThread(() -> adapter.setRecipes(filteredRecipes));
        });
    }
}

