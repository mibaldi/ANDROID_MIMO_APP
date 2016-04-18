package com.android.mikelpablo.otakucook.Main.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Main.holders.RecipeListHolder;
import com.android.mikelpablo.otakucook.Main.adapters.RecipesListAdapter;
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
public class RecipeListFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.recipeListRecyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.todas)
    Button btTodas;
    @Bind(R.id.favoritas)
    Button btFavoritas;
    @Bind(R.id.posibles)
    Button btPosibles;


    private static final String TAG = RecipeListFragment.class.getName();
    public ArrayList<Recipe> items = new ArrayList<>();
    public ArrayList<Recipe> itemsPossibles = new ArrayList<>();
    /* A reference to the Firebase */
    public Firebase mFirebaseRef;
    public static List<String> ingredientsId = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private Firebase refRoot;
    private FirebaseRecyclerAdapter<String, RecipeListHolder> fbadapter;
    public static int selected = R.id.todas;
    private RecipesListAdapter adapter;
    private MyAPI service;
    private Firebase mRef;
    private static Firebase mRefStorage;
    private ValueEventListener eventListener;
    private ValueEventListener valueEventListener;
    private RecipesListAdapter adapterPosibles;
    private Firebase refRecipe;

    public RecipeListFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            selected= savedInstanceState.getInt("selected");
            ArrayList<Recipe> itemsTemp = savedInstanceState.getParcelableArrayList("listaTodos");
            if (itemsTemp != null) {
                items = itemsTemp;
            }
            ArrayList<Recipe> itemsTemp2 = savedInstanceState.getParcelableArrayList("listaPosibles");
            if (itemsTemp2 != null) {
                itemsPossibles = itemsTemp2;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipelist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mProgressDialog = new ProgressDialog(getContext());
       // adapter = new RecipesListAdapter(getContext(), items);
        refRoot = new Firebase(getResources().getString(R.string.users));
        if (MainActivity.mAuthData != null){
            mRef = refRoot.child(MainActivity.mAuthData.getUid()).child("favorites");
            mRefStorage = refRoot.child(MainActivity.mAuthData.getUid()).child("storage");
            getIngredientsIdStorage(refRoot);
        }
        adapter = new RecipesListAdapter(getContext(), items);
        adapterPosibles = new RecipesListAdapter(getContext(), itemsPossibles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
        /*recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));*/
        btTodas.setOnClickListener(this);
        btFavoritas.setOnClickListener(this);
        btPosibles.setOnClickListener(this);


        //getFavoritesRecipes();
        //recyclerView.setAdapter(fbadapter);
        /*if (savedInstanceState != null){

            Log.d(TAG,"instancia creada");
            loadRecyclerview(selected);

        }else{
            loadRecyclerview(selected);
        }*/


        /*String nombre = getArguments().getString("nombreReceta");
        nombreReceta.setText(nombre)*/
        ;
    }

    private void loadRecyclerview(int savedInstanceStateInt) {
        switch (savedInstanceStateInt){
            case R.id.todas:
                getActivity().setTitle("Todas las recetas");
                onClick(btTodas);
                break;
            case R.id.favoritas:
                getActivity().setTitle("Recetas favoritas");
                onClick(btFavoritas);
                break;
            case R.id.posibles:
                getActivity().setTitle("Posibles recetas");
                onClick(btPosibles);
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            loadRecyclerview(selected);
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
        btTodas.setBackground(getResources().getDrawable(android.R.drawable.btn_default));
        btFavoritas.setBackground(getResources().getDrawable(android.R.drawable.btn_default));
        btPosibles.setBackground(getResources().getDrawable(android.R.drawable.btn_default));
        selected = v.getId();

        switch (v.getId()) {
            case R.id.todas:
                getActivity().setTitle("Todas las recetas");

                recyclerView.setAdapter(adapter);
                v.setBackgroundColor(Color.BLUE);
                if (items.isEmpty()){
                    initOnclick(v);
                    Call<List<Recipe>> recipes = service.recipes();
                    ServerRecipeList(recipes,items);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.posibles:
                getActivity().setTitle("Posibles recetas");
                recyclerView.setAdapter(adapterPosibles);
                v.setBackgroundColor(Color.BLUE);
                if (itemsPossibles.isEmpty()) {
                    initOnclick(v);
                    String ingredientsIdString = "0";
                    if (!ingredientsId.isEmpty()) {
                        ingredientsIdString = android.text.TextUtils.join(",", ingredientsId);
                    }
                    Call<List<Recipe>> possiblesRecipes = service.getPossiblesRecipes(ingredientsIdString);
                    ServerRecipeList(possiblesRecipes, itemsPossibles);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.favoritas:

                if (MainActivity.mAuthData != null){

                    if (fbadapter == null){
                        fbadapter = new FirebaseRecyclerAdapter<String, RecipeListHolder>(String.class, R.layout.item_recipelist,
                                RecipeListHolder.class, mRef) {
                            @Override
                            protected void populateViewHolder(final RecipeListHolder recipeHolder, final String s, int i) {

                                    recoveryRecipesNames(recipeHolder, s);

                                Log.d(TAG,"getFavoritesRecipes");
                            }

                        };
                        Log.d(TAG,"fbadapter");
                    }else {
                        fbadapter.notifyDataSetChanged();
                    }
                    recyclerView.setAdapter(fbadapter);

                }

                //recyclerView.setAdapter(fbadapter);

                getActivity().setTitle("Recetas favoritas");
                btFavoritas.setBackgroundColor(Color.BLUE);
                //recyclerView.setAdapter(fbadapter);
                //Toast.makeText(getContext(), "favoritos", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initOnclick(View view) {

        service = MyApiClient.createService(MyAPI.class);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        //items.clear();

    }

    private void getFavoritesRecipes() {
        Log.d(TAG,"getFavoritesRecipesInicio");

    }

    private void getIngredientsIdStorage(Firebase refRoot) {

        ingredientsId.clear();
         eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String id = postSnapshot.getValue(String.class);
                    ingredientsId.add(id);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        mRefStorage.addValueEventListener(eventListener);
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
        outState.putParcelableArrayList("listaTodos",items);
        outState.putParcelableArrayList("listaPosibles",itemsPossibles);
    }
}
