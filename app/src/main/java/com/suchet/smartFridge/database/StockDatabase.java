package com.suchet.smartFridge.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.suchet.smartFridge.database.TypeConverter.TypeLocalDateConverter;
import com.suchet.smartFridge.database.entities.Food;

@Database(entities = {Food.class},version = 1,exportSchema = false)
@TypeConverters(TypeLocalDateConverter.class)
public abstract class StockDatabase extends RoomDatabase {
    public abstract FoodDAO foodDAO();


}

