package com.android.mikelpablo.otakucook.Utils;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.mikelpablo.otakucook.Preferences.PreferencesManager;
import com.android.mikelpablo.otakucook.R;



public class BaseActivity extends AppCompatActivity {
    protected Toolbar mToolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        applySelectedTheme();
    }


    private void applySelectedTheme() {
        ThemeType theme = PreferencesManager.getInstance().getSelectedTheme();
        ThemeUtils.applyThemeIntoStatusBar(this, theme);
        ThemeUtils.applyThemeIntoToolbar(this, theme, mToolbar);
    }
}
