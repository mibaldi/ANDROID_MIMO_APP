package com.android.mikelpablo.otakucook.MyApiClient;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by mikelbalducieldiaz on 4/4/16.
 */
public interface MyAPI {
        @GET("/recipes")
        Call<List<Recipe>> recipes();
        @POST("/ingredients")
        Call<Ingredient> createIngredient(@Body Ingredient ingredient);
}
