package com.suchet.smartFridge.stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.suchet.smartFridge.LandingPage;
import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivityStockBinding;

import java.util.ArrayList;
import java.util.List;

public class StockActivity extends AppCompatActivity {
    private ActivityStockBinding binding;


    private StockAdapter stockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        stockAdapter = new StockAdapter(new ArrayList<>());
        binding.displayStock.setAdapter(stockAdapter);
        binding.displayStock.setLayoutManager(new LinearLayoutManager(this));
        displayStock();
        GoToAddStockActivity();
        backToLanding();
    }

    private void backToLanding() {
        binding.backToLandingFromStockStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LandingPage.landingPageActivityIntentFactory(getApplicationContext()));
            }
        });
    }

    private void displayStock() {
        new Thread(() -> {
            SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(getApplicationContext());

            String username = getSharedPreferences("user_session", MODE_PRIVATE).getString("current_username", null);
            if (username == null) return;

            // ⚠️ Ajoute getUserByUsernameSync si besoin
            User user = stockDatabase.userDAO().getUserByUsernameSync(username);
            if (user == null) return;

            List<Food> foodList = stockDatabase.foodDAO().getFoodByUser(user.getId());

            runOnUiThread(() -> stockAdapter.updateStockList(foodList));
        }).start();
    }

    private void GoToAddStockActivity() {
        binding.addFoodInStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddStockActivity.AddToStockIntentFactory(getApplicationContext()));
            }
        });
    }

    public static void addFoodToStock(Context context, Food food) {
        Log.d("STOCK", "Food 1 : appelle dans stockactivity " + food.getName());
        new Thread(() -> {
            SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(context);
            Log.d("STOCK", "Food 2 : rentre dans thread");
            String username = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
                    .getString("current_username", null);
            Log.d("STOCK", "Food 3 : username ? " + username);
            if (username == null) return;

            User user = stockDatabase.userDAO().getUserByUsernameSync(username);
            Log.d("STOCK", "Food 4 : bon user ? " + user.getUsername());

            food.setUserId(user.getId());
            stockDatabase.foodDAO().insert(food);
        }).start();


    }

    @Override
    protected void onResume() {
        super.onResume();
        displayStock(); // refresh la liste à chaque fois qu’on revient sur l’activité
    }

    public static void deleteFoodToStock(Context context, Food food) {
        SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(context);
        stockDatabase.foodDAO().delete(food);
    }
    public static Intent StockIntentFactory(Context context) {
        Intent intent = new Intent(context, StockActivity.class);
        return intent;
    }
}