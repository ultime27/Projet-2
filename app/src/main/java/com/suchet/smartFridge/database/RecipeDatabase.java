// src/main/java/com/suchet/smartFridge/database/entities/Recipe.java
package com.suchet.smartFridge.database;

import androidx.annotation.NonNull;
import androidx.room.Database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.suchet.smartFridge.database.entities.Converters;
import com.suchet.smartFridge.database.entities.Recipe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Recipe.class},version = 1,exportSchema = false)
@TypeConverters(Converters.class)
public abstract class RecipeDatabase extends RoomDatabase {
    public static final String RECIPE_TABLE = "recipe_table";
    private static final String DATABASE_NAME = "Recipe_database";
    private static volatile RecipeDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract RecipeDAO recipeDAO();

    static RecipeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    RecipeDatabase.class,
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
            databaseWriteExecutor.execute(() -> {
                RecipeDAO dao = INSTANCE.recipeDAO();
                // TODO: cree des recettes
            });
        }
    };
}

