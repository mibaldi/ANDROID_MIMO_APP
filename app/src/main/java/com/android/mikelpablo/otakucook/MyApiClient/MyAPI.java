package com.android.mikelpablo.otakucook.MyApiClient;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mikelbalducieldiaz on 4/4/16.
 */
public interface MyAPI {
        @GET("/recipes")
        Call<List<Recipe>> recipes();
        @GET("recipes/{id}")
        Call<Recipe> getRecipe(@Path("id") long id);
    @POST("/ingredients")
        Call<Ingredient> createIngredient(@Body Ingredient ingredient);
}
