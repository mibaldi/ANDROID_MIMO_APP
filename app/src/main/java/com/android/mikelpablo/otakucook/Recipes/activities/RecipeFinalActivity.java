package com.android.mikelpablo.otakucook.Recipes.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeFinalFragment;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeFragment;
import com.android.mikelpablo.otakucook.Utils.BaseActivity;

public class RecipeFinalActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_final);
        Intent intent = getIntent();
        Recipe recipe= intent.getParcelableExtra("recipe");
        Log.d("RecipeActivity",recipe.author);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.setTitle(getString(R.string.title_RecipeFinal));
        RecipeFinalFragment recipeFinalFragment = RecipeFinalFragment.newInstance(recipe);
        getSupportFragmentManager().beginTransaction().replace(R.id.flRecipeFinal,recipeFinalFragment).commit();
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
