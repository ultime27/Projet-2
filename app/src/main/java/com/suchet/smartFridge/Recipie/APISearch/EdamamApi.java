package com.suchet.smartFridge.Recipie.APISearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EdamamApi {

    @GET("/api/recipes/v2")
    Call<RecipeResponse> searchRecipes(
            @Query("type") String type,
            @Query("q") String query,
            @Query("app_id") String appId,
            @Query("app_key") String appKey
    );
}
