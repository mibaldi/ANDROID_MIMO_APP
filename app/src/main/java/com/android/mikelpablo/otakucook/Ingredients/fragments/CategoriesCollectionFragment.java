package com.android.mikelpablo.otakucook.Ingredients.fragments;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Ingredients.Models.Category;
import com.android.mikelpablo.otakucook.Ingredients.adapters.CategoriesCollectionAdapter;
import com.android.mikelpablo.otakucook.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CategoriesCollectionFragment extends Fragment{

    @Bind(R.id.categories_collection)
    RecyclerView categoriesCollection;

    public List<Category> categories = new ArrayList<>();


    public CategoriesCollectionFragment (){

    }

    public static CategoriesCollectionFragment newInstance() {
        CategoriesCollectionFragment fragment = new CategoriesCollectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] categoriesNames = getResources().getStringArray(R.array.categories);
        TypedArray icons = getResources().obtainTypedArray(R.array.iconos);
        for(int i= 0; i<categoriesNames.length; i++){
            categories.add(new Category(categoriesNames[i],icons.getDrawable(i)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        categoriesCollection.setAdapter(new CategoriesCollectionAdapter(categories));
        categoriesCollection.setLayoutManager(new GridLayoutManager(getActivity(),3));
    }

}
