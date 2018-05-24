package com.example.supanonymous.focoaedes;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by supanonymous on 23/04/18.
 */

public class FocoAedes extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);

    }
}
