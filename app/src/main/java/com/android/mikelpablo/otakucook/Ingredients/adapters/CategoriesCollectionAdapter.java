package com.android.mikelpablo.otakucook.Ingredients.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Ingredients.Models.Category;
import com.android.mikelpablo.otakucook.Ingredients.holders.CategoriesCollectionHolder;
import com.android.mikelpablo.otakucook.Ingredients.holders.IngredientListHolder;
import com.android.mikelpablo.otakucook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pabji on 13/04/2016.
 */
public class CategoriesCollectionAdapter extends RecyclerView.Adapter<CategoriesCollectionHolder> {

    public List<Category> categories = new ArrayList<>();

    public CategoriesCollectionAdapter(List<Category> categories){
        this.categories = categories;
    }

    @Override
    public CategoriesCollectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new CategoriesCollectionHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoriesCollectionHolder holder, int position) {
        holder.bindCategory(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
