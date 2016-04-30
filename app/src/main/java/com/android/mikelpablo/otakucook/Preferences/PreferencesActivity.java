package com.android.mikelpablo.otakucook.Preferences;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Utils.BaseActivity;


public class PreferencesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        PreferencesFragment preferencesFragment = new PreferencesFragment();
        preferencesFragment.setToolbar(mToolbar);
        getFragmentManager().beginTransaction().
                replace(R.id.contentPreferences, preferencesFragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }
}
