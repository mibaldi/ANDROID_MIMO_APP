package com.android.mikelpablo.otakucook.Preferences;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;

import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Utils.ThemeType;
import com.android.mikelpablo.otakucook.Utils.ThemeUtils;


@SuppressLint("ValidFragment")
public class PreferencesFragment extends PreferenceFragment {

    private Toolbar mToolbar;


    public void setToolbar(Toolbar mToolbar) {
        this.mToolbar = mToolbar;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final ListPreference listPreference = (ListPreference) findPreference("toolbarColor");
        listPreference.setValueIndex(PreferencesManager.getInstance().getSelectedTheme().ordinal());
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                listPreference.setValueIndex(Integer.parseInt(newValue.toString()));
                PreferencesManager.getInstance().saveThemePreference(Integer.valueOf(newValue.toString()));
                applyTheme();
                return false;
            }
        });
    }

    private void applyTheme() {
        ThemeType theme = PreferencesManager.getInstance().getSelectedTheme();
        ThemeUtils.applyThemeIntoStatusBar(getActivity(), theme);
        ThemeUtils.applyThemeIntoToolbar(getActivity(), theme, mToolbar);
    }
}
