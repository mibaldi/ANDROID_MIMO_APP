package com.android.mikelpablo.otakucook.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.android.mikelpablo.otakucook.R;


public class ThemeUtils {

    public static void applyThemeIntoToolbar(Context context, ThemeType theme, Toolbar toolbar) {
        int color = ColorUtils.ColorFromTheme(context, theme);
        toolbar.setBackgroundColor(color);
    }

    public static void applyThemeIntoStatusBar(Activity activity, ThemeType theme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ColorUtils.makeColorDarker(ColorUtils.ColorFromTheme(activity, theme));
            activity.getWindow().setStatusBarColor(color);
        }
    }

    public static void applyThemeIntoRefreshLayout(Context context, ThemeType theme, SwipeRefreshLayout swipeRefreshLayout) {
        int color = ColorUtils.ColorFromTheme(context, theme);
        swipeRefreshLayout.setColorSchemeColors(color);
    }

    public static void applyThemeIntoProgressBar(Context context, ThemeType theme, ProgressBar progressBar) {
        if(theme == ThemeType.GREEN) {
            progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(context, R.color.accent));
        } else if(theme == ThemeType.BLACK) {
            progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(context, R.color.primary));
        }
    }

    public static void applyThemeIntoFloatingActionButton(Context context, ThemeType theme, FloatingActionButton floatingActionButton){
        int color = ColorUtils.ColorFromTheme(context, theme);
        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public static void applyThemeIntoPagerTabStrip(Context context, ThemeType theme, PagerTabStrip pagerTabStrip) {
        int color = ColorUtils.ColorFromTheme(context, theme);
        pagerTabStrip.setTabIndicatorColor(color);
    }

}
