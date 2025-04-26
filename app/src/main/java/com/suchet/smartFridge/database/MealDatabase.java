package com.suchet.smartFridge.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.suchet.smartFridge.database.entities.Meal;

@Database(entities = {Meal.class},version = 1,exportSchema = false)

abstract class MealDatabase extends RoomDatabase {
}
