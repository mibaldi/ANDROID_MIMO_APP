package com.android.mikelpablo.otakucook.Main.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Ingredients.activities.CategoriesActivity;
import com.android.mikelpablo.otakucook.Ingredients.activities.HistoricalIngredientsActivity;
import com.android.mikelpablo.otakucook.Login.activities.LoginActivity;
import com.android.mikelpablo.otakucook.Main.adapters.IngredientListFirebaseAdapter;
import com.android.mikelpablo.otakucook.Main.holders.IngredientListFBHolder;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.OwnIngredientFB;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Utils.Connectivity;
import com.android.mikelpablo.otakucook.Utils.DividerItemDecoration;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IngredientListFragment  extends Fragment implements View.OnClickListener,IngredientListFirebaseAdapter.OnItemClickListener{

    private static final String TAG = IngredientListFragment.class.getName();
    public List<Ingredient> items = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private int ingredientType;
    private Firebase ref;

    @Bind(R.id.ingredientListRecyclerView)
    RecyclerView recyclerView;

    com.github.clans.fab.FloatingActionButton mBtAddCategoryIngredients;
    com.github.clans.fab.FloatingActionButton mBtAddHistoricalIngredients;
    com.github.clans.fab.FloatingActionMenu mBtFloatingMenu;

    private SearchView searchView;
    private MenuItem myActionMenuItem;
    private HashMap<String, Long> ingredientsMap;
    private Firebase mRefStorage;
    private Firebase refHistorico;
    private Firebase refShopping;
    private Query query;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingredientlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mBtAddCategoryIngredients = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.add_category_ingredient);
        mBtAddHistoricalIngredients = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.add_historical_ingredient);
        mBtFloatingMenu = (com.github.clans.fab.FloatingActionMenu) getActivity().findViewById(R.id.menu_red);

        setHasOptionsMenu(true);
        mProgressDialog = new ProgressDialog(getContext());
        ingredientType = getArguments().getInt("ingredientType");
        getActivity().setTitle(ingredientType);
        mBtAddCategoryIngredients.setOnClickListener(this);
        mBtAddHistoricalIngredients.setOnClickListener(this);
        AuthData authData = LoginActivity.mAuthData;
        ingredientsMap = new HashMap<>();
        if (authData != null){
            Firebase refRoot = new Firebase(getResources().getString(R.string.users));
            ref = refRoot.child(authData.getUid()).child("owningredient");

            switch (ingredientType){
                case R.string.shoping_cart_drawer:

                    query =ref.orderByChild("shoppingcart").equalTo("1");
                    break;
                case R.string.ingredients_drawer:
                    query = ref.orderByChild("storage").equalTo("1");
                    break;
            }
            if(Connectivity.isNetworkAvailable(getContext())) {
                FirebaseRecyclerAdapter<OwnIngredientFB, IngredientListFBHolder> fbadapter = new IngredientListFirebaseAdapter(OwnIngredientFB.class, R.layout.item_ingredientlist,
                        IngredientListFBHolder.class, query, ingredientType, this);

                recyclerView.setAdapter(fbadapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
            }else{
                Snackbar.make(view, "No tienes conexión", Snackbar.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if(Connectivity.isNetworkAvailable(v.getContext())) {
            mBtFloatingMenu.close(true);
            switch (v.getId()){
                case R.id.add_category_ingredient:
                    Intent intent = new Intent(getContext(), CategoriesActivity.class);
                    intent.putExtra("type", ingredientType);
                    getContext().startActivity(intent);
                    break;
                case R.id.add_historical_ingredient:
                    mBtAddHistoricalIngredients.hide(true);
                    Intent intent2 = new Intent(getContext(), HistoricalIngredientsActivity.class);
                    getContext().startActivity(intent2);
                    break;
            }
        }else{
            Snackbar.make(v, "No tienes conexión", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(View view, String id) {
        if(Connectivity.isNetworkAvailable(view.getContext())) {
            Firebase refUser = new Firebase(getResources().getString(R.string.users));
            switch (view.getId()){
                case R.id.btEliminarIngrediente:

                    switch (ingredientType){
                        case R.string.shoping_cart_drawer:
                            ref.child(id).child("shoppingcart").setValue("0");
                            break;
                        case R.string.ingredients_drawer:
                            ref.child(id).child("storage").setValue("0");
                            Log.d("fgfd",id);
                            break;

                    }
                    Toast.makeText(getContext(),"Ingrediente Eliminado", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btAddIngredient:

                    ref.child(String.valueOf(id)).child("shoppingcart").setValue("0");
                    ref.child(String.valueOf(id)).child("storage").setValue("1");
                    Toast.makeText(getContext(),"Ingrediente comprado", Toast.LENGTH_SHORT).show();
                    break;
            }
        }else{
            Snackbar.make(view, "No tienes conexión", Snackbar.LENGTH_LONG).show();
        }


    }
}
