package com.android.mikelpablo.otakucook.Ingredients.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;

import butterknife.Bind;
import retrofit2.Call;

public class IngredientListFBHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /*@Bind(R.id.ingredientName)
    public TextView name;*/

    public TextView name;

    public long id;
    public Context context;

    public IngredientListFBHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        name = (TextView) itemView.findViewById(R.id.ingredientName);
        itemView.setOnClickListener(this);
    }

    public void bindItem(Ingredient ingredient) {
        name.setText(ingredient.name);
        id = ingredient.id;
    }

    @Override
    public void onClick(View v) {
        /*MyAPI service = MyApiClient.createService(MyAPI.class);
        Call<Ingredient> ingredient = service.getIngredient(id);
        ingredient.enqueue(getCallback());*/
    }
}
