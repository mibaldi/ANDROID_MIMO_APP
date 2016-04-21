package com.android.mikelpablo.otakucook.Main.adapters;

import android.view.View;

import com.android.mikelpablo.otakucook.Ingredients.holders.IngredientListFBHolder;

import com.android.mikelpablo.otakucook.Models.Ingredient;

import com.android.mikelpablo.otakucook.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class IngredientListFirebaseAdapter extends FirebaseRecyclerAdapter<String,IngredientListFBHolder>{

    public interface OnItemClickListener {
        void onItemClick(View view, Long item);
    }

    private OnItemClickListener listener;
    private int ingredientType;

    public IngredientListFirebaseAdapter(Class<String> modelClass, int modelLayout, Class<IngredientListFBHolder> viewHolderClass, Query ref, int ingredientType,OnItemClickListener listener) {
        super(modelClass, modelLayout, viewHolderClass,ref);
        this.listener = listener;
        this.ingredientType = ingredientType;
    }

    @Override
    protected void populateViewHolder(IngredientListFBHolder ingredientListFBHolder, String s, int i) {
        recoveryIngredientsNames(ingredientListFBHolder,s);
    }

    private void recoveryIngredientsNames(final IngredientListFBHolder ingredientListHolder, String s) {
        Firebase refRoot = new Firebase("https://otakucook.firebaseio.com/ingredients");

        Firebase refIngredient = refRoot.child(s);

        refIngredient.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String title = (String) dataSnapshot.child("name").getValue();
                    final long id = (long) dataSnapshot.child("id").getValue();
                    ingredientListHolder.name.setText(title);
                    ingredientListHolder.id = id;
                    ingredientListHolder.delete.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            listener.onItemClick(v, id);
                        }
                    });

                    switch (ingredientType){
                        case R.string.shoping_cart_drawer:
                            ingredientListHolder.add.setVisibility(View.VISIBLE);
                            break;
                        case R.string.ingredients_drawer:
                            ingredientListHolder.add.setVisibility(View.GONE);
                            break;
                    }

                    ingredientListHolder.add.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            listener.onItemClick(v, id);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



}