package com.example.riads.plantfarm;

import android.util.Log;

import java.util.Map;

public class Plant {

    String plantID;
    String plantType;
    String plantMessage;

    Boolean plantDrying;

    Long plantInTime;

    public Plant(){}

    public Plant(String plantID, String plantType, String plantMessage) {
        this.plantID = plantID;
        this.plantType = plantType;
        this.plantMessage = plantMessage;
        this.plantInTime = null;

        this.plantDrying = true;
    }

    public String getPlantType() {
        return plantType;
    }

    public String getPlantMessage() {
        return plantMessage;
    }

    public Boolean getPlantDrying() {
        return plantDrying;
    }

    public String getPlantID() {
        return plantID;
    }
}
