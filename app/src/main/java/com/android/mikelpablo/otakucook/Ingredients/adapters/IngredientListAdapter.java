package com.android.mikelpablo.otakucook.Ingredients.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Ingredients.holders.IngredientListHolder;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.R;

import java.util.List;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListHolder>{

    private List<Ingredient> listItem;
    private Context context;
    public IngredientListAdapter(Context context,List<Ingredient> objects) {
        listItem = objects;
        this.context = context;
    }

    @Override
    public IngredientListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredientlist_item,parent,false);
        return new IngredientListHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientListHolder holder, int position) {
        holder.bindItem(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
