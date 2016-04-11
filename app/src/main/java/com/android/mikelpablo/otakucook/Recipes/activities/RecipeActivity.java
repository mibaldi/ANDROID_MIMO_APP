package com.android.mikelpablo.otakucook.Recipes.activities;



import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeFragment;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeListFragment;

public class RecipeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Intent intent = getIntent();
        Recipe recipe= intent.getParcelableExtra("recipe");
        Log.d("RecipeActivity",recipe.author);
        RecipeFragment recipesFragment = RecipeFragment.newInstance(recipe);
        getSupportFragmentManager().beginTransaction().replace(R.id.flRecipe,recipesFragment).commit();
       /*RecipeFragment recipesFragment = new RecipeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flRecipe,recipesFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();*/
    }
   /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (MainActivity.mAuthData != null) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        if (MainActivity.mAuthData != null) {

            MainActivity.mFirebaseRef.unauth();

            if (MainActivity.mAuthData.getProvider().equals("google")) {

                if (MainActivity.mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(MainActivity.mGoogleApiClient);
                    MainActivity.mGoogleApiClient.disconnect();
                }
            }

            setAuthenticatedUser(null);
        }
    }
    private void setAuthenticatedUser(AuthData authData) {
        if (authData == null) {
            Intent intent = new Intent(RecipeActivity.this, MainActivity.class);
            RecipeActivity.this.startActivity(intent);
            finish();
        }
    }*/
}
