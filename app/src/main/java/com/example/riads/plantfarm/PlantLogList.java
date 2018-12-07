package com.example.riads.plantfarm;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

//ArrayAdaptor returns a view for each object in a collection of data objects (the list of plants)
public class PlantLogList extends ArrayAdapter<Plant> {
    private Activity context;
    List<Plant> plants;

    public PlantLogList(Activity context, List<Plant> plants) {
        //Uses the layout_play_list xml file that was created (now an element in the list should look like)
        super(context, R.layout.layout_plant_list, plants);
        this.context = context;
        this.plants = plants;
    }


    /*Overriding the getView method which gets a View that displays the data at
    the specified position in the data.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_plantlog_list, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.textViewPlant);
        TextView textViewMessage = listViewItem.findViewById(R.id.textViewMessage);
        TextView textViewDryingTime = listViewItem.findViewById(R.id.textViewDryingTime);

        Plant plant = plants.get(position);
        textViewName.setText(plant.getPlantType());
        textViewMessage.setText(plant.getPlantMessage());
        textViewDryingTime.setText(plant.getPlantDryingTime());

        return listViewItem;
    }
}