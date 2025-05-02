package com.suchet.smartFridge.Recipie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.RecipeDatabase;
import com.suchet.smartFridge.database.entities.Recipe;
import com.suchet.smartFridge.databinding.ActivitySuggestionPageBinding;

import java.util.List;

public class SuggestionPageActivity extends AppCompatActivity {
    private RecipeAdapteur adapter;
    private RecipeViewModel recipeViewModel;

    private ActivitySuggestionPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySuggestionPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recipiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapteur();
        binding.recipiesRecyclerView.setAdapter(adapter);

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

        binding.CreateRecipieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(createRecipieActivity.createRecipieActivityIntentFactory(getApplicationContext()));
            }
        });




    }
    private void observeFilteredResults(String query) {
        if (query.isEmpty()) {
            recipeViewModel.getAllRecipes().observe(this, adapter::setRecipes);
            return;
        }

        recipeViewModel.searchRecipes(query).observe(this, localResults -> {
            if (localResults != null && !localResults.isEmpty()) {
                adapter.setRecipes(localResults);
            } else {
                searchFromApiAndInsert(query);
            }
        });
    }

    private void searchFromApiAndInsert(String query) {
        //TODO
    }



    public static Intent suggestionPageActivityIntentFactory(Context context) {
        return new Intent(context, SuggestionPageActivity.class);
    }



    private void loadRecipes() {
        RecipeDatabase.getDatabase(getApplicationContext())
                .recipeDAO()
                .getAllRecipesLive()
                .observe(this, recipes -> {
                    adapter.setRecipes(recipes);
                });
    }
}

