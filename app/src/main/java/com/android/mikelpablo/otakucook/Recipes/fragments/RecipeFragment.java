package com.android.mikelpablo.otakucook.Recipes.fragments;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Main.MainActivity;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.adapters.RecipeIngredientsListAdapter;
import com.android.mikelpablo.otakucook.Recipes.adapters.RecipesListAdapter;
import com.android.mikelpablo.otakucook.Recipes.holders.RecipeHolder;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

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
public class RecipeFragment extends Fragment {
    @Bind(R.id.recipeName)
    TextView recipeName;
    @Bind(R.id.recipePhoto)
    ImageView recipePhoto;
    @Bind(R.id.bt_cook)
    Button btCook;
    @Bind(R.id.bt_tasks)
    Button bt_tasks;
    @Bind(R.id.ib_favorite)
    ImageButton ib_favorite;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.recipeIngredientsListRecyclerView)
    RecyclerView recyclerView;
    private static final String TAG = RecipeFragment.class.getName();
   // public List<Recipe> items = new ArrayList<>();
    /* A reference to the Firebase */
    public Firebase mFirebaseRef;

    public RecipeFragment() {
    }

    public static RecipeFragment newInstance(Recipe recipe) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putParcelable("recipe", recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
       /* final RecipesListAdapter adapter = new RecipesListAdapter(getContext(), items);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        btTodas.setOnClickListener(this);
        btFavoritas.setOnClickListener(this);
        btPosibles.setOnClickListener(this);

        String nombre = getArguments().getString("nombreReceta");
        nombreReceta.setText(nombre);*/
        Recipe recipe = getArguments().getParcelable("recipe");
        recipeName.setText(recipe.name);
        Picasso.with(getContext()).load(recipe.photo).into(recipePhoto);
        Log.d(TAG,String.valueOf(recipe.score));
        ratingBar.setRating(recipe.score);

        final RecipeIngredientsListAdapter adapter = new RecipeIngredientsListAdapter(getContext(), recipe.ingredients);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {
            Log.i(TAG, "fragment creado");

        }

    }

   /* private void ServerRecipeList(Call<List<Recipe>> recipes) {
        recipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                allRecipes(response);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

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
    }*/
}
