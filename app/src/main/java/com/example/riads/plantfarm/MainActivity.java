package com.example.riads.plantfarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btnAddToDatabase;
    private EditText txtMessage;
    private Spinner spnHerb;
    DatabaseReference databasePlants;

    private void addPlantToFirebase(){
        String message = txtMessage.getText().toString().trim();
        String selectedHerb = spnHerb.getSelectedItem().toString();

        //Checks if the message is empty (message is required!)
        if(!TextUtils.isEmpty(message)){
            try{
                //We generate a unique plant ID every time
                String plantId = databasePlants.push().getKey();

                //We construct the plant object
                Plant plant = new Plant(plantId, selectedHerb, message);

                //Set the value of the child (given the key that was generated)
                databasePlants.child(plantId).setValue(plant);

                /*
                Time will not be part of the Plant object because TIMESTAMP is a Hashmap when
                when created. When it is retrieved, it's returned at a long by firebase
                */
                Map map = new HashMap();
                map.put("plantInTime", ServerValue.TIMESTAMP);
                databasePlants.child(plantId).updateChildren(map);

            } catch (DatabaseException dataError){
                Toast.makeText(getApplicationContext(), "Database Error!", Toast.LENGTH_LONG).show();
            }

            Toast.makeText(this, "Plant added!", Toast.LENGTH_LONG).show();
            txtMessage.setText("");

        }
        else{
            //Displays a message that the messagebox is empty!
            Toast.makeText(this, "Please add a message!", Toast.LENGTH_LONG).show();
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
        databasePlants = FirebaseDatabase.getInstance().getReference("Plants");

        // synchronizes and stores a local copy of the data for active listeners
        databasePlants.keepSynced(true);

        btnAddToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adds the plant
                addPlantToFirebase();
            }
        });
    }
}
