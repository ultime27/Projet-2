package com.suchet.smartFridge.database.entities;

public class Admin extends User{

    public Admin(String username, String password) {
        super(username, password);
    }

    @Override
    public String toString() {
        return this.getUsername();
    }

}
