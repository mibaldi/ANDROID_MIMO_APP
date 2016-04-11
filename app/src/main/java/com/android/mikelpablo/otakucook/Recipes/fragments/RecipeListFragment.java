package com.android.mikelpablo.otakucook.Recipes.fragments;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Main.MainActivity;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.holders.RecipeHolder;
import com.android.mikelpablo.otakucook.Recipes.adapters.RecipesListAdapter;
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
    public List<Recipe> items = new ArrayList<>();
    /* A reference to the Firebase */
    public Firebase mFirebaseRef;
    private ProgressDialog mProgressDialog;

    public RecipeListFragment() {
    }

    public static RecipeListFragment newInstance(String nombreReceta) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putString("nombreReceta", nombreReceta);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipelist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mProgressDialog = new ProgressDialog(getContext());
        final RecipesListAdapter adapter = new RecipesListAdapter(getContext(), items);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        btTodas.setOnClickListener(this);
        btFavoritas.setOnClickListener(this);
        btPosibles.setOnClickListener(this);

        /*String nombre = getArguments().getString("nombreReceta");
        nombreReceta.setText(nombre)*/
        ;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {
            Log.i(TAG, "fragment creado");

        }

    }

    private void ServerRecipeList(Call<List<Recipe>> recipes) {
        recipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                allRecipes(response);
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

    private void allRecipes(Response<List<Recipe>> response) {
        List<Recipe> recipesServer = response.body();
        for (Recipe r : recipesServer) {
            items.add(r);
            //System.out.println("id: " + r.id);
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.todas:

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();
                items.clear();
                final RecipesListAdapter adapter = new RecipesListAdapter(getContext(), items);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                MyAPI service = MyApiClient.createService(MyAPI.class);
                Call<List<Recipe>> recipes = service.recipes();
                ServerRecipeList(recipes);

                recyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.favoritas:
                Firebase refRoot = new Firebase(getResources().getString(R.string.users));
                Firebase ref = refRoot.child(MainActivity.mAuthData.getUid()).child("favorites");
                FirebaseRecyclerAdapter<String, RecipeHolder> fbadapter = new FirebaseRecyclerAdapter<String, RecipeHolder>(String.class, R.layout.recipelist_item,
                        RecipeHolder.class, ref) {
                    @Override
                    protected void populateViewHolder(final RecipeHolder recipeHolder, final String s, int i) {
                        recoveryRecipesNames(recipeHolder, s);

                    }
                };
                recyclerView.setAdapter(fbadapter);
                Toast.makeText(getContext(), "favoritos", Toast.LENGTH_SHORT).show();
                break;
            case R.id.posibles:
                Toast.makeText(getContext(), "posibles", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void recoveryRecipesNames(final RecipeHolder recipeHolder, String s) {
        Firebase refRoot = new Firebase(getResources().getString(R.string.recipes));
        Firebase refRecipe = refRoot.child(s);

        refRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String title = (String) dataSnapshot.child("name").getValue();
                    long id = (long) dataSnapshot.child("idServer").getValue();
                    Log.d(TAG,title);
                    recipeHolder.name.setText(title);
                    recipeHolder.id = id;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
