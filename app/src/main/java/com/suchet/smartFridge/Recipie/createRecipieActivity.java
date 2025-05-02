package com.suchet.smartFridge.Recipie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.suchet.smartFridge.database.RecipeDatabase;
import com.suchet.smartFridge.database.entities.Recipe;
import com.suchet.smartFridge.databinding.ActivityAddRecipieBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class createRecipieActivity extends AppCompatActivity {

    private ActivityAddRecipieBinding binding;
    private IngredientsAdapter ingredientsAdapter;
    private List<String> ingredientNames = new ArrayList<>();
    private List<Double> ingredientQuantities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRecipieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ingredientsAdapter = new IngredientsAdapter(ingredientNames, ingredientQuantities);
        binding.ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.ingredientsRecyclerView.setAdapter(ingredientsAdapter);


        binding.addIngredientButton.setOnClickListener(v -> {
            ingredientNames.add("Nouvel ingrédient");
            ingredientQuantities.add(0.0);
            ingredientsAdapter.notifyItemInserted(ingredientNames.size() - 1);
        });


        binding.saveRecipeButton.setOnClickListener(v -> saveRecipe());
    }

    private void saveRecipe() {
        String name = binding.recipeNameEditText.getText().toString();
        String description = binding.recipeDescriptionEditText.getText().toString();
        String instruction = binding.recipeInstructionEditText.getText().toString();

        HashMap<String, Double> ingredientMap = new HashMap<>();
        for (int i = 0; i < ingredientNames.size(); i++) {
            ingredientMap.put(ingredientNames.get(i), ingredientQuantities.get(i));
        }

        Recipe recipe = new Recipe(name, ingredientMap, description, instruction);

        new Thread(() -> RecipeDatabase.getDatabase(getApplicationContext())
                .recipeDAO()
                .insert(recipe)).start();
        startActivity(SuggestionPageActivity.suggestionPageActivityIntentFactory(this));



    }

    public static Intent createRecipieActivityIntentFactory(Context context) {
        return new Intent(context, createRecipieActivity.class);
    }

}
