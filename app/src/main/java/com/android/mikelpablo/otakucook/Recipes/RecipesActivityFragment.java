package com.android.mikelpablo.otakucook.Recipes;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipesActivityFragment extends Fragment {

    public RecipesActivityFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyAPI service = MyApiClient.createService(MyAPI.class);
        Call<List<Recipe>> recipes = service.recipes();
        recipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipesServer = response.body();
                for (Recipe r : recipesServer){
                    System.out.println("id: "+r.id);
                    //System.out.println(r.id);
                }
                response.body();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }

        });
        return inflater.inflate(R.layout.fragment_recipes, container, false);

    }
}
