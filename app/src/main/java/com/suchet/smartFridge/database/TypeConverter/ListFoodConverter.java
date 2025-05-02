package com.suchet.smartFridge.database.TypeConverter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suchet.smartFridge.database.entities.Food;

import java.lang.reflect.Type;
import java.util.List;

public class ListFoodConverter {
    @TypeConverter
    public static String fromFoodList(List<Food> foodList) {
        Gson gson = new Gson();
        return gson.toJson(foodList);
    }

    @TypeConverter
    public static List<Food> toFoodList(String foodListString) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Food>>() {}.getType();
        return gson.fromJson(foodListString, type);
    }
}
