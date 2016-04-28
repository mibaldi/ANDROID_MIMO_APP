package com.android.mikelpablo.otakucook.Login;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.firebase.client.Firebase;
import com.onesignal.OneSignal;

/**
 * Initialize Firebase with the application context. This must happen before the client is used.
 *
 */
public class LoginDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this).init();
        Firebase.setAndroidContext(this);
    }

}
