package com.suchet.smartFridge.Settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.suchet.smartFridge.LandingPage;
import com.suchet.smartFridge.LoginActivity;
import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivitySettingBinding;
import com.suchet.smartFridge.databinding.ActivityStockBinding;
import com.suchet.smartFridge.stocks.AddStockActivity;
import com.suchet.smartFridge.stocks.StockAdapter;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    private User user;

    private SettingAdminAdapter settingAdminAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SettingActivity", "setting activity onCreate called");
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.ChangePasswordEditText.setVisibility(View.INVISIBLE);
        binding.ConfirmNewPasswordButton.setVisibility(View.INVISIBLE);
        binding.displayStock.setVisibility(View.INVISIBLE);
        LightMode();
        DarkMode();
        GoToLandingPage();
        ChangePassword();
        DeleteAccount();
        showAdminButton();
        GoToAdmin();
        display();
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

        binding.ConfirmNewPasswordButton.setOnClickListener(v -> {
            String newPassword = binding.ChangePasswordEditText.getText().toString();
            if (newPassword.isEmpty()) return;
            startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
            finish();
            String username = getSharedPreferences("user_session", MODE_PRIVATE).getString("current_username", null);
            if (username == null) return;

            new Thread(() -> {
                SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(getApplicationContext());
                User user = stockDatabase.userDAO().getUserByUsernameSync(username);
                if (user != null) {
                    user.setPassword(newPassword);
                    stockDatabase.userDAO().insert(user);


                    runOnUiThread(() -> {
                        binding.ChangePasswordEditText.setVisibility(View.INVISIBLE);
                        binding.ConfirmNewPasswordButton.setVisibility(View.INVISIBLE);
                        getSharedPreferences("user_session", MODE_PRIVATE).edit().clear().apply();
                        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
                        finish();
                    });
                }
            }).start();
        });
    }

    private void gotoLogin() {
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    private void GoToAdmin() {
       binding.AdminButton.setOnClickListener(v -> {
           binding.displayStock.setVisibility(View.VISIBLE);
        });
    }

    private void showAdminButton() {
        SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(getApplicationContext());
        String username = getSharedPreferences("user_session", MODE_PRIVATE).getString("current_username", null);

        if (username != null) {
            new Thread(() -> {
                user = stockDatabase.userDAO().getUserByUsernameSync(username);

                runOnUiThread(() -> {
                    if (user != null && user.isAdmin()) {
                        binding.AdminButton.setVisibility(View.VISIBLE);
                        binding.displayStock.setVisibility(View.VISIBLE);

                        settingAdminAdapter = new SettingAdminAdapter(new ArrayList<>());
                        binding.displayStock.setLayoutManager(new LinearLayoutManager(this));
                        binding.displayStock.setAdapter(settingAdminAdapter);

                    } else {
                        binding.AdminButton.setVisibility(View.INVISIBLE);
                        binding.displayStock.setVisibility(View.INVISIBLE);
                    }
                });
            }).start();
        } else {
            Log.e("SettingActivity", "Username not found in shared preferences");
        }
    }

    private void display() {
        binding.AdminButton.setOnClickListener(v -> {
            SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(getApplicationContext());

            new Thread(() -> {
                List<User> userList = stockDatabase.userDAO().getAllUsersList();

                runOnUiThread(() -> {
                    binding.displayStock.setVisibility(View.VISIBLE);

                    if (settingAdminAdapter == null) {
                        settingAdminAdapter = new SettingAdminAdapter(userList);
                        binding.displayStock.setLayoutManager(new LinearLayoutManager(this));
                        binding.displayStock.setAdapter(settingAdminAdapter);
                    } else {
                        settingAdminAdapter.updateUserList(userList);
                    }
                });
            }).start();
        });
    }

    private void DeleteAccount(){
        binding.AdminButton.setOnClickListener(v -> {
            String username = getSharedPreferences("user_session", MODE_PRIVATE).getString("current_username", null);
            if (username == null) return;

            new Thread(() -> {
                SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(getApplicationContext());
                stockDatabase.userDAO().deleteByUsername(username);

                runOnUiThread(() -> {
                    getSharedPreferences("user_session", MODE_PRIVATE).edit().clear().apply();
                    startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
                    finish();
                });
            }).start();
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