package com.suchet.smartFridge.Recipie;

import static com.suchet.smartFridge.stocks.StockActivity.getFoodForTomorrow;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.entities.Recipe;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapteur extends RecyclerView.Adapter<RecipeAdapteur.RecipeViewHolder> {

    public List<String> foodForTomorrow = new ArrayList<>();

    private List<Recipe> recipes = new ArrayList<>();
    Context context = null;
    private OnRecipeClickListener listener;

    public void setFoodForTomorrow(List<String> foodForTomorrow) {
        this.foodForTomorrow = foodForTomorrow;
        notifyDataSetChanged();
    }

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public void setOnRecipeClickListener(OnRecipeClickListener listener) {
        this.listener = listener;
    }




    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;


        public RecipeViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.recipie_name);
            description = itemView.findViewById(R.id.recipie_description);
        }

        public void bind(Recipe recipe, List<String> foodForTomorrow) {

            //TODO check context
            for (String s:recipe.ingredientList.keySet()){
                if (foodForTomorrow.contains(s)){
                    name.setTextColor(Color.RED);
                    description.setTextColor(Color.RED);
                }
            }
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
        Recipe recipe = recipes.get(position);
        holder.bind(recipe,new ArrayList<>());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeClick(recipe);
            }
        });
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

