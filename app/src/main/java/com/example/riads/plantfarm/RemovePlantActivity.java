package com.example.riads.plantfarm;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RemovePlantActivity extends AppCompatActivity {

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
                //We show the delete dialog next...
                showDeleteDialog(plant.getPlantID(),plant.getPlantType());
                return true;
            }
        });
    }

    private boolean deletePlant(String id) {
        //getting the specified artist reference
        databasePlants = FirebaseDatabase.getInstance().getReference("Plants").child(id);

        //removing artist
        databasePlants.removeValue();

        Toast.makeText(getApplicationContext(), "Plant Deleted!", Toast.LENGTH_LONG).show();
        return true;
    }

    //Brings up the delete Dialog that has the delete button
    private void showDeleteDialog(final String plantId, final String plantType){
        //Using an AlertDialog...
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        //Using the delete_dialog xml file that I created
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        dialogBuilder.setView(dialogView);

        //Assigns the delete button
        final Button buttonDelete = dialogView.findViewById(R.id.buttonDeletePlant);
        final Button buttonCancel = dialogView.findViewById(R.id.buttonDeleteCancel);

        //Set the title and show the dialog window
        dialogBuilder.setTitle(plantType);
        final AlertDialog deleteDiag = dialogBuilder.create();
        //Shows the dialog
        deleteDiag.show();

        //Waits for the delete button to be pressed
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        //attaching a value event listener
        databasePlants.addValueEventListener(new ValueEventListener() {
            @Override
            //When the data changes, we need to reload the list
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous plant list
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

            }
        });
    }
}
