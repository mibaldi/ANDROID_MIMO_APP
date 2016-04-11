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
public class RecipeIngredientsHolder extends RecyclerView.ViewHolder{
    public TextView name;
    //public long id;
    public Context context;
    //public Context context;
    public RecipeIngredientsHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
       // name = (TextView) itemView.findViewById(R.id.recipeName);

    }
    /*public RecipeHolder(Context context,View itemView) {
        super(itemView);
        this.context=context;
        itemView.setOnClickListener(this);
        name = (TextView) itemView.findViewById(R.id.recipeName);
    }*/


    public void bindItem(Ingredient ingredient) {
        name.setText(ingredient.name);
       // id = in.id;
    }

    /*@Override
    public void onClick(View v) {
        //Toast.makeText(v.getContext(),String.valueOf(id),Toast.LENGTH_SHORT).show();
        MyAPI service = MyApiClient.createService(MyAPI.class);
        Call<Recipe> recipe= service.getRecipe(id);
        recipe.enqueue(getCallback());


    }

    @NonNull
    private Callback<Recipe> getCallback() {
        return new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                    Recipe recipe= response.body();
                Log.d("RecipeHolder",recipe.author);
                Intent intent = new Intent(context,RecipeActivity.class);
                intent.putExtra("recipe",recipe);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {

            }
        };
    }*/

}
