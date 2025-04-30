package com.suchet.smartFridge.Recipie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.RecipeDatabase;
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
        adapter.setOnRecipeClickListener(recipe -> {
            Toast.makeText(this, "Clicked: " + recipe.getName(), Toast.LENGTH_SHORT).show();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, RecipeFragment.newInstance(
                            recipe.getName(),
                            recipe.getDescription(),
                            recipe.instruction
                    ))
                    .addToBackStack(null)
                    .commit();
        });

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                observeFilteredResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                observeFilteredResults(newText);
                return true;
            }
        });



    }
    private void observeFilteredResults(String name) {
        recipeViewModel.searchRecipes(name).observe(this, adapter::setRecipes);
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

