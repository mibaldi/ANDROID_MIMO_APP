package com.android.mikelpablo.otakucook.Ingredients.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Ingredients.holders.IngredientListFBHolder;
import com.android.mikelpablo.otakucook.Ingredients.holders.IngredientListServerHolder;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.R;

import java.util.List;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListServerHolder>{

    private static final String TAG = IngredientListAdapter.class.getName();

    private List<Ingredient> listItem;
    public IngredientListAdapter(List<Ingredient> objects) {
        listItem = objects;
    }

    @Override
    public IngredientListServerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredientlist_item,parent,false);
        return new IngredientListServerHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientListServerHolder holder, int position) {
        holder.bindItem(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
