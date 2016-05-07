package com.android.mikelpablo.otakucook.Main.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Main.holders.RecipeListHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pabji on 04/04/2016.
 */
public class RecipesListAdapter extends RecyclerView.Adapter<RecipeListHolder>{

    private List<Recipe> listItem;
    public RecipesListAdapter(List<Recipe> objects) {
        listItem = objects;
    }

    @Override
    public RecipeListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipelist,parent,false);

        return new RecipeListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeListHolder holder, int position) {
        holder.bindItem(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
    public void setFilter(List<Recipe> objects){
        listItem = new ArrayList<>();
        listItem.addAll(objects);
        notifyDataSetChanged();
    }
}
