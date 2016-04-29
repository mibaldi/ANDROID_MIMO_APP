package com.android.mikelpablo.otakucook.Main.fragments;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Ingredients.activities.CategoriesActivity;
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

    @Bind(R.id.mainLayout)
    LinearLayout mainLayout;
    @Bind(R.id.mainLinearLayout)
    LinearLayout textLayout;
    @Bind(R.id.mainImage)
    ImageView mainImage;
    @Bind(R.id.mainRecipeName)
    TextView mainRecipeName;
    @Bind(R.id.mainRecipeAuthor)
    TextView mainRecipeAuthor;
    private Long id;

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

        mainLayout.setOnClickListener(this);

        Firebase ref = new Firebase(getResources().getString(R.string.users));
        ref.child(MainActivity.mAuthData.getUid()).child("favorites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numberChilds = (int) dataSnapshot.getChildrenCount();
                int x = (int) (Math.random() * numberChilds);
                int count = 0;

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (x == count) {
                        printRecipe(child.getKey());
                        break;
                    }
                    count++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void printRecipe(String key) {
        Firebase ref = new Firebase(getResources().getString(R.string.recipes));
        ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String photo = dataSnapshot.child("photo").getValue(String.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                String author = dataSnapshot.child("author").getValue(String.class);
                id = dataSnapshot.child("id").getValue(Long.class);
                Picasso.with(getContext()).load(photo)
                        .fit()
                        .centerCrop()
                        .into(mainImage);
                mainRecipeName.setText(name);
                mainRecipeAuthor.setText("by " + author);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        MyAPI service = MyApiClient.createService(MyAPI.class);
        Call<Recipe> recipe = service.getRecipe(id);
        recipe.enqueue(getCallback());
    }

    private Callback<Recipe> getCallback() {
        return new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                Recipe recipe = response.body();
                for (Measure measure : recipe.measureIngredients) {
                    recipe.ingredients.add(measure.ingredient);
                }
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
