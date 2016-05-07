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
import android.support.v4.content.ContextCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.BuildConfig;
import com.android.mikelpablo.otakucook.Login.activities.LoginActivity;
import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Measure;
import com.android.mikelpablo.otakucook.Models.MeasureFB;
import com.android.mikelpablo.otakucook.Models.OwnIngredientFB;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.Models.RecipeFB;
import com.android.mikelpablo.otakucook.Models.Task;
import com.android.mikelpablo.otakucook.Models.TaskFB;
import com.android.mikelpablo.otakucook.Preferences.PreferencesManager;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeTaskListActivity;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeTaskViewPageActivity;
import com.android.mikelpablo.otakucook.Recipes.adapters.RecipeAdapter;
import com.android.mikelpablo.otakucook.Utils.DividerItemDecoration;
import com.android.mikelpablo.otakucook.Utils.ThemeType;
import com.android.mikelpablo.otakucook.Utils.ThemeUtils;
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
    @Bind(R.id.buttonsLayout)
    RelativeLayout buttonsLayout;
    private static final String TAG = RecipeFragment.class.getName();
    Boolean favorito = false;
    public List<Ingredient> items = new ArrayList<>();
    public Firebase mFirebaseRef;
    private Recipe recipe;
    private ArrayList<String> lista = new ArrayList<>();
    private RecipeAdapter adapter;
    private List<IngredientType> itemsType = new ArrayList<>();
    private ArrayList<String> listaShoppingCart = new ArrayList<>();
    private ArrayList<String> listaHistorical= new ArrayList<>();

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
            applySelectedTheme();
            setSizeAppBarLayout();
            settingArguments();
            configureView();
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
        getActivity().setTitle("");
        recipeName.setText(recipe.name);
        Picasso.with(getContext()).load(recipe.photo).into(recipePhoto);
        Log.d(TAG,String.valueOf(recipe.score));
        ratingBar.setRating(recipe.score);
        items = recipe.ingredients;
        for (Measure measure:recipe.measureIngredients){
            IngredientType ingredientType= IngredientType.convert(measure.ingredient,measure.measure,measure.quantity);
            itemsType.add(ingredientType);
        }

        btCook.setOnClickListener(this);
        ib_favorite.setOnClickListener(this);
        bt_tasks.setOnClickListener(this);
        adapter = new RecipeAdapter(itemsType,this,lista,listaShoppingCart,listaHistorical);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
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
                    Toast.makeText(getContext(), R.string.toast_delete_favorite, Toast.LENGTH_SHORT).show();
                    ib_favorite.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.favoritos));
                }else {

                    sendIngredientFirebase(recipe);
                    ib_favorite.setImageDrawable(ContextCompat.getDrawable(getActivity(),android.R.drawable.ic_menu_delete));
                }
                favorito = !favorito;

                break;
            }
        }
    }
    private void removeFavoriteFirebase(Recipe recipe){
        Firebase refUser = new Firebase(getResources().getString(R.string.users));
        mFirebaseRef = refUser.child(LoginActivity.mAuthData.getUid()).child("favorites");
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
        mFirebaseRef = refUser.child(LoginActivity.mAuthData.getUid()).child("favorites");
        mFirebaseRef.child(String.valueOf(recipe.id)).setValue(recipe.id);

        Toast.makeText(getContext(), R.string.toast_save_favorite, Toast.LENGTH_SHORT).show();

    }
    public void recipeExistsReturn(){

        Firebase userRef= new Firebase(getResources().getString(R.string.users));
        userRef = userRef.child(LoginActivity.mAuthData.getUid()).child("favorites");
        userRef.child(String.valueOf(recipe.id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    favorito=true;
                } else {
                    favorito=false;
                }
                if (favorito){
                    ib_favorite.setImageDrawable(ContextCompat.getDrawable(getActivity(),android.R.drawable.ic_menu_delete));
                }else{
                    ib_favorite.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.favoritos));
                }
            }
            @Override
            public void onCancelled(FirebaseError arg0) {
            }
        });
    }
    public void storageIngredients(){
        lista.clear();
        listaHistorical.clear();
        listaShoppingCart.clear();
        Firebase ref = new Firebase(getResources().getString(R.string.users));
        Firebase storageRef =ref.child(LoginActivity.mAuthData.getUid()).child("owningredient");
        storageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    OwnIngredientFB ownIngredientFB = postSnapshot.getValue(OwnIngredientFB.class);
                    if (ownIngredientFB.storage.equals("1")){

                        lista.add(ownIngredientFB.id);
                    }else if (ownIngredientFB.shoppingcart.equals("1")) {
                        listaShoppingCart.add(ownIngredientFB.id);
                    }else if (ownIngredientFB.storage.equals("0") && ownIngredientFB.shoppingcart.equals("0")) {
                        listaHistorical.add(ownIngredientFB.id);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    private void applySelectedTheme() {
        ThemeType theme = PreferencesManager.getInstance().getSelectedTheme();
        ThemeUtils.applyThemeIntoStatusBar(getActivity(), theme);
       ThemeUtils.applyThemeIntoCollapsing(getActivity(),theme,collapsingToolbarLayout);
        ThemeUtils.applyThemeIntoRelative(getActivity(),theme,buttonsLayout);
        ThemeUtils.applyThemeIntoFloatingActionButton(getActivity(),theme,ib_favorite);
    }

    @Override
    public void onItemClick(View view, IngredientType item,int position) {
        Firebase ref = new Firebase(getResources().getString(R.string.users));
        Firebase storageRef =ref.child(LoginActivity.mAuthData.getUid()).child("owningredient");
        OwnIngredientFB ownIngredientFB = new OwnIngredientFB(String.valueOf(item.id),"1","0");
        storageRef.child(String.valueOf(item.id)).setValue(ownIngredientFB);
        Firebase refIngredient = new Firebase(getResources().getString(R.string.ingredients));
        refIngredient.child(String.valueOf(item.id)).setValue(item);
        adapter.notifyItemChanged(position);
        adapter.notifyDataSetChanged();
        Snackbar.make(getView(), R.string.ingredient_in_cart, Snackbar.LENGTH_LONG).show();
    }
    public static class IngredientType{
        public enum typeEnum {
            shoppingCart,storage,historical
        }
        public long id;
        public String name;
        public Boolean frozen;
        public String category;
        public String baseType;
        public String measure;
        public float quantity;
        public typeEnum type = typeEnum.historical;

        public IngredientType(long id, String name, Boolean frozen, String category, String baseType, typeEnum type) {
            this.id = id;
            this.name = name;
            this.frozen = frozen;
            this.category = category;
            this.baseType = baseType;
            this.type = type;
        }

        public static IngredientType convert(Ingredient i,String measure,float quantity){
            IngredientType ingredientType = new IngredientType(i.id,i.name,i.frozen,i.category,i.baseType,typeEnum.historical);
            ingredientType.measure = measure;
            ingredientType.quantity = quantity;
            return ingredientType;
        }
        public static IngredientType convert(Ingredient i){
            IngredientType ingredientType = new IngredientType(i.id,i.name,i.frozen,i.category,i.baseType,typeEnum.historical);
            return ingredientType;
        }
    }
}
