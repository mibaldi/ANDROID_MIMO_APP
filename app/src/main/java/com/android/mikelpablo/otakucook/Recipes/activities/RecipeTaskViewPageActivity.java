package com.android.mikelpablo.otakucook.Recipes.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Main.MainActivity;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.adapters.RecipeTaskListAdapter;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeTaskViewPageActivity extends AppCompatActivity {
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.pager_tab_strip)
    PagerTabStrip pagerTabStrip;
    private RecipeTaskListAdapter adapter;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_task_view_page);
        Intent intent = getIntent();
        recipe= intent.getParcelableExtra("recipe");
        ButterKnife.bind(this);
        adapter = new RecipeTaskListAdapter(this,recipe.tasks);
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTabIndicatorColorResource(R.color.colorPrimary);
        viewPager.addOnPageChangeListener(mListener);
        viewPager.setAdapter(adapter);

    }
    private ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {

        public boolean mPageEnd;
        public int selectedIndex;

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            selectedIndex = arg0;

        }
        boolean callHappened;
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
            if( mPageEnd && arg0 == selectedIndex && !callHappened)
            {
                Intent intent = new Intent(RecipeTaskViewPageActivity.this,RecipeFinalActivity.class);
                intent.putExtra("recipe",recipe);
                RecipeTaskViewPageActivity.this.startActivity(intent);
                Toast.makeText(getApplicationContext(),"Final",Toast.LENGTH_SHORT).show();
                mPageEnd = false;//To avoid multiple calls.
                callHappened = true;
            }else
            {
                mPageEnd = false;
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            if(selectedIndex == adapter.getCount() - 1)
            {
                mPageEnd = true;
            }
        }
    };
}
