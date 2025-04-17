package com.suchet.smartFridge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;


import com.suchet.smartFridge.databinding.ActivityMainBinding;

public class  MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static final String TAG="SF_SMARTLOG";
    private static final String MAIN_ACTIVITY_USER_ID = "com.suchet.smartFridge.MAIN_ACTIVITY_USER_ID";

    private static boolean isLoggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (isLoggedIn){
            loginUser();
        }
        else {
            loginButton();
            SignupButton();
        }

    }

    private void loginButton() {
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void SignupButton(){
        binding.signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity.RegisterIntentFactory(getApplicationContext()));
            }
        });
    }

    static Intent MainIntentFactory(Context context) {
        return new Intent(context, MainActivity.class);
    }
    private void loginUser() {
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }





}