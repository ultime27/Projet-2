package com.suchet.smartFridge.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.suchet.smartFridge.database.SmartFridgeDatabase;

import java.util.Objects;

@Entity(tableName = SmartFridgeDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;
    private boolean isAdmin;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void addMeal(Food food) {
        System.out.println(this + " added " + food + " (" + food.getQuantity() + ").\n[" + food.getExpirationDate() + "]");

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && isAdmin == user.isAdmin && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, isAdmin);
    }

    public User(String username, String password) {
        this.isAdmin = false;
        this.username = username;
        this.password = password;
    }

    public User() {
        //Empty constructor for Child, Parent, Admin class.
    }
}
