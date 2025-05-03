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
import com.suchet.smartFridge.database.TypeConverter.ListFoodConverter;
import com.suchet.smartFridge.database.TypeConverter.TypeLocalDateConverter;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.Meal;
import com.suchet.smartFridge.database.entities.ShoppingItem;
import com.suchet.smartFridge.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Food.class, ShoppingItem.class, Meal.class},version = 1,exportSchema = false)
@TypeConverters({TypeLocalDateConverter.class, ListFoodConverter.class})
public abstract class SmartFridgeDatabase extends RoomDatabase {
    public static final String USER_TABLE = "user_table";
    public static final String FOOD_TABLE = "food_table";
    public static final String MEAL_TABLE = "meal_table";
    private static final String DATABASE_NAME="SmartFridge_Database";
    private static volatile SmartFridgeDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS=4;
    static final ExecutorService dataBaseWriteExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static SmartFridgeDatabase getDatabase(final Context context){
        if (INSTANCE==null){
            synchronized (SmartFridgeDatabase.class){
                if (INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    SmartFridgeDatabase.class,
                                    DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static final RoomDatabase.Callback addDefaultValues=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED!");
            dataBaseWriteExecutor.execute(()->{
                UserDAO dao=INSTANCE.userDAO();
                dao.deleteAll();
                User admin = new User("admin2","admin2");
                admin.setAdmin(true);
                dao.insert(admin);
                User testuser1 = new User("testuser1","testuser1");
                dao.insert(testuser1);
            });
        }
    };
    public abstract UserDAO userDAO();
    public abstract FoodDAO foodDAO();
    public abstract ShoppingItemDAO shoppingItemDAO();

    public abstract MealDAO mealDAO();

}
