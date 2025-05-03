package com.suchet.smartFridge.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.Meal;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private List<Meal> mealList;
    private OnMealDeletedListener onMealDeletedListener;

    public interface OnMealDeletedListener {
        void onMealDeleted(Meal meal);
    }

    public MealAdapter(List<Meal> mealList, OnMealDeletedListener listener) {
        this.mealList = mealList;
        this.onMealDeletedListener = listener;
    }

    public void updateMealList(List<Meal> newList) {
        this.mealList.clear();
        this.mealList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {

        TextView mealName;
        TextView mealDate;
        TextView ingredientsTextView;
        Button deleteMealButton;

        public MealViewHolder(View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealNameTextView);
            mealDate = itemView.findViewById(R.id.mealDateTextView);
            ingredientsTextView = itemView.findViewById(R.id.ingredientsTextView);
            deleteMealButton = itemView.findViewById(R.id.deleteMealButton);
        }

        public void bind(Meal meal, OnMealDeletedListener listener) {
            mealName.setText(meal.getName());
            mealDate.setText(String.valueOf(meal.getDate()));

            // Affichage des ingrédients
            StringBuilder builder = new StringBuilder("Ingrédients :\n");
            for (Food food : meal.getFoodList()) {
                builder.append("- ").append(food.getName())
                        .append(" : ").append(food.getQuantity())
                        .append("\n");
            }
            ingredientsTextView.setText(builder.toString());

            // Gestion du bouton de suppression
            deleteMealButton.setOnClickListener(v -> {
                listener.onMealDeleted(meal);
            });
        }
    }

    @Override
    public MealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MealViewHolder holder, int position) {
        holder.bind(mealList.get(position), onMealDeletedListener); // Passer le listener ici
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void updateData(List<Meal> newMeal) {
        mealList = newMeal;
        notifyDataSetChanged();
    }
}