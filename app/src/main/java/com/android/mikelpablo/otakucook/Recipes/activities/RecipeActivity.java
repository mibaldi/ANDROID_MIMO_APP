package com.android.mikelpablo.otakucook.Recipes.activities;



import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeFragment;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeListFragment;

public class RecipeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Intent intent = getIntent();
        Recipe recipe= intent.getParcelableExtra("recipe");
        Log.d("RecipeActivity",recipe.author);
        RecipeFragment recipesFragment = RecipeFragment.newInstance(recipe);
        getSupportFragmentManager().beginTransaction().replace(R.id.flRecipe,recipesFragment).commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
