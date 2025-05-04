package com.suchet.smartFridge.UnitTest;

import static org.junit.Assert.*;

import com.suchet.smartFridge.database.entities.Food;

import org.junit.Test;

import java.time.LocalDate;

public class FoodClassTest {
    @Test
    public void testAddQuantity() {
        Food food = new Food("Apple");
        food.add(2.0);
        assertEquals(2.0, food.getQuantity(), 0.001);
    }

    @Test
    public void testRemoveQuantityFailsWhenLess() {
        Food food = new Food("Banana");
        food.setQuantity(1.0);
        assertFalse(food.remove(2.0));
    }

    @Test
    public void testGetAndSetName() {
        Food food = new Food("Milk");
        food.setName("Cheese");
        assertEquals("Cheese", food.getName());
    }

    @Test
    public void testDatePeremptionDefaultAndSet() {
        Food food = new Food("Yogurt");
        LocalDate expectedDate = LocalDate.now().plusDays(7);
        assertEquals(expectedDate, food.getDatePeremption());

        LocalDate newDate = LocalDate.now().plusDays(10);
        food.setDatePeremption(newDate);
        assertEquals(newDate, food.getDatePeremption());
    }

    @Test
    public void testUserId() {
        Food food = new Food("Eggs");
        food.setUserId(42);
        assertEquals(42, food.getUserId());
    }
}
