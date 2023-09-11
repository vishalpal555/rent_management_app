package com.vishalpal555.rentmanagement.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class Tenant {
    private String id;
    private String userUid;
    private String name;
    private int advancePaid;
    private String officialId;
    private LocalDate joiningDate;
    private LocalDate leavingDate;
    private List<LocalDate> payedRentOnDate;
    private HashMap<LocalDate, Integer> monthlyPaymentAmount;
    private HashMap<String, String> comments;
    private boolean isTenantActive;

    public boolean getIsTenantActive(){
        return joiningDate.isBefore(LocalDate.now()) && (leavingDate == null || leavingDate.isAfter(LocalDate.now()));
    }
}
