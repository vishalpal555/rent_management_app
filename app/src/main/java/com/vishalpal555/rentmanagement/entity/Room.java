package com.vishalpal555.rentmanagement.entity;

import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Room {
    private String id;
    private String userId;
    private String roomId;
    private String roomNumber;
    private String floorNumber;
    private String meterSerial;
    private long meterReading;
    private float amountPerUnit;
    private HashMap<String, String> comments;
    private List<Tenant> tenants;
}
