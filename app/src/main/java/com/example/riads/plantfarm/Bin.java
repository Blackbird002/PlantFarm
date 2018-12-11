package com.example.riads.plantfarm;
// Added for a commit
public class Bin {
    String binID;
    String binShelf;
    String binRow;
    String binMessage;
    long finishedTime;
    boolean status;

    public Bin() {
        this.binID = "";
        this.binShelf = "";
        this.binRow = "";
        this.binMessage = "";
        this.finishedTime = 0;
        this.status = false;
    }

    public Bin(String binID, String binShelf, String binRow, String binMessage) {
        this.binID = binID;
        this.binShelf = binShelf;
        this.binRow = binRow;
        this.binMessage = binMessage;
        this.finishedTime = 0;
        this.status = false;
    }


    public void setBinID(String binID) {
        this.binID = binID;
    }

    public void setBinShelf(String binShelf) {
        this.binShelf = binShelf;
    }

    public void setBinRow(String binRow) {
        this.binRow = binRow;
    }

    public void setBinMessage(String binMessage) {
        this.binMessage = binMessage;
    }

    public void setFinishedTime(long finishedTime) {
        this.finishedTime = finishedTime;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
