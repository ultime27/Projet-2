package com.suchet.smartFridge.database.UnitTest;

import static org.junit.Assert.*;

import com.suchet.smartFridge.database.entities.Recipe;

import org.junit.Test;

import java.util.HashMap;

public class RecipieClassTest {

    @Test
    public void testAddIngredient() {
        Recipe recipe = new Recipe("Test", "desc", "instr");
        recipe.addIngredient("Sugar", 100.0);
        assertTrue(recipe.getIngredientList().containsKey("Sugar"));
        assertEquals(Double.valueOf(100.0), recipe.getIngredientList().get("Sugar"));
    }

    @Test
    public void testRemoveIngredient() {
        Recipe recipe = new Recipe("Test", "desc", "instr");
        recipe.addIngredient("Salt", 50.0);
        recipe.removeIngredient("Salt");
        assertFalse(recipe.getIngredientList().containsKey("Salt"));
    }

    @Test
    public void testEqualsAndHashCode() {
        Recipe r1 = new Recipe("Cake", "desc", "instr");
        Recipe r2 = new Recipe("Cake", "other desc", "other instr");
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void testToString() {
        HashMap<String, Double> ingredients = new HashMap<>();
        ingredients.put("Flour", 200.0);
        Recipe recipe = new Recipe("Bread", ingredients, "desc", "instr");
        String result = recipe.toString();
        assertTrue(result.contains("Flour"));
        assertTrue(result.contains("200.00"));
    }
}
