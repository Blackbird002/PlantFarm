package com.example.riads.plantfarm;

import com.google.firebase.database.FirebaseDatabase;

public class PreStart extends android.app.Application  {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
