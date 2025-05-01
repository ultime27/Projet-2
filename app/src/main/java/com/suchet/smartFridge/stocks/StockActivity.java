package com.suchet.smartFridge.stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.suchet.smartFridge.LandingPage;
import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.entities.Food;
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
            List<Food> foodList = stockDatabase.foodDAO().getAllFoods();

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
        new Thread(() -> {
            SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(context);
            stockDatabase.foodDAO().insert(food);
        }).start();
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