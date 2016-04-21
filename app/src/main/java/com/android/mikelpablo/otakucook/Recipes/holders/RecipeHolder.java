package com.android.mikelpablo.otakucook.Recipes.holders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeActivity;
import com.android.mikelpablo.otakucook.Recipes.adapters.RecipeAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class RecipeHolder extends RecyclerView.ViewHolder {
    private final Button add;
    public TextView name;
    public Context context;
    private Firebase mFirebaseRef;

    public RecipeHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        name = (TextView) itemView.findViewById(R.id.ingredientName);
        add = (Button) itemView.findViewById(R.id.btAddIngredient);

    }

    public void bindItem(final Ingredient ingredient, final RecipeAdapter.OnItemClickListener listener,List<String> itemsID) {

        name.setText(ingredient.name);

        existFirebase(ingredient.id);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,ingredient.id);
            }
        });
    }
    public void existFirebase(long id) {
        Firebase userRef= new Firebase(context.getResources().getString(R.string.users));
        userRef = userRef.child(MainActivity.mAuthData.getUid()).child("owningredient");
        userRef.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for(DataSnapshot data:dataSnapshot.getChildren()) {
                        if (data.getKey().equals("storage") && data.getValue(String.class).equals("1")){
                            itemView.setBackgroundColor(Color.GREEN);
                            break;
                        }else if(data.getKey().equals("shoppingcart") && data.getValue(String.class).equals("1")){
                            itemView.setBackgroundColor(Color.BLUE);
                            break;
                        }else {
                            itemView.setBackgroundColor(Color.YELLOW);
                        }
                    }
                } else {
                    itemView.setBackgroundColor(Color.RED);
                    // sendIngredientFirebase(recipe);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    };

}
