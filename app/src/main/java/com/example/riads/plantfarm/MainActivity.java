package com.example.riads.plantfarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btnAddToDatabase;
    private EditText txtMessage;
    private Spinner spnHerb;
    DatabaseReference databasePlants;
    List<Plant> plants;

    String testId = " ";

    private void addPlantToFirebase(){
        String message = txtMessage.getText().toString().trim();
        String selectedHerb = spnHerb.getSelectedItem().toString();

        //Checks if the message is empty (message is required!)
        if(!TextUtils.isEmpty(message)){
            try{
                //We generate a unique plant ID every time
                String plantId = databasePlants.push().getKey();

                //Just in case we test
                testId = plantId;

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

        plants = new ArrayList<>();
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

    @Override
    protected void onStart() {
        super.onStart();
        /*Added a value event listener for when the database changes!
        When there is a change, the list gets re-generated (so the array list is also re-generated).
        This makes the app "dynamic" so to speak with the real time database :) .
        */
        databasePlants.addValueEventListener(new ValueEventListener() {
            @Override
            //When the data changes, we need to reload the list
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clear the previous plants
                plants.clear();

                //iterates through all the plants
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting plants
                    Plant plant = postSnapshot.getValue(Plant.class);
                    //adding the plant to the array list
                    plants.add(plant);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database Error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Test
    public void mainActivityTest(View v){
        boolean test = false;

        String expectedMessage = "TEST MESSAGE";
        String testMessage = "TEST MESSAGE";

        txtMessage.setText(testMessage);
        spnHerb.setSelection(0);
        btnAddToDatabase.performClick();

        //Sleep for 1 seconds for the database to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(Plant templant : plants){
            if(templant.getPlantMessage().equals(expectedMessage)){
                test = true;
                break;
            }
        }

        //Delete the testPlant case
        DatabaseReference testPlant = FirebaseDatabase.getInstance().getReference("Plants").child(testId);

        //removes the plant from the database
        testPlant.removeValue();

        if(test == false)
            Toast.makeText(getApplicationContext(), "Add Tests Passed!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Add Tests Failed!", Toast.LENGTH_LONG).show();
    }
}
