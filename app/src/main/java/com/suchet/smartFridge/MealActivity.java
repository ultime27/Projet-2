package com.suchet.smartFridge;

import static com.suchet.smartFridge.Recipie.SuggestionPageActivity.suggestionPageActivityIntentFactory;
import static com.suchet.smartFridge.stocks.StockActivity.StockIntentFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;



import com.suchet.smartFridge.database.MealAdapter;
import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.Meal;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivityMealBinding;
import java.util.ArrayList;
import java.util.List;

public class MealActivity extends AppCompatActivity implements MealAdapter.OnMealDeletedListener, MealAdapter.OnMealValidatedListener {
    private ActivityMealBinding binding;
    private MealAdapter mealAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMealBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mealAdapter = new MealAdapter(new ArrayList<>(), this, this);  // Ajout du listener pour la validation
        binding.displayStock.setAdapter(mealAdapter);
        binding.displayStock.setLayoutManager(new LinearLayoutManager(this));

        displayStock();
        GoToAddStockActivity();



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



    }

    @Override
    public void onMealDeleted(Meal meal) {
        new Thread(() -> {
            SmartFridgeDatabase db = SmartFridgeDatabase.getDatabase(getApplicationContext());
            db.mealDAO().delete(meal);

            List<Meal> meals = db.mealDAO().getMealsByUser(meal.getUserId());

            runOnUiThread(() -> {
                mealAdapter.updateMealList(meals);
            });
        }).start();
    }


    @Override
    public void onMealValidated(Meal meal) {
        new Thread(() -> {
            SmartFridgeDatabase db = SmartFridgeDatabase.getDatabase(getApplicationContext());

            for (Food food : meal.getFoodList()) {
                Food foodInStock = db.foodDAO().getFoodByName(food.getName(), meal.getUserId());

                if (foodInStock != null) {
                    double newQuantity = foodInStock.getQuantity() - food.getQuantity();
                    if (newQuantity <= 0) {
                        db.foodDAO().delete(foodInStock);
                    } else {
                        foodInStock.setQuantity(newQuantity);
                        db.foodDAO().update(foodInStock);
                    }
                }
            }

            db.mealDAO().delete(meal);
            List<Meal> meals = db.mealDAO().getMealsByUser(meal.getUserId());

            runOnUiThread(() -> {
                mealAdapter.updateMealList(meals);
            });
        }).start();
    }


    private void displayStock() {
        new Thread(() -> {
            SmartFridgeDatabase mealDatabase = SmartFridgeDatabase.getDatabase(getApplicationContext());

            String username = getSharedPreferences("user_session", MODE_PRIVATE).getString("current_username", null);
            if (username == null) return;

            User user = mealDatabase.userDAO().getUserByUsernameSync(username);
            if (user == null) return;

            List<Meal> mealList = mealDatabase.mealDAO().getMealsByUser(user.getId());

            runOnUiThread(() -> mealAdapter.updateMealList(mealList));
        }).start();
    }

    private void GoToAddStockActivity() {
        binding.addFoodInStockButton.setOnClickListener(v -> {
            startActivity(AddMealActivity.AddMealIntentFactory(getApplicationContext()));
        });
    }

    private void backToLanding() {
        binding.backButton.setOnClickListener(v -> {
            startActivity(LandingPage.landingPageActivityIntentFactory(getApplicationContext()));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayStock();
    }

    public static Intent MealIntentFactory(Context context) {
        Intent intent = new Intent(context, MealActivity.class);
        return intent;
    }
}
