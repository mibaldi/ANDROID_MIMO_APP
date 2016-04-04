package com.android.mikelpablo.otakucook.Recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.mikelpablo.otakucook.MainActivity;
import com.android.mikelpablo.otakucook.R;
import com.firebase.client.AuthData;
import com.google.android.gms.plus.Plus;

import java.util.HashMap;
import java.util.Map;

public class RecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            Intent intent = new Intent(RecipesActivity.this, MainActivity.class);
            RecipesActivity.this.startActivity(intent);
            finish();
        }
    }

}
