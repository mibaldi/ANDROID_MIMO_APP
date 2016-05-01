package com.android.mikelpablo.otakucook.Ingredients.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.mikelpablo.otakucook.Ingredients.fragments.CategoriesCollectionFragment;
import com.android.mikelpablo.otakucook.Ingredients.fragments.HistoricalIngredientsFragment;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Utils.BaseActivity;

/**
 * Created by pabji on 30/04/2016.
 */
public class HistoricalIngredientsActivity extends BaseActivity {
    private int typeStatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical);
        Intent intent = getIntent();
        typeStatic = intent.getIntExtra("type",0);
        this.setTitle(R.string.historical);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        HistoricalIngredientsFragment historicalIngredientsFragment = HistoricalIngredientsFragment.newInstance(typeStatic);
        getSupportFragmentManager().beginTransaction().replace(R.id.historical_content,historicalIngredientsFragment).commit();
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
