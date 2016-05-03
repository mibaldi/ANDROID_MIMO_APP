package com.android.mikelpablo.otakucook.Login;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.android.mikelpablo.otakucook.BuildConfig;
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


        OneSignal.startInit(this).init();
        OneSignal.sendTag("premium",String.valueOf(BuildConfig.SHOW_PREMIUM_ACTIONS));

        Firebase.setAndroidContext(this);
    }


}
