package com.suchet.smartFridge.database.TypeConverter;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class TypeLocalDateConverter {
        @TypeConverter
        public static String fromLocalDate(LocalDate date) {
            return date.toString();
        }

        @TypeConverter
        public static LocalDate toLocalDate(String dateString) {
            return LocalDate.parse(dateString);
        }

}
