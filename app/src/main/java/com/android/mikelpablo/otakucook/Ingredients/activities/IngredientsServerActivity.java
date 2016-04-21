package com.android.mikelpablo.otakucook.Ingredients.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.mikelpablo.otakucook.Ingredients.fragments.IngredientsExpandableFragment;
import com.android.mikelpablo.otakucook.R;

public class IngredientsServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        this.setTitle(category);
        IngredientsExpandableFragment ingredientsServerFragment = IngredientsExpandableFragment.newInstance(category);
        getSupportFragmentManager().beginTransaction().replace(R.id.ingredientsServer_content,ingredientsServerFragment).commit();
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
