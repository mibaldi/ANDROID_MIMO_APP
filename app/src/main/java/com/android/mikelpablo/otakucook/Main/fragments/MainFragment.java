package com.android.mikelpablo.otakucook.Main.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Ingredients.activities.CategoriesActivity;
import com.android.mikelpablo.otakucook.Login.activities.LoginActivity;
import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Measure;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeActivity;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pabji on 04/04/2016.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.mainLinearLayout)
    LinearLayout mainLayout;
    @Bind(R.id.mainImage)
    ImageView mainImage;
    @Bind(R.id.mainRecipeName)
    TextView mainRecipeName;
    @Bind(R.id.mainRecipeAuthor)
    TextView mainRecipeAuthor;
    @Bind(R.id.random)
    Button randomButton;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    private Long id;
    private ProgressDialog mProgressDialog;
    private String previousRecipe;
    private String currentRecipe;

    public MainFragment() {

    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        MainActivity.main_title.setVisibility(View.VISIBLE);
        randomButton.setOnClickListener(this);

        if (LoginActivity.mAuthData != null){
            mProgressDialog = new ProgressDialog(getContext());
            randomRecipe();
        }

    }

    private void randomRecipe() {
        Firebase ref = new Firebase(getResources().getString(R.string.users));
        ref.child(LoginActivity.mAuthData.getUid()).child("favorites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numberChilds = (int) dataSnapshot.getChildrenCount();
                if(numberChilds != 0) {
                    if(numberChilds > 1) {
                        randomButton.setVisibility(View.VISIBLE);
                    }
                    mainLayout.setOnClickListener(MainFragment.this);
                    int x = (int) (Math.random() * numberChilds);
                    int count = 0;

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (x == count) {
                            currentRecipe = child.getKey();
                            if (currentRecipe.equals(previousRecipe)){
                                randomRecipe();
                            }else {
                                printRecipe(child.getKey());
                            }
                            break;
                        }
                        count++;
                    }
                }else{
                    Picasso.with(getContext()).load(R.drawable.default_recipe)
                            .fit()
                            .into(mainImage);
                    mainRecipeName.setText("Sin favoritos");

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void printRecipe(String key) {
        previousRecipe = key;
        Firebase ref = new Firebase(getActivity().getResources().getString(R.string.recipes));
        ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String photo = dataSnapshot.child("photo").getValue(String.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                String author = dataSnapshot.child("author").getValue(String.class);
                int score = dataSnapshot.child("score").getValue(Integer.class);
                id = dataSnapshot.child("id").getValue(Long.class);
                Log.d("IMAGEN",photo);
                Picasso.with(getContext()).load(photo)
                        .fit()
                        .into(mainImage);
                mainRecipeName.setText(name);
                mainRecipeAuthor.setText(String.format(getActivity().getString(R.string.por), author));
                ratingBar.setRating(score);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.random){
            randomRecipe();
        }else {
            MyAPI service = MyApiClient.createService(MyAPI.class);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(getActivity().getString(R.string.progressDialogMessage));
            mProgressDialog.show();
            Call<Recipe> recipe = service.getRecipe(id);
            recipe.enqueue(getCallback());
        }
    }

    private Callback<Recipe> getCallback() {
        return new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                Recipe recipe = response.body();
                for (Measure measure : recipe.measureIngredients) {
                    recipe.ingredients.add(measure.ingredient);
                }
                mProgressDialog.dismiss();
                Context context = getContext();
                Intent intent = new Intent(context, RecipeActivity.class);
                intent.putExtra("recipe", recipe);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {

            }
        };
    }
}
