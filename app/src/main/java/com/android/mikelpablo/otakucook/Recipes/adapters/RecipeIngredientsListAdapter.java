package com.android.mikelpablo.otakucook.Recipes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.holders.RecipeIngredientsHolder;

import java.util.List;

/**
 * Created by pabji on 04/04/2016.
 */
public class RecipeIngredientsListAdapter extends RecyclerView.Adapter<RecipeIngredientsHolder>{

    private List<Ingredient> listItem;
    private Context context;
    public RecipeIngredientsListAdapter(Context context, List<Ingredient> objects) {
        listItem = objects;
        this.context = context;
    }

    @Override
    public RecipeIngredientsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipelist_item,parent,false);

        return new RecipeIngredientsHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeIngredientsHolder holder, int position) {
        holder.bindItem(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
