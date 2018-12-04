package com.example.riads.plantfarm;

import android.util.Log;

import java.util.Map;

public class Plant {

    String plantID;
    String plantType;
    String plantMessage;

    Boolean plantDrying;

    Map plantInTime;
    Map plantOutTime;
    Map plantTotalTime;

    public Plant(String plantID, String plantType, String plantMessage,Map plantInTime) {
        this.plantID = plantID;
        this.plantType = plantType;
        this.plantMessage = plantMessage;
        this.plantInTime = plantInTime;

        this.plantDrying = true;
        this.plantOutTime = null;
        this.plantTotalTime = null;
    }
}
