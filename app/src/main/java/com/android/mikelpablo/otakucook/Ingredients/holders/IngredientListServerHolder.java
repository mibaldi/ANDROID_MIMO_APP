package com.android.mikelpablo.otakucook.Ingredients.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.R;

/**
 * Created by pabji on 15/04/2016.
 */
public class IngredientListServerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name;

    public long id;
    public Context context;

    public IngredientListServerHolder(View itemView) {
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

    }

}
