package com.example.riads.plantfarm;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

//This activity removes Plants from the "Plants" database."D - Delete part of CRUD"
public class RemovePlantActivity extends AppCompatActivity {

    //Plant log candidate?
    Plant plantLogCandidate;

    Long deltaTime = 0L;
    Long startTime = 0L;
    Long endTime = 0L;


    DatabaseReference databasePlants;
    List<Plant> plants;
    ListView listViewPlants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_plant);
        listViewPlants = findViewById(R.id.listViewPlants);
        databasePlants = FirebaseDatabase.getInstance().getReference("Plants");
        plants = new ArrayList<>();

        //When we click on a Plant for a longer time, this gets triggered...
        listViewPlants.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //We get the plant ID here...
                Plant plant = plants.get(i);

                //Candidate for plant log database
                plantLogCandidate = plant;
                //We show the delete dialog next...
                showDeleteDialog(plant.getPlantID(),plant.getPlantType(), plant.getPlantMessage());
                return true;
            }
        });
    }

    //Adds the removed plant to the Logs (no longer drying)
    private void addPlantToLogs(Plant remPlant){
        //Set plant drying boolean to false
        remPlant.noLongerDrying();

        //Create reference to Logs in database
        DatabaseReference databaseLogs = FirebaseDatabase.getInstance().getReference("Logs");

        //We generate a unique Log ID every time
        String logId = databaseLogs.push().getKey();

        //Set the key field in plant object to the key we just got for the Log...
        remPlant.setPlantID(logId);

        //Set the value of the child (given the key that was generated)
        databaseLogs.child(logId).setValue(remPlant);

        //Create the server timestamp for plantOutTime
        Map map = new HashMap();
        map.put("plantOutTime", ServerValue.TIMESTAMP);
        databaseLogs.child(logId).updateChildren(map);

        databaseLogs.child(logId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                startTime = (Long) dataSnapshot.child("plantInTime").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseLogs.child(logId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                endTime = (Long) dataSnapshot.child("plantOutTime").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Calculate the difference in time
        deltaTime = endTime - startTime;

        Log.d("Operations:", String.valueOf(endTime) + " - " + String.valueOf(startTime)
         + " = " + String.valueOf(deltaTime));

        Log.d("DeltaTime:", String.valueOf(deltaTime));

        long different = deltaTime;

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String DryingTimeStr = "Days: " + String.valueOf(elapsedDays) + " H: " + String.valueOf(elapsedHours)
                + " M: " + String.valueOf(elapsedMinutes) + " S: " + String.valueOf(elapsedSeconds);


        //Add the calculated delta for drying time
        databaseLogs.child(logId).child("plantDryingTime").setValue(DryingTimeStr);

        //Reset
        startTime = 0L;
        endTime = 0L;
        endTime = 0L;

        plantLogCandidate = null;
    }


    //Deletes the plant from the database given ID
    private boolean deletePlant(String id) {
        try {
            //getting the specified plant reference from database
            DatabaseReference databasePlant = FirebaseDatabase.getInstance().getReference("Plants").child(id);

            //removes the plant from the database
            databasePlant.removeValue();

            //Bring up a Toast and update the user with information!
            Toast.makeText(getApplicationContext(), "Plant Deleted!", Toast.LENGTH_LONG).show();

        } catch (DatabaseException dataError){
            Toast.makeText(getApplicationContext(), "Database Error!", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    //Brings up the delete Dialog that has the delete button
    private void showDeleteDialog(final String plantId, final String plantType, final String plantMessage){
        //Using an AlertDialog...
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        //Using the delete_dialog xml file that I created
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        dialogBuilder.setView(dialogView);

        //Assigns the delete & cancel button & textview (message of plant)
        final Button buttonDelete = dialogView.findViewById(R.id.buttonDeletePlant);
        final Button buttonCancel = dialogView.findViewById(R.id.buttonDeleteCancel);
        final TextView textMessage = dialogView.findViewById(R.id.textViewDeleteMessage);

        //Set the title and show the dialog window
        dialogBuilder.setTitle(plantType);
        textMessage.setText(plantMessage);
        final AlertDialog deleteDiag = dialogBuilder.create();
        //Shows the dialog
        deleteDiag.show();

        //Waits for the delete button to be pressed
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addPlantToLogs(plantLogCandidate);
                //We delete the plant from the database and then dismiss the dialog
                deletePlant(plantId);
                deleteDiag.dismiss();
            }
        });

        //or... waits for the Cancel button to be pressed
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dismiss the dialog and do nothing!
                deleteDiag.dismiss();
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

                //clear the previous plant list
                plants.clear();

                //iterates through all the plants
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting plants
                    Plant plant = postSnapshot.getValue(Plant.class);
                    //adding the plant to the array list
                    plants.add(plant);
                }

                //creates adapter
                PlantList plantAdapter = new PlantList(RemovePlantActivity.this, plants);

                //attaches adapter to the listview
                listViewPlants.setAdapter(plantAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database Error!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
