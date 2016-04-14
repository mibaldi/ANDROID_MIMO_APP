package com.android.mikelpablo.otakucook.Ingredients.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.mikelpablo.otakucook.Ingredients.fragments.CategoriesCollectionFragment;
import com.android.mikelpablo.otakucook.Ingredients.fragments.IngredientsServerFragment;
import com.android.mikelpablo.otakucook.R;

public class IngredientsServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        this.setTitle(category);
        IngredientsServerFragment ingredientsServerFragment = IngredientsServerFragment.newInstance(category);
        getSupportFragmentManager().beginTransaction().replace(R.id.ingredientsServer_content,ingredientsServerFragment).commit();
    }
}
