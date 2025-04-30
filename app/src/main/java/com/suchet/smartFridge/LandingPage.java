package com.suchet.smartFridge;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.suchet.smartFridge.database.MealAdapter;
import com.suchet.smartFridge.database.MealViewModel;
import com.suchet.smartFridge.database.RecipeDatabase;
import com.suchet.smartFridge.database.SmartFridgeRepository;
import com.suchet.smartFridge.database.entities.Meal;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivityLandingPageBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LandingPage extends AppCompatActivity {
    private ActivityLandingPageBinding binding;

    public  static final int LOGGED_OUT = -1;


    private static final String LANDING_ACTIVITY_USER_ID = "com.suchet.smartFridge.MAIN_ACTIVITY_USER_ID";




    private static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.suchet.smartFridge.SAVED_INSTANCE_STATE_USERID_KEY";

    private SmartFridgeRepository repository;

    private int loggedInUserId = -1;
    private User user;

    private MealViewModel mealViewModel;
    private MealAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RecipeDatabase.getDatabase(getApplicationContext());

        repository = SmartFridgeRepository.getRepository(getApplication());



        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new MealAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);
        mealViewModel.getAllMeals().observe(this, adapter::setMeals);

        FloatingActionButton fab = findViewById(R.id.add_meal_fab);
        fab.setOnClickListener(v -> showAddMealDialog());
        binding.testLucasButton.setOnClickListener(view -> changeActivity());

        loginUser(savedInstanceState);
        updateSharedPreference();

        logoutButton();
    }

    private void changeActivity(){
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
                invalidateOptionsMenu();
                showAdminButton();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);
        if (user == null){
            item.setTitle("charging...");
        } else {
            item.setTitle(user.getUsername());
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    showLogoutDialog();
                    return false;
                }
            });
        }
        return true;
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

    static Intent landingPageActivityIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, LandingPage.class);
        intent.putExtra(LANDING_ACTIVITY_USER_ID,userId);
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
    private void showAddMealDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_meal, null);

        EditText nameInput = dialogView.findViewById(R.id.input_meal_name);
        Button pickDateButton = dialogView.findViewById(R.id.pick_date_button);
        final LocalDate[] selectedDate = {LocalDate.now()};

        pickDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedDate[0] = LocalDate.of(year, month + 1, dayOfMonth);
                pickDateButton.setText("Date: " + selectedDate[0].toString());
            }, selectedDate[0].getYear(), selectedDate[0].getMonthValue() - 1, selectedDate[0].getDayOfMonth());
            datePickerDialog.show();
        });

        new AlertDialog.Builder(this)
                .setTitle("Add Meal")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String mealName = nameInput.getText().toString().trim();
                    if (!mealName.isEmpty()) {
                        Meal newMeal = new Meal(mealName, selectedDate[0]);
                        mealViewModel.insert(newMeal);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}