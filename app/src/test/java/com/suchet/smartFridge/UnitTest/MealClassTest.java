package com.suchet.smartFridge.UnitTest;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import com.suchet.smartFridge.database.entities.Food;
import com.suchet.smartFridge.database.entities.Meal;

public class MealClassTest {

    private Meal meal;
    private List<Food> foodList;

    @Before
    public void setUp() {
        foodList = new ArrayList<>();
        foodList.add(new Food("Apple"));
        foodList.add(new Food("Banana"));

        meal = new Meal("Fruits Salad", LocalDate.now(), foodList, 1);
    }

    @Test
    public void testMealConstructor() {
        assertEquals("Fruits Salad", meal.getName());
        assertEquals(LocalDate.now(), meal.getDate());
        assertNotNull(meal.getFoodList());
        assertEquals(2, meal.getFoodList().size());
        assertEquals(1, meal.getUserId());
    }

    @Test
    public void testAddFoodToMeal() {
        Food orange = new Food("Orange");
        meal.getFoodList().add(orange);

        assertEquals(3, meal.getFoodList().size());
        assertEquals("Orange", meal.getFoodList().get(2).getName());
    }

    @Test
    public void testRemoveFoodFromMeal() {
        meal.getFoodList().remove(0);

        assertEquals(1, meal.getFoodList().size());
        assertEquals("Banana", meal.getFoodList().get(0).getName());
    }

    @Test
    public void testSettersAndGetters() {
        meal.setName("Updated Fruits Salad");
        meal.setDate(LocalDate.of(2025, 5, 1));

        assertEquals("Updated Fruits Salad", meal.getName());
        assertEquals(LocalDate.of(2025, 5, 1), meal.getDate());
    }

    @Test
    public void testMealWithEmptyFoodList() {
        Meal emptyMeal = new Meal("Empty Meal", LocalDate.now(), new ArrayList<>(), 2);

        assertNotNull(emptyMeal.getFoodList());
        assertTrue(emptyMeal.getFoodList().isEmpty());
    }
}