package com.suchet.smartFridge.database.TypeConverter;

import static android.os.Build.VERSION_CODES.R;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.databinding.ActivityDeleteStockBinding;

public class DeleteStockActivity extends AppCompatActivity {

    private ActivityDeleteStockBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
    }
}