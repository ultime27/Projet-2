package com.suchet.smartFridge;

import org.junit.Test;

import static org.junit.Assert.*;

import com.suchet.smartFridge.database.entities.Food;

import java.time.LocalDate;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    LocalDate dateToday = LocalDate.now();
    LocalDate dateTomorrow = dateToday.plusDays(1);
    Food apple = new Food("Apple", dateToday, 3);
    Food orange = new Food("Orange", dateTomorrow, 2);

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void expiration_isCorrect() {
        assertNotEquals(apple.getExpirationDate(), orange.getExpirationDate());
    }

    @Test
    public void quantity_isCorrect() {
        assertNotEquals(apple.getQuantity(), orange.getQuantity());
        orange.setQuantity(3);
        assertEquals(apple.getQuantity(), orange.getQuantity());
    }
}