package com.suchet.smartFridge.database.stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.suchet.smartFridge.database.StockDatabase;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.databinding.ActivityStockBinding;

import java.util.List;

public class StockActivity extends AppCompatActivity {
    private ActivityStockBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GoToAddStockActivity();
    }

    private void GoToAddStockActivity() {
        binding.addFoodInStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddStockActivity.AddToStockIntentFactory(getApplicationContext()));
            }
        });
    }

    private void addButton() {
        binding.addFoodInStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddStockActivity.AddToStockIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    public static String getStock(Context context){
        StockDatabase stockDatabase = StockClient.getInstance(context).getStockDatabase();
        List<Food> list = stockDatabase.foodDAO().getAllFoods();
        StringBuilder stock = new StringBuilder();
        for (Food food : list) {
            stock.append(food.getName()).append(" - ").append(food.getQuantity()).append("\n");
        }
        return stock.toString();
    }

    public static void addFoodToStock(Context context, Food food) {
        StockDatabase stockDatabase = StockClient.getInstance(context).getStockDatabase();
        stockDatabase.foodDAO().insert(food);
    }

    public static void deleteFoodToStock(Context context, Food food) {
        StockDatabase stockDatabase = StockClient.getInstance(context).getStockDatabase();
        stockDatabase.foodDAO().delete(food);
    }

    public static Intent StockIntentFactory(Context context) {
        return new Intent(context, StockActivity.class);
    }
}