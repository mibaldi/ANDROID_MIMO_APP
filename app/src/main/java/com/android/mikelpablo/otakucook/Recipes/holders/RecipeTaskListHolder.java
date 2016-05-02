package com.android.mikelpablo.otakucook.Recipes.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Models.Task;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.adapters.RecipeTaskListAdapter;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class RecipeTaskListHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public Context context;
    public RecipeTaskListAdapter.OnItemClickListener listener;
    public RecipeTaskListHolder(View itemView, RecipeTaskListAdapter.OnItemClickListener listener) {
        super(itemView);
        context = itemView.getContext();
        name = (TextView) itemView.findViewById(R.id.recipeName);
        this.listener = listener;
    }

    public void bindItem(final Task task) {
        name.setText(String.format(context.getString(R.string.Paso), task.name));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(itemView, task);
            }
        });
    }

}
