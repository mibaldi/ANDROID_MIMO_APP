package com.android.mikelpablo.otakucook.Recipes.holders;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class RecipeHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public Context context;

    public RecipeHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        name = (TextView) itemView.findViewById(R.id.recipeName);
    }

    public void bindItem(Ingredient ingredient) {
        name.setText(ingredient.name);
    }

}
