package com.suchet.smartFridge.database.Meal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.Meal;

import java.util.ArrayList;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private List<Meal> meals = new ArrayList<>();

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal currentMeal = meals.get(position);
        holder.nameText.setText(currentMeal.getName());
        holder.dateText.setText(currentMeal.getDate().toString());

        // Transformer la liste de Food en une string lisible
        List<Food> foodList = currentMeal.getFoodList();
        StringBuilder foodText = new StringBuilder("Ingrédients : ");
        for (Food food : foodList) {
            foodText.append(food.getName()).append(", ");
        }

        if (!foodList.isEmpty()) {
            // Supprimer la dernière virgule
            foodText.setLength(foodText.length() - 2);
        } else {
            foodText.append("aucun");
        }

        holder.foodText.setText(foodText.toString());
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, dateText, foodText;

        public MealViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.meal_name_text);
            dateText = itemView.findViewById(R.id.meal_date_text);
            foodText = itemView.findViewById(R.id.food_list_text);
        }
    }
}
