package com.android.mikelpablo.otakucook.Utils;

/**
 * Created by Victor on 12/4/16.
 */

public enum ThemeType {
    GREEN, BLACK;

    public static ThemeType ThemeTypeFromInt(int type){
        if(type == 1) {
            return ThemeType.BLACK;
        } else {
            return ThemeType.GREEN;
        }
    }
}
