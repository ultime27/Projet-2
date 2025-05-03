package com.suchet.smartFridge.Recipie.APISearch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeFromApi {
    @SerializedName("label")
    public String label;
    public String source;
    public String url;
    public List<String> ingredientLines;
    public List<Ingredient> ingredients;
}
