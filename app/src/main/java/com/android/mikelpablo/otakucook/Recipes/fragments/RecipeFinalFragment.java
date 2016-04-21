package com.android.mikelpablo.otakucook.Recipes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeActivity;
import com.android.mikelpablo.otakucook.Recipes.adapters.RecipeFinalIngredientsAdapter;
import com.android.mikelpablo.otakucook.Utils.DividerItemDecoration;
import com.android.mikelpablo.otakucook.Utils.FirebaseUtils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Firebase refRoot;
    private Firebase mRefStorage;
    private List<String> ingredientsId = new ArrayList<>();
    private RecipeFinalIngredientsAdapter adapter;
    private List<Ingredient> recipeIngredientStorage = new ArrayList<>();

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
        refRoot = new Firebase(getResources().getString(R.string.users));
        if (MainActivity.mAuthData != null) {
            mRefStorage = refRoot.child(MainActivity.mAuthData.getUid()).child("storage");
            getIngredientsIdStorage();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {


        }

    }
    private void getIngredientsIdStorage() {

        ingredientsId.clear();

        items = recipe.ingredients;
        mRefStorage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ingredientsId.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String id = postSnapshot.getValue(String.class);
                    ingredientsId.add(id);
                }
                recipeIngredientStorage= FirebaseUtils.getIngredientsAvailablesRecipe(ingredientsId,items);
                adapter = new RecipeFinalIngredientsAdapter(getContext(), recipeIngredientStorage);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
    /*public List<Ingredient> ingredientsDelete(List<String> ids, List<Ingredient>ingredientList){
        List<Ingredient> recipeIngredientStorage = new ArrayList<>();
        for ( Ingredient ingredient: ingredientList){
            if (ids.contains(String.valueOf(ingredient.id))){
                recipeIngredientStorage.add(ingredient);
            }
        }
        return recipeIngredientStorage;
    }*/
}
