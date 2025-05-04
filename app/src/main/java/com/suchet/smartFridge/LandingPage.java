package com.suchet.smartFridge;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.suchet.smartFridge.Recipie.SuggestionPageActivity;
import com.suchet.smartFridge.Settings.SettingActivity;
import com.suchet.smartFridge.database.SmartFridgeRepository;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.MealActivity;
import com.suchet.smartFridge.stocks.StockActivity;
import com.suchet.smartFridge.databinding.ActivityLandingPageBinding;


public class LandingPage extends AppCompatActivity {
    private ActivityLandingPageBinding binding;

    public  static final int LOGGED_OUT = -1;


    private static final String LANDING_ACTIVITY_USER_ID = "com.suchet.smartFridge.MAIN_ACTIVITY_USER_ID";




    private static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.suchet.smartFridge.SAVED_INSTANCE_STATE_USERID_KEY";

    private SmartFridgeRepository repository;
    private int loggedInUserId = -1;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = SmartFridgeRepository.getRepository(getApplication());

        loginUser(savedInstanceState);
        updateSharedPreference();
        logoutButton();
        goToCalendarButton();
        goToStockButton();
        goToSettingButton();
        startActivity(SuggestionPageActivity.suggestionPageActivityIntentFactory(getApplicationContext()));
    }

    private void loginUser(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key),LOGGED_OUT);

        if (loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY,LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            loggedInUserId = getIntent().getIntExtra(LANDING_ACTIVITY_USER_ID,LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
        userObserver.observe(this,user -> {
            this.user =user;
            if(this.user != null) {
                SharedPreferences userPrefs = getSharedPreferences("user_session", MODE_PRIVATE);
                SharedPreferences.Editor editor = userPrefs.edit();
                editor.putString("current_username", user.getUsername());
                editor.apply();

                invalidateOptionsMenu();
                showAdminButton();
            }
        });

        binding.testLucasButton.setOnClickListener(view -> changeActivity());
    }

    private void changeActivity(){
        startActivity(SuggestionPageActivity.suggestionPageActivityIntentFactory(getApplicationContext()));
    }





    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY,loggedInUserId);
        updateSharedPreference();
    }

    private void showLogoutDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LandingPage.this);
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

    private void logout(){
        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();

        getIntent().putExtra(LANDING_ACTIVITY_USER_ID,loggedInUserId);
        startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(),-1));
    }

    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor= sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key),loggedInUserId);
        sharedPrefEditor.apply();
    }

    public static Intent landingPageActivityIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, LandingPage.class);
        intent.putExtra(LANDING_ACTIVITY_USER_ID,userId);
        return intent;
    }

    public static Intent landingPageActivityIntentFactory(Context context){
        Intent intent = new Intent(context, LandingPage.class);
        return intent;
    }

    private void logoutButton(){
        binding.LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void showAdminButton(){
        if (user.isAdmin()){
            binding.ButtonAdmin.setVisibility(View.VISIBLE);

        }
        else {
            binding.ButtonAdmin.setVisibility(View.INVISIBLE);
        }
    }

    private void goToCalendarButton(){
        binding.GoToCalendarActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MealActivity.MealIntentFactory(getApplicationContext()));
            }
        });
    }

    private void goToStockButton(){
        binding.GoToStockActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(StockActivity.StockIntentFactory(getApplicationContext()));
            }
        });
    }

    private void goToSettingButton(){
        binding.GoToSettingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SettingActivity.SettingIntentFactory(getApplicationContext()));
            }
        });
    }

}