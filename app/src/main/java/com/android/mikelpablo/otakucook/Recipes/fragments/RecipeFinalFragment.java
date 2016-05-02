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

import com.android.mikelpablo.otakucook.Login.activities.LoginActivity;
import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.OwnIngredientFB;
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
    private List<IngredientSelectable> recipeIngredientSelectablesStorage = new ArrayList<>();

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
        btBorrar.setOnClickListener(this);
        tvMensaje.setText(R.string.mensajeFinalRecipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
        refRoot = new Firebase(getResources().getString(R.string.users));
        if (LoginActivity.mAuthData != null) {
            mRefStorage = refRoot.child(LoginActivity.mAuthData.getUid()).child("owningredient");
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
                    OwnIngredientFB ownIngredientFB = postSnapshot.getValue(OwnIngredientFB.class);
                    Log.d("FB",ownIngredientFB.id+"+"+ownIngredientFB.storage);
                    if (ownIngredientFB.storage.equals("1")){
                        ingredientsId.add(ownIngredientFB.id);
                    }

                }
                recipeIngredientStorage= FirebaseUtils.getIngredientsAvailablesRecipe(ingredientsId,items);
                recipeIngredientSelectablesStorage=IngredientSelectable.convertIngredient(recipeIngredientStorage);
                if (recipeIngredientSelectablesStorage.isEmpty()){
                   btBorrar.setVisibility(View.GONE);
                }
                adapter = new RecipeFinalIngredientsAdapter(getContext(), recipeIngredientSelectablesStorage);
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
                Intent intent = new Intent(getActivity(), RecipeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("recipe",recipe);
                getActivity().startActivity(intent);
                break;
            }
            case R.id.btBorrar: {
                deleteIngredients();
                break;
            }

        }
    }

    private void deleteIngredients() {
        for (IngredientSelectable i:recipeIngredientSelectablesStorage){
            if (i.isSelected){
                mRefStorage.child(String.valueOf(i.id)).child("storage").setValue("0");
            }
        }
    }

    public static class IngredientSelectable{
        public long id;
        public String name;
        public Boolean frozen;
        public String category;
        public String baseType;
        public boolean isSelected = false;

        public IngredientSelectable(long id, String name, Boolean frozen, String category, String baseType, boolean isSelected) {
            this.id = id;
            this.name = name;
            this.frozen = frozen;
            this.category = category;
            this.baseType = baseType;
            this.isSelected = isSelected;
        }

        public static IngredientSelectable convert(Ingredient i){
            IngredientSelectable ingredientSelectable = new IngredientSelectable(i.id,i.name,i.frozen,i.category,i.baseType,false);
            return ingredientSelectable;
        }
        public static List<IngredientSelectable> convertIngredient(List<Ingredient> lista){
            List<IngredientSelectable> listaFinal = new ArrayList<>();
            for (Ingredient i:lista){
                listaFinal.add(convert(i));
            }
            return listaFinal;
        }
    }
}
