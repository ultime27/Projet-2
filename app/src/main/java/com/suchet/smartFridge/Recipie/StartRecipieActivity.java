package com.suchet.smartFridge.Recipie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.RecipeDAO;
import com.suchet.smartFridge.database.RecipeDatabase;
import com.suchet.smartFridge.database.entities.Recipe;
import com.suchet.smartFridge.databinding.ActivityStartRecipieBinding;

public class StartRecipieActivity extends AppCompatActivity {


    ActivityStartRecipieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartRecipieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String recipeName = getIntent().getStringExtra("recipeName");
        Toast.makeText(this, "toastpass: "+recipeName, Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            Recipe recipe = RecipeDatabase.getDatabase(getApplicationContext()).recipeDAO().searchByName(recipeName);

            runOnUiThread(() -> {
                if (recipe != null) {
                    binding.recipieNameTextView.setText(recipe.name);
                    binding.recipieDescriptionTextView.setText(recipe.description);
                    binding.recipieIngredientTextView.setText(recipe.toString());
                    binding.recipieInstructionTextView.setText(recipe.instruction);

                } else {
                    binding.recipieNameTextView.setText("Unknown Recipe");
                }
            });
        }).start();

        binding.finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SuggestionPageActivity.suggestionPageActivityIntentFactory(getApplicationContext()));
            }
        });

    }
    public static Intent StartRecipieActivityFactory(Context context, String recipieName) {
        Intent intent = new Intent(context, StartRecipieActivity.class);
        intent.putExtra("recipeName", recipieName);
        return intent;
    }
}