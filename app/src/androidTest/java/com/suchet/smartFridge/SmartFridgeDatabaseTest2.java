package com.suchet.smartFridge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.suchet.smartFridge.database.MealDAO;
import com.suchet.smartFridge.database.ShoppingItemDAO;
import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.UserDAO;
import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.Meal;
import com.suchet.smartFridge.database.entities.ShoppingItem;
import com.suchet.smartFridge.database.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SmartFridgeDatabaseTest2 {
    private SmartFridgeDatabase db;
    private UserDAO daoUser;
    private MealDAO daoMeal;
    private ShoppingItemDAO daoShoppingItem;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, SmartFridgeDatabase.class)
                .allowMainThreadQueries()
                .build();
        daoUser = db.userDAO();
        daoMeal = db.mealDAO();
        daoShoppingItem = db.shoppingItemDAO();

    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void UserinsertTest() throws InterruptedException {
        User user= new User("lucas", "matthieu");
        daoUser.insert(user);


        assertNotNull(daoUser.getUserByUsername("lucas"));
        user = daoUser.getUserByUsername("lucas");


        assertEquals("lucas",user.getUsername());

        assertEquals("matthieu",user.getPassword());

    }

    @Test
    public void UserupdateTest() throws InterruptedException {
        User user= new User("lucas", "matthieu");
        daoUser.insert(user);


        User result = daoUser.getUserByUsername("lucas");
        assertNotNull(daoUser.getUserByUsername("lucas"));
        assertEquals("lucas",result.getUsername());
        assertEquals("matthieu",result.getPassword());


        result.setUsername("matthieu");
        result.setPassword("lucas");
        daoUser.insert(result);

        assertNotNull(daoUser.getUserByUsername("matthieu"));
        assertEquals("matthieu", daoUser.getUserByUsername("matthieu").getUsername());
        assertNotEquals("matthieu", daoUser.getUserByUsername("matthieu").getPassword());
        assertEquals("lucas", result.getPassword());
    }

    @Test
    public void UserDeleteTest() throws InterruptedException {
        User user= new User("lucas", "matthieu");
        daoUser.insert(user);
        User result = daoUser.getUserByUsername("lucas");


        assertNotNull(daoUser.getUserByUsername("lucas"));
        assertEquals("lucas",result.getUsername());
        assertEquals("matthieu",result.getPassword());

        daoUser.deleteByUsername("lucas");

        User deletedUser = daoUser.getUserByUsername("lucas");
        assertNull(deletedUser);
    }



    @Test
    public void MealinsertTest() {
        User user= new User("lucas", "matthieu");
        daoUser.insert(user);

        Food food = new Food("pasta");
        List<Food> foodlist = new ArrayList<>();
        foodlist.add(food);

        Meal meal = new Meal("pasta o pesto", LocalDate.now(), foodlist, daoUser.getUserByUsername("lucas").getId());
        daoMeal.insert(meal);

        int userId = daoUser.getUserByUsername("lucas").getId();


        assertNotNull(daoMeal.getMealsByUser(userId).get(0));

        assertEquals("pasta o pesto",daoMeal.getMealsByUser(userId).get(0).getName());

        assertEquals("pasta",daoMeal.getMealsByUser(userId).get(0).getFoodList().get(0).getName());

    }

    @Test
    public void MealupdateTest() throws InterruptedException {
        User user= new User("lucas", "matthieu");
        daoUser.insert(user);

        Food food = new Food("pasta");
        List<Food> foodlist = new ArrayList<>();
        foodlist.add(food);

        User result1 = daoUser.getUserByUsername("lucas");


        Meal meal = new Meal("pasta o pesto", LocalDate.now(), foodlist, result1.getId());
        daoMeal.insert(meal);

        int userId = result1.getId();


        assertNotNull(daoMeal.getMealsByUser(userId).get(0));

        assertEquals("pasta o pesto",daoMeal.getMealsByUser(userId).get(0).getName());

        assertEquals("pasta",daoMeal.getMealsByUser(userId).get(0).getFoodList().get(0).getName());


        Meal result = daoMeal.getMealsByUser(userId).get(0);
        Food food1 = new Food("pesto");
        Food food2 = new Food("salt");
        result.getFoodList().add(food1);
        result.getFoodList().add(food2);
        result.getFoodList().get(0).setName("test");

        daoMeal.update(result);
        result=daoMeal.getMealsByUser(userId).get(0);



        assertNotNull(result);
        assertEquals("pesto", result.getFoodList().get(1).getName());
        assertEquals("test", result.getFoodList().get(0).getName());
        assertEquals("salt", result.getFoodList().get(2).getName());
    }

    @Test
    public void MealDeleteTest() throws InterruptedException {
        User user= new User("lucas", "matthieu");
        daoUser.insert(user);

        Food food = new Food("pasta");
        List<Food> foodlist = new ArrayList<>();
        foodlist.add(food);
        User result1 = daoUser.getUserByUsername("lucas");


        Meal meal = new Meal("pasta o pesto", LocalDate.now(), foodlist, result1.getId());
        daoMeal.insert(meal);

        int userId = result1.getId();


        assertNotNull(daoMeal.getMealsByUser(userId).get(0));

        assertEquals("pasta o pesto",daoMeal.getMealsByUser(userId).get(0).getName());

        assertEquals("pasta",daoMeal.getMealsByUser(userId).get(0).getFoodList().get(0).getName());

        daoMeal.delete(meal);

        assertFalse(daoMeal.getMealsByUser(userId).contains(meal));
    }






}
