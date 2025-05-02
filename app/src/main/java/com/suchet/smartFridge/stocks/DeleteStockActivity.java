package com.suchet.smartFridge.stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivityDeleteStockBinding;

import java.util.ArrayList;
import java.util.List;

public class DeleteStockActivity extends AppCompatActivity {

    private ActivityDeleteStockBinding binding;
    private List<Food> deleteList = null;

    private DeleteStockAdapter deleteStockAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        deleteList = new ArrayList<>();
        deleteStockAdapater = new DeleteStockAdapter(new ArrayList<>(), deleteList);
        binding.displayStock.setAdapter(deleteStockAdapater);
        binding.displayStock.setLayoutManager(new LinearLayoutManager(this));

        deleteFromStock();
        backToStock();
        displayStock();
    }


    private void displayStock() {
        new Thread(() -> {
            SmartFridgeDatabase db = SmartFridgeDatabase.getDatabase(getApplicationContext());
            String username = getSharedPreferences("user_session", MODE_PRIVATE)
                    .getString("current_username", null);
            if (username == null) return;

            User user = db.userDAO().getUserByUsernameSync(username);
            if (user == null) return;

            List<Food> foodList = db.foodDAO().getFoodByUser(user.getId());

            runOnUiThread(() -> deleteStockAdapater.updateStockList(foodList));
        }).start();
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
        return new Intent(context, DeleteStockActivity.class);
    }
}