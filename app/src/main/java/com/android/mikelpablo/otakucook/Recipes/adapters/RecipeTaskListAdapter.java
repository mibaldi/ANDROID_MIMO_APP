package com.android.mikelpablo.otakucook.Recipes.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentPagerAdapter;

import com.android.mikelpablo.otakucook.Models.Task;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeTaskFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mikelbalducieldiaz on 12/4/16.
 */
public class RecipeTaskListAdapter extends FragmentPagerAdapter {
    private static  List<String> TITLES = new ArrayList<>();

    private static  List<Task> TASKS =new ArrayList<>();

    public RecipeTaskListAdapter(FragmentActivity activity,List<Task> taskList) {
        super(activity.getSupportFragmentManager());
        TASKS = taskList;
        Collections.sort(TASKS);
        TITLES.clear();
        for (Task t: TASKS){
            TITLES.add("tarea "+t.name);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return RecipeTaskFragment.newInstance(TASKS.get(position));
    }

    @Override
    public int getCount() {
        return TITLES.size();
    }
}
