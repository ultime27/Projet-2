package com.suchet.smartFridge.Recipie;

import static com.suchet.smartFridge.MealActivity.MealIntentFactory;
import static com.suchet.smartFridge.stocks.StockActivity.StockIntentFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.Recipie.APISearch.ApiClient;
import com.suchet.smartFridge.Recipie.APISearch.EdamamApi;
import com.suchet.smartFridge.Recipie.APISearch.Hit;
import com.suchet.smartFridge.Recipie.APISearch.Ingredient;
import com.suchet.smartFridge.Recipie.APISearch.RecipeFromApi;
import com.suchet.smartFridge.Recipie.APISearch.RecipeResponse;
import com.suchet.smartFridge.Settings.SettingActivity;
import com.suchet.smartFridge.database.RecipeDAO;
import com.suchet.smartFridge.database.RecipeDatabase;
import com.suchet.smartFridge.database.entities.Recipe;
import com.suchet.smartFridge.databinding.ActivitySuggestionPageBinding;
import com.suchet.smartFridge.stocks.StockActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        new Thread(() -> {
            List<String> foodForTomorrow = StockActivity.getFoodForTomorrow(getApplicationContext());
            runOnUiThread(() -> adapter.setFoodForTomorrow(foodForTomorrow));
        }).start();
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
        SearchView searchView = binding.searchView;
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

        binding.ApiSearchRecipieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipieName = binding.searchView.getQuery().toString();
                searchFromApi(recipieName);


            }
        });

        binding.HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(suggestionPageActivityIntentFactory(getApplicationContext()));
            }
        });


        binding.CalandarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MealIntentFactory(getApplicationContext()));

            }
        });


        binding.StockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(StockIntentFactory(getApplicationContext()));

            }
        });


        goToSettings();

    }

    private void goToSettings() {
        binding.SettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SettingActivity.SettingIntentFactory(getApplicationContext()));
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
            }
        });
    }

    private void searchFromApi(String query) {
        Log.d("test", "test: " + query);

        EdamamApi api = ApiClient.getRetrofit().create(EdamamApi.class);
        Call<RecipeResponse> call = api.searchRecipes("public", query, "a72dde41", "03ae47d25bd6afa248281143ce30b14d", "a72dde41");

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Response successful: " + response.isSuccessful() + ", body: " + response.body());
                    if (response.body() != null) {
                        Log.d("API_RESPONSE", "Hits size: " + response.body().hits.size());
                    }
                    Log.d("API_RESPONSE", response.toString());
                    for (Hit hit : response.body().hits) {
                        RecipeFromApi recipe = hit.recipe;
                        addRecipieToDatabase(recipe);



                    }
                    Toast.makeText(SuggestionPageActivity.this,
                            "Recipe/s added to the database", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SuggestionPageActivity.this, "Sorry we didn't find recipe for "+query+" but you can create it!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(SuggestionPageActivity.this, "Failure in searchFromApi", Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void addRecipieToDatabase(RecipeFromApi recipeApi) {
        Recipe recipe = new Recipe(recipeApi.label,"from API", recipeApi.url);

        for (Ingredient ingredient: recipeApi.ingredients) {
            recipe.ingredientList.put(ingredient.food, ingredient.weight);
            Log.d("API_RESPONSE_TEST",  ingredient.food +" , "+ ingredient.quantity+" , "+ingredient.measure+" , "+ingredient.weight);

        }
        new Thread(() -> RecipeDatabase.getDatabase(getApplicationContext())
                .recipeDAO()
                .insert(recipe)).start();

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

