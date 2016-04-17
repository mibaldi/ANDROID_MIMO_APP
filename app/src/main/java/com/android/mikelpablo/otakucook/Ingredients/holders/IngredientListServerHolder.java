package com.android.mikelpablo.otakucook.Ingredients.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Ingredients.adapters.IngredientListAdapter;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.R;

/**
 * Created by pabji on 15/04/2016.
 */
public class IngredientListServerHolder extends RecyclerView.ViewHolder{

    public TextView name;

    public long id;
    public Context context;
    public IngredientListAdapter.OnItemClickListener listener;

    public IngredientListServerHolder(View itemView, IngredientListAdapter.OnItemClickListener listener) {
        super(itemView);
        context = itemView.getContext();
        name = (TextView) itemView.findViewById(R.id.ingredientName);
        this.listener = listener;
    }

    public void bindItem(final Ingredient ingredient) {
        name.setText(ingredient.name);
        id = ingredient.id;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(itemView, ingredient);
            }
        });
    }
}
