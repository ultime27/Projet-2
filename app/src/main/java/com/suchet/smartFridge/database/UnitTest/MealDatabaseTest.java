package com.suchet.smartFridge.database.UnitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.suchet.smartFridge.database.MealDAO;
import com.suchet.smartFridge.database.MealDatabase;
import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.UserDAO;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.Meal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MealDatabaseTest {
    private MealDatabase db;
    private MealDAO dao;

    LocalDate today = LocalDate.now();
    Food milk = new Food("Milk");
    Food cream = new Food("Cream");


    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MealDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.mealDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertTest() {
        Meal meal = new Meal();
        meal.setName("Ice Creams");
        dao.insert(meal);

        Meal copy = dao.getMealByName("Ice Creams");

        assertNotNull(copy);
        assertEquals(meal.getName(), copy.getName());
    }

    @Test
    public void updateTest() {
        Meal meal = new Meal();
        meal.setName("Ice Creams");
        dao.insert(meal);

        Meal copy = dao.getMealByName("Ice Creams");
        assertNotNull(copy);

        assertEquals(meal.getName(), copy.getName());

        meal.setName("Gelato");
        dao.insert(meal);

        copy = dao.getMealByName("Gelato");
        assertNotNull(copy);
        assertEquals(meal.getName(), copy.getName());
    }

    @Test
    public void DeleteTest() {
        Meal meal = new Meal();
        meal.setName("Ice Creams");
        dao.insert(meal);

        Meal copy = dao.getMealByName("Ice Creams");

        assertNotNull(copy);
        assertEquals(meal.getName(), copy.getName());

        dao.deletebyName("Ice Creams");
        copy = dao.getMealByName("Ice Creams");
        assertNull(copy);
    }



}
