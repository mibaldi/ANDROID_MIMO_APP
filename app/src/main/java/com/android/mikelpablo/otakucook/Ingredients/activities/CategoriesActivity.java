package com.android.mikelpablo.otakucook.Ingredients.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.android.mikelpablo.otakucook.Ingredients.fragments.CategoriesCollectionFragment;
import com.android.mikelpablo.otakucook.Preferences.PreferencesManager;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeFinalFragment;
import com.android.mikelpablo.otakucook.Utils.BaseActivity;
import com.android.mikelpablo.otakucook.Utils.ThemeType;
import com.android.mikelpablo.otakucook.Utils.ThemeUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CategoriesActivity extends BaseActivity {

    public static int typeStatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        typeStatic = intent.getIntExtra("type",0);
        this.setTitle("Categorías");

        CategoriesCollectionFragment categoriesCollectionFragment = CategoriesCollectionFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.categories_content,categoriesCollectionFragment).commit();

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
