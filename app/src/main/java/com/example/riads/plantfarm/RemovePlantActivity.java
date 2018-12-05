package com.example.riads.plantfarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching a value event listener
        databasePlants.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous plant list
                plants.clear();

                //iterates through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting plants
                    Plant plant = postSnapshot.getValue(Plant.class);
                    //adding artist to the list
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
