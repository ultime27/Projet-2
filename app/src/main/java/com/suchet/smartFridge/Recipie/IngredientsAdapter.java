    package com.suchet.smartFridge.Recipie;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.EditText;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.suchet.smartFridge.R;

    import java.util.List;

    public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
        private final List<String> ingredientNames;
        private final List<Double> ingredientQuantities;
        public IngredientsAdapter(List<String> names, List<Double> quantities) {
            this.ingredientNames = names;
            this.ingredientQuantities = quantities;
        }
        public static class ViewHolder extends RecyclerView.ViewHolder {
            EditText ingredientName, ingredientQuantity;

            public ViewHolder(View itemView) {
                super(itemView);
                ingredientName = itemView.findViewById(R.id.ingredient_name_EditText);

                ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity_EditText);
            }
        }

        @NonNull
        @Override
        public IngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_add_recipie, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.ingredientName.setText("");
            holder.ingredientQuantity.setText("");

            holder.ingredientName.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus)
                    ingredientNames.set(position, holder.ingredientName.getText().toString());
            });

            holder.ingredientQuantity.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    try {
                        ingredientQuantities.set(position,
                                Double.parseDouble(holder.ingredientQuantity.getText().toString()));
                    } catch (NumberFormatException e) {
                        ingredientQuantities.set(position, 0.0);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return ingredientNames.size();
        }


    }
