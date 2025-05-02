package com.suchet.smartFridge.stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.entities.ShoppingItem;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivityShoppingListBinding;
import com.suchet.smartFridge.databinding.ActivityStockBinding;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {
    private ActivityShoppingListBinding binding;
    private ArrayList<ShoppingItem> shoppingItems;
    private ShoppingListAdapter shoppingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    }


    public static Intent ShoppingListIntentFactory(Context context) {
        Intent intent = new Intent(context, ShoppingListActivity.class);
        return intent;
    }
}