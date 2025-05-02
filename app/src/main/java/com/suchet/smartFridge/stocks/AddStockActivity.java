package com.suchet.smartFridge.stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.databinding.ActivityAddStockBinding;

public class AddStockActivity extends AppCompatActivity {

    private ActivityAddStockBinding binding;
    private String name;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addToStock();
        backToStock();
    }

    private void backToStock() {
        binding.BackToStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(StockActivity.StockIntentFactory(getApplicationContext()));
            }
        });
    }

    private void addToStock() {
        binding.addToTheStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.foodNameEditText.getText().toString().trim();
                String countStr = binding.foodAmountEditText.getText().toString().trim();

                if (name.isEmpty() || countStr.isEmpty()) {
                    return;
                }

                count = Integer.parseInt(countStr);
                Food food = new Food(name);
                food.setQuantity(count);

                StockActivity.addFoodToStock(getApplicationContext(), food);
                startActivity(StockActivity.StockIntentFactory(getApplicationContext()));
                finish();
            }
        });
    }

    static Intent AddToStockIntentFactory(Context context) {
        return new Intent(context, AddStockActivity.class);
    }

}