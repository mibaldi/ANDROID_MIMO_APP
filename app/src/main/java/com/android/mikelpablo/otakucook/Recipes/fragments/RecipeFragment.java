package com.android.mikelpablo.otakucook.Recipes.fragments;

import android.content.Intent;
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

import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Measure;
import com.android.mikelpablo.otakucook.Models.MeasureFB;
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
public class RecipeFragment extends Fragment implements View.OnClickListener {
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
    Boolean favorito = false;
    public List<Ingredient> items = new ArrayList<>();
    /* A reference to the Firebase */
    public Firebase mFirebaseRef;
    private Recipe recipe;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        recipeName.setText(recipe.name);
        Picasso.with(getContext()).load(recipe.photo).into(recipePhoto);
        Log.d(TAG,String.valueOf(recipe.score));
        ib_favorite.setEnabled(false);
        ratingBar.setRating(recipe.score);
        items = recipe.ingredients;
        btCook.setOnClickListener(this);
        ib_favorite.setOnClickListener(this);
        bt_tasks.setOnClickListener(this);
        final RecipeAdapter adapter = new RecipeAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
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
            case R.id.ib_favorite:{
                sendIngredientFirebase(recipe);
                break;
            }
        }
    }
    private void sendIngredientFirebase(Recipe recipe) {
        Firebase refUser = new Firebase(getResources().getString(R.string.users));
        Firebase refRecipes = new Firebase(getResources().getString(R.string.recipes));
        Firebase refMeasures = new Firebase(getResources().getString(R.string.measures));
        Firebase refTasks = new Firebase(getResources().getString(R.string.tasks));
        Firebase refIngredients = new Firebase(getResources().getString(R.string.ingredients));

        RecipeFB recipefb = new RecipeFB(recipe);
        refRecipes.child(String.valueOf(recipe.id)).setValue(recipefb);
        for (Task task:recipe.tasks){
            TaskFB taskfb = new TaskFB(task,recipefb.id);
            refTasks.child(String.valueOf(taskfb.id)).setValue(taskfb);
        }
        for (Measure measure:recipe.measureIngredients){
            MeasureFB measurefb = new MeasureFB(measure,recipefb.id);
            refMeasures.child(String.valueOf(measurefb.id)).setValue(measurefb);
        }
        for (Ingredient ingredient:recipe.ingredients){
            refIngredients.child(String.valueOf(ingredient.id)).setValue(ingredient);
        }
        mFirebaseRef = refUser.child(MainActivity.mAuthData.getUid()).child("favorites");
        mFirebaseRef.child(String.valueOf(recipe.id)).setValue(recipe.id);

        Toast.makeText(getContext(),"Receta guardada en favoritos", Toast.LENGTH_SHORT).show();
        ib_favorite.setEnabled(false);
    }
    /*public void recipeExists(){
        Firebase userRef= new Firebase(getResources().getString(R.string.users));
        userRef = userRef.child(MainActivity.mAuthData.getUid()).child("favorites");
        userRef.child(String.valueOf(recipe.id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {

                } else {
                    sendIngredientFirebase(recipe);
                }
            }
            @Override
            public void onCancelled(FirebaseError arg0) {
            }
        });
    }*/
    public void recipeExistsReturn(){

        Firebase userRef= new Firebase(getResources().getString(R.string.users));
        userRef = userRef.child(MainActivity.mAuthData.getUid()).child("favorites");
        userRef.child(String.valueOf(recipe.id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    favorito=true;
                    ib_favorite.setEnabled(!favorito);
                } else {
                    favorito=false;
                    ib_favorite.setEnabled(!favorito);
                   // sendIngredientFirebase(recipe);
                }
            }
            @Override
            public void onCancelled(FirebaseError arg0) {
            }
        });
    }

}
