package com.android.mikelpablo.otakucook.Ingredients.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.mikelpablo.otakucook.Ingredients.fragments.CategoriesCollectionFragment;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeFinalFragment;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CategoriesActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        this.setTitle("Categorías");
        CategoriesCollectionFragment categoriesCollectionFragment = CategoriesCollectionFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.categories_content,categoriesCollectionFragment).commit();

    }
}