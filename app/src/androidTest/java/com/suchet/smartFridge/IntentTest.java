package com.suchet.smartFridge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;

import com.suchet.smartFridge.Recipie.CreateRecipeActivity;
import com.suchet.smartFridge.Recipie.StartRecipieActivity;
import com.suchet.smartFridge.Recipie.SuggestionPageActivity;
import com.suchet.smartFridge.Settings.SettingActivity;
import com.suchet.smartFridge.stocks.AddStockActivity;
import com.suchet.smartFridge.stocks.DeleteStockActivity;
import com.suchet.smartFridge.stocks.ShoppingListActivity;
import com.suchet.smartFridge.stocks.StockActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IntentTest {

    @Test
    public void testLoginIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = LoginActivity.loginIntentFactory(context);

        assertNotNull(intent);
        assertEquals(LoginActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void testMainIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = MainActivity.mainActivityIntentFactory(context,0);

        assertNotNull(intent);
        assertEquals(MainActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void MealIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = MealActivity.MealIntentFactory(context);

        assertNotNull(intent);
        assertEquals(MealActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void LandingIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = LandingPage.landingPageActivityIntentFactory(context);

        assertNotNull(intent);
        assertEquals(LandingPage.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void AddMealIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = AddMealActivity.AddMealIntentFactory(context);

        assertNotNull(intent);
        assertEquals(AddMealActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void AddStockIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = AddStockActivity.AddToStockIntentFactory(context);

        assertNotNull(intent);
        assertEquals(AddStockActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void DeleteStockIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = DeleteStockActivity.DeleteStockIntentFactory(context);

        assertNotNull(intent);
        assertEquals(DeleteStockActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void ShoppingListIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = ShoppingListActivity.ShoppingListIntentFactory(context);

        assertNotNull(intent);
        assertEquals(ShoppingListActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void StockIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = StockActivity.StockIntentFactory(context);

        assertNotNull(intent);
        assertEquals(StockActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void SettingIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = SettingActivity.SettingIntentFactory(context);

        assertNotNull(intent);
        assertEquals(SettingActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void StartRecipeIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = StartRecipieActivity.StartRecipieActivityFactory(context, "Test Recipe");

        assertNotNull(intent);
        assertEquals(StartRecipieActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void SuggestionPageIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = SuggestionPageActivity.suggestionPageActivityIntentFactory(context);

        assertNotNull(intent);
        assertEquals(SuggestionPageActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void SuestionPageIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = CreateRecipeActivity.createRecipieActivityIntentFactory(context);

        assertNotNull(intent);
        assertEquals(CreateRecipeActivity.class.getName(), intent.getComponent().getClassName());
    }



}
