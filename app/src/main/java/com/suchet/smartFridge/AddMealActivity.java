package com.suchet.smartFridge;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.Meal;
import com.suchet.smartFridge.database.entities.Recipe;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivityAddMealBinding;
import com.suchet.smartFridge.Recipie.RecipeAdapteur;
import com.suchet.smartFridge.Recipie.RecipeViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class AddMealActivity extends AppCompatActivity {

    private ActivityAddMealBinding binding;
    private List<Food> ingredients = new ArrayList<>();
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
        setupRecipeSuggestions();
        setupAddIngredient();
        setupAddMeal();
        setupBackButton();
    }

    private void setupRecipeSuggestions() {
        binding.mealNameEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String q = s.toString().trim();
                if (!q.isEmpty()) {
                    recipeViewModel.searchRecipes(q).observe(AddMealActivity.this, recipes -> {
                        if (recipes != null && !recipes.isEmpty()) {
                            recipeAdapteur.setRecipes(recipes);
                            binding.recipiesRecyclerView.setVisibility(android.view.View.VISIBLE);
                        } else {
                            binding.recipiesRecyclerView.setVisibility(android.view.View.GONE);
                        }
                    });
                } else {
                    binding.recipiesRecyclerView.setVisibility(android.view.View.GONE);
                }
            }

            @Override public void afterTextChanged(android.text.Editable s) {}
        });

        recipeAdapteur.setOnRecipeClickListener(new RecipeAdapteur.OnRecipeClickListener() {
            @Override
            public void onRecipeClick(Recipe recipe) {
                new android.app.AlertDialog.Builder(AddMealActivity.this)
                        .setTitle("Add Recipe to Meal")
                        .setMessage("Do you want to add the recipe \"" + recipe.getName() + "\" to your meal?")
                        .setPositiveButton("Yes", (dialog, which) -> addRecipeToMeal(recipe))
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void addRecipeToMeal(Recipe recipe) {
        List<Food> recipeIngredients = new ArrayList<>();

        for (Map.Entry<String, Double> entry : recipe.getIngredientList().entrySet()) {
            Food food = new Food(entry.getKey());
            food.setQuantity(entry.getValue());
            recipeIngredients.add(food);
        }

        getUserIdFromSharedPrefs(userId -> {
            if (userId == -1) {
                Toast.makeText(AddMealActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                return;
            }

            String mealName = recipe.getName();
            if (selectedDate == null){
                selectedDate = LocalDate.now();
            }
            new Thread(() -> {
                SmartFridgeDatabase db = SmartFridgeDatabase.getDatabase(getApplicationContext());
                Meal newMeal = new Meal(mealName, selectedDate, recipeIngredients, userId);
                db.mealDAO().insert(newMeal);
                runOnUiThread(() -> {
                    Toast.makeText(AddMealActivity.this, "Recipe added to your meals!", Toast.LENGTH_SHORT).show();
                    startActivity(MealActivity.MealIntentFactory(AddMealActivity.this));
                    finish();
                });
            }).start();
        });
    }

    private void getUserIdFromSharedPrefs(final UserIdCallback callback) {
        String username = getSharedPreferences("user_session", MODE_PRIVATE)
                .getString("current_username", null);
        if (username == null) {
            callback.onUserIdFetched(-1);
            return;
        }

        new Thread(() -> {
            SmartFridgeDatabase db = SmartFridgeDatabase.getDatabase(getApplicationContext());
            User user = db.userDAO().getUserByUsernameSync(username);
            int userId;
            if (user != null) userId = user.getId();
            else userId = -1;
            runOnUiThread(() -> callback.onUserIdFetched(userId));
        }).start();
    }

    public interface UserIdCallback {
        void onUserIdFetched(int userId);
    }

    private void setupDatePicker() {
        binding.selectDateButton.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, year, month, day) -> {
                        selectedDate = LocalDate.of(year, month + 1, day);
                        binding.selectedDateTextView.setText("Meal date: " + selectedDate);
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private void setupAddIngredient() {
        binding.addIngredientButton.setOnClickListener(v -> {
            String name = binding.ingredientNameEditText.getText().toString().trim();
            String qtyStr = binding.ingredientQuantityEditText.getText().toString().trim();
            if (name.isEmpty() || qtyStr.isEmpty()) return;
            int qty = Integer.parseInt(qtyStr);
            Food f = new Food(name);
            f.setQuantity(qty);
            ingredients.add(f);
            updateIngredientsList();
            checkAndHandleStock(name, qty);
            binding.ingredientNameEditText.setText("");
            binding.ingredientQuantityEditText.setText("");
        });
    }

    private void checkAndHandleStock(String name, int neededQty) {
        new Thread(() -> {
            SmartFridgeDatabase db = SmartFridgeDatabase.getDatabase(getApplicationContext());
            String usern = getSharedPreferences("user_session", MODE_PRIVATE)
                    .getString("current_username", null);
            if (usern == null) return;
            User user = db.userDAO().getUserByUsernameSync(usern);
            if (user == null) return;
            Food stock = db.foodDAO().getFoodByName(name, user.getId());
            if (stock == null) {
                runOnUiThread(() -> showAddToStockDialog(name, neededQty, user.getId()));
            } else if (stock.getQuantity() < neededQty) {
                runOnUiThread(() -> showIncreaseStockDialog(stock, neededQty - stock.getQuantity()));
            }
        }).start();
    }

    private void showAddToStockDialog(String name, int qty, int userId) {
        SmartFridgeDatabase db = SmartFridgeDatabase.getDatabase(getApplicationContext());
        AlertDialog.Builder b = new AlertDialog.Builder(this)
                .setTitle("Add to stock: " + name);
        android.widget.EditText input = new android.widget.EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setHint("Quantity to add");
        android.widget.LinearLayout l = new android.widget.LinearLayout(this);
        l.setOrientation(android.widget.LinearLayout.VERTICAL);
        int p = (int)(24 * getResources().getDisplayMetrics().density);
        l.setPadding(p, p, p, p);
        l.addView(input);
        b.setView(l)
                .setPositiveButton("Add", (d, w) -> {
                    int add = Integer.parseInt(input.getText().toString().trim());
                    Food newF = new Food(name);
                    newF.setQuantity(add);
                    newF.setUserId(userId);
                    new Thread(() -> db.foodDAO().insert(newF)).start();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showIncreaseStockDialog(Food stock, double missingQty) {
        AlertDialog.Builder b = new AlertDialog.Builder(this)
                .setTitle("Insufficient stock: " + stock.getName())
                .setMessage("In stock: " + stock.getQuantity() + ", needed: +" + missingQty);
        b.setPositiveButton("Increase", (d, w) -> {
                    stock.setQuantity(stock.getQuantity() + missingQty);
                    new Thread(() -> {
                        SmartFridgeDatabase.getDatabase(getApplicationContext())
                                .foodDAO().update(stock);
                    }).start();
                })
                .setNegativeButton("Ignore", null)
                .show();
    }

    private void setupAddMeal() {
        binding.addMealButton.setOnClickListener(v -> {
            String m = binding.mealNameEditText.getText().toString().trim();
            if (m.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }
            if(selectedDate == null){
                selectedDate = LocalDate.now() ;
            }
            if(ingredients.isEmpty()){
                ingredients = new ArrayList<>();
            }
            new Thread(() -> {
                SmartFridgeDatabase db = SmartFridgeDatabase.getDatabase(getApplicationContext());
                String u = getSharedPreferences("user_session", MODE_PRIVATE)
                        .getString("current_username", null);
                if (u == null) return;
                User user = db.userDAO().getUserByUsernameSync(u);
                if (user == null) return;
                Meal meal = new Meal(m, selectedDate, ingredients, user.getId());
                db.mealDAO().insert(meal);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Meal added!", Toast.LENGTH_SHORT).show();
                    startActivity(MealActivity.MealIntentFactory(this));
                    finish();
                });
            }).start();
        });
    }
    private void setupBackButton() {
        binding.backButton.setOnClickListener(v -> finish());
    }
    private void updateIngredientsList() {
        StringBuilder sb = new StringBuilder("Ingredients:\n");
        for (Food f : ingredients) {
            sb.append("- ").append(f.getName()).append(" : ").append(f.getQuantity()).append(" g\n");
        }
        binding.ingredientsListTextView.setText(sb.toString());
    }

    public static Intent AddMealIntentFactory(Context context) {
        return new Intent(context, AddMealActivity.class);
    }
}