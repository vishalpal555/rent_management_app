package com.vishalpal555.rentmanagement.mock;

import com.vishalpal555.rentmanagement.entity.Tenant;
import com.vishalpal555.rentmanagement.global.Constants;

import java.time.LocalDate;
import java.util.HashMap;

import lombok.Getter;

public class TenantMock {
    public static Tenant tenant1 = new Tenant(
            "vishalpaldeveloper+tentant1@gmail.com",
            "Tenant One",
            5000,
            "adhaar12345",
            LocalDate.parse("12-08-2023", Constants.dateTimeFormatter),
            LocalDate.parse("20-09-2024", Constants.dateTimeFormatter),
            new HashMap<>(),
            new HashMap<>(),
            "1234567890"
    );
}
