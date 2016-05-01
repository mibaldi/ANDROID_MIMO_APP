package com.android.mikelpablo.otakucook.Main.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Login.activities.LoginActivity;
import com.android.mikelpablo.otakucook.Main.fragments.DialogExitApp;
import com.android.mikelpablo.otakucook.Main.fragments.IngredientListFragment;
import com.android.mikelpablo.otakucook.Main.fragments.MainFragment;
import com.android.mikelpablo.otakucook.Preferences.PreferencesActivity;
import com.android.mikelpablo.otakucook.Preferences.PreferencesManager;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Main.fragments.RecipeListFragment;
import com.android.mikelpablo.otakucook.Utils.ThemeType;
import com.android.mikelpablo.otakucook.Utils.ThemeUtils;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private static int itemIdPersist = R.id.item1;
    private NavigationView navigationDrawer;
    private TextView mLoggedInStatusTextView;
    private ImageView userImage;
    private com.github.clans.fab.FloatingActionButton mBtAddCategoryIngredients;
    private FloatingActionMenu mMnAddCategoryIngredients;
    private AuthData authdata;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("navigationDrawerSelectedItemId", itemIdPersist);
        Log.d("MIKEL", "instancia guardada" + String.valueOf(itemIdPersist));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        itemIdPersist = savedInstanceState.getInt("navigationDrawerSelectedItemId");
        navigationDrawer.setCheckedItem(itemIdPersist);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (LoginActivity.mAuthData != null) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mMnAddCategoryIngredients.setVisibility(View.GONE);

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.item1:
                itemIdPersist = R.id.item1;
                selectFragment(new MainFragment());
                return true;
            case R.id.item2:
                itemIdPersist = R.id.item2;
                mMnAddCategoryIngredients.setVisibility(View.VISIBLE);
                selectFragment(IngredientListFragment.newInstance(R.string.shoping_cart_drawer));
                return true;
            case R.id.item3:
                itemIdPersist = R.id.item3;
                selectFragment(new RecipeListFragment());
                return true;
            case R.id.item4:
                itemIdPersist = R.id.item4;
                mMnAddCategoryIngredients.setVisibility(View.VISIBLE);
                selectFragment(IngredientListFragment.newInstance(R.string.ingredients_drawer));
                return true;
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authdata = LoginActivity.mAuthData;
        navigationDrawer = (NavigationView) findViewById(R.id.navigation_view);
        mBtAddCategoryIngredients = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.add_category_ingredient);
        mMnAddCategoryIngredients= (FloatingActionMenu) findViewById(R.id.menu_red);
        View headerLayout = navigationDrawer.getHeaderView(0);

        mLoggedInStatusTextView =(TextView) headerLayout.findViewById(R.id.login_status);
        userImage = (ImageView) headerLayout.findViewById(R.id.user_image);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(new DrawerArrowDrawable(toolbar.getContext()));
        }

        navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });

        if (savedInstanceState == null ) {
            navigationDrawer.setCheckedItem(itemIdPersist);
            Log.d("MIKEL", "saveInstanceState = null");
            selectFragment(new MainFragment());
        }else{
            jumpSaveInstance();
        }

    }

    private void jumpSaveInstance() {
        switch (itemIdPersist) {
            case R.id.item1:
                selectFragment(new MainFragment());
                break;
            case R.id.item2:
                mMnAddCategoryIngredients.setVisibility(View.VISIBLE);
                selectFragment(IngredientListFragment.newInstance(R.string.shoping_cart_drawer));
                break;
            case R.id.item3:
                selectFragment(new RecipeListFragment());
                break;
            case R.id.item4:
                mMnAddCategoryIngredients.setVisibility(View.VISIBLE);
                selectFragment(IngredientListFragment.newInstance(R.string.ingredients_drawer));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        applySelectedTheme();

        if (authdata != null) {

            mLoggedInStatusTextView.setVisibility(View.VISIBLE);
            Picasso.with(MainActivity.this).load(this.authdata.getProviderData().get("profileImageURL").toString()).into(userImage);
            mLoggedInStatusTextView.setText(LoginActivity.mLoggedInStatusString);

        }
        Log.d("MIKEL", "onStart");
    }

    private void selectFragment(Fragment fragment) {
        drawerLayout.closeDrawer(GravityCompat.START);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void logout() {
        if (authdata != null) {
            LoginActivity.mFirebaseRef.unauth();
            GoogleApiClient mGoogleApiClient = LoginActivity.mGoogleApiClient;
            if (authdata.getProvider().equals("google")) {
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                    mGoogleApiClient.disconnect();
                    //Ir a ventana principal
                }
            }
            LoginActivity.mAuthData = null;

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            DialogExitApp.newInstance(1).show(fm, "dialog");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void applySelectedTheme() {
        ThemeType theme = PreferencesManager.getInstance().getSelectedTheme();
        ThemeUtils.applyThemeIntoStatusBar(this, theme);
        ThemeUtils.applyThemeIntoToolbar(this, theme, toolbar);
    }


}
