package com.suchet.smartFridge.stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.databinding.ActivityDeleteStockBinding;

import java.util.List;

public class DeleteStockActivity extends AppCompatActivity {

    private ActivityDeleteStockBinding binding;
    private List<Food> deleteList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        deleteFromStock();
        backToStock();
    }

    private void backToStock() {
        binding.backToStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(StockActivity.StockIntentFactory(getApplicationContext()));
            }
        });
    }

    private void deleteFromStock() {
        binding.deleteFoodInDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Food food : deleteList) {
                    StockActivity.deleteFoodToStock(getApplicationContext(), food);
                }

                startActivity(StockActivity.StockIntentFactory(getApplicationContext()));
                finish();
            }
        });
    }

    public static Intent DeleteStockIntentFactory(Context context) {
        return new Intent(context, AddStockActivity.class);
    }
}