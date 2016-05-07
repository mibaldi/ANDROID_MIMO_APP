package com.android.mikelpablo.otakucook.Recipes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeFinalFragment;
import com.android.mikelpablo.otakucook.Recipes.holders.RecipeFinalIngredientsHolder;

import java.util.List;

/**
 * Created by pabji on 04/04/2016.
 */
public class RecipeFinalIngredientsAdapter extends RecyclerView.Adapter<RecipeFinalIngredientsHolder>{

    private List<RecipeFinalFragment.IngredientSelectable> listItem;
    public RecipeFinalIngredientsAdapter(List<RecipeFinalFragment.IngredientSelectable> objects) {
        listItem = objects;
    }

    @Override
    public RecipeFinalIngredientsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipelist_final,parent,false);

        return new RecipeFinalIngredientsHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeFinalIngredientsHolder holder, final int position) {
        holder.bindItem(listItem.get(position));
        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                listItem.get(position).isSelected=isChecked;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
