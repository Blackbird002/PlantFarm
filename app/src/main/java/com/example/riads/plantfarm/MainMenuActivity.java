package com.example.riads.plantfarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
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

    public void goToTemp(View v) {
        Intent intent = new Intent(this, Graphs.class);
        startActivity(intent);
    }

}
