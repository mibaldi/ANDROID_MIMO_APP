package com.android.mikelpablo.otakucook.Recipes.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentPagerAdapter;

import com.android.mikelpablo.otakucook.Models.Task;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeTaskViewPageFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mikelbalducieldiaz on 12/4/16.
 */
public class RecipeTaskViewPageAdapter extends FragmentPagerAdapter {
    private static  List<String> TITLES = new ArrayList<>();

    private static  List<Task> TASKS =new ArrayList<>();

    public RecipeTaskViewPageAdapter(FragmentActivity activity, List<Task> taskList) {
        super(activity.getSupportFragmentManager());

        TASKS = taskList;
        Collections.sort(TASKS);
        TITLES.clear();
        for (Task t: TASKS){
            TITLES.add(String.format(activity.getString(R.string.tarea), t.name));
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return RecipeTaskViewPageFragment.newInstance(TASKS.get(position),position);
    }

    @Override
    public int getCount() {
        return TITLES.size();
    }
}
