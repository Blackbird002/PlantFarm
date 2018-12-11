package com.example.riads.plantfarm;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//This activity updates messages from the "Plants" database."U - Update part of CRUD"
public class UpdatePlantActivity extends AppCompatActivity {
    DatabaseReference databasePlants;
    List<Plant> plants;
    ListView listViewPlants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_plant);
        listViewPlants = findViewById(R.id.listViewUpdatePlants);
        databasePlants = FirebaseDatabase.getInstance().getReference("Plants");
        plants = new ArrayList<>();

        //When we click on a Plant for a longer time, this gets triggered...
        listViewPlants.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //We get the plant ID here...
                Plant plant = plants.get(i);

                //We show the update dialog next...
                showUpdateDialog(plant.getPlantID(),plant.getPlantType(), plant.getPlantMessage());
                return true;
            }
        });
    }

    private boolean updateMessage(String id, String newMessage) {
        //getting the specified plant reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Plants").child(id).child("plantMessage");

        //updating message
        dR.setValue(newMessage);
        Toast.makeText(getApplicationContext(), "Plant Message Updated", Toast.LENGTH_LONG).show();
        return true;
    }


    //Brings up the delete Dialog that has the delete button
    private void showUpdateDialog(final String plantId, final String plantType, final String plantMessage){
        //Using an AlertDialog...
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        //Using the update_dialog xml file that I created
        View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        //Assigns the update & cancel button & textview (message of plant)
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdatePlant);
        final Button buttonCancel = dialogView.findViewById(R.id.buttonUpdateCancel);
        final EditText newMessage = dialogView.findViewById(R.id.editMessage);
        final TextView textMessage = dialogView.findViewById(R.id.textViewUpdateMessage);

        //Set the title and show the dialog window
        dialogBuilder.setTitle(plantType);
        textMessage.setText(plantMessage);
        final AlertDialog updateDiag = dialogBuilder.create();
        //Shows the dialog
        updateDiag.show();
        //Waits for the update button to be pressed
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get the string in EditText in dialog
                String updatedMessage = newMessage.getText().toString();

                //We update the plant message in the database and then dismiss the dialog
                updateMessage(plantId, updatedMessage);
                updateDiag.dismiss();
            }
        });

        //or... waits for the Cancel button to be pressed
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dismiss the dialog and do nothing!
                updateDiag.dismiss();
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
                PlantList plantAdapter = new PlantList(UpdatePlantActivity.this, plants);

                //attaches adapter to the listview
                listViewPlants.setAdapter(plantAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database Error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void testUpdatePlantActivity(View v){
        boolean test = false;
        String testMessageOld = "TEST UPDATE OLD";
        String testMessageNew = "TEST UPDATE NEW";
        String testHerb = "UPDATE TEST";

        //Add a Test Plant to database
        DatabaseReference databasePlantUpdateTest;
        databasePlantUpdateTest = FirebaseDatabase.getInstance().getReference("Plants");
        String plantId = databasePlantUpdateTest.push().getKey();
        Plant plant = new Plant(plantId, testHerb, testMessageOld);
        databasePlantUpdateTest.child(plantId).setValue(plant);

        //Sleep for 1 second for the database to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Update the test Plant message
        updateMessage(plantId,testMessageNew);

        //Sleep for 1 seconds for the database to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check to see if the plant was updated to testMessageNew
        for(Plant tempPlant : plants){
            if(tempPlant.getPlantID().equals(plantId)){
                if(tempPlant.getPlantMessage().equals(testMessageNew)){
                    test = true;
                    break;
                }
            }
        }

        //Sleep for 1 seconds for the database to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Removes the update plant test case
        databasePlantUpdateTest = databasePlants.child(plantId);
        databasePlantUpdateTest.removeValue();

        if(test == true)
            Toast.makeText(getApplicationContext(), "Update Tests Passed!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Update Tests Failed!", Toast.LENGTH_LONG).show();
    }

}
