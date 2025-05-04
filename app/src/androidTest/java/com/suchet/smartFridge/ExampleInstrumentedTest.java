package com.suchet.smartFridge;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.suchet.smartFridge.database.FoodDAO;
import com.suchet.smartFridge.database.MealDAO;
import com.suchet.smartFridge.database.RecipeDAO;
import com.suchet.smartFridge.database.RecipeDatabase;
import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.UserDAO;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.Recipe;
import com.suchet.smartFridge.database.entities.User;

import junit.framework.TestCase;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest extends TestCase {

    User testUser = new User("testuser2", "testuser2");
    Recipe testRecipe = new Recipe("Poutine", "Fries with gravy and cheese curds.", "Add all ingredients into a bowl.");
    Food testFood = new Food("Cake");

    SmartFridgeDatabase db;
    //    UserDAO userDAO;

    RecipeDatabase recipeDb;
    RecipeDAO recipeDAO;



//    FoodDAO foodDAO;
//    MealDAO mealDAO;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, SmartFridgeDatabase.class).build();
//        userDAO = db.userDAO();
        recipeDAO = recipeDb.recipeDAO();
//        foodDAO = db.foodDAO();
//        mealDAO = db.mealDAO();
    }

    @After
    public void tearDown() throws Exception {
        testUser = null;
        testRecipe = null;
        testFood = null;

        db.close();
    }

//    @Test
//    public void testDeleteRecipeByRecipeId(){
//        /*long testRecipeId = recipeDAO.insert(testRecipe);
//        assertNotNull(RecipeDAO.getRecipeByRecipeId(testRecipeId));
//        RecipeDAO.deleteRecipeByRecipeId(testRecipeId);
//        assertNull(RecipeDAO.getRecipeByRecipeId(testRecipeId));*/
//    }

    @Test
    public void testInsertRecipe() {
        recipeDAO.insert(testRecipe);
    }
}