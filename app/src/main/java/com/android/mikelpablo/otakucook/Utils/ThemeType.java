package com.android.mikelpablo.otakucook.Utils;

/**
 * Created by Victor on 12/4/16.
 */

public enum ThemeType {
    ORANGE, BLUE;

    public static ThemeType ThemeTypeFromInt(int type){
        if(type == 1) {
            return ThemeType.BLUE;
        } else {
            return ThemeType.ORANGE;
        }
    }
}
