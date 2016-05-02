package com.android.mikelpablo.otakucook.Main.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.mikelpablo.otakucook.BuildConfig;
import com.android.mikelpablo.otakucook.Login.activities.LoginActivity;
import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.OwnIngredientFB;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Main.holders.RecipeListHolder;
import com.android.mikelpablo.otakucook.Main.adapters.RecipesListAdapter;
import com.android.mikelpablo.otakucook.Utils.Connectivity;
import com.android.mikelpablo.otakucook.Utils.DividerItemDecoration;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class RecipeListFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {
    @Bind(R.id.recipeListRecyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.todas)
    Button btTodas;
    @Bind(R.id.favoritas)
    Button btFavoritas;
    @Bind(R.id.posibles)
    Button btPosibles;
    @Bind(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    private static final String TAG = RecipeListFragment.class.getName();
    public ArrayList<Recipe> items = new ArrayList<>();
    public ArrayList<Recipe> itemsPossibles = new ArrayList<>();
    public static List<String> ingredientsId = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private Firebase refRoot;
    private FirebaseRecyclerAdapter<String, RecipeListHolder> fbadapter;
    public static int selected;
    private RecipesListAdapter adapter;
    private MyAPI service;
    private Firebase mRef;
    private static Firebase mRefStorage;
    private ValueEventListener eventListener;
    private ValueEventListener valueEventListener;
    private RecipesListAdapter adapterPosibles;
    private Firebase refRecipe;
    private MenuItem myActionMenuItem;
    private SearchView searchView;

    public RecipeListFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            selected= savedInstanceState.getInt("selected");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipelist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        if (BuildConfig.SHOW_PREMIUM_ACTIONS){
            btFavoritas.setVisibility(View.VISIBLE);
        }
        mProgressDialog = new ProgressDialog(getContext());
       // adapter = new RecipesListAdapter(getContext(), items);
        refRoot = new Firebase(getResources().getString(R.string.users));
        if (LoginActivity.mAuthData != null){
            mRef = refRoot.child(LoginActivity.mAuthData.getUid()).child("favorites");
            mRefStorage = refRoot.child(LoginActivity.mAuthData.getUid()).child("owningredient");
            getIngredientsIdStorage(mRefStorage);
        }
        adapter = new RecipesListAdapter(getContext(), items);
        adapterPosibles = new RecipesListAdapter(getContext(), itemsPossibles);
        MainActivity.main_title.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
        btTodas.setOnClickListener(this);
        btFavoritas.setOnClickListener(this);
        btPosibles.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (selected == R.id.todas){
                    items.clear();
                    refreshSwipe();
                }else if (selected == R.id.posibles){
                    itemsPossibles.clear();
                    refreshSwipePossibles();
                }else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }


                //ServerRecipeList(recipes, items);
            }
        });
    }

    private void refreshSwipePossibles() {
        service = MyApiClient.createService(MyAPI.class);
        String ingredientsIdString = "0";
        if (!ingredientsId.isEmpty()) {
            ingredientsIdString = android.text.TextUtils.join(",", ingredientsId);
        }
        Call<List<Recipe>> possiblesRecipes = service.getPossiblesRecipes(ingredientsIdString);
        possiblesRecipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipesServer = response.body();

                for (Recipe r : recipesServer) {
                    itemsPossibles.add(r);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }

    private void refreshSwipe() {
        service = MyApiClient.createService(MyAPI.class);
        Call<List<Recipe>> recipes = service.recipes();
        recipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipesServer = response.body();

                for (Recipe r : recipesServer) {
                    items.add(r);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }

    private void loadRecyclerview(int savedInstanceStateInt) {

        switch (savedInstanceStateInt) {
            case R.id.todas:
                getActivity().setTitle(getActivity().getString(R.string.title_mainFragment));
                onClick(btTodas);
                break;
            case R.id.favoritas:
                getActivity().setTitle(getActivity().getString(R.string.title_favoritesFragment));
                onClick(btFavoritas);
                break;
            case R.id.posibles:
                getActivity().setTitle(getActivity().getString(R.string.title_posiblesFragment));
                onClick(btPosibles);
                break;
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            if (savedInstanceState == null){
                loadRecyclerview(R.id.todas);
            }
            else{
                loadRecyclerview(selected);
            }

        }
    }

    private void ServerRecipeList(Call<List<Recipe>> recipes, final ArrayList<Recipe> lista) {
        recipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                allRecipes(response,lista);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

            }
        });
    }

    private void allRecipes(Response<List<Recipe>> response,ArrayList<Recipe> lista) {
        List<Recipe> recipesServer = response.body();

        for (Recipe r : recipesServer) {
            lista.add(r);
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(Connectivity.isNetworkAvailable(getContext())) {

                btTodas.setBackground(getResources().getDrawable(R.drawable.custom_tab_bar_button));
                btFavoritas.setBackground(getResources().getDrawable(R.drawable.custom_tab_bar_button));
                btPosibles.setBackground(getResources().getDrawable(R.drawable.custom_tab_bar_button));
                selected = v.getId();
               if (selected == R.id.todas){
                   btPosibles.setEnabled(true);
                   btFavoritas.setEnabled(true);
               }else if (selected == R.id.posibles){
                   btTodas.setEnabled(true);
                   btFavoritas.setEnabled(true);
               }else {
                   btTodas.setEnabled(true);
                   btPosibles.setEnabled(true);
               }
                if (searchView != null) {
                    myActionMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                }


                switch (v.getId()) {
                    case R.id.todas:
                        if (myActionMenuItem != null) {
                            myActionMenuItem.setVisible(true);
                        }
                        btTodas.setEnabled(false);
                        getActivity().setTitle(R.string.title_mainFragment);

                        recyclerView.setAdapter(adapter);
                        v.setBackgroundColor(getResources().getColor(R.color.accent2));
                        items.clear();
                        if (items.isEmpty()) {
                            initOnclick();
                            Call<List<Recipe>> recipes = service.recipes();
                            ServerRecipeList(recipes, items);
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        break;
                    case R.id.posibles:
                        if (myActionMenuItem != null) {
                            myActionMenuItem.setVisible(true);
                        }
                        btPosibles.setEnabled(false);
                        itemsPossibles.clear();
                        getActivity().setTitle(R.string.title_posiblesFragment);
                        v.setBackgroundColor(getResources().getColor(R.color.accent2));
                        if (itemsPossibles.isEmpty()) {
                            initOnclick();
                            String ingredientsIdString = "0";
                            if (!ingredientsId.isEmpty()) {
                                ingredientsIdString = android.text.TextUtils.join(",", ingredientsId);
                            }
                            Call<List<Recipe>> possiblesRecipes = service.getPossiblesRecipes(ingredientsIdString);
                            ServerRecipeList(possiblesRecipes, itemsPossibles);
                        }
                        adapterPosibles = new RecipesListAdapter(getContext(), itemsPossibles);
                        recyclerView.setAdapter(adapterPosibles);
                        recyclerView.getAdapter().notifyDataSetChanged();
                        break;
                    case R.id.favoritas:
                        if (myActionMenuItem != null) {
                            myActionMenuItem.setVisible(false);
                        }
                        btFavoritas.setEnabled(false);

                        if (LoginActivity.mAuthData != null) {

                            if (fbadapter == null) {
                                fbadapter = new FirebaseRecyclerAdapter<String, RecipeListHolder>(String.class, R.layout.item_recipelist,
                                        RecipeListHolder.class, mRef) {
                                    @Override
                                    protected void populateViewHolder(final RecipeListHolder recipeHolder, final String s, int i) {

                                        recoveryRecipesNames(recipeHolder, s);

                                        Log.d(TAG, "getFavoritesRecipes");
                                    }

                                };
                                Log.d(TAG, "fbadapter");
                            } else {
                                fbadapter.notifyDataSetChanged();
                            }
                            recyclerView.setAdapter(fbadapter);

                        }

                        //recyclerView.setAdapter(fbadapter);

                        getActivity().setTitle(R.string.title_favoritesFragment);
                        btFavoritas.setBackgroundColor(getResources().getColor(R.color.accent2));
                        //btFavoritas.setBackgroundColor(Color.BLUE);
                        //recyclerView.setAdapter(fbadapter);
                        //Toast.makeText(getContext(), "favoritos", Toast.LENGTH_SHORT).show();
                        break;
                }

        }else{
            Snackbar.make(v, "No tienes conexión", Snackbar.LENGTH_LONG).show();
        }
    }


    private void initOnclick() {

        service = MyApiClient.createService(MyAPI.class);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getActivity().getString(R.string.progressDialogMessage));
        mProgressDialog.show();
        //items.clear();

    }

    private void getIngredientsIdStorage(Firebase ref) {

        ingredientsId.clear();
         eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    OwnIngredientFB ownIngredientFB = postSnapshot.getValue(OwnIngredientFB.class);
                    if (ownIngredientFB.storage.equals("1")){
                        ingredientsId.add(ownIngredientFB.id);
                    }

                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        ref.addValueEventListener(eventListener);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_search,menu);

        myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(myActionMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

    }
    private ArrayList<Recipe> filter(List<Recipe> recipes, String query){
        query = query.toLowerCase();
        final ArrayList<Recipe> filteredRecipes = new ArrayList<>();
        for(Recipe recipe: recipes){
            final String name = recipe.name.toLowerCase();
            if(name.contains(query)){
                filteredRecipes.add(recipe);
            }
        }
        return filteredRecipes;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        switch (selected){
            case R.id.todas:{
                Log.d("MIKEL","todas");
                final List<Recipe> filteredModelList = filter(items, newText);
               adapter.setFilter(filteredModelList);
                break;
            }
            case R.id.posibles:{
                Log.d("MIKEL","possibles");
                final List<Recipe> filteredModelList = filter(itemsPossibles, newText);
                adapterPosibles.setFilter(filteredModelList);
                break;
            }
        }
        return false;
    }

    private void recoveryRecipesNames(final RecipeListHolder recipeListHolder, String s) {
        refRoot = new Firebase(getResources().getString(R.string.recipes));
        refRecipe = refRoot.child(s);
        Log.d(TAG,"dentro de recoveryRecipesNames");
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String title = (String) dataSnapshot.child("name").getValue();
                    long id = (long) dataSnapshot.child("id").getValue();
                    recipeListHolder.name.setText(title);
                    recipeListHolder.id = id;
                    Log.d(TAG,"recoveryRecipesNames");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        refRecipe.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected",selected);
    }
}
