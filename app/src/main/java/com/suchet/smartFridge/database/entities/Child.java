package com.suchet.smartFridge.database.entities;

public class Child  extends User {
    public Child(String username, String password) {
        super(username, password);
        this.setAdmin(false);
    }

    @Override
    public String toString() {
        return this.getUsername();
    }
}
