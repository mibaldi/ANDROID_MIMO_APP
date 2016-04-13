package com.android.mikelpablo.otakucook.Recipes.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.R;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class RecipeFinalIngredientsHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public Context context;

    public RecipeFinalIngredientsHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        name = (TextView) itemView.findViewById(R.id.recipeName);
    }

    public void bindItem(Ingredient ingredient) {
        name.setText(ingredient.name);
    }

}
