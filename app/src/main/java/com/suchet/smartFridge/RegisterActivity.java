package com.suchet.smartFridge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.suchet.smartFridge.databinding.ActivityLoginBinding;
import com.suchet.smartFridge.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginButton();
        logoutButton();
    }

    private void loginButton() {
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginActivity();
            }
        });
    }

    private void logoutButton(){
        binding.LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.MainIntentFactory(getApplicationContext()));
            }
        });
    }

    private void loginActivity() {
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    static Intent RegisterIntentFactory(Context context) {
        return new Intent(context, RegisterActivity.class);
    }


}