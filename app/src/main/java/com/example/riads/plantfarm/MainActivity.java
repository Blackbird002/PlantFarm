package com.example.riads.plantfarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {

    private Button btnAddToDatabase;
    private EditText txtMessage;
    private Spinner spnHerb;
    DatabaseReference databasePlants;

    private void addPlantToFirebase(){
        String message = txtMessage.getText().toString().trim();
        String selectedHerb = spnHerb.getSelectedItem().toString();
        databasePlants = FirebaseDatabase.getInstance().getReference("Plants");

        if(!TextUtils.isEmpty(message)){

            //We generate a unique bin ID every time
            String plantId = databasePlants.push().getKey();

            //We construct the plant object
            Plant plant = new Plant(plantId,selectedHerb,message, ServerValue.TIMESTAMP);

            databasePlants.child(plantId).setValue(plant);

            Toast.makeText(this, "Plant is added!", Toast.LENGTH_LONG).show();

        }
        else{
            //Displays a message that the messagebox is empty!
            Toast.makeText(this, "Please add a message", Toast.LENGTH_LONG).show();
        }
    }

    //Bundle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign all the UI elements
        btnAddToDatabase = findViewById(R.id.btnAddToDatabase);
        txtMessage = findViewById(R.id.txtMessage);
        spnHerb = findViewById(R.id.spnHerb);

        btnAddToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adds the plant
                addPlantToFirebase();
            }
        });



    }
}
