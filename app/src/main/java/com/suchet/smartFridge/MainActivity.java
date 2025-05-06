package com.suchet.smartFridge;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;


import com.suchet.smartFridge.database.SmartFridgeRepository;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivityMainBinding;

import java.util.ArrayList;


public class  MainActivity extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    public static ArrayList<Food> stock = new ArrayList<>();
    private ActivityMainBinding binding;
    public static final String TAG="SF_SMARTLOG";
    private static final String MAIN_ACTIVITY_USER_ID = "com.suchet.smartFridge.MAIN_ACTIVITY_USER_ID";

    private static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.suchet.smartFridge.SAVED_INSTANCE_STATE_USERID_KEY";

    private SmartFridgeRepository repository;

    private int loggedInUserId = -1;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());






        repository = SmartFridgeRepository.getRepository(getApplication());
        loginUser(savedInstanceState);

        if(loggedInUserId != LOGGED_OUT){
            Intent intent = LandingPage.landingPageActivityIntentFactory(getApplicationContext(),loggedInUserId);
            startActivity(intent);
        }

        updateSharedPreference();
        loginButton();
        SignupButton();
    }

    private void loginUser(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key),LOGGED_OUT);

        if (loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY,LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID,LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
        userObserver.observe(this,user -> {
            this.user =user;
            if(this.user != null) {
                invalidateOptionsMenu();
            }
        });
    }




    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY,loggedInUserId);
        updateSharedPreference();
    }

    private void showLogoutDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();
        alertBuilder.setMessage("Logout?");

        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertBuilder.create().show();
    }
    public static Intent mainActivityIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID,userId);
        return intent;
    }




    private void logout(){
        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();

        getIntent().putExtra(MAIN_ACTIVITY_USER_ID,loggedInUserId);
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor= sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key),loggedInUserId);
        sharedPrefEditor.apply();
    }

    private void loginButton() {
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
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
}