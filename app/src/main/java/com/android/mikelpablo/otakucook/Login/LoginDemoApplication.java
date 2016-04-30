package com.android.mikelpablo.otakucook.Login;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;
import com.onesignal.OneSignal;
import io.fabric.sdk.android.Fabric;

/**
 * Initialize Firebase with the application context. This must happen before the client is used.
 *
 */
public class LoginDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        // TODO: Move this to where you establish a user session
        logUser();

        OneSignal.startInit(this).init();
        Firebase.setAndroidContext(this);
    }
    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("user@fabric.io");
        Crashlytics.setUserName("Test User");
    }


}
