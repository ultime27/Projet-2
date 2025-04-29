package com.suchet.smartFridge.database.stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.suchet.smartFridge.LoginActivity;
import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.databinding.ActivityAddStockBinding;
import com.suchet.smartFridge.databinding.ActivityStockBinding;

public class AddStockActivity extends AppCompatActivity {

    private ActivityAddStockBinding binding;
    private String name;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void addToStock() {
        String name = binding.foodNameEditText.getText().toString().trim();
        String countStr = binding.foodAmountEditText.getText().toString().trim();

        if (name.isEmpty() || countStr.isEmpty()) {
            return;
        }

        int count = Integer.parseInt(countStr);
        binding.addToTheStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food food = new Food(name);
                food.setQuantity(count);
                StockActivity.addFoodToStock(getApplicationContext(), food);
            }
        });
    }
    static Intent AddToStockIntentFactory(Context context) {
        return new Intent(context, AddStockActivity.class);
    }

    //TODO : demander comment on peut verif que ca add a la db
}