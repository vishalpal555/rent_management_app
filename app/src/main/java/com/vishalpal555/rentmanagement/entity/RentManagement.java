package com.vishalpal555.rentmanagement.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RentManagement {
    private List<String> admins;
    private List<String> employees;
    private List<Room> roomsList;
    private List<Tenant> tenantsList;

    public RentManagement(List<String> admins, List<String> employees) {
        this.admins = admins;
        this.employees = employees;
        this.roomsList = new ArrayList<>();
        this.tenantsList = new ArrayList<>();
    }
}
