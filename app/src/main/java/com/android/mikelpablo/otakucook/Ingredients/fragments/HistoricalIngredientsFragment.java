package com.android.mikelpablo.otakucook.Ingredients.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Ingredients.activities.CategoriesActivity;
import com.android.mikelpablo.otakucook.Login.activities.LoginActivity;
import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Main.adapters.IngredientListFirebaseAdapter;
import com.android.mikelpablo.otakucook.Main.holders.IngredientListFBHolder;
import com.android.mikelpablo.otakucook.Models.OwnIngredientFB;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Utils.Connectivity;
import com.android.mikelpablo.otakucook.Utils.DividerItemDecoration;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pabji on 30/04/2016.
 */
public class HistoricalIngredientsFragment extends Fragment implements IngredientListFirebaseAdapter.OnItemClickListener {

    @Bind(R.id.ingredientListRecyclerView)
    RecyclerView recyclerView;

    private int ingredientType;
    private Query query;
    private Firebase ref;

    public static HistoricalIngredientsFragment newInstance(int type) {
        HistoricalIngredientsFragment fragment = new HistoricalIngredientsFragment();
        Bundle args = new Bundle();
        args.putInt("TYPE",type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingredientlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        AuthData authData = LoginActivity.mAuthData;

        if (authData != null) {
            Firebase refRoot = new Firebase(getResources().getString(R.string.users));
            ref = refRoot.child(authData.getUid()).child("owningredient");
            switch (getArguments().getInt("TYPE")) {
                case R.string.shoping_cart_drawer:
                    query =ref.orderByChild("shoppingcart").equalTo("0");
                    break;
                case R.string.ingredients_drawer:
                    query =ref.orderByChild("storage").equalTo("0");
                    break;
            }
        }

        ingredientType = R.string.historical;

        FirebaseRecyclerAdapter<OwnIngredientFB, IngredientListFBHolder> fbadapter = new IngredientListFirebaseAdapter(OwnIngredientFB.class, R.layout.item_ingredientlist,
                IngredientListFBHolder.class, query, ingredientType, this);

        recyclerView.setAdapter(fbadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));

    }

    @Override
    public void onItemClick(View view, String id) {
        if(Connectivity.isNetworkAvailable(view.getContext())) {
            if(view.getId() == R.id.btAddIngredient) {
                switch (getArguments().getInt("TYPE")) {
                    case R.string.shoping_cart_drawer:
                        ref.child(String.valueOf(id)).child("shoppingcart").setValue("1");
                        Toast.makeText(getContext(), "Ingrediente añadido a la lista de compra", Toast.LENGTH_SHORT).show();
                        break;
                    case R.string.ingredients_drawer:
                        ref.child(String.valueOf(id)).child("storage").setValue("1");
                        Toast.makeText(getContext(), "Ingrediente comprado", Toast.LENGTH_SHORT).show();
                        break;
                }

            }

        }else{
            Snackbar.make(view, "No tienes conexión", Snackbar.LENGTH_LONG).show();
        }
    }
}
