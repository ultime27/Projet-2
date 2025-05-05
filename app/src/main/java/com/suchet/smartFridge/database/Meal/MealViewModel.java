package com.suchet.smartFridge.database.Meal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.suchet.smartFridge.database.MealDAO;
import com.suchet.smartFridge.database.MealDatabase;
import com.suchet.smartFridge.database.entities.Meal;

import java.util.List;
import java.util.concurrent.Executors;

public class MealViewModel extends AndroidViewModel {
    private MealDAO mealDao;
    private LiveData<List<Meal>> allMeals;

    public MealViewModel(@NonNull Application application) {
        super(application);
        MealDatabase db = MealDatabase.getDatabase(application);
        mealDao = db.mealDao();
    }

    public LiveData<List<Meal>> getMealsForUser(int userId) {
        return mealDao.getMealsByUserId(userId);
    }

    public void insert(Meal meal) {
        Executors.newSingleThreadExecutor().execute(() -> mealDao.insert(meal));
    }
}
