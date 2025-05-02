package com.suchet.smartFridge.Recipie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.suchet.smartFridge.database.RecipeDAO;
import com.suchet.smartFridge.database.RecipeDatabase;
import com.suchet.smartFridge.database.entities.Recipe;
import com.suchet.smartFridge.databinding.ActivityStartRecipieBinding;

public class StartRecipieActivity extends AppCompatActivity {


    ActivityStartRecipieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        String recipeName = getIntent().getStringExtra("recipeName");
        Recipe recipe = RecipeDatabase.getDatabase(getApplicationContext()).recipeDAO().searchByName(recipeName);
        if (recipe != null) {
            binding.recipieNameTextView.setText(recipe.name);
            binding.recipieDescriptionTextView.setText(recipe.description);
            binding.recipieIngredientTextView.setText(recipe.toString());
            binding.recipieInstructionTextView.setText(recipe.instruction);
        }

    }
    public static Intent StartRecipieActivityFactory(Context context, String recipieName) {
        Intent intent = new Intent(context, StartRecipieActivity.class);
        intent.putExtra("recipieName", recipieName);
        return intent;
    }
}