package com.vishalpal555.rentmanagement.entity;

import java.util.ArrayList;
import java.util.List;
public class RentManagement {
    private List<String> admins;
    private List<String> employees;
    private List<String> roomsList;

    public RentManagement(String defaultAdmin) {
        this.admins = new ArrayList<>(){{add(defaultAdmin);}};
        this.employees = new ArrayList<>();
        this.roomsList = new ArrayList<>();
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public List<String> getEmployees() {
        return employees;
    }

    public void setEmployees(List<String> employees) {
        this.employees = employees;
    }

    public List<String> getRoomsList() {
        return roomsList;
    }

    public void setRoomsList(List<String> roomsList) {
        this.roomsList = roomsList;
    }
}
