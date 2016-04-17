package com.android.mikelpablo.otakucook.Ingredients.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.R;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

public class IngredientParentViewHolder extends ParentViewHolder {

    public TextView baseIngredient;

    public IngredientParentViewHolder(View itemView) {
        super(itemView);
        baseIngredient = (TextView) itemView.findViewById(R.id.parent_list_item);
    }

}
