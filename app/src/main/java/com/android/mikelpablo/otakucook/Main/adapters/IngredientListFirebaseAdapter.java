package com.android.mikelpablo.otakucook.Main.adapters;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.android.mikelpablo.otakucook.Ingredients.Models.Category;
import com.android.mikelpablo.otakucook.Main.fragments.IngredientListFragment;
import com.android.mikelpablo.otakucook.Main.holders.IngredientListFBHolder;

import com.android.mikelpablo.otakucook.Models.OwnIngredientFB;
import com.android.mikelpablo.otakucook.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

public class IngredientListFirebaseAdapter extends FirebaseRecyclerAdapter<OwnIngredientFB,IngredientListFBHolder>{

    public interface OnItemClickListener {
        void onItemClick(View view, String item);
    }

    private OnItemClickListener listener;
    private int ingredientType;

    public IngredientListFirebaseAdapter(Class<OwnIngredientFB> modelClass, int modelLayout, Class<IngredientListFBHolder> viewHolderClass, Query ref, int ingredientType, OnItemClickListener listener) {
        super(modelClass, modelLayout, viewHolderClass,ref);
        this.listener = listener;
        this.ingredientType = ingredientType;
    }

    @Override
    protected void populateViewHolder(IngredientListFBHolder ingredientListFBHolder, OwnIngredientFB s, int i) {
        recoveryIngredientsNames(ingredientListFBHolder,s);
    }

    private void recoveryIngredientsNames(final IngredientListFBHolder ingredientListHolder, OwnIngredientFB s) {
        Firebase refRoot = new Firebase("https://otakucook.firebaseio.com/ingredients");

        Firebase refIngredient = refRoot.child(s.id);

        refIngredient.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String title = dataSnapshot.child("name").getValue(String.class);
                    final String id = dataSnapshot.child("id").getValue(String.class);
                    ingredientListHolder.name.setText(title);
                    ingredientListHolder.id = id;
                    if(ingredientType != R.string.historical) {
                        ingredientListHolder.delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.onItemClick(v, id);
                            }
                        });
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
