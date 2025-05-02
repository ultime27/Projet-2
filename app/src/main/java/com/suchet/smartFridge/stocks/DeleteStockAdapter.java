package com.suchet.smartFridge.stocks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.entities.Food;

import java.util.List;

public class DeleteStockAdapter extends RecyclerView.Adapter<DeleteStockAdapter.DeleteViewHolder> {
    private List<Food> stockList;
    private List<Food> deleteList;

    public DeleteStockAdapter(List<Food> stockList, List<Food> deleteList) {
        this.stockList = stockList;
        this.deleteList = deleteList;
    }

    public void updateStockList(List<Food> newStockList) {
        this.stockList = newStockList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delete_food, parent, false);
        return new DeleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteViewHolder holder, int position) {
        Food food = stockList.get(position);
        holder.foodName.setText(food.getName());
        holder.foodQuantity.setText(String.valueOf(food.getQuantity()));

        holder.checkboxSelect.setOnCheckedChangeListener(null); // clear previous listener

        holder.checkboxSelect.setChecked(deleteList.contains(food)); // set checkbox state

        holder.checkboxSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!deleteList.contains(food)) deleteList.add(food);
            } else {
                deleteList.remove(food);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    public static class DeleteViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodQuantity;
        CheckBox checkboxSelect;

        public DeleteViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodQuantity = itemView.findViewById(R.id.foodQuantity);
            checkboxSelect = itemView.findViewById(R.id.checkboxSelect);
        }
    }
}
