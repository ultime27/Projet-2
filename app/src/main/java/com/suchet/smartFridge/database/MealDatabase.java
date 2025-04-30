package com.suchet.smartFridge.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.suchet.smartFridge.database.TypeConverter.ListConverter;
import com.suchet.smartFridge.database.TypeConverter.ListFoodConverter;
import com.suchet.smartFridge.database.TypeConverter.TypeLocalDateConverter;
import com.suchet.smartFridge.database.entities.Meal;


@Database(entities = {Meal.class}, version = 4)
@TypeConverters({TypeLocalDateConverter.class, ListFoodConverter.class})
public abstract class MealDatabase extends RoomDatabase {
    private static volatile MealDatabase INSTANCE;

    public abstract MealDAO mealDao();

    public static MealDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (MealDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MealDatabase.class, "meal_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

