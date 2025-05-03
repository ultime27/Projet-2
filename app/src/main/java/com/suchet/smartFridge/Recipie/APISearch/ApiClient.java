package com.suchet.smartFridge.Recipie.APISearch;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit.Builder builder=
            new Retrofit.Builder()
                    .baseUrl("https://api.edamam.com")
                    .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit = builder.build();

    public static Retrofit getRetrofit() {
        return retrofit;
    }

}
