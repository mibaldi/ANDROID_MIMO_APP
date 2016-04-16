package com.android.mikelpablo.otakucook.Ingredients.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.Ingredients.adapters.CategoriesCollectionAdapter;
import com.android.mikelpablo.otakucook.Ingredients.adapters.IngredientListAdapter;
import com.android.mikelpablo.otakucook.Main.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsServerFragment extends Fragment {

    private static final String TAG = IngredientsServerFragment.class.getName();

    public List<Ingredient> items = new ArrayList<>();
    public static List<String> ingredientsId = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private String category;
    private Firebase refRoot;
    private static Firebase mRefStorage;

    @Bind(R.id.ingredientsServer)
    RecyclerView recyclerView;

    public IngredientsServerFragment(){

    }

    public static String formatCategory (String category){
        return Normalizer
                .normalize(category, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll(" y ","_");

    }

    public static IngredientsServerFragment newInstance(String category) {
        IngredientsServerFragment fragment = new IngredientsServerFragment();
        Bundle args = new Bundle();
        args.putString("category",formatCategory(category));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingredients_server, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        mProgressDialog = new ProgressDialog(getContext());
        category = getArguments().getString("category");

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        refRoot = new Firebase(getResources().getString(R.string.users));
        if (MainActivity.mAuthData != null){
            mRefStorage = refRoot.child(MainActivity.mAuthData.getUid()).child("storage");
            getIngredientsIdStorage(refRoot);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void getIngredientsIdStorage(Firebase refRoot) {

        ingredientsId.clear();
        mRefStorage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String id = postSnapshot.getValue(String.class);
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
        Log.d(TAG,ingredientsIdString);
        Call<List<Ingredient>> ingredients = service.getCategoryIngredients(category,ingredientsIdString);
        LoadIngredientsServer(ingredients);
    }

    private void LoadIngredientsServer(Call<List<Ingredient>> ingredients) {
        ingredients.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                items = response.body();
                recyclerView.setAdapter(new IngredientListAdapter(items));
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
}
