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

public class MainActivity extends AppCompatActivity {

    private Button btnAddToDatabase;
    private EditText txtMessage;
    private Spinner spnRow;
    private Spinner spnShelf;
    DatabaseReference databasePlants;

    private void addPlantToFirebase(){
        String message = txtMessage.getText().toString().trim();
        String row = spnRow.getSelectedItem().toString();
        String shelf = spnShelf.getSelectedItem().toString();
        databasePlants = FirebaseDatabase.getInstance().getReference("Bins");

        if(!TextUtils.isEmpty(message)){

            //We generate a unique bin ID every time
            String binId = databasePlants.push().getKey();

            //We construct the plant object
            Bin bin = new Bin(binId,row,shelf,message);

            databasePlants.child(binId).setValue(bin);

            Toast.makeText(this, "Bin is added!", Toast.LENGTH_LONG).show();

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
        spnRow = findViewById(R.id.spnRow);
        spnShelf = findViewById(R.id.spnShelf);

        btnAddToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adds the plant
                addPlantToFirebase();
            }
        });



    }
}
