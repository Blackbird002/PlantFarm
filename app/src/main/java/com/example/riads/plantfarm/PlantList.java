package com.example.riads.plantfarm;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class PlantList extends ArrayAdapter<Plant> {
    private Activity context;
    List<Plant> plants;

    public PlantList(Activity context, List<Plant> plants) {
        super(context, R.layout.layout_plant_list, plants);
        this.context = context;
        this.plants = plants;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_plant_list, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.textViewPlant);
        TextView textViewMessage = listViewItem.findViewById(R.id.textViewMessage);

        Plant plant = plants.get(position);
        textViewName.setText(plant.getPlantType());
        textViewMessage.setText(plant.getPlantMessage());

        return listViewItem;
    }
}