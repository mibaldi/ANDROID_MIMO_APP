package com.android.mikelpablo.otakucook.Main.activities;

import android.Manifest;
import android.app.AlertDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Main.fragments.IngredientListFragment;
import com.android.mikelpablo.otakucook.Main.fragments.MainFragment;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Main.fragments.RecipeListFragment;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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


    private int titleFragment;
    private static int itemIdPersist = R.id.item1;
    private NavigationView navigationDrawer;
    private SignInButton mGoogleLoginButton;
    private TextView mLoggedInStatusTextView;
    private ImageView userImage;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            finish();
        }
    }

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

        //Log.d("MIKEL", "recuperando instancia" + String.valueOf(navigationDrawer.getMenu()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.mAuthData != null) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.item1:
                selectFragment(new MainFragment());
                return true;
            case R.id.item2:
                selectFragment(IngredientListFragment.newInstance(R.string.shoping_cart_drawer));
                return true;
            case R.id.item3:
                selectFragment(new RecipeListFragment());
                return true;
            case R.id.item4:
                selectFragment(IngredientListFragment.newInstance(R.string.ingredients_drawer));
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    /*@Override
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
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationDrawer = (NavigationView) findViewById(R.id.navigation_view);
        /*View view = View.inflate(this, R.layout.navigation_header, null);
        navigationDrawer.addHeaderView(view);*/
        View headerLayout = navigationDrawer.getHeaderView(0);
        mGoogleLoginButton = (SignInButton) headerLayout.findViewById(R.id.login_with_google);
        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoogleApiClient.isConnecting()){
                    connectToApiClient();
                }
            }
        });
        mLoggedInStatusTextView =(TextView) headerLayout.findViewById(R.id.login_status);
        userImage = (ImageView) headerLayout.findViewById(R.id.user_image);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(new DrawerArrowDrawable(toolbar.getContext()));
        }
        mAuthProgressDialog = new ProgressDialog(MainActivity.this);

        navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });


        if (savedInstanceState == null) {
            makeLogin();
            Log.d("MIKEL", "saveInstanceState = null");

            navigationDrawer.setCheckedItem(itemIdPersist);
            //onItemClick(itemIdPersist);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthProgressDialog != null) {
            mAuthProgressDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.mAuthData = mFirebaseRef.getAuth();
        if (this.mAuthData != null) {
            mGoogleLoginButton.setVisibility(View.GONE);
            mLoggedInStatusTextView.setVisibility(View.VISIBLE);
            Picasso.with(MainActivity.this).load(mAuthData.getProviderData().get("profileImageURL").toString()).into(userImage);
            mLoggedInStatusTextView.setText(generateLoginText());

        } else {
            generateAuthData();
        }
        Log.d("MIKEL", "onStart");
    }

    @Override
    public void onBackPressed() {
        if (itemIdPersist != R.id.item1){
            itemIdPersist = R.id.item1;
            navigationDrawer.setCheckedItem(R.id.item1);
            selectFragment(new MainFragment());
        }else{
            super.onBackPressed();
        }

        //super.onBackPressed();


        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    /*@Override
    public void onItemClick(int itemId) {
        if (itemId != itemIdPersist) {
            switch (itemId) {
                case R.id.main:
                    selectFragment(new MainFragment());
                    break;
                case R.id.shopping_cart:
                    selectFragment(IngredientListFragment.newInstance(R.string.shoping_cart_drawer));
                    break;
                case R.id.recipes:
                    selectFragment(new RecipeListFragment());
                    break;
                case R.id.ingredients:
                    selectFragment(IngredientListFragment.newInstance(R.string.ingredients_drawer));
                    break;
            }

            itemIdPersist = itemId;
            navigationDrawer.setSelectedItemId(itemId);
        }
    }*/


    private void selectFragment(Fragment fragment) {
        drawerLayout.closeDrawer(GravityCompat.START);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


    /*Login with firebase*/

    private String generateLoginText() {
        String name = null;
        if (mAuthData.getProvider().equals("google")) {
            name = (String) mAuthData.getProviderData().get("displayName");
        }
        String text = "Logged in as " + name + " (" + mAuthData.getProvider() + ")";
        return text;
    }

    private void makeLogin() {
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
        if (mAuthData == null) {
            generateAuthData();
        }
    }

    private void logout() {
        if (this.mAuthData != null) {
            mFirebaseRef.unauth();
            if (this.mAuthData.getProvider().equals("google")) {
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                    mGoogleApiClient.disconnect();
                }
            }
            setAuthenticatedUser(null);
        }
    }

    private void setAuthenticatedUser(final AuthData authData) {
        if (authData != null) {
            mGoogleLoginButton.setVisibility(View.GONE);
            mLoggedInStatusTextView.setVisibility(View.VISIBLE);
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
                            Log.d(TAG, "No existe");
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
            mGoogleLoginButton.setVisibility(View.VISIBLE);
            mLoggedInStatusTextView.setVisibility(View.GONE);
            Picasso.with(MainActivity.this).load("https://www.google.es/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwjumsn3p4rMAhVH1RQKHUvwAL8QjRwIBw&url=http%3A%2F%2Fwww.domestika.org%2Fes%2Fprojects%2F35336-fakebook-avatar-project&psig=AFQjCNHC2L_vKAetzHsOt2NQ0xuo4rUE-w&ust=1460592157859224").into(userImage);
        }
        this.mAuthData = authData;
        supportInvalidateOptionsMenu();
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

    private void resolveSignInError() {
        if (mGoogleConnectionResult.hasResolution()) {
            try {
                mGoogleIntentInProgress = true;
                mGoogleConnectionResult.startResolutionForResult(this, RC_GOOGLE_LOGIN);
            } catch (IntentSender.SendIntentException e) {
                mGoogleIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void getGoogleOAuthTokenAndLogin() {
        mAuthProgressDialog.show();
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;
                try {
                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
                    token = GoogleAuthUtil.getToken(MainActivity.this, Plus.AccountApi.getAccountName(mGoogleApiClient), scope);
                } catch (IOException transientEx) {
                    Log.e(TAG, "Error authenticating with Google: " + transientEx);
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(TAG, "Recoverable Google OAuth error: " + e.toString());
                    if (!mGoogleIntentInProgress) {
                        mGoogleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        startActivityForResult(recover, RC_GOOGLE_LOGIN);
                    }
                } catch (GoogleAuthException authEx) {
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
        getGoogleOAuthTokenAndLogin();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mGoogleIntentInProgress) {
            mGoogleConnectionResult = result;
            if (mGoogleLoginClicked) {
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

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

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


}
