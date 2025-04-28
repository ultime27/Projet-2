package com.suchet.smartFridge.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.entities.Meal;

import java.util.ArrayList;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private List<Meal> mealList = new ArrayList<>();

    public void setMeals(List<Meal> meals) {
        this.mealList = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.name.setText(meal.name);
        holder.date.setText("To be eaten on: " + meal.date.toString());
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView name, date;

        MealViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.meal_name);
            date = itemView.findViewById(R.id.meal_date);
        }
    }
}