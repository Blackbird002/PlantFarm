package com.example.riads.plantfarm;

import android.util.Log;

import java.util.Map;

public class Plant {

    String plantID;
    String plantType;
    String plantMessage;
    Boolean plantDrying;
    Long plantInTime;
    Long plantOutTime;
    String plantDryingTime;

    public Plant(){}

    public Plant(String plantID, String plantType, String plantMessage) {
        this.plantID = plantID;
        this.plantType = plantType;
        this.plantMessage = plantMessage;
        this.plantInTime = null;
        this.plantOutTime = null;
        this.plantDrying = true;
        this.plantDryingTime = " ";
    }

    public Plant(String plantID, String plantType, String plantMessage, String plantDryingTime) {
        this.plantID = plantID;
        this.plantType = plantType;
        this.plantMessage = plantMessage;
        this.plantInTime = null;
        this.plantOutTime = null;
        this.plantDrying = true;
        this.plantDryingTime = plantDryingTime;
    }

    public void setPlantID(String plantID) {
        this.plantID = plantID;
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

    public void noLongerDrying(){
        plantDrying = false;
    }

    public Long getPlantInTime() {
        return plantInTime;
    }

    public Long getPlantOutTime() {
        return plantOutTime;
    }

    public String getPlantDryingTime() {
        return plantDryingTime;
    }
}
