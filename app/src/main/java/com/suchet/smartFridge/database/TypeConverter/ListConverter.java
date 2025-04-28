package com.suchet.smartFridge.database.TypeConverter;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListConverter {
    @TypeConverter
    public static String fromList(List<String> list) {
        return list != null ? String.join(",", list) : null;
    }

    @TypeConverter
    public static List<String> toList(String concatenated) {
        return concatenated != null ? new ArrayList<>(Arrays.asList(concatenated.split(","))) : new ArrayList<>();
    }
}
