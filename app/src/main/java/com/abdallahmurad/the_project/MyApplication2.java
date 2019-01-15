package com.abdallahmurad.the_project;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Abdallah Murad on 10/26/2017.
 */

public class MyApplication2 extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
