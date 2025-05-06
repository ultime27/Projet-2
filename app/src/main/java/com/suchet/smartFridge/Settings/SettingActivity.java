package com.suchet.smartFridge.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
    private static final String LANDING_ACTIVITY_USER_ID = "com.suchet.smartFridge.MAIN_ACTIVITY_USER_ID";

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
    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor= sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key),-1);
        sharedPrefEditor.apply();
    }
    private void logout(){
        binding.LogoutButton.setOnClickListener(v -> {
            updateSharedPreference();
            getIntent().putExtra(LANDING_ACTIVITY_USER_ID,-1);
            startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(),-1));
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
                Toast.makeText(this, "empty password", Toast.LENGTH_SHORT).show();
                return;
            }
            String username = getSharedPreferences("user_session", MODE_PRIVATE)
                    .getString("current_username", null);
            if (username == null) {
                Toast.makeText(this, "no user found", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                SmartFridgeDatabase db = SmartFridgeDatabase.getDatabase(getApplicationContext());
                User user = db.userDAO().getUserByUsernameSync(username);

                if (user == null) {
                    Log.e("SettingActivity", "no user found");
                    runOnUiThread(() -> Toast.makeText(this, "no user found", Toast.LENGTH_SHORT).show());
                    return;
                }

                user.setPassword(newPassword);
                db.userDAO().insert(user);

                runOnUiThread(() -> {
                    Log.d("SettingActivity", "Mot de passe modifié pour " + user.getUsername() + " mdp  :"+user.getPassword());
                    binding.ChangePasswordEditText.setVisibility(View.INVISIBLE);
                    binding.ConfirmNewPasswordButton.setVisibility(View.INVISIBLE);
                    gotoLogin();
                });
            }).start();
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
        updateSharedPreference();
        getIntent().putExtra(LANDING_ACTIVITY_USER_ID,-1);
        startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(),-1));
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
                        binding.AdminButton.setOnClickListener(v -> {
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
                        });

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
