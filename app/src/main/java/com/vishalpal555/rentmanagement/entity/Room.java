package com.vishalpal555.rentmanagement.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Room {
    private String roomId;
    private String roomNumber;
    private String floorNumber;
    private String meterSerial;
    private long meterReading;
    private float ratePerUnit;
    private HashMap<String, String> comments;
    private List<String> previousTenants;
    private String currentTenantForeignKey;
    private boolean isRoomVacant;
    private String managerId;

    public Room(String rentManagementId, String roomNumber, String floorNumber, String meterSerial, long meterReading, float ratePerUnit) {
        this.managerId = rentManagementId;
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.meterSerial = meterSerial;
        this.meterReading = meterReading;
        this.ratePerUnit = ratePerUnit;
        this.comments = new HashMap<>();
        this.previousTenants = new ArrayList<>();
        this.currentTenantForeignKey = "";

        this.setIsRoomVacant();
        this.setRoomId();
    }
    public void setIsRoomVacant() {
        this.isRoomVacant = (this.currentTenantForeignKey.isBlank() || this.currentTenantForeignKey.isEmpty());
    }
    public void setRoomId(){
        this.roomId = this.managerId +"+" +this.floorNumber +"+" +this.roomNumber;
        //using this so that room can be unique based on floor and room number
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public String getMeterSerial() {
        return meterSerial;
    }

    public long getMeterReading() {
        return meterReading;
    }

    public float getRatePerUnit() {
        return ratePerUnit;
    }

    public HashMap<String, String> getComments() {
        return comments;
    }

    public List<String> getPreviousTenants() {
        return previousTenants;
    }

    public String getCurrentTenantForeignKey() {
        return currentTenantForeignKey;
    }

    public boolean isRoomVacant() {
        return isRoomVacant;
    }

    public String getManagerId() {
        return managerId;
    }
}
