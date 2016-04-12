package com.android.mikelpablo.otakucook.Ingredients.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Ingredients.adapters.IngredientListAdapter;
import com.android.mikelpablo.otakucook.Ingredients.holders.IngredientListHolder;
import com.android.mikelpablo.otakucook.Main.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.holders.RecipeListHolder;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IngredientListFragment  extends Fragment{

    private static final String TAG = IngredientListFragment.class.getName();
    public List<Ingredient> items = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private int ingredientType;
    private Firebase ref;

    @Bind(R.id.ingredientListRecyclerView)
    RecyclerView recyclerView;

    public IngredientListFragment() {
    }

    public static IngredientListFragment newInstance(int type) {
        IngredientListFragment fragment = new IngredientListFragment();
        Bundle args = new Bundle();
        args.putInt("ingredientType",type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingredientlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mProgressDialog = new ProgressDialog(getContext());
        ingredientType = getArguments().getInt("ingredientType");

        Firebase refRoot = new Firebase(getResources().getString(R.string.users));

        if (ingredientType == R.string.shoping_cart_drawer){
            ref = refRoot.child(MainActivity.mAuthData.getUid()).child("shoppingcart");
        }else if (ingredientType == R.string.ingredients_drawer){
            ref = refRoot.child(MainActivity.mAuthData.getUid()).child("storage");
        }

        FirebaseRecyclerAdapter<String, IngredientListHolder> fbadapter = new FirebaseRecyclerAdapter<String, IngredientListHolder>(String.class, R.layout.ingredientlist_item,
                IngredientListHolder.class, ref) {

            @Override
            protected void populateViewHolder(IngredientListHolder ingredientListHolder, String s, int i) {
                recoveryIngredientsNames(ingredientListHolder, s);
            }
        };

        recyclerView.setAdapter(fbadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void recoveryIngredientsNames(final IngredientListHolder ingredientListHolder, String s) {
        Firebase refRoot = new Firebase(getResources().getString(R.string.ingredients));
        Firebase refIngredient = refRoot.child(s);

        refIngredient.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String title = (String) dataSnapshot.child("name").getValue();
                    long id = (long) dataSnapshot.child("idServer").getValue();
                    Log.d(TAG,title);
                    Log.d(TAG,"ID: "+id);
                    ingredientListHolder.name.setText(title);
                    ingredientListHolder.id = id;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
