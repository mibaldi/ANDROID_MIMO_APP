package com.android.mikelpablo.otakucook.Utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.android.mikelpablo.otakucook.R;


/**
 * Created by Victor on 12/4/16.
 */
public class ColorUtils {

    public static int ColorFromTheme(Context context, ThemeType theme) {
        int color = 0;
        if(theme == ThemeType.BLUE) {
            color = ContextCompat.getColor(context, R.color.accent);
        } else if(theme == ThemeType.ORANGE) {
            color = ContextCompat.getColor(context, R.color.primary);
        }
        return color;
    }
    public static int ColorFromThemeReset(Context context, ThemeType theme) {
        int color = 0;
        if(theme == ThemeType.BLUE) {
            color = ContextCompat.getColor(context, R.color.accent2);
        } else if(theme == ThemeType.ORANGE) {
            color = ContextCompat.getColor(context, R.color.primary2);
        }
        return color;
    }

    public static int makeColorDarker(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }
}
