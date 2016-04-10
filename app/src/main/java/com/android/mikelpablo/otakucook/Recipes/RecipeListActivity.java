package com.android.mikelpablo.otakucook.Recipes;



import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.mikelpablo.otakucook.Main.MainActivity;
import com.android.mikelpablo.otakucook.Main.MainFragment;
import com.android.mikelpablo.otakucook.R;
import com.firebase.client.AuthData;
import com.google.android.gms.plus.Plus;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

       RecipeListFragment recipesListFragment = new RecipeListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flRecipeList,recipesListFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* If a user is currently authenticated, display a logout menu */
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
    /**
     * Unauthenticate from Firebase and from providers where necessary.
     */
    private void logout() {
        if (MainActivity.mAuthData != null) {
            /* logout of Firebase */
            MainActivity.mFirebaseRef.unauth();
            /* Logout of any of the Frameworks. This step is optional, but ensures the user is not logged into
             * Facebook/Google+ after logging out of Firebase. */
            if (MainActivity.mAuthData.getProvider().equals("google")) {
                /* Logout from Google+ */
                if (MainActivity.mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(MainActivity.mGoogleApiClient);
                    MainActivity.mGoogleApiClient.disconnect();
                }
            }
            /* Update authenticated user and show login buttons */
            setAuthenticatedUser(null);
        }
    }
    private void setAuthenticatedUser(AuthData authData) {
        if (authData == null) {
            Intent intent = new Intent(RecipeListActivity.this, MainActivity.class);
            RecipeListActivity.this.startActivity(intent);
            finish();
        }
    }
}
