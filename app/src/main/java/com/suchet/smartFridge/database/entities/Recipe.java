package com.suchet.smartFridge.database.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Objects;

@Entity(tableName = "recipe_table")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public long recipeId;


    public String name;
    public String description;
    public String instruction;




    // Room stockera cette map en JSON grâce au converter ci-dessous
    public HashMap<String, Double> ingredientList;

    public Recipe(){
        name = null;
        ingredientList= new HashMap<>();
        description=null;
        instruction=null;
    }
    public Recipe(String _name,String _description, String _instruction){
        name = _name;
        ingredientList= new HashMap<>();
        description=_description;
        instruction=_instruction;
    }
    public Recipe(String _name, HashMap<String, Double> _ingredients,String _description, String _instruction){
        name = _name;
        ingredientList=_ingredients;
        description=_description;
        instruction=_instruction;

    }

    public void addIngredient(String food,Double quantity){
        ingredientList.put(food,quantity);
    }
    public void removeIngredient(String food){
        ingredientList.remove(food);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Double> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(HashMap<String, Double> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String _description) {
        description = _description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(name, recipe.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ingredientList, description);
    }

    public String ToString(){
        return description;
    }
}
