package com.android.mikelpablo.otakucook.Recipes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeActivity;
import com.android.mikelpablo.otakucook.Recipes.adapters.RecipeFinalIngredientsAdapter;
import com.android.mikelpablo.otakucook.Utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class RecipeFinalFragment extends Fragment implements View.OnClickListener{
    @Bind(R.id.recipeFinalListRecyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.btBorrar) Button btBorrar;
    @Bind(R.id.btFinal) Button btFinal;
    @Bind(R.id.tvMensaje) TextView tvMensaje;
    private List<Ingredient> items = new ArrayList<>();;
    private Recipe recipe;

    public RecipeFinalFragment() {
    }

    public static RecipeFinalFragment newInstance(Recipe recipe) {
        RecipeFinalFragment fragment = new RecipeFinalFragment();
        Bundle args = new Bundle();
        args.putParcelable("recipe", recipe);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipefinal, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        recipe = getArguments().getParcelable("recipe");
        btFinal.setOnClickListener(this);
        tvMensaje.setText("Selecciona los ingredientes que se te han acabado durante la preparacion de la receta");
        items = recipe.ingredients;
        //falta la parte de join entre items y los ingredientes del almacen
        final RecipeFinalIngredientsAdapter adapter = new RecipeFinalIngredientsAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {


        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btFinal:{
                Intent intent = new Intent(getActivity(), RecipeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("recipe",recipe);
                getActivity().finish();
                getActivity().startActivity(intent);
            }
        }
    }
}
