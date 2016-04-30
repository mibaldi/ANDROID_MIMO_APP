package com.android.mikelpablo.otakucook.Ingredients.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.mikelpablo.otakucook.Ingredients.fragments.CategoriesCollectionFragment;
import com.android.mikelpablo.otakucook.Ingredients.fragments.HistoricalIngredientsFragment;
import com.android.mikelpablo.otakucook.R;

/**
 * Created by pabji on 30/04/2016.
 */
public class HistoricalIngredientsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        this.setTitle(R.string.historical);
        HistoricalIngredientsFragment historicalIngredientsFragment = HistoricalIngredientsFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.ingredientsServer_content,historicalIngredientsFragment).commit();

    }
}
