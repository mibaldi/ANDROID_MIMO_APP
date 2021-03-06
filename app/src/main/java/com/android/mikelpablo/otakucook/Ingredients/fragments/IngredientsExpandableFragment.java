package com.android.mikelpablo.otakucook.Ingredients.fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Ingredients.Models.BaseIngredient;
import com.android.mikelpablo.otakucook.Ingredients.activities.CategoriesActivity;
import com.android.mikelpablo.otakucook.Ingredients.adapters.IngredientsExpandableAdapter;
import com.android.mikelpablo.otakucook.Login.activities.LoginActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.OwnIngredientFB;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Utils.Connectivity;
import com.android.mikelpablo.otakucook.Utils.DividerItemDecoration;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsExpandableFragment extends Fragment  implements IngredientsExpandableAdapter.OnItemClickListener {

    public  ArrayList<Ingredient> items = new ArrayList<>();
    public  ArrayList<String> ingredientsId = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private String category;
    private Firebase refRoot;
    public Context context;

    private IngredientsExpandableAdapter adapter;
    private ArrayList<BaseIngredient> baseIngredients = new ArrayList<>();
    private Query mRefStorage;

    @Bind(R.id.ingredientsServer)
    RecyclerView recyclerView;

    @Bind(R.id.not_exist_ingredients)
    TextView not_exist_ingredients;

    public IngredientsExpandableFragment(){

    }

    public static String formatCategory (String category){
        return Normalizer
                .normalize(category, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll(" y ","_");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            ArrayList<String> itemsTemp = savedInstanceState.getStringArrayList("listaIngredientes");
            if (itemsTemp != null) {
                ingredientsId = itemsTemp;
            }
        }
    }

    public static IngredientsExpandableFragment newInstance(String category) {
        IngredientsExpandableFragment fragment = new IngredientsExpandableFragment();
        Bundle args = new Bundle();
        args.putString("category",formatCategory(category));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
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
        mProgressDialog.setMessage(context.getString(R.string.progressDialogMessage));
        mProgressDialog.show();

        refRoot = new Firebase(getResources().getString(R.string.users));
        if (items.isEmpty()){
            if (LoginActivity.mAuthData != null) {
                mRefStorage = refRoot.child(LoginActivity.mAuthData.getUid()).child("owningredient");
                getIngredientsIdStorage(refRoot);
            }
        }else {
            mProgressDialog.dismiss();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));

        generateBaseIngredients();
    }

    private void getIngredientsIdStorage(Firebase refRoot) {

        ingredientsId.clear();
        mRefStorage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ingredientsId.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String id = postSnapshot.child("id").getValue(String.class);
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
        Call<List<Ingredient>> ingredients = service.getCategoryIngredients(category,ingredientsIdString);
        LoadIngredientsServer(ingredients);
    }

    private void LoadIngredientsServer(Call<List<Ingredient>> ingredients) {
        ingredients.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                items = (ArrayList<Ingredient>) response.body();
                if(items.size() == 0){
                    recyclerView.setVisibility(View.GONE);
                    not_exist_ingredients.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    not_exist_ingredients.setVisibility(View.GONE);
                }
                generateBaseIngredients();
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
    public void onItemClick(View view, Ingredient ingredient) {
        if(Connectivity.isNetworkAvailable(view.getContext())) {
            Firebase refUser = new Firebase(getResources().getString(R.string.users));
            Firebase refIngredient = new Firebase(getResources().getString(R.string.ingredients));
            refIngredient.child(String.valueOf(ingredient.id)).setValue(ingredient);
            Firebase refOwnIngredient = refUser.child(LoginActivity.mAuthData.getUid()).child("owningredient").child(String.valueOf(ingredient.id));
            OwnIngredientFB ownIngredientFB = new OwnIngredientFB(String.valueOf(ingredient.id), "0", "0");
            switch (CategoriesActivity.typeStatic) {
                case R.string.shoping_cart_drawer:
                    ownIngredientFB.shoppingcart = "1";
                    break;
                case R.string.ingredients_drawer:
                    ownIngredientFB.storage = "1";
                    break;
            }
            refOwnIngredient.setValue(ownIngredientFB);

            Toast.makeText(getContext(), R.string.toast_add_ingredient, Toast.LENGTH_SHORT).show();
        }else{
            Snackbar.make(view, R.string.no_connectivity, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("listaIngredientes",items);
    }

    private void generateBaseIngredients() {

        baseIngredients.clear();
        Map<String,Integer> basetypesMap = new HashMap<>();
        for (Ingredient ingredient : items) {
            if(basetypesMap.containsKey(ingredient.baseType)){
                BaseIngredient baseIngredient = baseIngredients.get(basetypesMap.get(ingredient.baseType));
                baseIngredient.addIngredient(ingredient);
            }else{
                ArrayList<Ingredient> ingredientsArray = new ArrayList<>();
                ingredientsArray.add(ingredient);
                BaseIngredient baseIngredient = new BaseIngredient(ingredient.baseType,ingredientsArray);
                baseIngredients.add(baseIngredient);
                int index = baseIngredients.indexOf(baseIngredient);
                basetypesMap.put(ingredient.baseType,index);
            }

        }
        adapter = new IngredientsExpandableAdapter(baseIngredients, context,IngredientsExpandableFragment.this);
        recyclerView.setAdapter(adapter);
    }
}
