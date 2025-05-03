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

import java.time.LocalDate;
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
        GoToDeleteStockActivity();
        backToLanding();
        GoToShoppingListActivity();
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
    private void GoToDeleteStockActivity() {
        binding.deleteFoodInStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(DeleteStockActivity.DeleteStockIntentFactory(getApplicationContext()));
            }
        });
    }

    private void GoToShoppingListActivity() {
        binding.ShoppingListStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ShoppingListActivity.ShoppingListIntentFactory(getApplicationContext()));
            }
        });
    }
    public static void addFoodToStock(Context context, Food food) {
        new Thread(() -> {
            SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(context);
            String username = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
                    .getString("current_username", null);
            if (username == null) return;

            User user = stockDatabase.userDAO().getUserByUsernameSync(username);
            List<Food> list = stockDatabase.foodDAO().getFoodByUser(user.getId());
            for(Food f : list){
                if(f.getName().equals(food.getName())){
                    f.setQuantity(f.getQuantity() + food.getQuantity());
                    stockDatabase.foodDAO().update(f);
                    return;
                }

            }
            food.setUserId(user.getId());
            stockDatabase.foodDAO().insert(food);
        }).start();


    }

    @Override
    protected void onResume() {
        super.onResume();
        displayStock();
    }

    public static void deleteFoodToStock(Context context, Food food) {
        new Thread(() -> {
            SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(context);
            String username = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
                    .getString("current_username", null);
            if (username == null) return;

            User user = stockDatabase.userDAO().getUserByUsernameSync(username);

            food.setUserId(user.getId());
            stockDatabase.foodDAO().delete(food);
        }).start();

    }
    public static Intent StockIntentFactory(Context context) {
        Intent intent = new Intent(context, StockActivity.class);
        return intent;
    }

    public static List<String> getFoodForTomorrow(Context context) {
        List<String> foodForTomorrow = new ArrayList<>();
        new Thread(() -> {
            SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(context);
            String username = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
                    .getString("current_username", null);
            if (username == null) return;

            User user = stockDatabase.userDAO().getUserByUsernameSync(username);
            List<Food> foodList = stockDatabase.foodDAO().getFoodByUser(user.getId());
            for (Food food : foodList) {
                if(food.getDatePeremption() != null && food.getDatePeremption().isEqual(java.time.LocalDate.now().plusDays(1))){
                    foodForTomorrow.add(food.getName());
                }

            }
        }).start();
        return foodForTomorrow;
    }

}