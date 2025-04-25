package com.suchet.smartFridge.database.entities;

import java.time.LocalDate;
import java.util.ArrayList;

public class Food {
    private String name;
    private double quantity;
    private ArrayList<FoodBox> boxes;

    public Food(String name) {
        this.name = name;
        boxes = new ArrayList<>();
        quantity=0.0;

    }
    public void add(double _quantity){
        quantity+=_quantity;
        boxes.add(new FoodBox(_quantity));
    }
    public boolean remove(double _quantity){
        if (_quantity<quantity){
            return false;
        }
        double temp =_quantity;
        for (FoodBox b: boxes){
            temp=b.remove(temp);
        }
        return true;
    }

    public LocalDate getMinExpirationDate(){
        if (boxes.isEmpty()){
            return null;
        }

        FoodBox box=boxes.get(0);
        for (FoodBox b: boxes){
            if (box.getDatePeremption().isAfter(b.getDatePeremption())) {
                box = b;
            }
        }
        return box.getDatePeremption();
    }
    public FoodBox getMinExpirationDateBox(){
        if (boxes.isEmpty()){
            return null;
        }

        FoodBox box=boxes.get(0);
        for (FoodBox b: boxes){
            if (box.getDatePeremption().isAfter(b.getDatePeremption())) {
                box = b;
            }
        }
        return box;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public ArrayList<FoodBox> getBoxes() {
        return boxes;
    }

    public void setBoxes(ArrayList<FoodBox> boxes) {
        this.boxes = boxes;
    }
}
