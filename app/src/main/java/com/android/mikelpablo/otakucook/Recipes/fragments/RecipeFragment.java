package com.android.mikelpablo.otakucook.Recipes.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.BuildConfig;
import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Measure;
import com.android.mikelpablo.otakucook.Models.MeasureFB;
import com.android.mikelpablo.otakucook.Models.OwnIngredientFB;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.Models.RecipeFB;
import com.android.mikelpablo.otakucook.Models.Task;
import com.android.mikelpablo.otakucook.Models.TaskFB;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeTaskListActivity;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeTaskViewPageActivity;
import com.android.mikelpablo.otakucook.Recipes.adapters.RecipeAdapter;
import com.android.mikelpablo.otakucook.Utils.DividerItemDecoration;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class RecipeFragment extends Fragment implements View.OnClickListener, RecipeAdapter.OnItemClickListener {
    @Bind(R.id.recipeName)
    TextView recipeName;
    @Bind(R.id.recipePhoto)
    ImageView recipePhoto;
    @Bind(R.id.bt_cook)
    Button btCook;
    @Bind(R.id.bt_tasks)
    Button bt_tasks;
    @Bind(R.id.fab)
    FloatingActionButton ib_favorite;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @Bind(R.id.recipeIngredientsListRecyclerView)
    RecyclerView recyclerView;
    private static final String TAG = RecipeFragment.class.getName();
    Boolean favorito = false;
    public List<Ingredient> items = new ArrayList<>();
    /* A reference to the Firebase */
    public Firebase mFirebaseRef;
    private Recipe recipe;
    private ArrayList<String> lista = new ArrayList<>();
    private RecipeAdapter adapter;

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
        recipe = getArguments().getParcelable("recipe");

        recipeExistsReturn();
        storageIngredients();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        if(getView()!=null) {
            if (!BuildConfig.SHOW_PREMIUM_ACTIONS){
                ib_favorite.setVisibility(View.GONE);
            }
            setSizeAppBarLayout();
            settingArguments();
            configureView();
            populateFragment();
            setHasOptionsMenu(true);
        }

    }
    public void setSizeAppBarLayout(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            params.width = params.MATCH_PARENT;
            appBarLayout.setLayoutParams(params);
        }
    }

    private void settingArguments() {
        if (getArguments() != null) {
          recipe = getArguments().getParcelable("recipe");
        }
    }

    private void configureView() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //collapsingToolbarLayout.setTitle("");
        getActivity().setTitle("");
        recipeName.setText(recipe.name);

        Picasso.with(getContext()).load(recipe.photo).into(recipePhoto);
        Log.d(TAG,String.valueOf(recipe.score));
        if (favorito){
            ib_favorite.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_menu_delete));
        }else{
            ib_favorite.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_menu_add));
        }
        //ib_favorite.setEnabled(false);
        ratingBar.setRating(recipe.score);
        items = recipe.ingredients;
        btCook.setOnClickListener(this);
        ib_favorite.setOnClickListener(this);
        bt_tasks.setOnClickListener(this);
        adapter = new RecipeAdapter(getContext(), items,this,lista);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
    }

    private void populateFragment() {

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {
            Log.i(TAG, "fragment creado");

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_cook:{
                Intent intent = new Intent(getContext(),RecipeTaskViewPageActivity.class);
                intent.putExtra("recipe",getArguments().getParcelable("recipe"));
                getContext().startActivity(intent);
                break;
            }
            case R.id.bt_tasks:{
                Intent intent = new Intent(getContext(),RecipeTaskListActivity.class);
                intent.putExtra("recipe",getArguments().getParcelable("recipe"));
                getContext().startActivity(intent);
                break;
            }
            case R.id.fab:{
                if (favorito){
                    removeFavoriteFirebase(recipe);
                    Toast.makeText(getContext(),"Receta eliminada de favoritos", Toast.LENGTH_SHORT).show();
                    ib_favorite.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_menu_add));
                }else {

                    sendIngredientFirebase(recipe);
                    ib_favorite.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_menu_delete));
                }
                favorito = !favorito;

                break;
            }
        }
    }
    private void removeFavoriteFirebase(Recipe recipe){
        Firebase refUser = new Firebase(getResources().getString(R.string.users));
        mFirebaseRef = refUser.child(MainActivity.mAuthData.getUid()).child("favorites");
        mFirebaseRef.child(String.valueOf(recipe.id)).removeValue();
    }
    private void sendIngredientFirebase(Recipe recipe) {
        Firebase refUser = new Firebase(getResources().getString(R.string.users));
        Firebase refRecipes = new Firebase(getResources().getString(R.string.recipes));
        Firebase refIngredients = new Firebase(getResources().getString(R.string.ingredients));

        RecipeFB recipefb = new RecipeFB(recipe);
        refRecipes.child(String.valueOf(recipe.id)).setValue(recipefb);
        for (Ingredient ingredient:recipe.ingredients){
            refIngredients.child(String.valueOf(ingredient.id)).setValue(ingredient);
        }
        mFirebaseRef = refUser.child(MainActivity.mAuthData.getUid()).child("favorites");
        mFirebaseRef.child(String.valueOf(recipe.id)).setValue(recipe.id);

        Toast.makeText(getContext(),"Receta guardada en favoritos", Toast.LENGTH_SHORT).show();

    }
    public void recipeExistsReturn(){

        Firebase userRef= new Firebase(getResources().getString(R.string.users));
        userRef = userRef.child(MainActivity.mAuthData.getUid()).child("favorites");
        userRef.child(String.valueOf(recipe.id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    favorito=true;

                   // ib_favorite.setEnabled(!favorito);
                } else {
                    favorito=false;
                   // ib_favorite.setEnabled(!favorito);
                   // sendIngredientFirebase(recipe);
                }
            }
            @Override
            public void onCancelled(FirebaseError arg0) {
            }
        });
    }
    public void storageIngredients(){
        lista.clear();
        Firebase ref = new Firebase(getResources().getString(R.string.users));
        Firebase storageRef =ref.child(MainActivity.mAuthData.getUid()).child("owningredient");
        storageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    OwnIngredientFB ownIngredientFB = postSnapshot.getValue(OwnIngredientFB.class);
                    if (ownIngredientFB.storage.equals("1")){

                        lista.add(ownIngredientFB.id);
                        Log.d("PABLO",ownIngredientFB.id);
                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onItemClick(View view, Ingredient item) {
        Firebase ref = new Firebase(getResources().getString(R.string.users));
        Firebase storageRef =ref.child(MainActivity.mAuthData.getUid()).child("owningredient");
        OwnIngredientFB ownIngredientFB = new OwnIngredientFB(String.valueOf(item.id),"1","0");
        storageRef.child(String.valueOf(item.id)).setValue(ownIngredientFB);
        Firebase refIngredient = new Firebase(getResources().getString(R.string.ingredients));
        refIngredient.child(String.valueOf(item.id)).setValue(item);
        //view.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
       // view.setBackgroundColor(Color.YELLOW);
        Snackbar.make(getView(), "Ingrediente en el carrito", Snackbar.LENGTH_LONG).show();
        Log.d("PABLO",""+ item);
    }
}
