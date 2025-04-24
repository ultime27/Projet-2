package com.suchet.smartFridge.database.entities;

public class Parent extends User {

    public Parent(String username, String password) {
        super(username, password);
    }

    @Override
    public String toString() {
        return this.getUsername();
    }

}
