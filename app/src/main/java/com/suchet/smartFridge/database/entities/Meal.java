package com.suchet.smartFridge.database.entities;

import androidx.room.PrimaryKey;

import java.time.LocalDate;
public class Meal {
    @PrimaryKey
    LocalDate date;


}
