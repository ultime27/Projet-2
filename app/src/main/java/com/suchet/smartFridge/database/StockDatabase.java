package com.suchet.smartFridge.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.suchet.smartFridge.MainActivity;
import com.suchet.smartFridge.database.TypeConverter.TypeLocalDateConverter;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Food.class},version = 1,exportSchema = false)
@TypeConverters(TypeLocalDateConverter.class)
public abstract class StockDatabase extends RoomDatabase {
    public static final String STOCK_TABLE = "stock_table";
    private static volatile StockDatabase INSTANCE;
    private static final String DATABASE_NAME = "Stock_database";
    public abstract FoodDAO foodDAO();
    private static final int NUMBER_OF_THREADS=4;
    static final ExecutorService dataBaseWriteExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static StockDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    StockDatabase.class,
                                    DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED!");
            dataBaseWriteExecutor.execute(()->{
                FoodDAO dao=INSTANCE.foodDAO();
                dao.deleteAll();
                Food food1 = new Food("salt");;
                dao.insert(food1);
            });
        }
    };
}



