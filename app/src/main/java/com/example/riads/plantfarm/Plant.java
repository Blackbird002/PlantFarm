package com.example.riads.plantfarm;

public class Plant {

    String plantID;
    String plantRow;
    String plantShelf;
    String plantMessage;

    public Plant() {

    }



    //Constructor
    public Plant(String plantID, String plantRow, String plantShelf, String plantMessage) {
        this.plantID = plantID;
        this.plantRow = plantRow;
        this.plantShelf = plantShelf;
        this.plantMessage = plantMessage;
    }

    public String getPlantID() {
        return plantID;
    }

    public String getPlantRow() {
        return plantRow;
    }

    public String getPlantShelf() {
        return plantShelf;
    }

    public String getPlantMessage() {
        return plantMessage;
    }
}
