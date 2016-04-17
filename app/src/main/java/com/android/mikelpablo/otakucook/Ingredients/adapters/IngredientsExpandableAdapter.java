package com.android.mikelpablo.otakucook.Ingredients.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Ingredients.Models.BaseIngredient;
import com.android.mikelpablo.otakucook.Ingredients.holders.IngredientChildViewHolder;
import com.android.mikelpablo.otakucook.Ingredients.holders.IngredientParentViewHolder;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.R;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

public class IngredientsExpandableAdapter extends ExpandableRecyclerAdapter<IngredientParentViewHolder,IngredientChildViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(View view, Ingredient item);
    }
    private final LayoutInflater mInflater;
    private OnItemClickListener listener;

    public IngredientsExpandableAdapter(List<? extends BaseIngredient> parentItemList, Context context,OnItemClickListener listener) {
        super(parentItemList);
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public IngredientParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = mInflater.inflate(R.layout.item_parent_list, parentViewGroup, false);
        return new IngredientParentViewHolder(view);
    }

    @Override
    public IngredientChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view = mInflater.inflate(R.layout.item_child_list, childViewGroup, false);
        return new IngredientChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(IngredientParentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        BaseIngredient baseIngredient = (BaseIngredient) parentListItem;
        parentViewHolder.baseIngredient.setText(baseIngredient.getBaseName());
    }

    @Override
    public void onBindChildViewHolder(IngredientChildViewHolder childViewHolder, int position, Object childListItem) {
        final Ingredient ingredientChild = (Ingredient) childListItem;
        childViewHolder.ingredientName.setText(ingredientChild.name);
        childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(v, ingredientChild);
            }
        });
    }
}
