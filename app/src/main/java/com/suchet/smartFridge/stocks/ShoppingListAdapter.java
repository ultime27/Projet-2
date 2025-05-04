package com.suchet.smartFridge.stocks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.entities.ShoppingItem;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    public interface OnItemInteractionListener {
        void onCheckedChanged(ShoppingItem item, boolean isChecked);
        void onDelete(ShoppingItem item);
    }

    private List<ShoppingItem> shoppingItems;
    private final OnItemInteractionListener listener;

    public ShoppingListAdapter(List<ShoppingItem> items, OnItemInteractionListener listener) {
        this.shoppingItems = items;
        this.listener = listener;
    }

    public void setItems(List<ShoppingItem> items) {
        this.shoppingItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ViewHolder holder, int position) {
        ShoppingItem item = shoppingItems.get(position);
        holder.nameText.setText(item.getName());
        holder.quantityText.setText("x" + item.getQuantity());
        holder.checkBox.setChecked(item.isChecked());

        // Gestion de la checkbox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onCheckedChanged(item, isChecked);
        });

        // Gestion du bouton suppression
        holder.deleteButton.setOnClickListener(v -> {
            listener.onDelete(item);
        });
    }

    @Override
    public int getItemCount() {
        return shoppingItems != null ? shoppingItems.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView quantityText;
        CheckBox checkBox;
        ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);
            nameText = view.findViewById(R.id.foodName);
            quantityText = view.findViewById(R.id.foodQuantity);
            checkBox = view.findViewById(R.id.checkboxSelect);
            deleteButton = view.findViewById(R.id.item_delete_button);
        }
    }
}
