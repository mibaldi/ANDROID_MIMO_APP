package com.android.mikelpablo.otakucook.Recipes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.Models.Task;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeSingleTaskActivity;
import com.android.mikelpablo.otakucook.Recipes.adapters.RecipeTaskListAdapter;
import com.android.mikelpablo.otakucook.Utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class RecipeTaskListFragment extends Fragment implements RecipeTaskListAdapter.OnItemClickListener{
    @Bind(R.id.recipeTaskListRecyclerView)
    RecyclerView recyclerView;

    private List<Task> items = new ArrayList<>();;
    private Recipe recipe;

    public RecipeTaskListFragment() {
    }

    public static RecipeTaskListFragment newInstance(Recipe recipe) {
        RecipeTaskListFragment fragment = new RecipeTaskListFragment();
        Bundle args = new Bundle();
        args.putParcelable("recipe", recipe);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_task_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        recipe = getArguments().getParcelable("recipe");
        items = recipe.tasks;
        //falta la parte de join entre items y los ingredientes del almacen
        final RecipeTaskListAdapter adapter = new RecipeTaskListAdapter(items,RecipeTaskListFragment.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
    }

    @Override
    public void onItemClick(View view, Task task) {
        Intent intent = new Intent(getActivity(), RecipeSingleTaskActivity.class);
        intent.putExtra("task",task);
        startActivity(intent);
    }
}
