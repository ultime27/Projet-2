package com.suchet.smartFridge.stocks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.suchet.smartFridge.R;
import com.suchet.smartFridge.database.entities.Food;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {
    private List<Food> stockList;
    public void updateStockList(List<Food> newList) {
        this.stockList.clear();
        this.stockList.addAll(newList);
        notifyDataSetChanged();
    }
    public static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView quantityView;

        public StockViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.foodName);
            quantityView = itemView.findViewById(R.id.foodQuantity);
        }

        public void bind(Food food) {
            nameView.setText(food.getName());
            quantityView.setText(String.valueOf(food.getQuantity()));
        }
    }


    public StockAdapter(List<Food> stockList) {
        this.stockList = stockList;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {
        holder.bind(stockList.get(position));
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    public void updateData(List<Food> newStock) {
        stockList = newStock;
        notifyDataSetChanged();
    }
}
