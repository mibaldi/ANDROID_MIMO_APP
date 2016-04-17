package com.android.mikelpablo.otakucook.Recipes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Models.Task;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.holders.RecipeTaskListHolder;

import java.util.Collections;
import java.util.List;

public class RecipeTaskListAdapter extends RecyclerView.Adapter<RecipeTaskListHolder>{

    public interface OnItemClickListener {
        void onItemClick(View view, Task item);
    }
    private List<Task> listItem;
    private final OnItemClickListener listener;

    public RecipeTaskListAdapter(List<Task> objects,OnItemClickListener listener) {
        listItem = objects;
        Collections.sort(listItem);
        this.listener = listener;
    }

    @Override
    public RecipeTaskListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipelist,parent,false);
        return new RecipeTaskListHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(RecipeTaskListHolder holder, int position) {
        holder.bindItem(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
