package com.suchet.smartFridge.database.UnitTest;

import static org.junit.Assert.*;

import com.suchet.smartFridge.database.entities.User;

import org.junit.Test;

public class UserClassTest {

    @Test
    public void testChangeName() {
        User testUser = new User("testUser", "abcde");
        assertEquals(testUser.getUsername(), "testUser");
        testUser.setUsername("N/A");
        assertNotEquals(testUser.getUsername(), "testUser");
    }

    @Test
    public void testChangePassword() {
        User testUser = new User("testUser", "abcde");
        assertEquals(testUser.getPassword(), "abcde");
        testUser.setPassword("fghij");
        assertNotEquals(testUser.getPassword(), "abcde");
    }

}
