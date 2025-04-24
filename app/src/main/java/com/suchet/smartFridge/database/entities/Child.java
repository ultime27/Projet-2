package com.suchet.smartFridge.database.entities;

public class Child  extends User {
    public Child(String username, String password) {
        super(username, password);
    }

    @Override
    public String toString() {
        return this.getUsername();
    }
}
