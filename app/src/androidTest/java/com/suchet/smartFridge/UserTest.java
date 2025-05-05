package com.suchet.smartFridge;

import static com.suchet.smartFridge.MainActivity.TAG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.suchet.smartFridge.database.RecipeDAO;
import com.suchet.smartFridge.database.RecipeDatabase;
import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.UserDAO;
import com.suchet.smartFridge.database.entities.Recipe;
import com.suchet.smartFridge.database.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    private SmartFridgeDatabase db;
    private UserDAO dao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, SmartFridgeDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.userDAO();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertTest() {
        User user = new User("testuser0", "abcde");
        dao.insert(user);

        User copy = dao.getUserByUsernameSync("testuser0");

        assertNotNull(copy);
        assertEquals(user.getUsername(), copy.getUsername());
        assertEquals(user.getPassword(), copy.getPassword());
    }

    @Test
    public void updateTest() {
        User user = new User("testuser0", "abcde");
        dao.insert(user);
        User copy = dao.getUserByUsernameSync("testuser0");

        assertNotNull(copy);
        assertEquals(user.getUsername(), copy.getUsername());
        assertEquals(user.getPassword(), copy.getPassword());

        user = new User("testuser0", "fghij");
        dao.insert(user);

        copy = dao.getUserByUsernameSync("testuser0");

        assertNotNull(copy);
        assertEquals(user.getUsername(), copy.getUsername());
        assertEquals(user.getPassword(), copy.getPassword());
    }

    @Test
    public void DeleteTest() {
        User user = new User("testuser0", "abcde");
        dao.insert(user);

        User copy = dao.getUserByUsernameSync("testuser0");

        assertNotNull(copy);
        assertEquals(user.getUsername(), copy.getUsername());
        assertEquals(user.getPassword(), copy.getPassword());

        dao.deleteByUsername("testuser0");
        copy = dao.getUserByUsernameSync("testuser0");
        assertNull(copy);
    }



}
