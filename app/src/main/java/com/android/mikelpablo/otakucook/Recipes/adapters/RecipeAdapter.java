package com.android.mikelpablo.otakucook.Recipes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.holders.RecipeHolder;

import java.util.List;

/**
 * Created by pabji on 04/04/2016.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder>{

    private List<Ingredient> listItem;
    private Context context;
    public RecipeAdapter(Context context, List<Ingredient> objects) {
        listItem = objects;
        this.context = context;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_ingredientlist,parent,false);

        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        holder.bindItem(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
