package com.suchet.smartFridge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.Meal;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivityAddMealBinding;
import com.suchet.smartFridge.Recipie.RecipeAdapteur;
import com.suchet.smartFridge.Recipie.RecipeViewModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddMealActivity extends AppCompatActivity {

    private ActivityAddMealBinding binding;
    private List<Food> ingredients = new ArrayList<>();
    private String name;
    private LocalDate selectedDate = null;
    private RecipeViewModel recipeViewModel;
    private RecipeAdapteur recipeAdapteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMealBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        recipeAdapteur = new RecipeAdapteur();
        binding.recipiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recipiesRecyclerView.setAdapter(recipeAdapteur);

        setupDatePicker();
        setupAddIngredient();
        setupAddMeal();
        setupBackButton();

        binding.mealNameEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    recipeViewModel.searchRecipes(query).observe(AddMealActivity.this, recipes -> {
                        if (recipes != null && !recipes.isEmpty()) {
                            recipeAdapteur.setRecipes(recipes);
                            binding.recipiesRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            binding.recipiesRecyclerView.setVisibility(View.GONE);
                        }
                    });
                } else {
                    binding.recipiesRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void setupDatePicker() {
        binding.selectDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                    AddMealActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                        binding.selectedDateTextView.setText("Date du repas : " + selectedDate.toString());
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void setupAddIngredient() {
        binding.addIngredientButton.setOnClickListener(v -> {
            String ingredientName = binding.ingredientNameEditText.getText().toString().trim();
            String quantityStr = binding.ingredientQuantityEditText.getText().toString().trim();

            if (ingredientName.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir le nom et la quantité de l'ingrédient", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Quantité invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            Food ingredient = new Food(ingredientName);
            ingredient.setQuantity(quantity);
            ingredients.add(ingredient);

            updateIngredientsList();

            binding.ingredientNameEditText.setText("");
            binding.ingredientQuantityEditText.setText("");
        });
    }

    private void setupAddMeal() {
        binding.addMealButton.setOnClickListener(v -> {
            name = binding.mealNameEditText.getText().toString().trim();

            if (name.isEmpty() || selectedDate == null || ingredients.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer toutes les informations (nom, date, ingrédients)", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                SmartFridgeDatabase db = SmartFridgeDatabase.getDatabase(getApplicationContext());

                String username = getSharedPreferences("user_session", MODE_PRIVATE).getString("current_username", null);
                if (username == null) return;

                User user = db.userDAO().getUserByUsernameSync(username);
                if (user == null) return;

                Meal meal = new Meal(name, selectedDate, ingredients, user.getId());
                db.mealDAO().insert(meal);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Repas ajouté !", Toast.LENGTH_SHORT).show();
                    startActivity(MealActivity.MealIntentFactory(this));
                    finish();
                });
            }).start();
        });
    }

    private void setupBackButton() {
        binding.backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void updateIngredientsList() {
        StringBuilder ingredientsText = new StringBuilder("Ingrédients ajoutés :\n");
        for (Food f : ingredients) {
            ingredientsText.append("- ").append(f.getName()).append(" : ").append(f.getQuantity()).append("\n");
        }
        binding.ingredientsListTextView.setText(ingredientsText.toString());
    }

    public static Intent AddToMealActivity(Context context) {
        return new Intent(context, AddMealActivity.class);
    }
}