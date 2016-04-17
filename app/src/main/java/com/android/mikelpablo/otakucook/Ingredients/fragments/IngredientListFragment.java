package com.android.mikelpablo.otakucook.Ingredients.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Ingredients.activities.CategoriesActivity;
import com.android.mikelpablo.otakucook.Ingredients.holders.IngredientListFBHolder;
import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Utils.DividerItemDecoration;
import com.firebase.client.AuthData;
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

public class IngredientListFragment  extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener{

    private static final String TAG = IngredientListFragment.class.getName();
    public List<Ingredient> items = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private int ingredientType;
    private Firebase ref;

    @Bind(R.id.ingredientListRecyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.addIngredientServer)
    Button mBtAddIngredientServer;
    @Bind(R.id.add_category_ingredient)
    FloatingActionButton mBtAddCategoryIngredients;

    private SearchView searchView;
    private MenuItem myActionMenuItem;

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
        setHasOptionsMenu(true);
        mProgressDialog = new ProgressDialog(getContext());
        ingredientType = getArguments().getInt("ingredientType");
        getActivity().setTitle(ingredientType);
        mBtAddIngredientServer.setOnClickListener(this);
        mBtAddCategoryIngredients.setOnClickListener(this);
        AuthData authData = MainActivity.mAuthData;
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

            FirebaseRecyclerAdapter<String, IngredientListFBHolder> fbadapter = new FirebaseRecyclerAdapter<String, IngredientListFBHolder>(String.class, R.layout.item_ingredientlist,
                    IngredientListFBHolder.class, ref) {

                @Override
                protected void populateViewHolder(IngredientListFBHolder ingredientListHolder, String s, int i) {
                    recoveryIngredientsNames(ingredientListHolder, s);
                }
            };

            recyclerView.setAdapter(fbadapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
        }

    }

    private void recoveryIngredientsNames(final IngredientListFBHolder ingredientListHolder, String s) {
        Firebase refRoot = new Firebase(getResources().getString(R.string.ingredients));
        Firebase refIngredient = refRoot.child(s);

        refIngredient.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String title = (String) dataSnapshot.child("name").getValue();
                    long id = (long) dataSnapshot.child("id").getValue();
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

    private void sendIngredientFirebase(Ingredient ingredient) {
        Firebase refUser = new Firebase(getResources().getString(R.string.users));
        Firebase refIngredient = new Firebase(getResources().getString(R.string.ingredients));
        refIngredient.child(String.valueOf(ingredient.id)).setValue(ingredient);
        ref = refUser.child(MainActivity.mAuthData.getUid()).child("storage");
        ref.child(String.valueOf(ingredient.id)).setValue(ingredient.id);
        Toast.makeText(getContext(),"Ingrediente cargado en storage", Toast.LENGTH_SHORT).show();
    }

    private void LoadIngredientFirebase(Call<Ingredient> ingredient) {
        ingredient.enqueue(new Callback<Ingredient>() {
            @Override
            public void onResponse(Call<Ingredient> call, Response<Ingredient> response) {
                Log.d(TAG,response.body().name);
                sendIngredientFirebase(response.body());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Ingredient> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addIngredientServer:
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();
                items.clear();

                MyAPI service = MyApiClient.createService(MyAPI.class);
                Call<Ingredient> ingredient = service.getIngredient(7);
                LoadIngredientFirebase(ingredient);

            case R.id.add_category_ingredient:
                Intent intent = new Intent(getContext(),CategoriesActivity.class);
                getContext().startActivity(intent);

        }
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
