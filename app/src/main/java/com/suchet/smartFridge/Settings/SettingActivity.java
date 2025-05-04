package com.suchet.smartFridge.Settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.suchet.smartFridge.LandingPage;
import com.suchet.smartFridge.LoginActivity;
import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivitySettingBinding;
import com.suchet.smartFridge.databinding.ActivityStockBinding;
import com.suchet.smartFridge.stocks.AddStockActivity;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ChangePasswordEditText.setVisibility(View.INVISIBLE);
        binding.ConfirmNewPasswordButton.setVisibility(View.INVISIBLE);
        LightMode();
        DarkMode();
        GoToLandingPage();
        ChangePassword();
        DeleteAccount();
        showAdminButton();
        GoToAdmin();
    }

    private void LightMode() {
        binding.LightModeButton.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        });
    }

    private void DarkMode() {
        binding.DarkModeButton.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        });
    }

    private void ChangePassword() {
        binding.ChangePasswordButton.setOnClickListener(v -> {
            binding.ChangePasswordEditText.setVisibility(View.VISIBLE);
            binding.ConfirmNewPasswordButton.setVisibility(View.VISIBLE);

        });
    }

    private void GoToAdmin() {
        binding.AdminButton.setOnClickListener(v -> {

        });
    }

    private void showAdminButton(){
        if (user.isAdmin()){
            binding.AdminButton.setVisibility(View.VISIBLE);
        }
        else {
            binding.AdminButton.setVisibility(View.INVISIBLE);
        }
    }

    private void DeleteAccount() {
        binding.DeleteAccountButton.setOnClickListener(v -> {
            SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(getApplicationContext());
            String username = getSharedPreferences("user_session", MODE_PRIVATE).getString("current_username", null);
            stockDatabase.userDAO().deleteByUsername(username);
            startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
        });
    }

    private void GoToLandingPage() {
        binding.backToLandingFromSettingButton.setOnClickListener(v -> {
            startActivity(LandingPage.landingPageActivityIntentFactory(getApplicationContext()));
        });
    }

    public static Intent SettingIntentFactory(Context context) {
        return new Intent(context, SettingActivity.class);
    }
}