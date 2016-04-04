package com.android.mikelpablo.otakucook.Recipes;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipesActivityFragment extends Fragment {

    public RecipesActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipes, container, false);
    }
}
