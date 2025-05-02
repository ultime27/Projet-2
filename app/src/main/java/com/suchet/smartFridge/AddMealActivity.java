package com.suchet.smartFridge;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.entities.Meal;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivityAddMealBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class AddMealActivity extends AppCompatActivity {

    private ActivityAddMealBinding binding;
    private String name;
    private LocalDate selectedDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMealBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupDatePicker();
        setupAddMeal();
        setupBackButton();
    }

    private void setupDatePicker() {
        binding.selectDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddMealActivity.this,
                    (DatePicker view, int year, int month, int dayOfMonth) -> {
                        selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                        binding.selectedDateTextView.setText("Date du repas : " + selectedDate.toString());
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void setupAddMeal() {
        binding.addMealButton.setOnClickListener(v -> {
            name = binding.mealNameEditText.getText().toString().trim();

            if (name.isEmpty() || selectedDate == null) {
                Toast.makeText(this, "Veuillez entrer le nom du repas et une date.", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                SmartFridgeDatabase db = SmartFridgeDatabase.getDatabase(getApplicationContext());

                String username = getSharedPreferences("user_session", MODE_PRIVATE).getString("current_username", null);
                if (username == null) return;

                User user = db.userDAO().getUserByUsernameSync(username);
                if (user == null) return;

                Meal meal = new Meal(name, selectedDate, new ArrayList<>(), user.getId());
                db.mealDAO().insert(meal);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Repas ajouté !", Toast.LENGTH_SHORT).show();
                    startActivity(MealActivity.MealIntentFactory(this));
                    finish();
                });
            }).start();
        });
    }

    private void setupBackButton() {
        binding.backButton.setOnClickListener(v -> {
            finish();
        });
    }

    public static Intent AddToMealActivity(Context context) {
        return new Intent(context, AddMealActivity.class);
    }
}