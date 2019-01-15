package com.abdallahmurad.the_project.controller;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Abdallah Murad on 12/9/2017.
 */

public class SingeltonFirebase    {
    private static FirebaseDatabase instance =FirebaseDatabase.getInstance();

    private SingeltonFirebase() {
        if (instance != null) {
            instance = FirebaseDatabase.getInstance();
        }
    }

    public static FirebaseDatabase getInstance(){
        return instance;
    }


}
