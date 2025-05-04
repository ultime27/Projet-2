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

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Recipe.class},version = 4,exportSchema = false)
@TypeConverters(Converters.class)
public abstract class RecipeDatabase extends RoomDatabase {
    public static final String RECIPE_TABLE = "recipe_table";
    private static final String DATABASE_NAME = "Recipe_database";
    private static volatile RecipeDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract RecipeDAO recipeDAO();

    public static RecipeDatabase getDatabase(final Context context) {
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

                // Pasta Carbonara with Sour Cream
                HashMap<String, Double> carbonaraSourCreamIngredients = new HashMap<>();
                carbonaraSourCreamIngredients.put("Spaghetti", 200.0);
                carbonaraSourCreamIngredients.put("Bacon", 100.0);
                carbonaraSourCreamIngredients.put("Sour Cream", 100.0);
                carbonaraSourCreamIngredients.put("Parmesan Cheese", 50.0);
                carbonaraSourCreamIngredients.put("Eggs", 2.0);
                carbonaraSourCreamIngredients.put("Black Pepper", 1.0);
                dao.insert(new Recipe(
                        "Pasta Carbonara with Sour Cream",
                        carbonaraSourCreamIngredients,
                        "A creamy twist on the classic carbonara using sour cream.",
                        "Cook spaghetti. In a separate pan, cook bacon until crisp. In a bowl, mix sour cream, eggs, and Parmesan. Combine everything and season with black pepper."
                ));

                // Italian Pasta Carbonara
                HashMap<String, Double> italianCarbonaraIngredients = new HashMap<>();
                italianCarbonaraIngredients.put("Spaghetti", 200.0);
                italianCarbonaraIngredients.put("Guanciale", 100.0);
                italianCarbonaraIngredients.put("Pecorino Romano", 50.0);
                italianCarbonaraIngredients.put("Egg Yolks", 4.0);
                italianCarbonaraIngredients.put("Black Pepper", 1.0);
                dao.insert(new Recipe(
                        "Italian Pasta Carbonara",
                        italianCarbonaraIngredients,
                        "Traditional Italian carbonara without cream.",
                        "Cook spaghetti. Sauté guanciale until crispy. In a bowl, mix egg yolks and Pecorino. Combine everything and season with black pepper."
                ));

                // Tiramisu
                HashMap<String, Double> tiramisuIngredients = new HashMap<>();
                tiramisuIngredients.put("Ladyfingers", 200.0);
                tiramisuIngredients.put("Mascarpone Cheese", 250.0);
                tiramisuIngredients.put("Egg Yolks", 3.0);
                tiramisuIngredients.put("Sugar", 100.0);
                tiramisuIngredients.put("Espresso", 200.0);
                tiramisuIngredients.put("Cocoa Powder", 10.0);
                dao.insert(new Recipe(
                        "Tiramisu",
                        tiramisuIngredients,
                        "Classic Italian coffee-flavored dessert.",
                        "Dip ladyfingers in espresso. Layer with a mixture of mascarpone, egg yolks, and sugar. Repeat layers and dust with cocoa powder. Chill before serving."
                ));

                // Pizza Margherita
                HashMap<String, Double> margheritaIngredients = new HashMap<>();
                margheritaIngredients.put("Pizza Dough", 1.0);
                margheritaIngredients.put("Tomato Sauce", 100.0);
                margheritaIngredients.put("Fresh Mozzarella", 150.0);
                margheritaIngredients.put("Fresh Basil Leaves", 10.0);
                margheritaIngredients.put("Olive Oil", 10.0);
                dao.insert(new Recipe(
                        "Pizza Margherita",
                        margheritaIngredients,
                        "Simple and classic Italian pizza.",
                        "Spread tomato sauce over rolled-out dough. Top with mozzarella slices and basil leaves. Drizzle with olive oil and bake until crust is golden."
                ));

                // Mayonnaise
                HashMap<String, Double> mayonnaiseIngredients = new HashMap<>();
                mayonnaiseIngredients.put("Egg Yolk", 1.0);
                mayonnaiseIngredients.put("Lemon Juice", 15.0);
                mayonnaiseIngredients.put("Dijon Mustard", 5.0);
                mayonnaiseIngredients.put("Vegetable Oil", 240.0);
                mayonnaiseIngredients.put("Salt", 1.0);
                dao.insert(new Recipe(
                        "Mayonnaise",
                        mayonnaiseIngredients,
                        "Homemade creamy mayonnaise.",
                        "Whisk egg yolk, lemon juice, and mustard. Slowly add oil while whisking until emulsified. Season with salt."
                ));

                // Chocolate Fondant
                HashMap<String, Double> fondantIngredients = new HashMap<>();
                fondantIngredients.put("Dark Chocolate", 120.0);
                fondantIngredients.put("Unsalted Butter", 100.0);
                fondantIngredients.put("Eggs", 2.0);
                fondantIngredients.put("Sugar", 100.0);
                fondantIngredients.put("Flour", 50.0);
                dao.insert(new Recipe(
                        "Chocolate Fondant",
                        fondantIngredients,
                        "Rich and gooey chocolate dessert.",
                        "Melt chocolate and butter together. In a separate bowl, beat eggs and sugar until fluffy. Combine with melted chocolate and fold in flour. Pour into molds and bake until edges are set but center is still soft."
                ));
                // TODO: cree des recettes
            });
        }
    };

    public static ExecutorService getExecutor() {
        return databaseWriteExecutor;
    }


}

