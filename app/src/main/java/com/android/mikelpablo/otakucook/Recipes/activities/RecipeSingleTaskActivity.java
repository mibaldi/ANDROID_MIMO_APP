package com.android.mikelpablo.otakucook.Recipes.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.Models.Task;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeFinalFragment;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeSingleTaskFragment;
import com.android.mikelpablo.otakucook.Utils.BaseActivity;

public class RecipeSingleTaskActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_single_task);
        Intent intent = getIntent();
        Task task= intent.getParcelableExtra("task");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.setTitle(String.format(getString(R.string.Paso), task.name));
        RecipeSingleTaskFragment recipeSingleTaskFragment = RecipeSingleTaskFragment.newInstance(task);
        getSupportFragmentManager().beginTransaction().replace(R.id.flRecipeSingleTask,recipeSingleTaskFragment).commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
