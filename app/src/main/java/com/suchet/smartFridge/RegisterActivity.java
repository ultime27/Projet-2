package com.suchet.smartFridge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.suchet.smartFridge.database.SmartFridgeRepository;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private SmartFridgeRepository repository;

    private ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createAccountButton();
        logoutButton();
    }

    private void createAccountButton() {
        binding.SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void logoutButton(){
        binding.LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(),-1));
            }
        });
    }

    private void registerUser() {
        String username = binding.userNameRegisterEditText.getText().toString();
        String password = binding.passwordRegisterEditText.getText().toString();
        String confirmPassword = binding.ConfirmPasswordRegisterEditText.getText().toString();

        if(username.isEmpty()){
            toastMaker("Username may not be blank");
            return;
        }
        if(password.isEmpty() || confirmPassword.isEmpty()) {
            toastMaker("Password may not be blank");
            return;
        }

        if(password.equals(confirmPassword)){
            repository.addUser(username, password);
            LiveData<User> userObserver = repository.getUserByUsername(username);
            userObserver.observe(this, user -> {
                startActivity(LandingPage.landingPageActivityIntentFactory(getApplicationContext(), user.getId()));
            });
        } else {
            toastMaker("Passwords do not match.");
            binding.passwordRegisterEditText.setSelection(0);
        }
    }



    private void toastMaker(String format) {
        Toast.makeText(this, format, Toast.LENGTH_SHORT).show();
    }

    private void loginActivity() {
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    static Intent RegisterIntentFactory(Context context) {
        return new Intent(context, RegisterActivity.class);
    }
}