package com.vishalpal555.rentmanagement.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RentManagement {
    private Set<String> admins;
    private Set<String> employees;
    private Set<Room> roomsList;
    private Set<Tenant> tenantsList;

    public RentManagement(String defaultAdmin) {
        this.admins = new HashSet<>(){{add(defaultAdmin);}};
        this.employees = new HashSet<>();
        this.roomsList = new HashSet<>();
        this.tenantsList = new HashSet<>();
    }
}
