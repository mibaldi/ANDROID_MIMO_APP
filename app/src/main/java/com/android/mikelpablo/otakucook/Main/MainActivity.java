package com.android.mikelpablo.otakucook.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Ingredients.fragments.IngredientListFragment;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeFragment;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeListFragment;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
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
import java.util.prefs.Preferences;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DrawerMenu.Listener {

    private static final String TAG = MainActivity.class.getSimpleName();
    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;
    /* A reference to the Firebase */
    public static Firebase mFirebaseRef;
    /* Data from the authenticated user */
    public static AuthData mAuthData;
    private Firebase.AuthStateListener mAuthStateListener;
    public static final int RC_GOOGLE_LOGIN = 1;
    public static GoogleApiClient mGoogleApiClient;
    /* A flag indicating that a PendingIntent is in progress and prevents us from starting further intents. */
    private boolean mGoogleIntentInProgress;
    private boolean mGoogleLoginClicked;
    private ConnectionResult mGoogleConnectionResult;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.drawer_menu)
    DrawerMenu navigationDrawer;
    @Bind(R.id.login_with_google)
    SignInButton mGoogleLoginButton;
    @Bind(R.id.login_status)
    TextView mLoggedInStatusTextView;
    @Bind(R.id.user_image)
    ImageView userImage;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mAuthProgressDialog = new ProgressDialog(MainActivity.this);


        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, 0);
            }
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));
        if (mAuthData == null){
            generateAuthData();
        }
    }

    private String generateLoginText() {
        String name = null;
        if (mAuthData.getProvider().equals("google")) {
            name = (String) mAuthData.getProviderData().get("displayName");
        }
        String text = "Logged in as " + name + " (" + mAuthData.getProvider() + ")";
        return text;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ( mAuthProgressDialog != null){
            mAuthProgressDialog.dismiss();
        }
        saveNavigationDrawerItem();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("saveInstanceState", Context.MODE_PRIVATE);
        onItemClick(prefs.getInt("selectedItem", R.id.main));
        this.mAuthData = mFirebaseRef.getAuth();
        if (this.mAuthData != null) {
            mGoogleLoginButton.setVisibility(View.GONE);
            mLoggedInStatusTextView.setVisibility(View.VISIBLE);
            Picasso.with(MainActivity.this).load(mAuthData.getProviderData().get("profileImageURL").toString()).into(userImage);
            mLoggedInStatusTextView.setText(generateLoginText());

        } else {
            generateAuthData();
        }
        Log.i("MainActivity", "onStart");
    }

    private void generateAuthData() {
        if (!mGoogleApiClient.isConnecting()) {
            connectToApiClient();
        }
        mGoogleLoginButton.setVisibility(View.VISIBLE);
        mLoggedInStatusTextView.setVisibility(View.GONE);


        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mAuthProgressDialog.hide();
                setAuthenticatedUser(authData);
            }
        };
        mFirebaseRef.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // if user logged in with Facebook, stop tracking their token
        Log.i("MainActivity", "onDestroy");

        // if changing configurations, stop tracking firebase session.
        //mFirebaseRef.removeAuthStateListener(mAuthStateListener);
    }

    private void saveNavigationDrawerItem() {
        SharedPreferences prefs =
                getSharedPreferences("saveInstanceState", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("selectedItem", navigationDrawer.getSelectedItemId());
        editor.commit();
    }

    /**
     * This method fires when any startActivityForResult finishes. The requestCode maps to
     * the value passed into startActivityForResult.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Map<String, String> options = new HashMap<String, String>();
        if (requestCode == RC_GOOGLE_LOGIN) {
            /* This was a request by the Google API */
            if (resultCode != RESULT_OK) {

            }
            mGoogleIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* If a user is currently authenticated, display a logout menu */
        if (this.mAuthData != null) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Unauthenticate from Firebase and from providers where necessary.
     */
    private void logout() {
        if (this.mAuthData != null) {
            /* logout of Firebase */
            mFirebaseRef.unauth();
            if (this.mAuthData.getProvider().equals("google")) {
                /* Logout from Google+ */
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                    mGoogleApiClient.disconnect();
                }

            }
            /* Update authenticated user and show login buttons */
            setAuthenticatedUser(null);
            //finish();
        }
    }
    private void setAuthenticatedUser(final AuthData authData) {
        if (authData != null) {
            /* Hide all the login buttons */

            mGoogleLoginButton.setVisibility(View.GONE);
            mLoggedInStatusTextView.setVisibility(View.VISIBLE);
            /* show a provider specific status text */
            String name = null;
            if (authData.getProvider().equals("google")) {
                name = (String) authData.getProviderData().get("displayName");
            }
            if (name != null) {
                final String finalName = name;
                mFirebaseRef.child("Users").child(authData.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.d(TAG,"No existe");
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("provider", authData.getProvider());
                            map.put("name", finalName);
                            map.put("uid", authData.getUid());
                            mFirebaseRef.child("Users").child(authData.getUid()).setValue(map);
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
                Picasso.with(MainActivity.this).load(authData.getProviderData().get("profileImageURL").toString()).into(userImage);
                mLoggedInStatusTextView.setText("Logged in as " + name + " (" + authData.getProvider() + ")");
            }
        } else {
            /* No authenticated user show all the login buttons */

            mGoogleLoginButton.setVisibility(View.VISIBLE);
            mLoggedInStatusTextView.setVisibility(View.GONE);
            Picasso.with(MainActivity.this).load("https://www.google.es/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwjumsn3p4rMAhVH1RQKHUvwAL8QjRwIBw&url=http%3A%2F%2Fwww.domestika.org%2Fes%2Fprojects%2F35336-fakebook-avatar-project&psig=AFQjCNHC2L_vKAetzHsOt2NQ0xuo4rUE-w&ust=1460592157859224").into(userImage);
        }
        this.mAuthData = authData;
        /* invalidate options menu to hide/show the logout button */
        supportInvalidateOptionsMenu();
    }

    /**
     * Show errors to users
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onItemClick(int itemId) {
        System.out.println(itemId);
        switch (itemId) {
            case R.id.main:
                System.out.println("Main");
                if (mAuthData != null){
                    selectFragment(new MainFragment(), R.string.main_drawer);
                }
                else
                    Toast.makeText(this,"No estas logueado",Toast.LENGTH_LONG).show();
                break;
            case R.id.shopping_cart:
                System.out.println("Shopping");
                if (mAuthData != null) {
                    IngredientListFragment shoppingFragment = IngredientListFragment.newInstance(R.string.shoping_cart_drawer);
                    selectFragment(shoppingFragment, R.string.shoping_cart_drawer);
                }
                else
                    Toast.makeText(this,"No estas logueado",Toast.LENGTH_LONG).show();
                break;
            case R.id.recipes:
                System.out.println("Recipes");
                RecipeListFragment recipesListFragment = new RecipeListFragment();
                selectFragment(recipesListFragment, R.string.recipes_drawer);
                break;
            case R.id.ingredients:
                System.out.println("Ingredients");
                if (mAuthData != null){
                    IngredientListFragment ingredientsFragment = IngredientListFragment.newInstance(R.string.ingredients_drawer);
                    selectFragment(ingredientsFragment, R.string.ingredients_drawer);
                }
                else
                    Toast.makeText(this,"No estas logueado",Toast.LENGTH_LONG).show();

                break;
        }

        navigationDrawer.setSelectedItemId(itemId);
    }


    private void selectFragment(Fragment fragment, int titleResId) {
        fragment.setRetainInstance(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        System.out.println(titleResId);
        this.setTitle(titleResId);
        //toolbar.setTitle(titleResId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    public void connectToApiClient() {
        mGoogleLoginClicked = false;
        if (mGoogleConnectionResult != null) {
            resolveSignInError();
        } else if (mGoogleApiClient.isConnected()) {
            getGoogleOAuthTokenAndLogin();
        } else {
                    /* connect API now */
            Log.d(TAG, "Trying to connect to Google API");
            mGoogleApiClient.connect();

        }
    }

    /**
     * Utility class for authentication results
     */
    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.hide();
            Log.i(TAG, provider + " auth successful");
            setAuthenticatedUser(authData);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());
        }
    }

    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if (mGoogleConnectionResult.hasResolution()) {
            try {
                mGoogleIntentInProgress = true;
                mGoogleConnectionResult.startResolutionForResult(this, RC_GOOGLE_LOGIN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mGoogleIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void getGoogleOAuthTokenAndLogin() {
        mAuthProgressDialog.show();
        /* Get OAuth token in Background */
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
                    token = GoogleAuthUtil.getToken(MainActivity.this, Plus.AccountApi.getAccountName(mGoogleApiClient), scope);
                } catch (IOException transientEx) {
                    /* Network or server error */
                    Log.e(TAG, "Error authenticating with Google: " + transientEx);
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(TAG, "Recoverable Google OAuth error: " + e.toString());
                    /* We probably need to ask for permissions, so start the intent if there is none pending */
                    if (!mGoogleIntentInProgress) {
                        mGoogleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        startActivityForResult(recover, RC_GOOGLE_LOGIN);
                    }
                } catch (GoogleAuthException authEx) {
                    /* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed. */
                    Log.e(TAG, "Error authenticating with Google: " + authEx.getMessage(), authEx);
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                mGoogleLoginClicked = false;
                if (token != null) {
                    /* Successfully got OAuth token, now login with Google */
                    mFirebaseRef.authWithOAuthToken("google", token, new AuthResultHandler("google"));
                } else if (errorMessage != null) {
                    mAuthProgressDialog.hide();
                    showErrorDialog(errorMessage);
                }
            }
        };
        task.execute();
    }

    @Override
    public void onConnected(final Bundle bundle) {
        /* Connected with Google API, use this to authenticate with Firebase */
        getGoogleOAuthTokenAndLogin();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mGoogleIntentInProgress) {
            /* Store the ConnectionResult so that we can use it later when the user clicks on the Google+ login button */
            mGoogleConnectionResult = result;

            if (mGoogleLoginClicked) {
                /* The user has already clicked login so we attempt to resolve all errors until the user is signed in,
                 * or they cancel. */
                resolveSignInError();
            } else {
                resolveSignInError();
                Log.e(TAG, result.toString());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // ignore
    }
}
