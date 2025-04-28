package com.suchet.smartFridge.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.List;


@Entity(tableName = "meal_table")
public class Meal {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    @NonNull
    public LocalDate date;

    public Meal(String name, @NonNull LocalDate date) {
        this.name = name;
        this.date = date;
    }
}
