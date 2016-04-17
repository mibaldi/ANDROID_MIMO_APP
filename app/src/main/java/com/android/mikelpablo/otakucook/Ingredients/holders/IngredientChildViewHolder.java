package com.android.mikelpablo.otakucook.Ingredients.holders;

import android.view.View;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.R;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

public class IngredientChildViewHolder extends ChildViewHolder {

    public TextView ingredientName;

    public IngredientChildViewHolder(View itemView) {
        super(itemView);
        ingredientName = (TextView) itemView.findViewById(R.id.child_list_item);
    }
}
