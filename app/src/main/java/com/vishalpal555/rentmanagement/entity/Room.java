package com.vishalpal555.rentmanagement.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Room {
    private String roomId;
    private String roomNumber;
    private String floorNumber;
    private String meterSerial;
    private long meterReading;
    private float amountPerUnit;
    private HashMap<String, String> comments;
    private Set<Tenant> previousTenants;
    private Tenant currentTenant;
    private boolean isRoomVacant;

    public Room(String roomId, String roomNumber, String floorNumber, String meterSerial, long meterReading, float amountPerUnit) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.meterSerial = meterSerial;
        this.meterReading = meterReading;
        this.amountPerUnit = amountPerUnit;
        this.comments = new HashMap<>();
        this.isRoomVacant = true;
        this.previousTenants = new HashSet<>();
        this.currentTenant = new Tenant();
    }
}
