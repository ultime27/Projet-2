package com.suchet.smartFridge.Recipie;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.suchet.smartFridge.MealActivity;
import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.RecipeDatabase;
import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.Meal;
import com.suchet.smartFridge.database.entities.Recipe;
import com.suchet.smartFridge.database.entities.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_DESC = "description";
    private static final String ARG_INSTRUCTION = "instruction";

    private LocalDate selectedDate;

    private ExecutorService executorService = Executors.newSingleThreadExecutor(); // Executor pour gérer les threads en arrière-plan

    public static RecipeFragment newInstance(String name, String description, String instruction) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_DESC, description);
        args.putString(ARG_INSTRUCTION, instruction);
        fragment.setArguments(args);
        return fragment;
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
            addMealToDatabase();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        Button StartButton = view.findViewById(R.id.start_recipie_button);
        StartButton.setOnClickListener(v -> {
                    Intent intent = StartRecipieActivity.StartRecipieActivityFactory(requireContext(), getArguments() != null ? getArguments().getString(ARG_NAME) : null);
                    startActivity(intent);
        });

        Button scheduleButton = view.findViewById(R.id.recipie_schedule_button);
        scheduleButton.setOnClickListener(v -> openDatePicker());

        return view;
    }

    private void addMealToDatabase() {
        if (selectedDate == null) {
            Toast.makeText(requireContext(), "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle args = getArguments();
        if (args != null) {
            String recipeName = args.getString(ARG_NAME);

            executorService.execute(() -> {
                RecipeDatabase rb = RecipeDatabase.getDatabase(requireContext());
                Recipe recipe = rb.recipeDAO().searchByName(recipeName);
                SmartFridgeDatabase db = SmartFridgeDatabase.getDatabase(requireContext());
                if (recipe != null) {
                    List<Food> recipeIngredients = new ArrayList<>();
                    for (Map.Entry<String, Double> entry : recipe.getIngredientList().entrySet()) {
                        Food food = new Food(entry.getKey());
                        food.setQuantity(entry.getValue());
                        recipeIngredients.add(food);
                    }

                    getUserIdFromSharedPrefs(userId -> {
                        if (userId == -1) {
                            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String mealName = recipe.getName();
                        if (selectedDate == null) {
                            selectedDate = LocalDate.now();
                        }

                        Meal newMeal = new Meal(mealName, selectedDate, recipeIngredients, userId);
                        executorService.execute(() -> db.mealDAO().insert(newMeal));

                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "Meal scheduled successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(requireContext(), MealActivity.class));
                        });
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Recipe not found", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    private void getUserIdFromSharedPrefs(final UserIdCallback callback) {
        String username = getActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
                .getString("current_username", null);
        if (username == null) {
            callback.onUserIdFetched(-1);
            return;
        }

        executorService.execute(() -> {
            SmartFridgeDatabase db = SmartFridgeDatabase.getDatabase(requireContext());
            User user = db.userDAO().getUserByUsernameSync(username);
            int userId = (user != null) ? user.getId() : -1;
            requireActivity().runOnUiThread(() -> callback.onUserIdFetched(userId));
        });
    }

    public interface UserIdCallback {
        void onUserIdFetched(int userId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView title = view.findViewById(R.id.recipieNameTextView);
        TextView desc = view.findViewById(R.id.recipieDescriptionTextView);
        TextView instruction = view.findViewById(R.id.recipieInstructionTextView);

        Bundle args = getArguments();
        if (args != null) {
            title.setText(args.getString(ARG_NAME));
            desc.setText(args.getString(ARG_DESC));
            instruction.setText(args.getString(ARG_INSTRUCTION));
        }
    }
}