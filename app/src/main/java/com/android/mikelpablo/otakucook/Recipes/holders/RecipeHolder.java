package com.android.mikelpablo.otakucook.Recipes.holders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Login.activities.LoginActivity;
import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeActivity;
import com.android.mikelpablo.otakucook.Recipes.adapters.RecipeAdapter;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeFragment;
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
    private final TextView status;
    public TextView name;
    public TextView measure;
    public Context context;

    public RecipeHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        name = (TextView) itemView.findViewById(R.id.ingredientName);
        add = (Button) itemView.findViewById(R.id.btAddIngredient);
        status = (TextView) itemView.findViewById(R.id.status_text);
        measure = (TextView) itemView.findViewById(R.id.measure);
    }

    public void bindItem(final RecipeFragment.IngredientType ingredient, final RecipeAdapter.OnItemClickListener listener, List<String>itemsIDStorage, List<String>itemsIDShoppingCart, List<String>itemsIDHistorical, final int position) {
        add.setVisibility(View.GONE);
        name.setText(ingredient.name);
        measure.setText(String.valueOf(ingredient.quantity)+" "+ingredient.measure);
        status.setText("");
        if (itemsIDStorage.contains(String.valueOf(ingredient.id))){
            ingredient.type = RecipeFragment.IngredientType.typeEnum.storage;
            itemView.setBackground(ContextCompat.getDrawable(context,R.drawable.exist_item));
            status.setVisibility(View.VISIBLE);
            status.setText(R.string.disponible);
        }else if (itemsIDShoppingCart.contains(String.valueOf(ingredient.id))){
            ingredient.type = RecipeFragment.IngredientType.typeEnum.shoppingCart;
            itemView.setBackground(ContextCompat.getDrawable(context,R.drawable.in_cart_item));
            status.setVisibility(View.VISIBLE);
            status.setText(R.string.pendiente);
        }else {
            add.setVisibility(View.VISIBLE);
            itemView.setBackground(ContextCompat.getDrawable(context,R.drawable.not_exist_item));
            ingredient.type = RecipeFragment.IngredientType.typeEnum.historical;
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,ingredient,position);
            }
        });
    }

}
