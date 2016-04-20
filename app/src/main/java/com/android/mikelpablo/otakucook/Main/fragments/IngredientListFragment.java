package com.android.mikelpablo.otakucook.Main.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Ingredients.activities.CategoriesActivity;
import com.android.mikelpablo.otakucook.Ingredients.adapters.IngredientListFirebaseAdapter;
import com.android.mikelpablo.otakucook.Ingredients.holders.IngredientListFBHolder;
import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Utils.DividerItemDecoration;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientListFragment  extends Fragment implements View.OnClickListener,IngredientListFirebaseAdapter.OnItemClickListener{

    private static final String TAG = IngredientListFragment.class.getName();
    public List<Ingredient> items = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private int ingredientType;
    private Firebase ref;

    @Bind(R.id.ingredientListRecyclerView)
    RecyclerView recyclerView;
    FloatingActionButton mBtAddCategoryIngredients;

    private SearchView searchView;
    private MenuItem myActionMenuItem;
    private HashMap<String, Long> ingredientsMap;
    private Firebase mRefStorage;
    private Firebase refHistorico;
    private Firebase refShopping;

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
        mBtAddCategoryIngredients = (FloatingActionButton) getActivity().findViewById(R.id.add_category_ingredient);

        setHasOptionsMenu(true);
        mProgressDialog = new ProgressDialog(getContext());
        ingredientType = getArguments().getInt("ingredientType");
        getActivity().setTitle(ingredientType);
        mBtAddCategoryIngredients.setOnClickListener(this);
        AuthData authData = MainActivity.mAuthData;
        ingredientsMap = new HashMap<>();
        if (authData != null){
            Firebase refRoot = new Firebase(getResources().getString(R.string.users));
            switch (ingredientType){
                case R.string.shoping_cart_drawer:

                    ref = refRoot.child(authData.getUid()).child("shoppingcart");
                    break;
                case R.string.ingredients_drawer:
                    ref = refRoot.child(authData.getUid()).child("storage");
                    break;
            }

            FirebaseRecyclerAdapter<String,IngredientListFBHolder> fbadapter = new IngredientListFirebaseAdapter(String.class, R.layout.item_ingredientlist,
                    IngredientListFBHolder.class, ref,ingredientType,this);

            recyclerView.setAdapter(fbadapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_category_ingredient:
                Intent intent = new Intent(getContext(),CategoriesActivity.class);
                getContext().startActivity(intent);
        }
    }

    @Override
    public void onItemClick(View view, Long id) {
        Firebase refUser = new Firebase(getResources().getString(R.string.users));
        switch (view.getId()){
            case R.id.btEliminarIngrediente:

                switch (ingredientType){
                    case R.string.shoping_cart_drawer:
                        mRefStorage = refUser.child(MainActivity.mAuthData.getUid()).child("shoppingcart");
                        break;
                    case R.string.ingredients_drawer:
                        mRefStorage = refUser.child(MainActivity.mAuthData.getUid()).child("storage");
                        break;
                }
                mRefStorage.child(String.valueOf(id)).removeValue();
                Toast.makeText(getContext(),"Ingrediente Eliminado", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btAddIngredient:

                refShopping = refUser.child(MainActivity.mAuthData.getUid()).child("shoppingcart");
                mRefStorage = refUser.child(MainActivity.mAuthData.getUid()).child("storage");
                refShopping.child(String.valueOf(id)).removeValue();
                mRefStorage.child(String.valueOf(id)).setValue(id);
                Toast.makeText(getContext(),"Ingrediente comprado", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
