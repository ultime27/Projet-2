package com.suchet.smartFridge.database.entities;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class Converters {
    @TypeConverter
    public static HashMap<String, Double> fromString(String value) {
        Type mapType = new TypeToken<HashMap<String, Double>>() {}.getType();
        return new Gson().fromJson(value, mapType);
    }

    @TypeConverter
    public static String fromMap(HashMap<String, Double> map) {
        return new Gson().toJson(map);
    }
}

