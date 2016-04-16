package com.android.mikelpablo.otakucook.MyApiClient;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
        @GET("/ingredients")
        Call<List<Ingredient>> ingredients();
        @GET("/ingredients/{id}")
        Call<Ingredient> getIngredient(@Path("id") long id);
        @GET("/recipesIngredients")
        Call<List<Recipe>> getPossiblesRecipes(@Query("ingredientes") String ingredientes);
        @GET("/ingredients/category/{category}")
        Call<List<Ingredient>> getCategoryIngredients(@Path("category") String category,@Query("ids") String ids);

}
