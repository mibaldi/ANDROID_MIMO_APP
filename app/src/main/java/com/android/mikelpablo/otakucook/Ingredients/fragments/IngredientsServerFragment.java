package com.android.mikelpablo.otakucook.Ingredients.fragments;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class IngredientsServerFragment extends Fragment {

    private static final String TAG = IngredientsServerFragment.class.getName();

    public List<Ingredient> items = new ArrayList<>();
    private ProgressDialog mProgressDialog;

    @Bind(R.id.ingredientsServer)
    RecyclerView recyclerView;

    public IngredientsServerFragment(){

    }

    public static IngredientsServerFragment newInstance(String category) {
        IngredientsServerFragment fragment = new IngredientsServerFragment();
        Bundle args = new Bundle();
        args.putString("category",category);
        fragment.setArguments(args);
        return fragment;
    }
}
