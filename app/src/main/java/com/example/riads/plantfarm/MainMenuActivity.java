package com.example.riads.plantfarm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenuActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainMenuActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        signOut = (Button) findViewById(R.id.btnSignOut);
    }

    //Goes to the Create Activity
    public void goToCreate(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Goes to the Delete Activity
    public void goToDelete(View v){
        Intent intent = new Intent(this, RemovePlantActivity.class);
        startActivity(intent);
    }

    public void goToUpdate(View v) {
        Intent intent = new Intent(this, UpdatePlantActivity.class);
        startActivity(intent);
    }

    public void goToPlantLogs(View v){
        Intent intent = new Intent(this, PlantLogsActivity.class);
        startActivity(intent);
    }


    public void signOut(View v) {
        auth.signOut();
        startActivity(new Intent(MainMenuActivity.this, LoginActivity.class));
    }


}
