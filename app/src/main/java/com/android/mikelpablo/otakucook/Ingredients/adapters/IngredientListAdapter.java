package com.android.mikelpablo.otakucook.Ingredients.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Ingredients.holders.IngredientHolder;
import com.android.mikelpablo.otakucook.Models.Ingredient;

import java.util.List;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientHolder>{

    private List<Ingredient> listItem;
    private Context context;
    public IngredientListAdapter(Context context,List<Ingredient> objects) {
        listItem = objects;
        this.context = context;
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
