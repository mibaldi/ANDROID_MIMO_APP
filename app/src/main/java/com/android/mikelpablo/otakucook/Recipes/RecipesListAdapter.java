package com.android.mikelpablo.otakucook.Recipes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pabji on 04/04/2016.
 */
public class RecipesListAdapter extends RecyclerView.Adapter<RecipeHolder>{

    private List<Recipe> listItem;
    private Context context;
    public RecipesListAdapter(Context context,List<Recipe> objects) {
        listItem = objects;
        this.context = context;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipelist_item,parent,false);
        //return new RecipeHolder(view);

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
