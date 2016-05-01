package com.android.mikelpablo.otakucook.Recipes.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeFinalFragment;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class RecipeFinalIngredientsHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public Context context;
    public CheckBox cbSelect;

    public RecipeFinalIngredientsHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        name = (TextView) itemView.findViewById(R.id.recipeName);
        cbSelect = (CheckBox) itemView.findViewById(R.id.checkBox);
    }

    public void bindItem(final RecipeFinalFragment.IngredientSelectable ingredient) {
        name.setText(ingredient.name);
        cbSelect.setOnCheckedChangeListener(null);
        cbSelect.setChecked(ingredient.isSelected);

    }

}
