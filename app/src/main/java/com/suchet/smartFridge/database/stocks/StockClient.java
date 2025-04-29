package com.suchet.smartFridge.database.stocks;


import android.content.Context;
import androidx.room.Room;

import com.suchet.smartFridge.database.StockDatabase;

public class StockClient {

    private static StockClient instance;
    private final StockDatabase stockDatabase;

    private StockClient(Context context) {
        stockDatabase = Room.databaseBuilder(
                        context.getApplicationContext(),
                        StockDatabase.class,
                        "StockDatabase"
                ).fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized StockClient getInstance(Context context) {
        if (instance == null) {
            instance = new StockClient(context);
        }
        return instance;
    }

    public StockDatabase getStockDatabase() {
        return stockDatabase;
    }
}
