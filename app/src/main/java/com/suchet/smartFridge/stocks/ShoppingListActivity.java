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
import com.suchet.smartFridge.database.entities.ShoppingItem;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivityShoppingListBinding;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {
    private ActivityShoppingListBinding binding;
    private ArrayList<ShoppingItem> shoppingItems;
    private ShoppingListAdapter shoppingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ShoppingListActivity", "ShoppingList 1: create");
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        shoppingItems = new ArrayList<>();
        shoppingListAdapter = new ShoppingListAdapter(new ArrayList<>(), new ShoppingListAdapter.OnItemInteractionListener() {
            @Override
            public void onCheckedChanged(ShoppingItem item, boolean isChecked) {
                item.setChecked(isChecked);
                new Thread(() -> SmartFridgeDatabase.getDatabase(getApplicationContext())
                        .shoppingItemDAO()
                        .update(item)).start();
            }

            @Override
            public void onDelete(ShoppingItem item) {
                new Thread(() -> {
                    SmartFridgeDatabase.getDatabase(getApplicationContext())
                            .shoppingItemDAO()
                            .delete(item);

                    runOnUiThread(() -> {
                        shoppingItems.remove(item);
                        shoppingListAdapter.notifyDataSetChanged();
                    });
                }).start();
            }
        });
        binding.shoppingListRecyclerView.setAdapter(shoppingListAdapter);
        binding.shoppingListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addToSL();
        displayStock();
        backToStock();
        addToTheStock();
    }
    private void backToStock() {
        binding.BackToStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(StockActivity.StockIntentFactory(getApplicationContext()));
            }
        });
    }
    private void addToTheStock() {
        Log.d("ShoppingListActivity", "ShoppingList 4: addToTheStock");
        binding.BoughtToStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(ShoppingItem item : shoppingItems) {
                    Food food = new Food(item.getName());
                    food.setQuantity(item.getQuantity());
                    food.setUserId(item.getUserId());
                    StockActivity.addFoodToStock(getApplicationContext(), food);
                    Log.d("ShoppingListActivity", "ShoppingList 4: vadelete");
                    deleteFromSL(item);
                }
                displayStock();
            }
        });
    }
    private void deleteFromSL(ShoppingItem food) {
        new Thread(() -> {
            SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(getApplicationContext());
            String username = getApplicationContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
                    .getString("current_username", null);
            if (username == null) return;

            User user = stockDatabase.userDAO().getUserByUsernameSync(username);
            food.setUserId(user.getId());
            stockDatabase.shoppingItemDAO().delete(food);
            Log.d("ShoppingListActivity", "ShoppingList 5: c delete");
            runOnUiThread(() -> {
                shoppingItems.remove(food);
                shoppingListAdapter.notifyDataSetChanged();
            });
        }).start();
    }
    private void displayStock() {
        new Thread(() -> {
            Log.d("ShoppingListActivity", "ShoppingList 3: display");
            SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(getApplicationContext());

            String username = getSharedPreferences("user_session", MODE_PRIVATE).getString("current_username", null);
            if (username == null) return;

            User user = stockDatabase.userDAO().getUserByUsernameSync(username);
            if (user == null) return;

            List<ShoppingItem> foodList = stockDatabase.shoppingItemDAO().getAllForUser(user.getId());
            runOnUiThread(() -> {
                shoppingItems.clear();
                shoppingItems.addAll(foodList);
                shoppingListAdapter.setItems(shoppingItems);
                binding.shoppingListRecyclerView.setAdapter(shoppingListAdapter);
            });
        }).start();
    }
    private void addToSL() {
        Log.d("ShoppingListActivity", "ShoppingList 2.0: add");
        binding.addToTheSLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.foodNameSLEditText.getText().toString().trim();
                String countStr = binding.foodQuantitySLEditText.getText().toString().trim();
                Log.d("ShoppingListActivity", "ShoppingList 2.1: add" + name + " " + countStr);
                if (name.isEmpty() || countStr.isEmpty()) {
                    return;
                }

                int count = Integer.parseInt(countStr);
                ShoppingItem food = new ShoppingItem(name);
                food.setQuantity(count);
                Log.d("ShoppingListActivity", "ShoppingList 2.2: add"+ food.getName() + " " + food.getQuantity());
                new Thread(() -> {
                    SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(getApplicationContext());
                    String username = getApplicationContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
                            .getString("current_username", null);
                    if (username == null) return;

                    User user = stockDatabase.userDAO().getUserByUsernameSync(username);
                    food.setUserId(user.getId());
                    stockDatabase.shoppingItemDAO().insert(food);
                    Log.d("ShoppingListActivity", "ShoppingList 2.3: c insere");
                    runOnUiThread(() -> {
                        shoppingItems.add(food);
                        shoppingListAdapter.notifyDataSetChanged();
                    });
                }).start();


                binding.foodNameSLEditText.setText("");
                binding.foodQuantitySLEditText.setText("");
            }
        });

    }
    public static Intent ShoppingListIntentFactory(Context context) {
        Intent intent = new Intent(context, ShoppingListActivity.class);
        return intent;
    }
}