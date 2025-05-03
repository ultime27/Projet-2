package com.suchet.smartFridge.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.suchet.smartFridge.database.entities.ShoppingItem;

import java.util.List;

@Dao
public interface ShoppingItemDAO {

    @Insert
    void insert(ShoppingItem item);

    @Update
    void update(ShoppingItem item);

    @Delete
    void delete(ShoppingItem item);

    @Query("SELECT * FROM shopping_items WHERE userId = :userId")
    List<ShoppingItem> getAllForUser(int userId);


}
