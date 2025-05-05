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
import com.suchet.smartFridge.MainActivity;
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
        LightMode();
        DarkMode();
        GoToLandingPage();
        ChangePassword();
        DeleteAccount();
        showAdminButton();
        logout();
        //isAdmin();
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

    private void logout(){
        binding.LogoutButton.setOnClickListener(v -> {
            gotoLogin();
            finish();
        });
    }
    private void ChangePassword() {
        binding.ChangePasswordButton.setOnClickListener(v -> {
            binding.ChangePasswordEditText.setVisibility(View.VISIBLE);
            binding.ConfirmNewPasswordButton.setVisibility(View.VISIBLE);
        });

        binding.ConfirmNewPasswordButton.setOnClickListener(v -> {
            String newPassword = binding.ChangePasswordEditText.getText().toString();
            if (newPassword.isEmpty()) {
                return;
            }
            SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(getApplicationContext());
            String username = getSharedPreferences("user_session", MODE_PRIVATE).getString("current_username", null);
            if (username == null) return;
            user = stockDatabase.userDAO().getUserByUsernameSync(username);
            user.setPassword(newPassword);
            Log.d("SettingActivity", "Changing password for user: " + user.getUsername());
            stockDatabase.userDAO().insert(user);
            binding.ChangePasswordEditText.setVisibility(View.INVISIBLE);
            binding.ConfirmNewPasswordButton.setVisibility(View.INVISIBLE);
            gotoLogin();
        });

    }
    private void isAdmin(){
        if (user.isAdmin()){
            binding.AdminButton.setVisibility(View.VISIBLE);
            binding.displayStock.setVisibility(View.VISIBLE);

        }
        else {
            binding.AdminButton.setVisibility(View.INVISIBLE);
            binding.displayStock.setVisibility(View.INVISIBLE);
        }
    }
    private void gotoLogin() {
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    private void showAdminButton() {
        SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(getApplicationContext());
        String username = getSharedPreferences("user_session", MODE_PRIVATE).getString("current_username", null);

        if (username != null) {
            new Thread(() -> {
                user = stockDatabase.userDAO().getUserByUsernameSync(username);

                runOnUiThread(() -> {
                    if (user == null) {
                        Log.e("SettingActivity", "User not found in DB");
                        return;
                    }

                    if (user.isAdmin()) {
                        binding.AdminButton.setVisibility(View.VISIBLE);
                        binding.displayStock.setVisibility(View.VISIBLE);
                        settingAdminAdapter = new SettingAdminAdapter(new ArrayList<>());
                        binding.displayStock.setAdapter(settingAdminAdapter);
                        binding.displayStock.setLayoutManager(new LinearLayoutManager(this));

                        new Thread(() -> {
                            List<User> userList = stockDatabase.userDAO().getAllUsersSync();
                            runOnUiThread(() -> {
                                if (userList != null) {
                                    settingAdminAdapter.updateUserList(userList);
                                }
                            });
                        }).start();
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




    private void DeleteAccount() {
        binding.DeleteAccountButton.setOnClickListener(v -> {
            SmartFridgeDatabase stockDatabase = SmartFridgeDatabase.getDatabase(getApplicationContext());
            String username = getSharedPreferences("user_session", MODE_PRIVATE).getString("current_username", null);
            if (username == null) return;
            user = stockDatabase.userDAO().getUserByUsernameSync(username);
            gotoLogin();
            stockDatabase.userDAO().delete(user);
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
