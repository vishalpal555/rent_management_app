package com.vishalpal555.rentmanagement.entity;

import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {
    private String userUid;
    private String roomId;
    private String roomNumber;
    private String floorNumber;
    private String meterSerial;
    private long meterReading;
    private float amountPerUnit;
    private HashMap<String, String> comments;
    private List<Tenant> tenants;

    public Room(String userUid, String roomId, String roomNumber, String floorNumber, String meterSerial, long meterReading, float amountPerUnit) {
        this.userUid = userUid;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.meterSerial = meterSerial;
        this.meterReading = meterReading;
        this.amountPerUnit = amountPerUnit;
        this.comments = new HashMap<>();
    }
}
