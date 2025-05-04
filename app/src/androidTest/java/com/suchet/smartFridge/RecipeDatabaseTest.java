package com.suchet.smartFridge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;

import androidx.test.core.app.ApplicationProvider;

import com.suchet.smartFridge.database.RecipeDAO;
import com.suchet.smartFridge.database.RecipeDatabase;
import com.suchet.smartFridge.database.entities.Recipe;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;


public class RecipeDatabaseTest {
    private RecipeDatabase db;
    private RecipeDAO dao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, RecipeDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.recipeDAO();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertTest() {
        HashMap<String, Double> ingredients = new HashMap<>();
        ingredients.put("tomato", 100.0);

        Recipe recipe = new Recipe("pizza", ingredients, "description", "instruction");
        dao.insert(recipe);

        Recipe result = dao.searchByName("pizza");

        assertNotNull(result);
        assertEquals("pizza", result.name);
        assertEquals("description", result.description);
    }

    @Test
    public void updateTest() {
        HashMap<String, Double> ingredients = new HashMap<>();
        ingredients.put("tomato", 100.0);


        Recipe recipe = new Recipe("pizza", ingredients, "description", "instruction");
        dao.insert(recipe);

        Recipe result = dao.searchByName("pizza");

        assertNotNull(result);
        assertEquals("pizza", result.name);
        assertEquals("description", result.description);


        recipe = dao.searchByName("pizza");
        recipe.description= "desc2";
        dao.insert(recipe);

        result = dao.searchByName("pizza");

        assertNotNull(result);
        assertEquals("pizza", result.name);
        assertNotEquals("description", result.description);
        assertEquals("desc2", result.description);
    }

    @Test
    public void DeleteTest() {
        HashMap<String, Double> ingredients = new HashMap<>();
        ingredients.put("tomato", 100.0);

        Recipe recipe = new Recipe("pizza", ingredients, "description", "instruction");
        dao.insert(recipe);

        Recipe result = dao.searchByName("pizza");

        assertNotNull(result);
        assertEquals("pizza", result.name);
        assertEquals("description", result.description);
        dao.deletebyName(result.name);
        result = dao.searchByName("pizza");
        assertNull(result);
    }



}
