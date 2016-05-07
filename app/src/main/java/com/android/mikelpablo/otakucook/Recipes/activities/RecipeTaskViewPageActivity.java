package com.android.mikelpablo.otakucook.Recipes.activities;

import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Main.fragments.DialogExitApp;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.adapters.RecipeTaskViewPageAdapter;
import com.android.mikelpablo.otakucook.Recipes.fragments.DialogFinishRecipeApp;
import com.android.mikelpablo.otakucook.Utils.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeTaskViewPageActivity extends BaseActivity {
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.pager_tab_strip)
    PagerTabStrip pagerTabStrip;
    public static RecipeTaskViewPageAdapter adapter;
    public static Recipe recipe;
    public static boolean clicked = false;
    public static long task = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_task_view_page);
        Intent intent = getIntent();
        recipe= intent.getParcelableExtra("recipe");
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.setTitle(getString(R.string.title_RecipeTaskViewPage));
        adapter = new RecipeTaskViewPageAdapter(this,recipe.tasks);
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTabIndicatorColorResource(R.color.primary);
        viewPager.addOnPageChangeListener(mListener);
        viewPager.setAdapter(adapter);

    }
    private ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {

        public boolean mPageEnd;
        public int selectedIndex = 0;
        public boolean izquierda = false;

        @Override
        public void onPageSelected(int arg0) {
            if (selectedIndex > arg0){
                izquierda = true;
            }else {
                izquierda=false;
            }
          selectedIndex = arg0;


        }
        boolean callHappened;
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

            if( mPageEnd && arg0 == selectedIndex && !callHappened && !izquierda && !clicked)
            {
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                DialogFinishRecipeApp.newInstance(1,recipe).show(fm, "dialog");
                mPageEnd = false;
                callHappened = true;

            }else
            {
                mPageEnd = false;
            }
        }


        @Override
        public void onPageScrollStateChanged(int arg0) {
            if(selectedIndex == adapter.getCount() - 1)
            {
                mPageEnd = true;
                callHappened = false;
            }
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                clicked=false;
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clicked = false;
        finish();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
