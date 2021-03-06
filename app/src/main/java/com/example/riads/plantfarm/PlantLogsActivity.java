package com.example.riads.plantfarm;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlantLogsActivity extends AppCompatActivity {

    DatabaseReference databasePlantLogs;
    List<Plant> plantLogs;
    ListView listViewPlantLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_logs);
        listViewPlantLogs = findViewById(R.id.listViewPlants);
        databasePlantLogs = FirebaseDatabase.getInstance().getReference("Logs");

        // synchronizes and stores a local copy of the data for active listeners
        databasePlantLogs.keepSynced(true);

        plantLogs = new ArrayList<>();

        //When we click on a Plant for a longer time, this gets triggered...
        listViewPlantLogs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //We get the plant ID here...
                Plant plant = plantLogs.get(i);

                //We show the delete dialog next...
                showDeleteDialog(plant.getPlantID(),plant.getPlantType(), plant.getPlantMessage());
                return true;
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
        databasePlantLogs.addValueEventListener(new ValueEventListener() {
            @Override
            //When the data changes, we need to reload the list
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clear the previous plant list
                plantLogs.clear();

                //iterates through all the plants in Log
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting plants
                    Plant plant = postSnapshot.getValue(Plant.class);
                    //adding the plant to the array list
                    plantLogs.add(plant);
                }

                //creates adapter TO DO... needs a different adaptor
                PlantLogList plantLogsAdapter = new PlantLogList(PlantLogsActivity.this, plantLogs);

                //attaches adapter to the listview
                listViewPlantLogs.setAdapter(plantLogsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database Error!", Toast.LENGTH_LONG).show();
            }
        });
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
                deletePlantLog(plantId);
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

    //Deletes the plant Log from the database given ID
    private boolean deletePlantLog(String id) {
        try {
            //getting the specified plant reference from database
            DatabaseReference databasePlant = FirebaseDatabase.getInstance().getReference("Logs").child(id);

            //removes the plant from the database
            databasePlant.removeValue();

            //Bring up a Toast and update the user with information!
            Toast.makeText(getApplicationContext(), "Plant Log Deleted!", Toast.LENGTH_LONG).show();

        } catch (DatabaseException dataError){
            Toast.makeText(getApplicationContext(), "Database Error!", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public void testPlantLogsActivity(View v){
        boolean test = true;
        String testMessage = "TEST LOG REMOVE";
        String testHerb = "LOG TEST";

        //Add a Test Log to database
        DatabaseReference databasePlantLogTest;
        databasePlantLogTest = FirebaseDatabase.getInstance().getReference("Logs");
        String plantId = databasePlantLogTest.push().getKey();
        Plant plant = new Plant(plantId, testHerb, testMessage);
        databasePlantLogTest.child(plantId).setValue(plant);

        //Sleep for 1 seconds for the database to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Remove the test Plant
        deletePlantLog(plantId);

        //Sleep for 1 seconds for the database to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check to see if the plant is still there
        for(Plant tempLog : plantLogs){
            if(tempLog.getPlantMessage().equals(testMessage) && tempLog.getPlantType().equals(testHerb)){
                test = false;
            }
        }

        if(test == true)
            Toast.makeText(getApplicationContext(), "Log Tests Passed!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Log Tests Failed!", Toast.LENGTH_LONG).show();
    }
}
