package com.vishalpal555.rentmanagement.global;

import com.vishalpal555.rentmanagement.entity.Room;
import com.vishalpal555.rentmanagement.entity.Tenant;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class MockData {
    public final static Tenant tenant1 = new Tenant(
            "vishalpaldeveloper+tentant1@gmail.com",
            "Tenant One",
            5000,
            "adhaar12345",
            LocalDate.parse("12-08-2023", Constants.dateTimeFormatter),
            LocalDate.parse("20-09-2024", Constants.dateTimeFormatter),
            "1234567890"
    );

    public static Room room1(String userUid){
        return new Room(
                userUid,
                "A1",
                "1",
                "1",
                "METER1",
                100,
                6
        );
    }
}
