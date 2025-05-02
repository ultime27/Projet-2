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
import com.suchet.smartFridge.databinding.ActivityShoppingListBinding;
import com.suchet.smartFridge.databinding.ActivityStockBinding;

public class ShoppingListActivity extends AppCompatActivity {
    private ActivityShoppingListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public static Intent ShoppingListIntentFactory(Context context) {
        Intent intent = new Intent(context, ShoppingListActivity.class);
        return intent;
    }
}