package com.android.mikelpablo.otakucook.Ingredients.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Ingredients.adapters.CategoriesCollectionAdapter;
import com.android.mikelpablo.otakucook.Ingredients.adapters.IngredientListAdapter;
import com.android.mikelpablo.otakucook.Main.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Utils.DividerItemDecoration;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsServerFragment extends Fragment  implements SearchView.OnQueryTextListener ,IngredientListAdapter.OnItemClickListener {

    private static final String TAG = IngredientsServerFragment.class.getName();

    public List<Ingredient> items = new ArrayList<>();
    public static List<String> ingredientsId = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private String category;
    private Firebase refRoot;
    private static Firebase mRefStorage;

    @Bind(R.id.ingredientsServer)
    RecyclerView recyclerView;

    private SearchView searchView;
    private MenuItem myActionMenuItem;
    private IngredientListAdapter adapter;

    public IngredientsServerFragment(){

    }

    public static String formatCategory (String category){
        return Normalizer
                .normalize(category, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll(" y ","_");

    }

    public static IngredientsServerFragment newInstance(String category) {
        IngredientsServerFragment fragment = new IngredientsServerFragment();
        Bundle args = new Bundle();
        args.putString("category",formatCategory(category));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingredients_server, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        mProgressDialog = new ProgressDialog(getContext());
        category = getArguments().getString("category");

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        refRoot = new Firebase(getResources().getString(R.string.users));
        if (MainActivity.mAuthData != null){
            mRefStorage = refRoot.child(MainActivity.mAuthData.getUid()).child("storage");
            getIngredientsIdStorage(refRoot);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));

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
                adapter.setFilter(items);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

    }

    private void getIngredientsIdStorage(Firebase refRoot) {


        mRefStorage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ingredientsId.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String id = postSnapshot.getValue(String.class);
                    ingredientsId.add(id);
                }
                getIngredientsServer();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getIngredientsServer(){
        MyAPI service = MyApiClient.createService(MyAPI.class);
        String ingredientsIdString = "0";
        if (!ingredientsId.isEmpty()) {
            ingredientsIdString  = android.text.TextUtils.join(",", ingredientsId);
        }
        Log.d(TAG,ingredientsIdString);
        Call<List<Ingredient>> ingredients = service.getCategoryIngredients(category,ingredientsIdString);
        LoadIngredientsServer(ingredients);
    }

    private void LoadIngredientsServer(Call<List<Ingredient>> ingredients) {
        ingredients.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                items = response.body();
                adapter = new IngredientListAdapter(items,IngredientsServerFragment.this);
                recyclerView.setAdapter(adapter);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Ingredient> filteredModelList = filter(items, newText);
        adapter.setFilter(filteredModelList);
        return false;
    }

    private List<Ingredient> filter(List<Ingredient> ingredients, String query){
        query = query.toLowerCase();
        final List<Ingredient> filteredIngredients = new ArrayList<>();
        for(Ingredient ingredient: ingredients){
            final String name = ingredient.name.toLowerCase();
            if(name.contains(query)){
                filteredIngredients.add(ingredient);
            }
        }
        return filteredIngredients;
    }

    @Override
    public void onItemClick(View view, Ingredient ingredient) {
        Firebase refUser = new Firebase(getResources().getString(R.string.users));
        Firebase refIngredient = new Firebase(getResources().getString(R.string.ingredients));
        refIngredient.child(String.valueOf(ingredient.id)).setValue(ingredient);
        mRefStorage = refUser.child(MainActivity.mAuthData.getUid()).child("storage");
        mRefStorage.child(String.valueOf(ingredient.id)).setValue(ingredient.id);
        Toast.makeText(getContext(),"Ingrediente añadido", Toast.LENGTH_SHORT).show();
    }
}
