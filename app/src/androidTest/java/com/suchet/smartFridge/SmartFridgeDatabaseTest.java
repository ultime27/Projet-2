package com.suchet.smartFridge;

import static org.junit.Assert.*;

import android.content.Context;


import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.suchet.smartFridge.database.FoodDAO;
import com.suchet.smartFridge.database.MealDAO;
import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.UserDAO;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.Meal;
import com.suchet.smartFridge.database.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SmartFridgeDatabaseTest {
    private SmartFridgeDatabase db;
    private FoodDAO foodDAO;
    private MealDAO mealDAO;

    private UserDAO userDAO;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, SmartFridgeDatabase.class)
                .allowMainThreadQueries()
                .build();
        foodDAO = db.foodDAO();
        mealDAO = db.mealDAO();
        userDAO = db.userDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testInsertFood() {
        Food food = new Food("Apple");
        food.setQuantity(5);
        food.setUserId(1);

        foodDAO.insert(food);
        Food result = foodDAO.getFoodByName("Apple", 1);

        assertNotNull(result);
        assertEquals("Apple", result.getName());
        assertEquals(5, result.getQuantity(), 0);
    }

    @Test
    public void testUpdateFood() {
        Food food = new Food("Banana");
        food.setQuantity(2);
        food.setUserId(1);
        foodDAO.insert(food);

        Food inserted = foodDAO.getFoodByName("Banana", 1);
        inserted.setQuantity(8);
        foodDAO.update(inserted);

        Food updated = foodDAO.getFoodByName("Banana", 1);
        assertEquals(8, updated.getQuantity(), 0);
    }

    @Test
    public void testDeleteFood() {
        Food food = new Food("Orange");
        food.setQuantity(3);
        food.setUserId(1);
        foodDAO.insert(food);

        Food inserted = foodDAO.getFoodByName("Orange", 1);
        foodDAO.delete(inserted);

        Food deleted = foodDAO.getFoodByName("Orange", 1);
        assertNull(deleted);
    }



    @Test
    public void testInsertMeal() {
        Meal meal = new Meal("Lunch", LocalDate.now(), new ArrayList<>(), 1);
        mealDAO.insert(meal);

        List<Meal> meals = mealDAO.getMealsByUser(1);
        assertEquals(1, meals.size());
        assertEquals("Lunch", meals.get(0).getName());
    }

    @Test
    public void testUpdateMeal() {
        Meal meal = new Meal("Dinner", LocalDate.now(), new ArrayList<>(), 1);
        mealDAO.insert(meal);

        List<Meal> meals = mealDAO.getMealsByUser(1);
        Meal toUpdate = meals.get(0);
        toUpdate.setName("Updated Dinner");
        mealDAO.update(toUpdate);

        List<Meal> updatedMeals = mealDAO.getMealsByUser(1);
        assertEquals("Updated Dinner", updatedMeals.get(0).getName());
    }

    @Test
    public void testDeleteMeal() {
        Meal meal = new Meal("Breakfast", LocalDate.now(), new ArrayList<>(), 1);
        mealDAO.insert(meal);

        List<Meal> meals = mealDAO.getMealsByUser(1);
        assertEquals(1, meals.size());

        mealDAO.delete(meals.get(0));
        List<Meal> afterDelete = mealDAO.getMealsByUser(1);
        assertTrue(afterDelete.isEmpty());
    }


    @Test
    public void testInsertUser() {
        User user = new User("testuser", "password");
        userDAO.insert(user);

        List<User> allUsers = userDAO.getAllUsersList();
        assertEquals(1, allUsers.size());
        assertEquals("testuser", allUsers.get(0).getUsername());
    }

    @Test
    public void testUpdateUser() {
        User user = new User("toUpdate", "1234");
        userDAO.insert(user);

        User inserted = userDAO.getUserByUsernameSync("toUpdate");
        inserted.setPassword("updatedPassword");
        userDAO.insert(inserted);

        User updated = userDAO.getUserByUsernameSync("toUpdate");
        assertEquals("updatedPassword", updated.getPassword());
    }

    @Test
    public void testDeleteUser() {
        User user = new User("toDelete", "pass");
        userDAO.insert(user);

        User inserted = userDAO.getUserByUsernameSync("toDelete");
        userDAO.delete(inserted);

        List<User> remaining = userDAO.getAllUsersList();
        assertTrue(remaining.isEmpty());
    }
}
