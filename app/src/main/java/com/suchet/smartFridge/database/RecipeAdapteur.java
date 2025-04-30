package com.suchet.smartFridge.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.entities.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapteur extends RecyclerView.Adapter<RecipeAdapteur.RecipeViewHolder> {

    private List<Recipe> recipes = new ArrayList<>();

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recipie_name);
            description = itemView.findViewById(R.id.meal_description);
        }

        public void bind(Recipe recipe) {
            name.setText(recipe.getName());
            description.setText(recipe.getDescription());
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipie, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setRecipes(List<Recipe> newRecipes) {
        recipes = newRecipes;
        notifyDataSetChanged();
    }
}

